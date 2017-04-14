/*
 ****************************************************************************
 *
 *  (c) Copyright 2009 AIM Systems, Inc. All rights reserved.
 *
 *  This software is proprietary to and embodies the confidential
 *  technology of AIM Systems, Inc. Possession, use, or copying of this
 *  software and media is authorized only pursuant to a valid written
 *  license from AIM Systems, Inc.
 *
 ****************************************************************************
 */

package kr.co.aim.nanoframe;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import kr.co.aim.nanoflow.core.BpelProcessManager;
import kr.co.aim.nanoflow.core.activity.Activity;
import kr.co.aim.nanoflow.core.activity.BpelProcess;
import kr.co.aim.nanoframe.cache.CacheManager;
import kr.co.aim.nanoframe.esb.GenericSender;
import kr.co.aim.nanoframe.esb.SenderProxy;
import kr.co.aim.nanoframe.esb.email.EmailTransport;
import kr.co.aim.nanoframe.event.BundleMessageEventAdaptor;
import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameErrorSignal;
import kr.co.aim.nanoframe.orm.ObjectAttributeDef;
import kr.co.aim.nanoframe.orm.ObjectAttributeMap;
import kr.co.aim.nanoframe.orm.OrmStandardEngine;
import kr.co.aim.nanoframe.orm.SqlTemplate;
import kr.co.aim.nanoframe.osgi.NanoFrameCommand;
import kr.co.aim.nanoframe.transaction.TransactionManager;
import kr.co.aim.nanoframe.transaction.TxDataSourceManager;
import kr.co.aim.nanoframe.util.bundle.BundleUtil;
import kr.co.aim.nanoframe.util.bundle.nanoFrameProperties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.osgi.framework.internal.core.FrameworkCommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;

/*
 ****************************************************************************
 *  PACKAGE : kr.co.aim.nanoframe
 *  NAME    : nanoFrameServiceProxy.java
 *  TYPE    : JAVA
 *  DESCRIPTION :
 *
 ****************************************************************************
 */

public class nanoFrameServiceProxy implements BundleActivator
{
    private static Log					log			= LogFactory.getLog(nanoFrameServiceProxy.class);

    private static BundleContext		bundleContext;

    private static Map<Long, Thread>	threadMap	= new ConcurrentHashMap<Long, Thread>();

    private static int bpelCompletionTimeout		= 5000;

    @Override
    public void start(BundleContext bundleContext) throws Exception
    {
        Bundle systemBundle = bundleContext.getBundle(0);
        setBundleContext(systemBundle.getBundleContext());

        addSystemProperty();

        bundleContext.registerService( CommandProvider.class.getName(), new NanoFrameCommand(), new Properties());

        String timeout = System.getProperty( nanoFrameProperties.BPEL_COMPLETION_TIMEOUT );
        if ( StringUtils.isNotEmpty( timeout ) && StringUtils.isNumeric( timeout ))
            bpelCompletionTimeout = Integer.parseInt( timeout );
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception
    {
    }

    public void setBundleContext(BundleContext bundleContext)
    {
        nanoFrameServiceProxy.bundleContext = bundleContext;

        if (!threadMap.isEmpty())
        {
            Iterator<Thread> iter = threadMap.values().iterator();
            while (iter.hasNext())
            {
                iter.next().interrupt();
            }
        }
    }

    /**
     * bundleContext:bundle上下文
     * @return
     */
    public static BundleContext getBundleContext()
    {
        if (bundleContext == null)
        {
            Thread thread = Thread.currentThread();
            log.info("GETBUNDLE : " + thread.getName());
            threadMap.put(thread.getId(), thread);

            try
            {
                Thread.sleep(BundleUtil.getServiceLookupTimeout());
            }
            catch (InterruptedException ex)
            {
                return bundleContext;
            }
            finally
            {
                threadMap.remove(thread.getId());
            }

            throw new nanoFrameErrorSignal(ErrorSignal.NotActive, "nanoframe.kernel is not Active.");
        }
        else
            return bundleContext;
    }

    public static void terminateImmediately()
    {
        log.info("***************************************************************************************");
        System.exit(0);
    }

    public static void terminateAfterManagement()
    {
        log.info("***************************************************************************************");
        log.info("Closing message listeners .....");
        BundleMessageEventAdaptor bundleMessageEventAdaptor = (BundleMessageEventAdaptor) BundleUtil.waitForService(BundleMessageEventAdaptor.class.getName());
        bundleMessageEventAdaptor.terminate();

        try
        {
            Thread.sleep( 100 );
        }
        catch ( Exception ex ){}

        BpelProcessManager bpelProcessManager =	(BpelProcessManager) BundleUtil.getService(BpelProcessManager.class.getName());

        if ( bpelProcessManager != null )
        {
            log.info("Checking running bpels");

            int waittingCount = bpelCompletionTimeout / 1000;
            int i = 0;

            for (; i < waittingCount; i++)
            {
                if ( bpelProcessManager.getRunningBpelProcessSize() == 0 )
                {
                    log.info( "No running bpels" );
                    break;
                }
                else
                {
                    log.info( "Waitting for running bpels(" + bpelProcessManager.getRunningBpelProcessSize() + ") to be completed" );

                    try
                    {
                        Thread.sleep( 1000 );
                    }
                    catch ( Exception ex ){}
                }
            }

            if ( i == waittingCount )
            {
                log.warn( "Bpel Completion Waitting Timeout. Will be terminated." );

                Map<String, Activity> map = bpelProcessManager.getRunningBpelProcesses();

                Iterator<Entry<String, Activity>> iter = map.entrySet().iterator();
                while ( iter.hasNext())
                {
                    Entry<String, Activity> entry = iter.next();
                    Activity activity = entry.getValue();
                    long duration = System.currentTimeMillis() - activity.getStartTime().getTime();

                    String runningBpel = activity.getName() + " [Activity=" + ((BpelProcess)activity).getRunningActivity() + ", Duration=" + duration + "]";
                    log.info( "Running Bpel : " + runningBpel );
                }
            }

            bpelProcessManager.getThreadPool().shutdownNow();
        }

        log.info("***************************************************************************************");

        System.exit(0);
    }

    public static TxDataSourceManager getTxDataSourceManager()
    {
        return (TxDataSourceManager) BundleUtil.waitForService(TxDataSourceManager.class.getName());
    }

    public static TransactionManager getTransactionManager(Class<? extends TransactionManager> cls)
    {
        return (TransactionManager) BundleUtil.waitForService(cls.getName());
    }

    public static GenericSender getGenericSender()
    {
        return (GenericSender) BundleUtil.waitForService(GenericSender.class.getName());
    }

    public static SenderProxy getSenderProxy()
    {
        return (SenderProxy) BundleUtil.waitForService( SenderProxy.class.getName());
    }

    public static EmailTransport getEmailTransport()
    {
        return (EmailTransport) BundleUtil.waitForService(EmailTransport.class.getName());
    }

    public static CacheManager getCacheManager()
    {
        return (CacheManager) BundleUtil.waitForService(CacheManager.class.getName());
    }

    @Deprecated
    public static OracleLobHandler getOracleLobHandler()
    {
        return (OracleLobHandler) BundleUtil.waitForService(OracleLobHandler.class.getName());
    }

    public static LobHandler getLobHandler()
    {
        return (LobHandler) BundleUtil.waitForService(LobHandler.class.getName());
    }

    public static DataSource getDataSource()
    {
        return (DataSource) BundleUtil.waitForService(DataSource.class.getName());
    }

    public static SqlTemplate getSqlTemplate()
    {
        return (SqlTemplate) BundleUtil.waitForService(SqlTemplate.class.getName());
    }

    @SuppressWarnings( "rawtypes" )
    public static OrmStandardEngine getOrmStandardEngine()
    {
        return (OrmStandardEngine) BundleUtil.waitForService(OrmStandardEngine.class.getName());
    }

    public static ObjectAttributeMap getObjectAttributeMap()
    {
        return (ObjectAttributeMap) BundleUtil.waitForService(ObjectAttributeMap.class.getName());
    }

    @Deprecated
    public static Map<String, Map<String, String>> getObjectAttributeDef()
    {
        Map<String, Map<String, String>> returns = new HashMap<String, Map<String, String>>();
        ObjectAttributeMap attributeMap = getObjectAttributeMap();
        Map<String, List<ObjectAttributeDef>> map = attributeMap.getMap();
        Set<String> set = map.keySet();
        for (Iterator<String> iterator = set.iterator(); iterator.hasNext();)
        {
            String string = (String) iterator.next();
            String key = string.substring(string.lastIndexOf(".") + 1, string.length());

            if (ObjectAttributeMap.ExtendedC_Type.equals(key))
            {
                List<ObjectAttributeDef> list = map.get(string);
                for (Iterator<ObjectAttributeDef> iter = list.iterator(); iter.hasNext();)
                {
                    ObjectAttributeDef object = iter.next();
                    if (returns.containsKey(object.getTypeName()))
                    {
                        returns.get(object.getTypeName()).put(object.getAttributeName(), "");
                    }
                    else
                    {
                        Map<String, String> objDefs = new HashMap<String, String>();
                        objDefs.put(object.getAttributeName(), "");
                        returns.put(object.getTypeName(), objDefs);
                    }
                }
            }
        }
        return returns;
    }

    private void addSystemProperty()
    {
        try
        {
            System.setProperty( "nanoframe.ip", InetAddress.getLocalHost().getHostAddress());
            System.setProperty( "nanoframe.hostname", InetAddress.getLocalHost().getHostName());
        }
        catch ( UnknownHostException ex )
        {}
    }

    public static void closeListeners()
    {
        try
        {
            BundleMessageEventAdaptor bundleMessageEventAdaptor = (BundleMessageEventAdaptor) BundleUtil.getService(BundleMessageEventAdaptor.class.getName());

            if ( bundleMessageEventAdaptor == null )
                log.info( "No BundleMessageEventAdaptor" );
            else
            {
                log.info("Closing message listeners .....");
                bundleMessageEventAdaptor.terminate();
            }
        }
        catch ( Exception ex )
        {
            log.error( ex, ex );
        }
    }

    public static void shutdownBpelProcessManager()
    {
        try
        {
            BpelProcessManager bpelProcessManager =	(BpelProcessManager) BundleUtil.getService(BpelProcessManager.class.getName());

            if ( bpelProcessManager == null )
                log.info( "No BpelProcessManager" );
            else
                bpelProcessManager.shutdown( bpelCompletionTimeout );
        }
        catch ( NoClassDefFoundError ex )
        {
            log.info( "No BpelProcessManager" );
        }
        catch ( Exception ex )
        {
            log.error( ex, ex );
        }
    }

    public static void shutdownOSGi()
    {
        try
        {
            FrameworkCommandProvider cmdProvider = BundleUtil.getFrameworkCommandProvider();

            if ( cmdProvider != null )
                cmdProvider._shutdown( null );
            else
                log.info( "No FrameworkCommandProvider" );
        }
        catch ( Exception ex )
        {
            log.error( ex, ex );
        }
    }
}
