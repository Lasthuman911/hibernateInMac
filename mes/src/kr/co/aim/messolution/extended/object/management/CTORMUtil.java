package kr.co.aim.messolution.extended.object.management;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexTypeHost;

import kr.co.aim.messolution.extended.object.CTORMHeader;
import kr.co.aim.messolution.extended.object.CTORMTemplate;
import kr.co.aim.messolution.generic.GenericServiceProxy;
import kr.co.aim.messolution.generic.errorHandler.CustomException;
import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameDBErrorSignal;
import kr.co.aim.nanoframe.util.object.ObjectUtil;
import kr.co.aim.nanoframe.util.support.InvokeUtils;
import kr.co.aim.nanotrack.generic.util.StringUtil;

public class CTORMUtil {

    private static Log logger = LogFactory.getLog(CTORMUtil.class);

    public static Log getLogger() {
        return logger;
    }

    public static final String servicePath = "kr.co.aim.messolution.extended.object.management.impl";

    /**wzm
     * 返回dataObject对应的CTORMTemplate 的初始信息
     *
     * @param dataObject
     * @return
     */
    public static Object createDataInfo(Class dataObject) {
        Object createdInfo = null;

        try {
            createdInfo = (Object) Class.forName(dataObject.getName()).newInstance();

        } catch (Exception e) {
            logger.warn(e, e);
        }

        if (createdInfo != null) {
            for (Field column : createdInfo.getClass().getDeclaredFields()) {
                if (column.isAnnotationPresent(CTORMTemplate.class)) {
                    CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);

                    String name = annotation.name();
                    String dataType = annotation.dataType();
                    String initial = annotation.initial();

                    //specific data type setting
                    if (dataType.equalsIgnoreCase("timestamp"))
                        ObjectUtil.setFieldValue(createdInfo, name, null);
                    else if (!initial.isEmpty())
                        ObjectUtil.setFieldValue(createdInfo, name, initial);
                } else {
                    //annotation is mandatory at hibernate
                }

            }
        }

        return createdInfo;
    }

    /**
     * wzm:
     * @param clazz
     * @return
     */
    public static String getTableNameByClassName(Class clazz) {
        String tableName = clazz.getSimpleName();
        tableName = new StringBuffer().append(getHeader(clazz)).append(tableName).toString();
        return tableName;
    }

    public static String getHistoryTableNameByClassName(Class clazz, boolean isBrief) {
        //if (tableName.endsWith("Key"))
        //	tableName = tableName.substring(0, tableName.length() - 3);
        //else if (tableName.endsWith("ServiceImpl"))
        //	tableName = tableName.substring(0, tableName.length() - 11);
        //else if (tableName.endsWith("Service"))
        //	tableName = tableName.substring(0, tableName.length() - 7);

        StringBuffer tableNameBuffer = new StringBuffer().append(getHeader(clazz)).append(clazz.getSimpleName());

        if (isBrief)
            tableNameBuffer.append("Hist");
        else
            tableNameBuffer.append("History");

        return tableNameBuffer.toString();
    }

    /**
     * wzm:
     * @param sql
     * @param condition
     * @return
     */
    public static String getConditionSql(String sql, String condition) {
        if (condition.toLowerCase().trim().startsWith("where ")) {
            //sql = sql + " " + condition.trim();
            sql = new StringBuffer(sql).append(" ").append(condition.trim()).toString();
        } else {
            //sql = sql + " where " + condition;
            sql = new StringBuffer(sql).append(" WHERE ").append(condition).toString();//注意前后留空格
        }
        return sql;
    }

    /**
     * wzm:
     * @param sql
     * @param dataInfo
     * @return
     */
    public static String getKeySql(String sql, Object dataInfo) {
        StringBuffer sCondition = new StringBuffer(sql).append(" WHERE 1=1 ");

        if (dataInfo != null) {
            for (Field column : dataInfo.getClass().getDeclaredFields()) {
                //only by annotation presentation
                if (column.isAnnotationPresent(CTORMTemplate.class)) {
                    CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);

                    String name = annotation.name();
                    String type = annotation.type();
                    String dataType = annotation.dataType();
                    String initial = annotation.initial();

                    if (type.equalsIgnoreCase("key"))
                        sCondition.append(" AND ").append(name).append("=").append("?");
                }
            }
        }

        return sCondition.toString();
    }

    /**
     * wzm:
     * @param dataInfo
     * @param tableName
     * @return
     */
    public static String getUpdateSql(Class dataInfo, String tableName) {
        StringBuffer sql = new StringBuffer();
        StringBuffer subSql = new StringBuffer(" ").append("WHERE 1=1").append(" ");

        sql.append("UPDATE").append(" ").append(tableName).append(" ").append("SET").append(" ");

        for (Field column : dataInfo.getDeclaredFields()) {
            if (column.isAnnotationPresent(CTORMTemplate.class)) {
                CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);

                String name = annotation.name();
                String type = annotation.type();
                //String dataType = annotation.dataType();
                String initial = annotation.initial();

                //never key update
                //never null update
                if (!initial.equalsIgnoreCase("never")) {
                    if (type.equalsIgnoreCase("key"))
                        subSql.append(" AND ").append(name).append("=").append("?");
                    else
                        sql.append(name).append("=").append("?").append(",");
                }
            }
        }

        String refinedSql = StringUtil.removeEnd(sql.toString(), ",");

        sql = new StringBuffer(refinedSql).append(subSql.toString());

        return sql.toString();
    }

    /**
     * wzm:
     * @param dataInfo
     * @param tableName
     * @return
     */
    public static String getDeleteSql(Class dataInfo, String tableName) {
        StringBuffer sql = new StringBuffer();
        StringBuffer subSql = new StringBuffer(" ").append("WHERE 1=1").append(" ");

        sql.append("DELETE").append(" ").append(tableName).append(" ");

        for (Field column : dataInfo.getDeclaredFields()) {
            if (column.isAnnotationPresent(CTORMTemplate.class)) {
                CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);

                String name = annotation.name();
                String type = annotation.type();

                //never key update
                if (type.equalsIgnoreCase("key"))
                    subSql.append(" AND ").append(name).append("=").append("?");

            }
        }

        String refinedSql = StringUtil.removeEnd(sql.toString(), ",");

        sql = new StringBuffer(refinedSql).append(subSql.toString());

        return sql.toString();
    }

    /**
     * wzm:
     * @param dataInfo
     * @param tableName
     * @return
     */
    public static String getInsertSql(Class dataInfo, String tableName) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT").append(" ").append("INTO").append(" ").append(tableName).append(" ");

        StringBuilder attrBuilder = new StringBuilder("");
        StringBuilder valueBuilder = new StringBuilder("");

        for (Field column : dataInfo.getDeclaredFields()) {
            if (column.isAnnotationPresent(CTORMTemplate.class)) {
                CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);

                String name = annotation.name();
                String dataType = annotation.dataType();
                String initial = annotation.initial();

                if (initial.equalsIgnoreCase("never"))
                    continue;

                {//column generation
                    if (!attrBuilder.toString().isEmpty())
                        attrBuilder.append(",");

                    attrBuilder.append(name);
                }

                {//value generation : default setting demanded?
                    if (!valueBuilder.toString().isEmpty())
                        valueBuilder.append(",");

                    if ((dataType.equalsIgnoreCase("timestamp") || dataType.equalsIgnoreCase("systemtime"))
                            && initial.equalsIgnoreCase("current")) {
                        valueBuilder.append("SYSDATE");
                    } else {
                        valueBuilder.append("?");
                    }
                }

            }
        }

        sql.append("(").append(attrBuilder).append(")").append(" VALUES ").append("(").append(valueBuilder).append(")");

        return sql.toString();
    }

    /**
     * wzm:
     * @param dataInfo
     * @param tableName
     * @return
     */
    public static String getSql(Class dataInfo, String tableName) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT").append(" ");

        for (Field column : dataInfo.getDeclaredFields()) {
            if (column.isAnnotationPresent(CTORMTemplate.class))//如果存在该元素的指定类型的注释，则返回这些注释，否则返回 null
            {
                CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);//返回此元素上存在的所有注释

                String name = annotation.name();
                sql.append(name);
                sql.append(",");
            }
        }

        String refinedSql = StringUtil.removeEnd(sql.toString(), ",");

        sql = new StringBuffer(refinedSql).append(" ").append("FROM").append(" ").append(tableName);

        return sql.toString();
    }

    /**
     * wzm:
     * @param clazz
     * @return
     * @throws CustomException
     */
    public static Object loadServiceProxy(Class clazz)
            throws CustomException {
        String serviceName = new StringBuffer(servicePath).append(".")
                .append(clazz.getSimpleName()).append("Service").toString();

        Object serviceClass = InvokeUtils.newInstance(serviceName, new Class[0], new Object[0]);

        if (serviceClass != null) {
            return serviceClass;
        } else {
            throw new CustomException("SYS-8001", serviceName);
        }
    }

    /**
     * wzm:只是用来验证key是否匹配
     * @param dataInfo
     * @param args
     * @return
     * @author wzm
     */
    public static boolean isKeyParamMatch(Object dataInfo, Object[] args) {
        //key info validating
        try {
            if (dataInfo == null)
                throw new Exception("dataInfo is null");

            int keyCnt = 0;

            for (Field column : dataInfo.getClass().getDeclaredFields()) {
                //only by annotation presentation
                if (column.isAnnotationPresent(CTORMTemplate.class)) {
                    CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);
                    String type = annotation.type();

                    if (type.equalsIgnoreCase("key"))
                        keyCnt++;
                }
            }

            if (keyCnt != args.length)
                throw new Exception("key value is null");
        } catch (Exception ex) {
            //throw new nanoFrameDBErrorSignal(ErrorSignal.NullPointKeySignal, "", sql, "Key can't be null");
            ex.printStackTrace();
        }

        return true;
    }

    /**
     * wzm:从dataInfo中获取primary key List
     * @param dataInfo
     * @return
     */
    public static List<Object> makeKeyParam(Object dataInfo) {
        List<Object> temp = new ArrayList<Object>();

        if (dataInfo == null)
            return temp;

        int idx = 0;

        for (Field column : dataInfo.getClass().getDeclaredFields()) {
            if (column.isAnnotationPresent(CTORMTemplate.class)) {
                CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);

                String name = annotation.name();
                String type = annotation.type();
                if (type.equalsIgnoreCase("key")) {
                    temp.add(idx, ObjectUtil.getFieldValue(dataInfo, name));
                    idx++;
                }
            }
        }

        return temp;
    }

    /**
     * wzm:
     * dataInfo 中去除掉key 以后的的其他字段组成的List，
     * make primary non-key parameters
     *
     * @param dataInfo
     * @return
     */
    public static List<Object> makeNonKeyParam(Object dataInfo) {
        List<Object> temp = new ArrayList<>();

        if (dataInfo == null)
            return temp;

        int idx = 0;

        for (Field column : dataInfo.getClass().getDeclaredFields()) {
            if (column.isAnnotationPresent(CTORMTemplate.class)) {
                CTORMTemplate annotation = column.getAnnotation(CTORMTemplate.class);

                String name = annotation.name();
                String type = annotation.type();
                String initial = annotation.initial();

                if (!type.equalsIgnoreCase("key")
                        && !initial.equalsIgnoreCase("never")) {
                    temp.add(idx, ObjectUtil.getFieldValue(dataInfo, name));
                    idx++;
                }
            }
        }

        return temp;
    }

    /**
     * wzm:
     * dynamic CTORM mapping header:默认用CT_,有指定的前前缀，就用指定的前缀
     *
     * @param clazz
     * @return
     * @author swcho
     * @since 2015-10-29
     */
    public static String getHeader(Class clazz) {
        String headerName = "";
        String connector = "";

        if (clazz.isAnnotationPresent(CTORMHeader.class)) {
            CTORMHeader header = (CTORMHeader) clazz.getAnnotation(CTORMHeader.class);

            if (header != null) {
                headerName = header.tag();
                connector = header.divider();
            }
        }

        if (headerName.isEmpty()) headerName = "CT";
        if (connector.isEmpty()) connector = "_";

        return new StringBuilder(headerName).append(connector).toString();
    }
}
