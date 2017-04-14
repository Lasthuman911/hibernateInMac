package kr.co.aim.messolution.extended.object;

import kr.co.aim.messolution.extended.object.management.CTORMUtil;

import kr.co.aim.messolution.extended.object.management.data.VirtualGlass;
import kr.co.aim.messolution.extended.object.management.impl.VirtualGlassService;
import kr.co.aim.messolution.generic.MESStackTrace;
import kr.co.aim.messolution.generic.errorHandler.CustomException;

import org.apache.commons.logging.Log;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
/**
 *  使用了ApplicationContextAware接口的类，如果受spring容器管理的
 * 话，那么就会自动的调用ApplicationContextAware中的setApplicationContext方法
 * @author admin
 *
 */
public class ExtendedObjectProxy extends MESStackTrace implements ApplicationContextAware {

    private static ApplicationContext						ac;

    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        this.ac = arg0;
    }

    /**
     * custom stack trace engine : must be implement in each proxy
     * @param eventLogger
     * @param beanName
     * @param methodName
     * @param args
     * @return
     * @throws CustomException
     */
    public static Object executeMethod(Log eventLogger, String beanName, String methodName, Object... args)
            throws CustomException
    {
        return executeMethodMonitor(eventLogger, beanName, methodName, args);
    }

    public static VirtualGlassService getVirtualGlassService() throws CustomException
    {
        return (VirtualGlassService) CTORMUtil.loadServiceProxy(VirtualGlass.class);
    }
}
