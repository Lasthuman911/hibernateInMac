package kr.co.aim.messolution.extended.object.management;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.co.aim.messolution.extended.object.CTORMTemplate;
import kr.co.aim.messolution.generic.util.CommonUtil;
import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameDBErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameErrorSignal;
import kr.co.aim.nanoframe.orm.SQLLogUtil;
import kr.co.aim.nanoframe.orm.info.access.UdfAccessor;
import kr.co.aim.nanoframe.util.object.ObjectUtil;
import kr.co.aim.nanotrack.generic.GenericServiceProxy;
import kr.co.aim.nanotrack.generic.info.EventInfo;
import kr.co.aim.nanotrack.generic.util.StringUtil;
import kr.co.aim.nanotrack.generic.util.TimeUtils;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

public class CTORMService<DATA> {

    public List<DATA> select(String condition, Object[] bindSet, Class clazz)
            throws nanoFrameDBErrorSignal {
        String tableName = CTORMUtil.getTableNameByClassName(clazz);
        //String sql = "select 字段1，字段2 from " + tableName;
        String sql = CTORMUtil.getSql(clazz, tableName);

        if (StringUtils.isNotEmpty(condition)) {
            sql = CTORMUtil.getConditionSql(sql, condition);
        }

        //sql:  "select 字段1，字段2 from tableName where 字段x=？,字段y=？“
        List resultList = GenericServiceProxy.getSqlMesTemplate().queryForList(sql, bindSet);

        //case not found
        if (resultList.size() == 0) {
            throw new nanoFrameDBErrorSignal(ErrorSignal.NotFoundSignal, ObjectUtil.getString(bindSet),
                    SQLLogUtil.getLogFormatSqlStatement(sql, bindSet, CTORMUtil.getLogger()));
        }

        //refine result data
        Object result = ormExecute(CTORMUtil.createDataInfo(clazz), resultList);

        //return
        if (result instanceof List) {
            return (List<DATA>) result;
        } else if (result instanceof UdfAccessor) {
            List<DATA> resultSet = new ArrayList<DATA>();
            resultSet.add((DATA) result);

            return resultSet;
        } else {
            throw new nanoFrameDBErrorSignal(ErrorSignal.CouldNotMatchData, ObjectUtil.getString(bindSet),
                    SQLLogUtil.getLogFormatSqlStatement(sql, bindSet, CTORMUtil.getLogger()), condition);
        }
    }

    public DATA selectByKey(Class clazz, boolean isLock, Object... keyValue)
            throws nanoFrameDBErrorSignal {
        //create base already
        Object dataInfo = CTORMUtil.createDataInfo(clazz);

        String tableName = CTORMUtil.getTableNameByClassName(clazz);

        //String sql = "select * from " + tableName;
        String sql = CTORMUtil.getSql(clazz, tableName);
        sql = CTORMUtil.getKeySql(sql, dataInfo);

        //suffix for lock
        if (isLock)
            sql = new StringBuffer(sql).append(" FOR UPDATE").toString();

        String param = CommonUtil.toStringFromCollection(keyValue);

        //wzm 重构过
        if (!CTORMUtil.isKeyParamMatch(dataInfo, keyValue))
            throw new nanoFrameDBErrorSignal(ErrorSignal.NullPointKeySignal, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql, param, CTORMUtil.getLogger()));

        List resultList = GenericServiceProxy.getSqlMesTemplate().queryForList(sql, keyValue);

        if (resultList.size() == 0) {
            throw new nanoFrameDBErrorSignal(ErrorSignal.NotFoundSignal, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql, param, CTORMUtil.getLogger()));
        }

        //refine result data,将List重新组装成一个类
        Object result = ormExecute(CTORMUtil.createDataInfo(clazz), resultList);

        //return
        if (result instanceof UdfAccessor) {
            return (DATA) result;
        } else {
            throw new nanoFrameDBErrorSignal(ErrorSignal.CouldNotMatchData, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql, param, CTORMUtil.getLogger()), keyValue.toString());
        }
    }

    public void update(DATA dataInfo) throws nanoFrameDBErrorSignal {
        String tableName = CTORMUtil.getTableNameByClassName(dataInfo.getClass());

        //String sql = "update tableName set 字段1=？，字段2=？where 1=1 and keyValue1 =？ and keyValue2 =？"
        String sql = CTORMUtil.getUpdateSql(dataInfo.getClass(), tableName);

        //generate bind parameter
        //by sequence
        List<Object> keySet = CTORMUtil.makeKeyParam(dataInfo);
        String param = CommonUtil.toStringFromCollection(keySet.toArray());

        if (!CTORMUtil.isKeyParamMatch(dataInfo, keySet.toArray()))
            throw new nanoFrameDBErrorSignal(ErrorSignal.NullPointKeySignal, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql, param, CTORMUtil.getLogger()));

        List<Object> bindSet = CTORMUtil.makeNonKeyParam(dataInfo);
        bindSet.addAll(keySet);
        param = CommonUtil.toStringFromCollection(bindSet.toArray());

        //query with just one
        int result = GenericServiceProxy.getSqlMesTemplate().update(sql, bindSet.toArray());

        //case not found
        if (result == 0) {
            throw new nanoFrameDBErrorSignal(ErrorSignal.NotFoundSignal, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql, param, CTORMUtil.getLogger()));
        }
    }

    public void delete(DATA dataInfo) throws nanoFrameDBErrorSignal {
        List<Object> keySet = CTORMUtil.makeKeyParam(dataInfo);

        delete(dataInfo.getClass(), keySet.toArray());//委托
    }

    public void delete(Class clazz, Object... keyValue) throws nanoFrameDBErrorSignal {
        //create base already
        Object dataInfo = CTORMUtil.createDataInfo(clazz);

        String tableName = CTORMUtil.getTableNameByClassName(dataInfo.getClass());

        // sql : delete tableName where 1=1 and key1=? and keyN=?
        String sql = CTORMUtil.getDeleteSql(dataInfo.getClass(), tableName);

        String param = CommonUtil.toStringFromCollection(keyValue);

        if (!CTORMUtil.isKeyParamMatch(dataInfo, keyValue))
            throw new nanoFrameDBErrorSignal(ErrorSignal.NullPointKeySignal, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql, param, CTORMUtil.getLogger()));

        //result 是删除的记录的个数
        int result = GenericServiceProxy.getSqlMesTemplate().update(sql, keyValue);

        //case not found
        if (result == 0) {
            throw new nanoFrameDBErrorSignal(ErrorSignal.NotFoundSignal, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql, param, CTORMUtil.getLogger()));
        }
    }

    public void insert(DATA dataInfo) throws nanoFrameDBErrorSignal {
        String tableName = CTORMUtil.getTableNameByClassName(dataInfo.getClass());

        //String sql = "insert into tableName （字段1，字段N，timestamp\systemtime）values (？，？，sysdata，？)
        String sql = CTORMUtil.getInsertSql(dataInfo.getClass(), tableName);

        //generate bind parameter
        //by sequence
        List<Object> keySet = CTORMUtil.makeKeyParam(dataInfo);
        String param = CommonUtil.toStringFromCollection(keySet.toArray());
        if (!CTORMUtil.isKeyParamMatch(dataInfo, keySet.toArray()))
            throw new nanoFrameDBErrorSignal(ErrorSignal.NullPointKeySignal, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql, param, CTORMUtil.getLogger()));

        List<Object> bindSet = CTORMUtil.makeNonKeyParam(dataInfo);
        keySet.addAll(bindSet);
        param = CommonUtil.toStringFromCollection(bindSet.toArray());

        //query with just one
        int result = GenericServiceProxy.getSqlMesTemplate().update(sql, keySet.toArray());

        //case not found
        if (result == 0) {
            throw new nanoFrameDBErrorSignal(ErrorSignal.InvalidQueryState, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql, param, CTORMUtil.getLogger()));
        }
    }

    public static Object ormExecute(Object dataObject, List<ListOrderedMap> resultList)
            throws nanoFrameErrorSignal {
        Object dataInfo = null;
        List<Object> resultDataInfos = null;

        for (int j = 0; j < resultList.size(); j++) {
            //with pure data
            dataInfo = CTORMUtil.createDataInfo(dataObject.getClass());
            setProperties(dataInfo, (Map<String, Object>) resultList.get(j));

            if (resultList.size() > 1) {
                if (resultDataInfos == null)
                    resultDataInfos = new ArrayList<Object>();
                resultDataInfos.add(dataInfo);
            }
        }

        resultList.clear();//从列表中移除所有元素

        if (resultDataInfos != null)
            return resultDataInfos;
        else
            return dataInfo;
    }

    /**
     * 若有值，则给它赋值，timestamp设置为空，其他值不处理
     * @param dataInfo
     * @param dataMap
     */
    private static void setProperties(Object dataInfo, Map<String, Object> dataMap) {
        for (Field column : dataInfo.getClass().getDeclaredFields()) {
            if (column.isAnnotationPresent(CTORMTemplate.class)) {
                CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);

                String name = annotation.name();
                String dataType = annotation.dataType();

                Object resultValue = dataMap.get(name);
                //class to object converting
                if (resultValue != null)
                    ObjectUtil.setFieldValue(dataInfo, name, resultValue);
                else if (resultValue == null && dataType.equalsIgnoreCase("timestamp"))
                    ObjectUtil.setFieldValue(dataInfo, name, null);
            }
        }
    }


    public void addHistory(EventInfo eventInfo, String historyEntityName, DATA dataInfo, Log logger) {
        try {
            if (StringUtil.isEmpty(historyEntityName))
                return;
            else
                historyEntityName = CTORMUtil.getHeader(dataInfo.getClass()) + historyEntityName;

            insertHistory(eventInfo, historyEntityName, dataInfo);
        } catch (nanoFrameDBErrorSignal ne) {
            if (logger.isDebugEnabled()) logger.debug(ne);
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) logger.debug(ex);
        }
    }

    private void insertHistory(EventInfo eventInfo, String historyEntityName, DATA dataInfo)
            throws nanoFrameDBErrorSignal {
        List<Object> bindList = new ArrayList<Object>();
        List<String> attrList = new ArrayList<String>();

        //make attribute list
        for (Field column : dataInfo.getClass().getDeclaredFields()) {
            if (column.isAnnotationPresent(CTORMTemplate.class)) {
                CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);

                String name = annotation.name();
                String initial = annotation.initial();
                //mirror table must be consistent with data type
                String mirror = annotation.history();

                Object value = ObjectUtil.getFieldValue(dataInfo, column.getName());

                if (value == null
                        || initial.equalsIgnoreCase("never")
                        || mirror.equalsIgnoreCase(kr.co.aim.messolution.generic.GenericServiceProxy.getConstantMap().Flag_N))
                    continue;

                if (StringUtil.isNotEmpty(mirror))
                    attrList.add(mirror);
                else
                    attrList.add(name);

                //set value, reflect all at current and assure sequence
                bindList.add(value);
            }
        }

        {//prerequisite for trace
            //who
            attrList.add("eventUser");
            bindList.add(eventInfo.getEventUser());
            //when
            attrList.add("eventTime");
            bindList.add(eventInfo.getEventTime());
            //where(in mirror)
            //what
            attrList.add("eventName");
            bindList.add(eventInfo.getEventName());
            //how, why
            attrList.add("eventComment");
            bindList.add(eventInfo.getEventComment());

            //trace ID
            attrList.add("timeKey");
            bindList.add(StringUtil.isEmpty(eventInfo.getEventTimeKey()) ? TimeUtils.getCurrentEventTimeKey() : eventInfo.getEventTimeKey());
        }

        //query generation
        StringBuffer sql = new StringBuffer();
        {
            StringBuffer bindBuilder = new StringBuffer();

            sql.append("INSERT").append(" ").append("INTO").append(" ").append(historyEntityName).append(" ");
            sql.append("(");

            for (int idx = 0; idx < attrList.size(); idx++) {
                sql.append(attrList.get(idx));

                if (idx < attrList.size() - 1)
                    sql.append(",");

                //bind value is equal with size
                bindBuilder.append("?");
                if (idx < attrList.size() - 1)
                    bindBuilder.append(",");
            }

            sql.append(")");
            sql.append(" VALUES ").append("(").append(bindBuilder).append(")");
        }

        //radical validation
        List<Object> keySet = CTORMUtil.makeKeyParam(dataInfo);
        String param = CommonUtil.toStringFromCollection(keySet.toArray());
        if (!CTORMUtil.isKeyParamMatch(dataInfo, keySet.toArray()))
            throw new nanoFrameDBErrorSignal(ErrorSignal.NullPointKeySignal, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql.toString(), param, CTORMUtil.getLogger()));

        int result = GenericServiceProxy.getSqlMesTemplate().update(sql.toString(), bindList.toArray());

        //case not found
        if (result == 0) {
            param = CommonUtil.toStringFromCollection(bindList.toArray());
            throw new nanoFrameDBErrorSignal(ErrorSignal.InvalidQueryState, param,
                    SQLLogUtil.getLogFormatSqlStatement(sql.toString(), param, CTORMUtil.getLogger()));
        }
    }
}
