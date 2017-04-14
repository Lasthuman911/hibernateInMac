package kr.co.aim.nanoframe.orm;

import java.util.ArrayList;
import java.util.List;

import kr.co.aim.nanoframe.nanoFrameServiceProxy;
import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameDBErrorSignal;
import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanoframe.orm.support.OrmStandardEngineUtil;
import kr.co.aim.nanoframe.util.object.ObjectUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;

public class OrmStandardEngine<KEY extends KeyInfo, DATA extends DataInfo> implements ApplicationContextAware {
    protected ApplicationContext ac;
    private static Log log = LogFactory.getLog(OrmStandardEngine.class);

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.ac = arg0;
    }

    public ApplicationContext getApplicationContext() {
        return this.ac;
    }

    public OrmStandardEngine() {
    }

    public List<DATA> transform(List resultList, Class clazz) {
        if (resultList.size() == 0)
            return null;

        Object result = OrmStandardEngineUtil.ormExecute(OrmStandardEngineUtil.createDataInfo(clazz), resultList);

        if (result instanceof List)
            return (List<DATA>) result;
        else {
            List<DATA> resultSet = new ArrayList<DATA>();
            resultSet.add((DATA) result);
            return resultSet;
        }
    }

    /**
     * wzm:
     * @param condition
     * @param bindSet
     * @param clazz
     * @return
     * @throws nanoFrameDBErrorSignal
     */
    public List<DATA> select(String condition, Object[] bindSet, Class clazz) throws nanoFrameDBErrorSignal {
        String tableName = OrmStandardEngineUtil.getTableNameByClassName(clazz);
        if (tableName == null || tableName.length() == 0)
            tableName = OrmStandardEngineUtil.getTableNameByClassName(this.getClass());
        String sql = "select * from " + tableName;//这里可以进行优化
        if (StringUtils.isNotEmpty(condition)) {
            sql = OrmStandardEngineUtil.getConditionSql(sql, condition);
        }

        List resultList = queryForList(sql, bindSet);
        if (resultList.size() == 0)
            throw new nanoFrameDBErrorSignal(ErrorSignal.NotFoundSignal, ObjectUtil.getString(bindSet),
                    SQLLogUtil.getLogFormatSqlStatement(sql, bindSet, log));
        /**
         * wzm:对resultList 进行了进一步处理
         */
        Object result = OrmStandardEngineUtil.ormExecute(OrmStandardEngineUtil.createDataInfo(clazz), resultList);

        if (result instanceof List)
            return (List<DATA>) result;
        else if (result instanceof DataInfo) {
            List<DATA> resultSet = new ArrayList<DATA>();
            resultSet.add((DATA) result);
            return resultSet;
        } else
            throw new nanoFrameDBErrorSignal(ErrorSignal.CouldNotMatchData, ObjectUtil.getString(bindSet),
                    SQLLogUtil.getLogFormatSqlStatement(sql, bindSet, log), condition);
    }

    public DATA selectByKeyForUpdate(KEY keyInfo) throws nanoFrameDBErrorSignal {
        return (DATA) execute("select.for.update", keyInfo);
    }

    public DATA selectByKey(KEY keyInfo) throws nanoFrameDBErrorSignal {
        return (DATA) execute("select", keyInfo);
    }

    public int update(DATA dataInfo, String condition, Object[] bindSet) throws nanoFrameDBErrorSignal {
        String sql = OrmStandardEngineUtil.generateSqlStatement("update", dataInfo);
        int idx = sql.indexOf(" where ");
        sql = sql.substring(0, idx + 1);
        sql = OrmStandardEngineUtil.getConditionSql(sql, condition);
        return executeQuery(sql, bindSet, ObjectUtil.getString(OrmStandardEngineUtil.getKeyInfo(dataInfo)));
    }

    public int update(DATA dataInfo) throws nanoFrameDBErrorSignal {
        return (Integer) execute("update", dataInfo);
    }

    public int insert(DATA dataInfo) throws nanoFrameDBErrorSignal {
        return (Integer) execute("insert", dataInfo);
    }

    public int delete(KEY keyInfo) throws nanoFrameDBErrorSignal {
        return (Integer) execute("delete", keyInfo);
    }

    public int delete(String condition, Object[] bindSet, Class clazz) throws nanoFrameDBErrorSignal {
        String tableName = OrmStandardEngineUtil.getTableNameByClassName(clazz);
        if (tableName == null || tableName.length() == 0)
            tableName = OrmStandardEngineUtil.getTableNameByClassName(this.getClass());

        String sql = "delete " + tableName;
        sql = OrmStandardEngineUtil.getConditionSql(sql, condition);
        return executeQuery(sql, bindSet, ObjectUtil.getString(condition, bindSet));
    }

    /*
     * [Insert/Update]
     * 1. data狼 Field 蔼捞 null牢 版快
     * 1.1. Date, TimeStamp 包访 屈狼 版快, SYSDATE肺 蔼捞 盲况咙.
     * 1.2. 弊 寇狼 data 屈狼 版快, DML 措惑俊辑 力寇 凳.
     * 弊矾唱 StandardType篮 积己 矫 String, Primitive 箭磊屈 殿篮 檬扁拳 登扁 锭巩俊, 荤侩磊啊 烙狼肺 null阑 汲沥窍瘤 臼绰 茄 null老 版快 绝澜.
     * 1.3. Insert狼 版快, UDF捞搁辑
     * ObjectAttributeDef俊 沥狼茄 Default 蔼捞 粮犁窍搁 秦寸 蔼阑 爱霸 凳
     *
     * 2. String 屈 棺 UDF狼 蔼捞 "" 牢 版快, 秦寸 Field狼 蔼篮 瘤盔瘤霸 凳. (搬惫 DB狼 Field绰 null捞 凳)
     */
    protected Object execute(String queryType, Object dataObject) throws nanoFrameDBErrorSignal {
        List resultList = null;
        int resultSize = 0;
        String sql = "";
        try {
            if (queryType.equalsIgnoreCase("select") || queryType.equalsIgnoreCase("select.for.update")) {
                KeyInfo keyInfo = OrmStandardEngineUtil.getKeyInfo(dataObject);
                if (ObjectUtil.isNullOrNullString(keyInfo))
                    throw new nanoFrameDBErrorSignal(ErrorSignal.NullPointKeySignal, ObjectUtil.getString(keyInfo),
                            queryType, "Key can't be null");

                sql = OrmStandardEngineUtil.generateSqlStatement("select", dataObject);
                if (queryType.equalsIgnoreCase("select.for.update"))
                    sql = sql + " for update";
                resultList = queryForList(sql, OrmStandardEngineUtil.getSelectOrDeleteBindObjects(dataObject));

                if (resultList.size() == 0)
                    throw new nanoFrameDBErrorSignal(ErrorSignal.NotFoundSignal,
                            ObjectUtil.getString(OrmStandardEngineUtil.getKeyInfo(dataObject)),
                            SQLLogUtil.getLogFormatSqlStatement(sql,
                                    OrmStandardEngineUtil.getSelectOrDeleteBindObjects(dataObject), log));
            } else {
                sql = OrmStandardEngineUtil.generateSqlStatement(queryType, dataObject);
                if (queryType.equalsIgnoreCase("delete"))
                    resultSize =
                            update(sql, OrmStandardEngineUtil.getSelectOrDeleteBindObjects(dataObject),
                                    ObjectUtil.getString(OrmStandardEngineUtil.getKeyInfo(dataObject)));
                else if (queryType.equalsIgnoreCase("update"))
                    resultSize =
                            update(sql, OrmStandardEngineUtil.getUpdateBindObjects(dataObject),
                                    ObjectUtil.getString(OrmStandardEngineUtil.getKeyInfo(dataObject)));
                else if (queryType.equalsIgnoreCase("insert"))
                    resultSize =
                            update(sql, OrmStandardEngineUtil.getInsertBindObjects(dataObject),
                                    ObjectUtil.getString(OrmStandardEngineUtil.getKeyInfo(dataObject)));
                else {
                    throw new nanoFrameDBErrorSignal(ErrorSignal.InvalidQueryType, queryType);
                }

                if (resultSize == 0) {
                    if (queryType.equalsIgnoreCase("delete") || queryType.equalsIgnoreCase("update"))
                        throw new nanoFrameDBErrorSignal(ErrorSignal.NotFoundSignal,
                                ObjectUtil.getString(OrmStandardEngineUtil.getKeyInfo(dataObject)),
                                SQLLogUtil.getLogFormatSqlStatement(sql,
                                        OrmStandardEngineUtil.getSelectOrDeleteBindObjects(dataObject), log));
                    else
                        throw new nanoFrameDBErrorSignal(ErrorSignal.InvalidQueryState,
                                ObjectUtil.getString(OrmStandardEngineUtil.getKeyInfo(dataObject)),
                                SQLLogUtil.getLogFormatSqlStatement(sql,
                                        OrmStandardEngineUtil.getSelectOrDeleteBindObjects(dataObject), log));
                }
                return resultSize;
            }
        } catch (DataAccessException e) {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(OrmStandardEngineUtil.getKeyInfo(dataObject)),
                    sql);
        }
        return OrmStandardEngineUtil.ormExecute(dataObject, resultList);
    }

    private int executeQuery(String sql, Object[] bindSet, String keyString) throws nanoFrameDBErrorSignal {
        int resultSize = update(sql, bindSet, keyString);
        if (resultSize == 0)
            throw new nanoFrameDBErrorSignal(ErrorSignal.NotFoundSignal, ObjectUtil.getString(bindSet),
                    SQLLogUtil.getLogFormatSqlStatement(sql, bindSet, log));
        return resultSize;
    }

    /**
     * wzm:其实就是对jdbcTemplate 做了一次封装
     * @param sql
     * @param args
     * @return
     * @throws nanoFrameDBErrorSignal
     */
    private List queryForList(String sql, Object... args) throws nanoFrameDBErrorSignal {
        List resultList = null;
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);

        try {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (ArrayUtils.isEmpty(args))
                resultList = nanoFrameServiceProxy.getSqlTemplate().getJdbcTemplate().queryForList(sql);
            else {
                if (args.length == 1 && args[0] == null)
                    resultList = nanoFrameServiceProxy.getSqlTemplate().getJdbcTemplate().queryForList(sql);
                else
                    resultList = nanoFrameServiceProxy.getSqlTemplate().getJdbcTemplate().queryForList(sql, args);
            }
        } catch (DataAccessException e) {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterQuery(resultList, log);
        return resultList;
    }

    private int update(String sql, Object[] args, String keyString) throws nanoFrameDBErrorSignal {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);
        int result = 0;

        try {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null)
                result = nanoFrameServiceProxy.getSqlTemplate().getJdbcTemplate().update(sql);
            else
                result = nanoFrameServiceProxy.getSqlTemplate().getJdbcTemplate().update(sql, args);
        } catch (DataAccessException e) {
            if (keyString != null) {
                throw ErrorSignal.getNotifyException(e, keyString, logFormatSql);
            } else {
                throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
            }
        }

        SQLLogUtil.logAfterUpdate(result, log);
        return result;
    }

}
