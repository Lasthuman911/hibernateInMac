package kr.co.aim.nanoframe.util.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InvokeUtils
{

    private static Log	log	= LogFactory.getLog(InvokeUtils.class);

    public static Object invokeMethod(Object instance, String methodName, Object[] args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        //return instance.getClass().getMethod(methodName, getClassType(args)).invoke(instance, args);
        return getMethod(instance, methodName, getClassType(args)).invoke(instance, args);
    }

    public static Object invokeStaticMethod(String className, String methodName, Object[] args)
    {
        try
        {
            Method m;
            Class c = Class.forName(className);
            // 绢驴荐 绝绰 府敲泛记狼 茄拌 锭巩俊....铝...
            if (methodName.equals("narrow"))
                m = c.getDeclaredMethod(methodName, org.omg.CORBA.Object.class);
            else
                m = c.getDeclaredMethod(methodName, getClassType(args));
            return m.invoke(null, args);
        } catch (Exception e)
        {
            if (log.isDebugEnabled())
                log.error(e, e);
            else
                log.error(e);
            return null;
        }
    }

    private static Class[] getClassType(Object[] args)
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
                        classTypes[i] = args[i].getClass();
                }
            }
        }
        return classTypes;
    }

    private static Method getMethod(Object instance, String methodName, Class[] classTypes)
            throws NoSuchMethodException
    {
        Method accessMethod = getMethod(instance.getClass(), methodName, classTypes);
        accessMethod.setAccessible(true);
        return accessMethod;
    }

    public static Method getMethod(Class thisClass, String methodName, Class[] classTypes) throws NoSuchMethodException
    {
        if (thisClass == null)
            throw new NoSuchMethodException("Invalid method : " + methodName);
        try
        {
            log.debug("[" + methodName + "] [" + classTypes.toString() + "]");
            return thisClass.getMethod(methodName, classTypes);
        } catch (NoSuchMethodException e)
        {
            log.warn("NoSuchMethodException, re-try");
            //log.debug("[" + methodName + "] [" + classTypes.toString() + "]");
            return getMethod(thisClass.getSuperclass(), methodName, classTypes);
        }
    }

    /**
     * wzm:
     * @param className
     * @param argumentTypes
     * @param arguments
     * @return
     */
    public static Object newInstance(String className, Class[] argumentTypes, Object[] arguments)
    {
        try
        {
            Class aClass = Class.forName(className);
            //返回一个 Constructor 对象，它反映此 Class 对象所表示的类的指定公共构造方法
            Constructor classConstruct = aClass.getConstructor(argumentTypes);
            //使用此 Constructor 对象表示的构造方法来创建该构造方法的声明类的新实例，并用指定的初始化参数初始化该实例。
            Object newObject = classConstruct.newInstance(arguments);
            return newObject;
        } catch (Exception e)
        {
            return null;
        }
    }

}
