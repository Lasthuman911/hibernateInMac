package kr.co.aim.nanoframe.util.object;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameErrorSignal;
import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanoframe.orm.info.access.UdfAccessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectUtil
{
    private static Log	log	= LogFactory.getLog(ObjectUtil.class);

    public static Object newInstanceByClassName(String className)
    {
        try
        {
            Class clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (Exception e)
        {
            return null;
        }
    }

    public static Object copyTo(Object object) throws nanoFrameErrorSignal
    {
        try
        {
            Object copyData = object.getClass().newInstance();
            copyField(object, copyData);
            try
            {
                KeyInfo sourceKey = getKeyInfo(object);
                Object copyKeyData = sourceKey.getClass().newInstance();
                copyField(sourceKey, copyKeyData);
                setFieldValue(copyData, "key", copyKeyData);
            } catch (Exception e)
            {}
            return copyData;
        } catch (Throwable e)
        {
            throw ErrorSignal.getNotifyException(e);
        }
    }

    public static void copyField4HistoryAdaptor(Object source, String sourceFieldName, Object target,
                                                String targetFieldName)
    {
        try
        {
            if (sourceFieldName.equals("udfs") && targetFieldName.equals("udfs"))
            {
                Map<String, String> udfs = getUdfsValue(source);
                setUdfsValue(target, udfs);
                return;
            }

            Field sourceField = source.getClass().getDeclaredField(sourceFieldName);
            Field targetField = target.getClass().getDeclaredField(targetFieldName);

            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            copyFieldValue(targetField, target, sourceField.get(source));
        } catch (Exception e)
        {} // 俊矾啊 酒丛.
    }

    public static void copyField(Object source, String sourceFieldName, Object target, String targetFieldName)
    {
        try
        {
            if (sourceFieldName.equals("udfs") && targetFieldName.equals("udfs"))
            {
                Map<String, String> udfs = getUdfsValue(source);
                setUdfsValue(target, udfs);
                return;
            }

            Field sourceField = source.getClass().getDeclaredField(sourceFieldName);
            Field targetField = target.getClass().getDeclaredField(targetFieldName);

            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            copyFieldValue(targetField, target, sourceField.get(source));
        } catch (Throwable e)
        {
            if (log.isDebugEnabled())
                log.warn(e, e);
            else
                log.warn(e);
        }
    }

    public static void copyField(Object source, Field sourceField, Object target, String targetFieldName)
    {
        try
        {
            Field targetField = target.getClass().getDeclaredField(targetFieldName);
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            copyFieldValue(targetField, target, sourceField.get(source));
        } catch (Throwable e)
        {}
    }

    public static void copyField(Object source, Object target, String fieldName)
    {
        try
        {
            Field targetField = target.getClass().getDeclaredField(fieldName);
            Field sourceField = source.getClass().getDeclaredField(fieldName);
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            copyFieldValue(targetField, target, sourceField.get(source));
        } catch (Throwable e)
        {
            if (log.isDebugEnabled())
                log.warn(e, e);
            else
                log.warn(e);
        }
    }

    public static void copyField(Object source, Object target)
    {
        try
        {
            Field[] sourcefields = source.getClass().getDeclaredFields();

            for (int i = 0; i < sourcefields.length; i++)
            {
                sourcefields[i].setAccessible(true);
                try
                {
                    Field targetField = target.getClass().getDeclaredField(sourcefields[i].getName());
                    targetField.setAccessible(true);
                    copyFieldValue(targetField, target, sourcefields[i].get(source));
                } catch (Exception e)
                {}
            }

            if (source instanceof UdfAccessor)
            {
                try
                {
                    Map<String, String> udfValue = getUdfsValue(source);
                    setUdfsValue(target, udfValue);
                } catch (Exception e)
                {}
            }
        } catch (Throwable e)
        {
            if (log.isDebugEnabled())
                log.warn(e, e);
            else
                log.warn(e);
        }
    }

    public static void copyFieldNotEqual(Object source, Object target)
    {
        try
        {
            Field[] sourcefields = source.getClass().getDeclaredFields();

            for (int i = 0; i < sourcefields.length; i++)
            {
                sourcefields[i].setAccessible(true);
                try
                {
                    Field targetField = target.getClass().getDeclaredField(sourcefields[i].getName());
                    if (targetField != null)
                    {
                        targetField.setAccessible(true);
                        copyFieldValue(targetField, target, sourcefields[i].get(source));
                    }
                } catch (Exception e)
                {}
            }
            if (source instanceof UdfAccessor)
            {
                try
                {
                    Map<String, String> udfValue = getUdfsValue(source);
                    setUdfsValue(target, udfValue);
                } catch (Exception e)
                {}
            }
        } catch (Throwable e)
        {
            if (log.isDebugEnabled())
                log.warn(e, e);
            else
                log.warn(e);
        }
    }

    public static void copyField(Object target, String targetFieldName, Object value)
    {
        try
        {
            Field targetField = target.getClass().getDeclaredField(targetFieldName);
            targetField.setAccessible(true);
            copyFieldValue(targetField, target, value);
        } catch (Throwable e)
        {
            if (log.isDebugEnabled())
                log.warn(e, e);
            else
                log.warn(e);
        }
    }

    /**
     * 将value的值赋值给target
     * @param field
     * @param target
     * @param value
     */
    public static void copyFieldValue(Field field, Object target, Object value)
    {
        try
        {
            Class type = field.getType();//返回一个 Class 对象，它标识了此 Field 对象所表示字段的声明类型

            if ( type.equals( Double.class ) || type.equals( double.class ))
                field.set(target, Double.parseDouble(String.valueOf(value)));
            else if ( type.equals( Long.class ) || type.equals( long.class ))
                field.set(target, Long.parseLong(String.valueOf(value)));
            else if ( type.equals( Integer.class ) || type.equals( int.class ))
                field.set(target, Integer.parseInt(String.valueOf(value)));
            else if ( type.equals( Float.class ) || type.equals( float.class ))
                field.set(target, Float.parseFloat(String.valueOf(value)));
            else if ( type.equals( Short.class ) || type.equals( short.class ))
                field.set(target, Short.parseShort(String.valueOf(value )));
            else if ( type.equals( Boolean.class ) || type.equals( boolean.class ))
                field.set(target, Boolean.parseBoolean(String.valueOf(value)));
            else
            {
                if (field.getName().equalsIgnoreCase("key"))//返回此 Field 对象表示的字段的名称
                {
                    if (ObjectUtil.getFieldValue(target, "key") != null
                            && !ObjectUtil.getFieldValue(target, "key").getClass().equals(value.getClass()))
                        return;
                }
                field.set(target, value);
            }
        } catch (Throwable e)
        {
            if (log.isDebugEnabled())

                log.warn(e, e);
            else
                log.warn(e);
        }

    }

    public static String getFieldNamesByCommaDelimeter(Object object)
    {
        StringBuilder strBuilder = new StringBuilder();
        try
        {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++)
            {
                strBuilder.append(fields[i].getName()).append(",");
            }
        } catch (Throwable e)
        {}
        return strBuilder.delete(strBuilder.length() - 1, strBuilder.length()).toString();
    }

    public static List<String> getFieldNames(Object object)
    {
        List<String> arrayList = new ArrayList<String>();
        try
        {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++)
            {
                arrayList.add(fields[i].getName());
            }
        } catch (Exception e)
        {}
        return arrayList;
    }

    public static Object[] appendFirstObjects(Object[] obj, Object append)
    {
        List<Object> objects = new ArrayList<Object>();
        objects.add(append);
        if (obj != null)
        {
            for (int i = 0; i < obj.length; i++)
            {
                objects.add(obj[i]);
            }
        }
        return objects.toArray();
    }

    public static Object[] appendObjects(Object[] obj, Object append)
    {
        List<Object> objects = new ArrayList<Object>();
        if (obj != null)
        {
            for (int i = 0; i < obj.length; i++)
            {
                objects.add(obj[i]);
            }
        }
        objects.add(append);
        return objects.toArray();
    }

    public static Object[] appendObjects(Object[] obj, Object[] append)
    {
        List<Object> objects = new ArrayList<Object>();
        if (obj != null)
        {
            for (int i = 0; i < obj.length; i++)
            {
                objects.add(obj[i]);
            }
        }
        for (int i = 0; i < append.length; i++)
            objects.add(append[i]);
        return objects.toArray();
    }

    public static List<Object> getFieldValues(Object object)
    {
        List arrayList = new ArrayList();
        try
        {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++)
            {
                fields[i].setAccessible(true);
                arrayList.add(fields[i].get(object));
            }
        } catch (Exception e)
        {}
        return arrayList;
    }

    public static Object getFieldValue(Object object, String fieldName)
    {
        try
        {
            if (fieldName.equalsIgnoreCase("udfs"))
                return getUdfsValue(object);//委托

            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException e)
        {
            String newFieldName = getChangeField(fieldName);
            if (!newFieldName.equals(fieldName))
                return getFieldValue(object, newFieldName);

            if (log.isDebugEnabled())
                log.warn(e, e);
            else
                log.warn(e);
        } catch (Exception e)
        {
            if (log.isDebugEnabled())
                log.warn(e, e);
            else
                log.warn(e);
        }
        return null;
    }

    public static Object getUdfFieldValue(Object object, String fieldName)
    {
        return getUdfsValue(object).get(fieldName);
    }

    /**
     * wzm:
     * @param dataInfo
     * @return
     */
    public static Map<String, String> getUdfsValue(Object dataInfo)
    {
        try
        {
            Method method = dataInfo.getClass().getMethod("getUdfs");
            return (Map<String, String>) method.invoke(dataInfo, null);
        } catch (Exception e)
        {
            if (log.isDebugEnabled())
                log.warn(e, e);
            else
                log.warn(e);
        }
        return null;
    }

    public static String getChangeField(String fieldName)
    {
        char first = fieldName.charAt(0);
        int asciiCode = (int) first;
        if (asciiCode >= 65 && asciiCode <= 90)
        {
            String fisrt = fieldName.substring(0, 1);
            return fisrt.toLowerCase() + fieldName.substring(1, fieldName.length());
        }
        return fieldName;
    }

    public static boolean hasFiled(Object object, String fieldName)
    {
        try
        {
            Field field = object.getClass().getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException ex)
        {
            String newFieldName = getChangeField(fieldName);
            if (!newFieldName.equals(fieldName))
                return hasFiled(object, newFieldName);
        } catch (Throwable e)
        {}
        return false;
    }

    /**
     * wzm:
     * @param object
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(Object object, String fieldName, Object value)
    {
        try
        {
            if (fieldName.equals("udfs"))
            {
                setUdfsValue(object, (Map<String, String>) value);
            }
            else
            {
                try
                {
                    Field field = object.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    copyFieldValue(field, object, value);
                } catch (NoSuchFieldException e)
                {
                    fieldName = getChangeField(fieldName);
                    Field field = object.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    copyFieldValue(field, object, value);
                }
            }
        } catch (Exception e)
        {
            if (log.isDebugEnabled())

                log.warn(e, e);
            else
                log.warn(e);
        }
    }

    /**
     * 设置udf字段的值
     * @author wzm
     * @param dataInfo
     * @param value
     */
    public static void setUdfsValue(Object dataInfo, Map<String, String> value)
    {
        try
        {//name - 方法名,  parameterTypes - 参数列表
            Method method = dataInfo.getClass().getMethod("setUdfs", Map.class);//返回一个 Method 对象，它反映此 Class 对象所表示的类或接口的指定公共成员方法
            method.invoke(dataInfo, value);//对带有指定参数的指定对象调用由此 Method 对象表示的底层方法,obj - 从中调用底层方法的对象,args - 用于方法调用的参数
        } catch (Exception e)
        {
            if (log.isDebugEnabled())
                log.warn(e, e);
            else
                log.warn(e);
        }
    }

    /**
     * Source UDF 狼 备己 夸家甫 捞侩窍咯 Target UDF 甫 备己钦聪促.
     *
     * <pre>
     * Target UDF 啊 厚绢 乐阑 版快 Source UDF 狼 葛电 备己 夸家甫 盲矿聪促.
     * Target UDF 啊 厚绢 乐瘤 臼阑 版快 Source 客 Target 狼 Key 啊 老摹窍绰 夸家父 Source Value 甫 捞侩窍咯 Target Value 甫 汲沥钦聪促.
     * </pre>
     *
     * @param targetUdfs
     *            Target UDF
     * @param sourceUdfs
     *            Source UDF
     */
    public static void setUdfs(Map<String, String> targetUdfs, Map<String, String> sourceUdfs)
    {
        if (targetUdfs.size() == 0)
        {
            for (String srcKkey : sourceUdfs.keySet())
            {
                targetUdfs.put(srcKkey, sourceUdfs.get(srcKkey));
            }
        }
        else
        {
            for (String srcKkey : sourceUdfs.keySet())
            {
                if (targetUdfs.containsKey(srcKkey))
                {
                    targetUdfs.put(srcKkey, sourceUdfs.get(srcKkey));
                }
            }
        }
    }

    public static boolean isNullOrNullString(Object object)
    {

        try
        {
            Field[] fields = object.getClass().getDeclaredFields();

            for (int i = 0; i < fields.length; i++)
            {
                fields[i].setAccessible(true);
                if (fields[i].get(object) == null)
                    return true;
                else if (fields[i].get(object) instanceof String)
                {
                    if (fields[i].get(object).toString().length() == 0)
                        return true;
                }
            }
        } catch (Exception e)
        {}
        return false;
    }

    public static String getString(KeyInfo keyInfo)
    {
        if (keyInfo == null)
            return "";
        StringBuilder strBuilder = new StringBuilder();
        try
        {
            Class c = Class.forName(keyInfo.getClass().getName());
            Method[] methods = c.getMethods();
            for (int i = 0; i < methods.length; i++)
            {
                if (methods[i].getName().startsWith("get") && !methods[i].getName().equalsIgnoreCase("getclass"))
                {
                    strBuilder.append(methods[i].getName().substring(3, methods[i].getName().length()))
                            .append(":")
                            .append(methods[i].invoke(keyInfo, null).toString());
                    strBuilder.append(",");
                }
            }
            strBuilder.setLength(strBuilder.length() - 1);
        } catch (Exception e)
        {}
        return strBuilder.toString();
    }

    public static String getString(Map<String, Object> map)
    {
        if (map == null)
            return "";
        StringBuilder strBuilder = new StringBuilder();
        try
        {
            Iterator keys = map.keySet().iterator();
            while (keys.hasNext())
            {
                String keyName = (String) keys.next();
                strBuilder.append(keyName).append("=").append(map.get(keyName)).append(",");
            }
            strBuilder.setLength(strBuilder.length() - 1);
        } catch (Exception e)
        {}
        return strBuilder.toString();
    }

    public static String getString(DataInfo dataInfo)
    {
        if (dataInfo == null)
            return "";
        StringBuilder strBuilder = new StringBuilder();
        KeyInfo keyInfo = getKeyInfo(dataInfo);
        if (keyInfo != null)
        {
            strBuilder.append(getString(keyInfo));
            strBuilder.append(",");
        }
        try
        {
            Method[] methods = dataInfo.getClass().getMethods();
            for (Method method : methods)
            {
                if (method.getName().startsWith("get")
                        && method.getName().equalsIgnoreCase("getClass") == false
                        && method.getName().equalsIgnoreCase("getKey") == false)
                {
                    strBuilder.append(method.getName().substring(3, method.getName().length()));
                    strBuilder.append(":");
                    strBuilder.append(method.invoke(dataInfo, null).toString());
                    strBuilder.append(",");
                }
            }
            strBuilder.setLength(strBuilder.length() - 1);
        } catch (Exception e)
        {}
        return strBuilder.toString();
    }

    public static String getString(String where, Object[] bindSet)
    {
        if (bindSet == null)
            return where;
        StringBuilder str = new StringBuilder();
        String v = "";
        int j = 0;
        for (int i = 0; i < where.length(); i++)
        {
            char a = where.charAt(i);
            v = String.valueOf(a);
            if (v.equalsIgnoreCase("?"))
            {
                str.append(bindSet[j]);
                j++;
            }
            else
                str.append(v);
        }
        return str.toString();
    }

    public static Object[] getObjectValue(Map<String, Object> map)
    {
        List arrayList = new ArrayList();
        Iterator keys = map.keySet().iterator();
        while (keys.hasNext())
        {
            String keyName = (String) keys.next();
            arrayList.add(map.get(keyName));
        }
        return arrayList.toArray();
    }

    public static String getString(Object[] bindSet)
    {
        if (bindSet == null || bindSet.length == 0)
            return "";
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < bindSet.length; i++)
        {
            str.append(bindSet[i]).append(",");
        }
        return str.delete(str.length() - 1, str.length()).toString();
    }

    public static Object convertBigDecimal(BigDecimal obj)
    {
        if (!obj.toString().contains("."))
            return Long.valueOf(obj.toString());
        else
            return Double.valueOf(obj.toString());

    }

    public static KeyInfo getKeyInfo(Object dataInfo)
    {
        if (dataInfo instanceof DataInfo)
        {
            try
            {
                return (KeyInfo) ObjectUtil.getFieldValue(dataInfo, "key");
            } catch (Exception e)
            {
                if (log.isDebugEnabled())
                    log.warn(e, e);
                else
                    log.warn(e);
            }
            return null;
        }
        else if (dataInfo instanceof KeyInfo)
            return (KeyInfo) dataInfo;
        else
            return null;
    }
}
