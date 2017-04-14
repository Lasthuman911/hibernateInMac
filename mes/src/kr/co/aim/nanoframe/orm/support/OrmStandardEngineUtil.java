package kr.co.aim.nanoframe.orm.support;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.aim.nanoframe.nanoFrameServiceProxy;
import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameErrorSignal;
import kr.co.aim.nanoframe.orm.ObjectAttributeDef;
import kr.co.aim.nanoframe.orm.ObjectAttributeMap;
import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanoframe.util.object.ObjectUtil;
import kr.co.aim.nanoframe.util.time.TimeStampUtil;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OrmStandardEngineUtil
{
    private static Log	log	= LogFactory.getLog(OrmStandardEngineUtil.class);

    public static String getTableName(Object dataKeyInfo)
    {
        String tableName = dataKeyInfo.getClass().getSimpleName();
        if (dataKeyInfo instanceof KeyInfo)
        {
            tableName = tableName.substring(0, tableName.length() - 3);
        }
        return tableName;
    }

    public static String getServiceName(String serviceName)
    {
        if (serviceName.endsWith("Impl"))
            return serviceName.substring(0, serviceName.length() - 4);
        return serviceName;
    }

    public static String getTableNameByClassName(Class clazz)
    {
        String tableName = clazz.getSimpleName();
        if (tableName.endsWith("Key"))
            tableName = tableName.substring(0, tableName.length() - 3);
        else if (tableName.endsWith("ServiceImpl"))
            tableName = tableName.substring(0, tableName.length() - 11);
        else if (tableName.endsWith("Service"))
            tableName = tableName.substring(0, tableName.length() - 7);

        return tableName;
    }

    public static String getMethodName(String attributeName)
    {
        String a = attributeName.substring(0, 1);
        String b = attributeName.substring(1, attributeName.length());
        return a.toUpperCase() + b;
    }

    public static String getFieldName(String attributeName)
    {
        String a = attributeName.substring(0, 1);
        String b = attributeName.substring(1, attributeName.length());
        return a.toLowerCase() + b;
    }

    public static String getPolicyPackageName(DataInfo dataInfo, String className)
    {
        String dataClassName = dataInfo.getClass().getName();
        String[] names = org.springframework.util.StringUtils.delimitedListToStringArray(dataClassName, ".");
        String policyPackageName = "";
        for (int i = 0; i < names.length; i++)
        {
            if (names[i].equalsIgnoreCase("data"))
            {
                if (className == null || className.length() == 0)
                    return policyPackageName + "policy";
                else
                    return policyPackageName + "policy" + "." + className;
            }
            policyPackageName = policyPackageName + names[i] + ".";
        }
        return policyPackageName;
    }

    public static String getHistoryPackageName(DataInfo dataInfo)
    {
        String dataClassName = dataInfo.getClass().getName();
        String[] names = org.springframework.util.StringUtils.delimitedListToStringArray(dataClassName, ".");
        String policyPackageName = "";
        for (int i = 0; i < names.length; i++)
        {
            if (names[i].equalsIgnoreCase("data"))
            {
                return policyPackageName + dataInfo.getClass().getSimpleName() + "HistoryService";
            }
            policyPackageName = policyPackageName + names[i] + ".";
        }
        return policyPackageName;
    }

    public static String getHistoryAdaptorPackageName(DataInfo dataInfo)
    {
        return getPolicyPackageName(dataInfo, null)
                + ".util."
                + dataInfo.getClass().getSimpleName()
                + "HistoryDataAdaptor";
    }

    public static String getVersionAttributeName(KeyInfo keyInfo)
    {
        Field[] fields = keyInfo.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
        {
            if (fields[i].getName().toLowerCase().endsWith("version"))
                return fields[i].getName();
        }
        return null;
    }

    public static String getSpecNameAttributeName(KeyInfo keyInfo)
    {
        Field[] fields = keyInfo.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++)
        {
            if (!fields[i].getName().toLowerCase().endsWith("version"))
            {
                if (!fields[i].getName().equalsIgnoreCase("factoryname"))
                    return fields[i].getName();
            }
        }
        return null;
    }

    /**
     * wzm:
     * @param dataObject
     * @return
     */
    public static DataInfo createDataInfo(Object dataObject)
    {
        Class clazz = null;
        // Data 积己
        DataInfo createdInfo = null;
        try
        {
            if (dataObject instanceof DataInfo)
            {
                clazz = Class.forName(dataObject.getClass().getName());
            }
            else if (dataObject instanceof KeyInfo)
            {
                clazz =
                        Class.forName(dataObject.getClass().getName().substring(0,
                                dataObject.getClass().getName().length() - 3));
            }
            createdInfo = (DataInfo) clazz.newInstance();
        } catch (Exception e)
        {
            log.warn(e, e);
        }

        KeyInfo createdKeyInfo = OrmStandardEngineUtil.getKeyInfo(createdInfo);
        if (createdKeyInfo == null)
        {
            try
            {
                if (dataObject instanceof DataInfo)
                {
                    clazz = Class.forName(dataObject.getClass().getName() + "Key");
                }
                else if (dataObject instanceof KeyInfo)
                {
                    clazz = Class.forName(dataObject.getClass().getName());
                }
                createdKeyInfo = (KeyInfo) clazz.newInstance();
            } catch (Exception e)
            {
                log.warn(e, e);
            }
            ObjectUtil.setFieldValue(createdInfo, "Key", createdKeyInfo);
        }

        if (dataObject instanceof KeyInfo)
            ObjectUtil.copyField(createdInfo, "key", dataObject);

        String tableName = OrmStandardEngineUtil.getTableName(dataObject);

        List<ObjectAttributeDef> ObjectAttributeDefs =
                nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                        ObjectAttributeMap.ExtendedC_Type);
        if (ObjectAttributeDefs != null)
        {
            Map<String, String> udfs = new HashMap<String, String>();
            for (ObjectAttributeDef attributeDef : ObjectAttributeDefs)
            {
                if (StringUtils.isNotEmpty(attributeDef.getDefaultValue()))
                    udfs.put(attributeDef.getAttributeName(), attributeDef.getDefaultValue());
                else
                    udfs.put(attributeDef.getAttributeName(), "");
            }
            ObjectUtil.setFieldValue(createdInfo, "udfs", udfs);
        }
        return createdInfo;
    }

    /**
     * wzm: 创建这个Class 的pure 对象
     * @param clazz
     * @return
     * @throws nanoFrameErrorSignal
     */
    public static DataInfo createDataInfo(Class clazz) throws nanoFrameErrorSignal
    {
        String className = clazz.getName();
        if (className.endsWith("Key"))
            className = className.substring(0, className.length() - 3);
        else if (className.endsWith("ServiceImpl"))
            className = className.substring(0, className.length() - 11);
        else if (className.endsWith("Service"))
            className = className.substring(0, className.length() - 7);

        className = className.replaceFirst(".impl.", ".data.");
        try
        {
            return (DataInfo) Class.forName(className).newInstance();
        } catch (Exception e)
        {
            throw new nanoFrameErrorSignal(ErrorSignal.UnexprectedSignal, e);
        }
    }

    /**
     * wzm:
     * @param dataInfo
     * @return
     */
    public static KeyInfo getKeyInfo(Object dataInfo)
    {
        if (dataInfo instanceof DataInfo)
        {
            try
            {
                return ((DataInfo) dataInfo).getKey();
            } catch (Exception e)
            {
                log.warn(e, e);
            }
            return null;
        }
        else if (dataInfo instanceof KeyInfo)
            return (KeyInfo) dataInfo;
        else
            return null;
    }

    public static Object[] getSelectOrDeleteBindObjects(Object dataKeyInfo)
    {
        String tableName = getTableName(dataKeyInfo);

        KeyInfo keyInfo = null;
        if (dataKeyInfo instanceof DataInfo)
        {
            keyInfo = getKeyInfo((DataInfo) dataKeyInfo);
        }
        else if (dataKeyInfo instanceof KeyInfo)
        {
            keyInfo = (KeyInfo) dataKeyInfo;
        }

        List<Object> objectValue = new ArrayList<Object>();
        List<ObjectAttributeDef> ObjectAttributeDefs =
                nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                        ObjectAttributeMap.Standard_Type);
        for (ObjectAttributeDef attributeDef : ObjectAttributeDefs)
        {
            if (attributeDef.getPrimaryKeyFlag().equalsIgnoreCase("y"))
            {
                objectValue.add(ObjectUtil.getFieldValue(keyInfo, attributeDef.getAttributeName()));
            }
            else
            {
                if (dataKeyInfo instanceof KeyInfo)
                    break;
            }
        }
        return objectValue.toArray();
    }

    // ObjectAttributeDef俊辑 DefaultValue甫 汲沥茄 版快, Default Value甫 汲沥秦具 窃
    public static Object[] getInsertBindObjects(Object dataKeyInfo) throws nanoFrameErrorSignal
    {
        String tableName = getTableName(dataKeyInfo);

        KeyInfo keyInfo = null;
        if (dataKeyInfo instanceof DataInfo)
        {
            keyInfo = getKeyInfo((DataInfo) dataKeyInfo);
        }
        else if (dataKeyInfo instanceof KeyInfo)
        {
            keyInfo = (KeyInfo) dataKeyInfo;
            throw new nanoFrameErrorSignal(ErrorSignal.InvalidArgumentSignal, dataKeyInfo.getClass().getSimpleName());
        }

        List<Object> objectValue = new ArrayList<Object>();

        List<ObjectAttributeDef> ObjectAttributeDefs =
                nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                        ObjectAttributeMap.Standard_Type);
        for (ObjectAttributeDef attributeDef : ObjectAttributeDefs)
        {
            if (attributeDef.getAttributeName().equalsIgnoreCase("systemtime")
                    && attributeDef.getDataType().equalsIgnoreCase("timestamp"))
                continue;

            if (attributeDef.getPrimaryKeyFlag().equalsIgnoreCase("y"))
            {
                Object value = ObjectUtil.getFieldValue(keyInfo, attributeDef.getAttributeName());
                if (value == null)
                    throw new nanoFrameErrorSignal(ErrorSignal.InvalidArgumentSignal, String.format(
                            "Input Object[%s] is not have attribute(%s).", ObjectUtil.getString(keyInfo),
                            attributeDef.getAttributeName()));
                else
                    objectValue.add(value);
            }
            else
            {
                if (checkNotNullValue(dataKeyInfo, attributeDef, null))
                    objectValue.add(ObjectUtil.getFieldValue(dataKeyInfo, attributeDef.getAttributeName()));
                else if (StringUtils.isNotEmpty(attributeDef.getDefaultValue())) // DefaultValue 瘤盔
                    objectValue.add(getValueDataType(attributeDef, attributeDef.getDefaultValue()));
            }
        }

        ObjectAttributeDefs =
                nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                        ObjectAttributeMap.ExtendedC_Type);
        if (ObjectAttributeDefs != null && ObjectAttributeDefs.size() > 0)
        {
            Map<String, String> udfs = ObjectUtil.getUdfsValue((DataInfo) dataKeyInfo);
            for (ObjectAttributeDef attributeDef : ObjectAttributeDefs)
            {
                if (checkNotNullValue(dataKeyInfo, attributeDef, udfs))
                {
                    objectValue.add(getValueDataType(attributeDef, (String) ObjectUtil.getUdfFieldValue(dataKeyInfo,
                            attributeDef.getAttributeName())));
                }
                else if (StringUtils.isNotEmpty(attributeDef.getDefaultValue())) // DefaultValue 瘤盔
                    objectValue.add(getValueDataType(attributeDef, attributeDef.getDefaultValue()));
            }
        }
        return objectValue.toArray();
    }

    public static Object[] getUpdateBindObjects(Object dataKeyInfo) throws nanoFrameErrorSignal
    {
        String tableName = getTableName(dataKeyInfo);

        KeyInfo keyInfo = null;
        if (dataKeyInfo instanceof DataInfo)
        {
            keyInfo = getKeyInfo((DataInfo) dataKeyInfo);
        }
        else if (dataKeyInfo instanceof KeyInfo)
        {
            keyInfo = (KeyInfo) dataKeyInfo;
        }

        List<Object> objectValue = new ArrayList<Object>();
        List<Object> objectPrimaryValue = new ArrayList<Object>();

        List<ObjectAttributeDef> ObjectAttributeDefs =
                nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                        ObjectAttributeMap.Standard_Type);
        for (ObjectAttributeDef attributeDef : ObjectAttributeDefs)
        {
            if (attributeDef.getPrimaryKeyFlag().equalsIgnoreCase("y"))
            {
                objectPrimaryValue.add(ObjectUtil.getFieldValue(keyInfo, attributeDef.getAttributeName()));
            }
            else
            {
                if (checkNotNullValue(dataKeyInfo, attributeDef, null))
                    objectValue.add(ObjectUtil.getFieldValue(dataKeyInfo, attributeDef.getAttributeName()));
            }
        }

        ObjectAttributeDefs =
                nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                        ObjectAttributeMap.ExtendedC_Type);
        if (ObjectAttributeDefs != null && ObjectAttributeDefs.size() > 0)
        {
            Map<String, String> udfs = ObjectUtil.getUdfsValue((DataInfo) dataKeyInfo);
            for (ObjectAttributeDef attributeDef : ObjectAttributeDefs)
            {
                if (checkNotNullValue(dataKeyInfo, attributeDef, udfs))
                {
                    objectValue.add(getValueDataType(attributeDef, (String) ObjectUtil.getUdfFieldValue(dataKeyInfo,
                            attributeDef.getAttributeName())));
                }
            }
        }

        for (int i = 0; i < objectPrimaryValue.size(); i++)
        {
            objectValue.add(objectPrimaryValue.get(i));
        }
        return objectValue.toArray();
    }

    public static Object getValueDataType(ObjectAttributeDef objectAttributeDef, String value)
    {
        if (objectAttributeDef.getDataType().equalsIgnoreCase("String"))
            return value;
        else if (objectAttributeDef.getDataType().equalsIgnoreCase("Double"))
            return Double.parseDouble(value);
        else if (objectAttributeDef.getDataType().equalsIgnoreCase("Long"))
        {
            int index = StringUtils.indexOf(value, ".");
            if (index >= 0)
                value = StringUtils.substring(value, 0, index);
            return Long.parseLong(value);
        }
        else if (objectAttributeDef.getDataType().equalsIgnoreCase("TimeStamp"))
            return TimeStampUtil.getTimestamp(value);
        return "";
    }

    public static Class[] getClassType(Object[] args)
    {
        Class[] classTypes = null;
        if (args != null)
        {
            classTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++)
            {
                if (args[i] != null)
                {
                    if (args[i].getClass().getSimpleName().equalsIgnoreCase("integer"))
                        classTypes[i] = Integer.TYPE;
                    else if (args[i].getClass().getSimpleName().equalsIgnoreCase("boolean"))
                        classTypes[i] = Boolean.TYPE;
                    else if (args[i].getClass().getSimpleName().equalsIgnoreCase("byte"))
                        classTypes[i] = Byte.TYPE;
                    else if (args[i].getClass().getSimpleName().equalsIgnoreCase("double"))
                        classTypes[i] = Double.TYPE;
                    else if (args[i].getClass().getSimpleName().equalsIgnoreCase("long"))
                        classTypes[i] = Long.TYPE;
                    else
                    {
                        if (args[i] instanceof BigDecimal)
                        {
                            classTypes[i] = Long.TYPE;
                            args[i] = Long.valueOf(args[i].toString());
                        }
                        else
                        {
                            classTypes[i] = args[i].getClass();
                        }
                    }
                }
            }
        }
        return classTypes;
    }

    public static List<DataInfo> ormExecute(Class clazz, List<ListOrderedMap> resultList) throws nanoFrameErrorSignal
    {
        String tableName = clazz.getSimpleName();
        String[] attributeNameTypes =
                new String[] { ObjectAttributeMap.Standard_Type, ObjectAttributeMap.ExtendedC_Type };
        DataInfo dataInfo = null;
        List<DataInfo> resultDataInfos = new ArrayList<DataInfo>();
        ObjectAttributeMap objectAttributeMapService = nanoFrameServiceProxy.getObjectAttributeMap();
        for (int j = 0; j < resultList.size(); j++)
        {
            for (int i = 0; i < attributeNameTypes.length; i++)
            {
                List<ObjectAttributeDef> ObjectAttributeDefs =
                        objectAttributeMapService.getAttributeNames(tableName, attributeNameTypes[i]);
                ListOrderedMap orderMap = (ListOrderedMap) resultList.get(j);
                if (attributeNameTypes[i].equalsIgnoreCase(ObjectAttributeMap.Standard_Type))
                {
                    dataInfo = (DataInfo) OrmStandardEngineUtil.createDataInfo(clazz);
                    setStandardProperties(dataInfo, orderMap, ObjectAttributeDefs);
                }
                else
                    setExtendedCProperties(dataInfo, orderMap, ObjectAttributeDefs);
            }
            resultDataInfos.add(dataInfo);
        }
        resultList.clear();
        return resultDataInfos;
    }

    /**
     * 和annotation （扩展表）的处理方式是类似的
     * @param dataObject
     * @param resultList
     * @return
     * @throws nanoFrameErrorSignal
     */
    public static Object ormExecute(Object dataObject, List<ListOrderedMap> resultList) throws nanoFrameErrorSignal
    {
        String tableName = OrmStandardEngineUtil.getTableName(dataObject);
        String[] attributeNameTypes =
                new String[] { ObjectAttributeMap.Standard_Type, ObjectAttributeMap.ExtendedC_Type };
        DataInfo dataInfo = null;
        List<DataInfo> resultDataInfos = null;
        ObjectAttributeMap objectAttributeMapService = nanoFrameServiceProxy.getObjectAttributeMap();
        for (int j = 0; j < resultList.size(); j++)
        {
            for (int i = 0; i < attributeNameTypes.length; i++)//对标准字段和扩展字段分别进行处理
            {
                List<ObjectAttributeDef> ObjectAttributeDefs =
                        objectAttributeMapService.getAttributeNames(tableName, attributeNameTypes[i]);
                ListOrderedMap orderMap = (ListOrderedMap) resultList.get(j);
                if (attributeNameTypes[i].equalsIgnoreCase(ObjectAttributeMap.Standard_Type))
                {
                    dataInfo = OrmStandardEngineUtil.createDataInfo(dataObject);
                    setStandardProperties(dataInfo, orderMap, ObjectAttributeDefs);
                }
                else
                    setExtendedCProperties(dataInfo, orderMap, ObjectAttributeDefs);
            }
            if (resultList.size() > 1)
            {
                if (resultDataInfos == null)
                    resultDataInfos = new ArrayList<DataInfo>();
                resultDataInfos.add(dataInfo);
            }

        }
        resultList.clear();
        if (resultDataInfos != null)
            return resultDataInfos;
        else
            return dataInfo;
    }

    /**
     * wzm:
     * @param dataInfo
     * @param dataMap
     * @param attributeDefs
     */
    private static void setStandardProperties(DataInfo dataInfo, Map<String, Object> dataMap,
                                              List<ObjectAttributeDef> attributeDefs)
    {
        for (ObjectAttributeDef attributeDef : attributeDefs)
        {
            Object resultValue = dataMap.get(attributeDef.getAttributeName());
            if (attributeDef.getPrimaryKeyFlag().equalsIgnoreCase("y"))
                OrmStandardEngineUtil.setPropertyValueIntoKeyInfo(dataInfo, attributeDef.getAttributeName(),
                        resultValue);
            else
            {
                if (resultValue != null)
                    ObjectUtil.setFieldValue(dataInfo, attributeDef.getAttributeName(), resultValue);
                else if (resultValue == null && attributeDef.getDataType().equalsIgnoreCase("timestamp"))
                    ObjectUtil.setFieldValue(dataInfo, attributeDef.getAttributeName(), null);
            }
        }
    }

    /**
     * wzm:
     * @param dataInfo
     * @param dataMap
     * @param attributeDefs
     */
    private static void setExtendedCProperties(DataInfo dataInfo, Map<String, Object> dataMap,
                                               List<ObjectAttributeDef> attributeDefs)
    {
        if (attributeDefs == null)
            return;
        Map<String, String> udfs = null;
        udfs = ObjectUtil.getUdfsValue(dataInfo);
        if (udfs == null)
            udfs = new HashMap<String, String>();

        for (ObjectAttributeDef attributeDef : attributeDefs)
        {
            Object resultValue = dataMap.get(attributeDef.getAttributeName());

            if (resultValue != null)
            {
                udfs.put(attributeDef.getAttributeName(), resultValue.toString());
            }
            else if (resultValue == null && attributeDef.getDataType().equalsIgnoreCase("timestamp"))
                udfs.put(attributeDef.getAttributeName(), null);
        }
    }

    /**
     * wzm:
     * @param dataInfo
     * @param fieldName
     * @param value
     */
    private static void setPropertyValueIntoKeyInfo(DataInfo dataInfo, String fieldName, Object value)
    {
        try
        {
            ObjectUtil.setFieldValue(getKeyInfo(dataInfo), fieldName, value);
        } catch (Exception e)
        {
            log.warn(e, e);
        }
    }

    public static String generateSqlStatement(String queryType, Object dataKeyObject) throws nanoFrameErrorSignal
    {
        StringBuilder strBuilder = new StringBuilder();

        String tableName = OrmStandardEngineUtil.getTableName(dataKeyObject);
        if (queryType.toLowerCase().equalsIgnoreCase("select"))
        {
            strBuilder.append("select * from " + tableName);
        }
        else if (queryType.toLowerCase().equalsIgnoreCase("delete"))
        {
            strBuilder.append("delete " + tableName);
        }
        else if (queryType.toLowerCase().equalsIgnoreCase("update"))
        {
            strBuilder.append("update " + tableName).append(" set ");
        }
        else if (queryType.toLowerCase().equalsIgnoreCase("insert"))
        {
            strBuilder.append("insert ").append("into ").append(tableName);
        }

        return generateCondition(strBuilder, queryType, tableName, dataKeyObject).toString();
    }

    public static String generateExtendedSqlStatement(String queryType, Object dataObject, String queryDetailType,
                                                      String sql, String cancelFalg) throws nanoFrameErrorSignal
    {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(sql);
        String tableName = getTableName(dataObject);
        if (queryType.equalsIgnoreCase("select") || queryType.equalsIgnoreCase("delete"))
        {
            if (queryDetailType.equalsIgnoreCase("selectLastOne")
                    || queryDetailType.equalsIgnoreCase("selectLastOneByEventName")
                    || queryDetailType.equalsIgnoreCase("deleteLastOne"))
            {
                strBuilder.delete(strBuilder.length() - 1, strBuilder.length());
                strBuilder.append("(select /*+ index_desc(");
                strBuilder.append(tableName + ", " + tableName.toUpperCase() + "_PK)" + " */ timekey ");
                strBuilder.append("from " + tableName + " where ");
                KeyInfo keyInfo = getKeyInfo(dataObject);

                List<ObjectAttributeDef> ObjectAttributeDefs =
                        nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                                ObjectAttributeMap.Standard_Type);
                if (ObjectAttributeDefs == null)
                    throw new nanoFrameErrorSignal(ErrorSignal.NotDefineObjectAttributeSignal, tableName);

                for (int i = 0; i < ObjectAttributeDefs.size(); i++)
                {
                    if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("y")
                            && !ObjectAttributeDefs.get(i).getAttributeName().equalsIgnoreCase("timekey"))
                    {
                        strBuilder.append(ObjectAttributeDefs.get(i).getAttributeName())
                                .append("=")
                                .append(" ? ")
                                .append(" and ");
                    }
                    else if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("n"))
                    {
                        strBuilder.delete(strBuilder.length() - 5, strBuilder.length());
                        break;
                    }
                }

                if (queryDetailType.equalsIgnoreCase("selectLastOneByEventName"))
                {
                    if (cancelFalg == null)
                        strBuilder.append("and eventName = ? and rownum = 1)");
                    else
                        strBuilder.append("and cancelFlag='" + cancelFalg + "'" + " and eventName = ? and rownum = 1)");
                }
                else
                {
                    if (cancelFalg == null)
                        strBuilder.append("and rownum = 1)");
                    else
                        strBuilder.append("and cancelFlag='" + cancelFalg + "'" + " and rownum = 1)");
                }
            }
            else if (queryDetailType.equalsIgnoreCase("selectBetweenTime"))
            {
                strBuilder.delete(strBuilder.length() - 10, strBuilder.length());
                strBuilder.append(" (eventTime between ? and ?) ");
                if (cancelFalg == null)
                    strBuilder.append("order by timekey");
                else
                    strBuilder.append("and cancelFlag='" + cancelFalg + "'" + " order by timekey");
            }
            else if (queryDetailType.equalsIgnoreCase("selectBetweenTimeByEventName"))
            {
                strBuilder.delete(strBuilder.length() - 10, strBuilder.length());
                strBuilder.append(" (eventTime between ? and ?) ");
                if (cancelFalg == null)
                    strBuilder.append("and eventName = ? " + " order by timekey");
                else
                    strBuilder.append("and cancelFlag='"
                            + cancelFalg
                            + "'"
                            + " and eventName = ? "
                            + " order by timekey");
            }
        }
        else if (queryType.equalsIgnoreCase("update"))
        {
            if (queryDetailType.equalsIgnoreCase("updateWithCancelFlag"))
            {
                strBuilder.append("update " + tableName + " set cancelFlag = ? ");
                strBuilder.append(" where ");
                List<ObjectAttributeDef> ObjectAttributeDefs =
                        nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                                ObjectAttributeMap.Standard_Type);
                if (ObjectAttributeDefs == null)
                    throw new nanoFrameErrorSignal(ErrorSignal.NotDefineObjectAttributeSignal, tableName);

                for (int i = 0; i < ObjectAttributeDefs.size(); i++)
                {
                    if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("y"))
                    {
                        strBuilder.append(ObjectAttributeDefs.get(i).getAttributeName())
                                .append("=")
                                .append(" ? ")
                                .append(" and ");
                    }
                    else if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("n"))
                    {
                        strBuilder.delete(strBuilder.length() - 5, strBuilder.length());
                        break;
                    }
                }
            }
        }

        return strBuilder.toString();
    }

    public static String getConditionSql(String sql, String condition)
    {
        if (condition.toLowerCase().trim().startsWith("where "))
        {
            sql = sql + " " + condition.trim();
        }
        else
        {
            sql = sql + " where " + condition;
        }
        return sql;
    }

    public static StringBuilder generateCondition(StringBuilder strBuilder, String queryType, String tableName, Object dataKeyObject) throws nanoFrameErrorSignal
    {

        List<ObjectAttributeDef> ObjectAttributeDefs = nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName, ObjectAttributeMap.Standard_Type);
        if (ObjectAttributeDefs == null)
            throw new nanoFrameErrorSignal(ErrorSignal.NotDefineObjectAttributeSignal, tableName);

        if (queryType.equalsIgnoreCase("select") || queryType.equalsIgnoreCase("delete"))
        {
            strBuilder.append(" where ");
            for (ObjectAttributeDef attributeDef : ObjectAttributeDefs)
            {
                if (attributeDef.getPrimaryKeyFlag().equalsIgnoreCase("y"))
                {
                    strBuilder.append(attributeDef.getAttributeName()).append("=").append("?");
                    strBuilder.append(" and ");
                }
            }

            if (strBuilder.toString().endsWith(" and "))
                strBuilder.delete(strBuilder.length() - 5, strBuilder.length());
            return strBuilder;
        }
        else if (queryType.equalsIgnoreCase("update"))
        {
            String where = " where ";
            List<ObjectAttributeDef> ObjectAttributeCDefs =
                    nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName, ObjectAttributeMap.ExtendedC_Type);
            Object[] attributes = new Object[] { ObjectAttributeDefs, ObjectAttributeCDefs };

            for (int h = 0; h < attributes.length; h++)
            {
                if (attributes[h] == null)
                    continue;
                List<ObjectAttributeDef> oads = (List<ObjectAttributeDef>) attributes[h];
                for (ObjectAttributeDef attributeDef : oads)
                {
                    if (attributeDef.getPrimaryKeyFlag().equalsIgnoreCase("y"))
                        where = where + attributeDef.getAttributeName() + "=" + "?" + " and ";
                    else
                    {
                        if (h == 1)
                        { // udfs
                            if (checkNotNullValue(dataKeyObject, attributeDef, ObjectUtil.getUdfsValue(dataKeyObject)))
                                strBuilder.append(attributeDef.getAttributeName()).append("=").append("?").append(",");
                            else if (attributeDef.getDataType().equalsIgnoreCase("timestamp"))
                                strBuilder.append(attributeDef.getAttributeName()).append("=").append("null").append(",");
                        }
                        else
                        {
                            if (checkNotNullValue(dataKeyObject, attributeDef, null))
                                strBuilder.append(attributeDef.getAttributeName()).append("=").append("?").append(",");
                            else if (attributeDef.getDataType().equalsIgnoreCase("timestamp"))
                                strBuilder.append(attributeDef.getAttributeName()).append("=").append("null").append(",");
                        }
                    }
                }
            }

            where = where.substring(0, where.length() - 5);
            strBuilder.delete(strBuilder.length() - 1, strBuilder.length()).append(where);

            return strBuilder;
        }
        else if (queryType.equalsIgnoreCase("insert")) // ObjectAttributeDef俊辑 DefaultValue甫 汲沥茄 版快, Default Value甫 汲沥秦具 窃
        {
            strBuilder.append(" (");

            StringBuilder strBuilder2 = new StringBuilder();
            List<ObjectAttributeDef> ObjectAttributeCDefs =
                    nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName, ObjectAttributeMap.ExtendedC_Type);
            Object[] attributes = new Object[] { ObjectAttributeDefs, ObjectAttributeCDefs };

            for (int h = 0; h < attributes.length; h++)
            {
                if (attributes[h] == null)
                    continue;

                List<ObjectAttributeDef> oads = (List<ObjectAttributeDef>) attributes[h];

                for (ObjectAttributeDef attributeDef : oads)
                {
                    if (h == 1)
                    {
                        if (checkNotNullValue(dataKeyObject, attributeDef, ObjectUtil.getUdfsValue(dataKeyObject)))
                        {
                            if (attributeDef.getDataType().equalsIgnoreCase("timestamp")
                                    && attributeDef.getAttributeName().equalsIgnoreCase("systemtime"))
                            {
                                strBuilder.append(attributeDef.getAttributeName()).append(",");
                                strBuilder2.append("SYSDATE").append(",");
                            }
                            else
                            {
                                strBuilder.append(attributeDef.getAttributeName()).append(",");
                                strBuilder2.append("?").append(",");
                            }
                        }
                        else if (StringUtils.isNotEmpty(attributeDef.getDefaultValue())) // DefaultValue 瘤盔
                        {
                            strBuilder.append(attributeDef.getAttributeName()).append(",");
                            strBuilder2.append("?").append(",");
                        }
                    }
                    else
                    {
                        if (checkNotNullValue(dataKeyObject, attributeDef, null))
                        {
                            if (attributeDef.getDataType().equalsIgnoreCase("timestamp") && attributeDef.getAttributeName().equalsIgnoreCase("systemtime"))
                            {
                                strBuilder.append(attributeDef.getAttributeName()).append(",");
                                strBuilder2.append("SYSDATE").append(",");
                            }
                            else
                            {
                                strBuilder.append(attributeDef.getAttributeName()).append(",");
                                strBuilder2.append("?").append(",");
                            }
                        }
                        else if (StringUtils.isNotEmpty(attributeDef.getDefaultValue())) // DefaultValue 瘤盔
                        {
                            strBuilder.append(attributeDef.getAttributeName()).append(",");
                            strBuilder2.append("?").append(",");
                        }
                    }
                }
            }

            strBuilder.delete(strBuilder.length() - 1, strBuilder.length()).append(")").append(" values (");
            strBuilder.append(strBuilder2.delete(strBuilder2.length() - 1, strBuilder2.length()));
            strBuilder.append(")");

            return strBuilder;
        }
        else
            throw new nanoFrameErrorSignal(ErrorSignal.InvalidQueryType, queryType);
    }

    private static boolean checkNotNullValue(Object dataKeyObject, ObjectAttributeDef objectAttributeDef,
                                             Map<String, String> udfs)
    {
        if (objectAttributeDef.getDataType().equalsIgnoreCase("timestamp")
                && objectAttributeDef.getAttributeName().equalsIgnoreCase("systemtime"))
            return true;
        Object value = null;

        if (objectAttributeDef.getPrimaryKeyFlag().equalsIgnoreCase("y"))
            value =
                    ObjectUtil.getFieldValue(OrmStandardEngineUtil.getKeyInfo(dataKeyObject),
                            objectAttributeDef.getAttributeName());
        else if (udfs == null)
            value = ObjectUtil.getFieldValue(dataKeyObject, objectAttributeDef.getAttributeName());
        else
            value = udfs.get(objectAttributeDef.getAttributeName());

        if (value == null)
            return false;

        if (objectAttributeDef.getDataType().equalsIgnoreCase("timestamp"))
        {
            if (value.equals(new Timestamp(0)) || value.toString().length() == 0)
                return false;
        }
        else if (value != null
                && (objectAttributeDef.getDataType().equalsIgnoreCase("Long") || objectAttributeDef.getDataType()
                .equalsIgnoreCase("Double")))
        {
            if (value.toString().length() == 0)
            {
                return false;
            }
        }
        return true;
    }

    public static OrmSqlStatement getSqlPrimaryWithoutColumns(Object dataKeyInfo, Map<String, String> withoutColumns)
    {
        String tableName = getTableName(dataKeyInfo);
        String sql = "";
        KeyInfo keyInfo = null;
        if (dataKeyInfo instanceof DataInfo)
        {
            keyInfo = getKeyInfo((DataInfo) dataKeyInfo);
        }
        else if (dataKeyInfo instanceof KeyInfo)
        {
            keyInfo = (KeyInfo) dataKeyInfo;
        }

        List objectValue = new ArrayList();
        List<ObjectAttributeDef> ObjectAttributeDefs =
                nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                        ObjectAttributeMap.Standard_Type);
        for (int i = 0; i < ObjectAttributeDefs.size(); i++)
        {
            if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("y"))
            {
                if (withoutColumns == null)
                {
                    sql = sql + ObjectAttributeDefs.get(i).getAttributeName() + "=? and ";
                    objectValue.add(ObjectUtil.getFieldValue(keyInfo, ObjectAttributeDefs.get(i).getAttributeName()));
                }
                else if (!withoutColumns.containsKey(ObjectAttributeDefs.get(i).getAttributeName()))
                {
                    sql = sql + ObjectAttributeDefs.get(i).getAttributeName() + "=? and ";
                    objectValue.add(ObjectUtil.getFieldValue(keyInfo, ObjectAttributeDefs.get(i).getAttributeName()));
                }
            }
            else if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("n"))
                break;
        }

        return new OrmSqlStatement(sql.substring(0, sql.length() - 5), objectValue.toArray());
    }

    public static Object[] getPrimaryWithoutColumns(Object dataKeyInfo, Map<String, String> withoutColumns)
    {
        String tableName = getTableName(dataKeyInfo);

        KeyInfo keyInfo = null;
        if (dataKeyInfo instanceof DataInfo)
        {
            keyInfo = getKeyInfo((DataInfo) dataKeyInfo);
        }
        else if (dataKeyInfo instanceof KeyInfo)
        {
            keyInfo = (KeyInfo) dataKeyInfo;
        }

        List objectValue = new ArrayList();
        List<ObjectAttributeDef> ObjectAttributeDefs =
                nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                        ObjectAttributeMap.Standard_Type);
        for (int i = 0; i < ObjectAttributeDefs.size(); i++)
        {
            if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("y"))
            {
                if (withoutColumns == null)
                {
                    objectValue.add(ObjectUtil.getFieldValue(keyInfo, ObjectAttributeDefs.get(i).getAttributeName()));
                }
                else if (!withoutColumns.containsKey(ObjectAttributeDefs.get(i).getAttributeName()))
                {
                    objectValue.add(ObjectUtil.getFieldValue(keyInfo, ObjectAttributeDefs.get(i).getAttributeName()));
                }
            }
            else if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("n"))
                break;
        }
        return objectValue.toArray();
    }

    public static Object[] getPrimaryWithoutTimeKey(Object dataKeyInfo, int duplicate)
    {
        String tableName = getTableName(dataKeyInfo);

        KeyInfo keyInfo = null;
        if (dataKeyInfo instanceof DataInfo)
        {
            keyInfo = getKeyInfo((DataInfo) dataKeyInfo);
        }
        else if (dataKeyInfo instanceof KeyInfo)
        {
            keyInfo = (KeyInfo) dataKeyInfo;
        }

        List objectValue = new ArrayList();
        List<ObjectAttributeDef> ObjectAttributeDefs =
                nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(tableName,
                        ObjectAttributeMap.Standard_Type);
        for (int j = 0; j < duplicate; j++)
        {
            for (int i = 0; i < ObjectAttributeDefs.size(); i++)
            {
                {
                    if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("y")
                            && !ObjectAttributeDefs.get(i).getAttributeName().equalsIgnoreCase("timekey"))
                    {
                        objectValue.add(ObjectUtil.getFieldValue(keyInfo, ObjectAttributeDefs.get(i).getAttributeName()));
                    }
                    else if (ObjectAttributeDefs.get(i).getPrimaryKeyFlag().equalsIgnoreCase("n"))
                        break;
                }
            }
        }
        return objectValue.toArray();
    }


}
