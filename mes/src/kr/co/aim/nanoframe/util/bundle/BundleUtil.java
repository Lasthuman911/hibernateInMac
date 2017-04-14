package kr.co.aim.nanoframe.util.bundle;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import kr.co.aim.nanoframe.nanoFrameServiceProxy;
import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameErrorSignal;
import kr.co.aim.nanoframe.util.file.CollectionUtil;
import kr.co.aim.nanoframe.util.file.FileUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.osgi.baseadaptor.BaseData;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.eclipse.osgi.framework.internal.core.AbstractBundle;
import org.eclipse.osgi.framework.internal.core.FrameworkCommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

public class BundleUtil {

    private static Log log = LogFactory.getLog(BundleUtil.class);

    private static final String FILTER_NAME_BEAN = "org.springframework.osgi.bean.name";

    private static long serviceLookupTimeout = 30000;

    static {
        String timeout = System.getProperty(nanoFrameProperties.SERVICE_LOOKUP_TIMEOUT);

        if (org.apache.commons.lang.StringUtils.isNotEmpty(timeout))
            serviceLookupTimeout = Long.parseLong(timeout);
    }

    public static long getServiceLookupTimeout() {
        return serviceLookupTimeout;
    }

    public static Object getService(String interfaceName) {
        return getService(nanoFrameServiceProxy.getBundleContext(), interfaceName);
    }

    public static Object getService(BundleContext bundleContext, String interfaceName) {
        ServiceTracker serviceTracker = new ServiceTracker(bundleContext, interfaceName, null);
        serviceTracker.open();

        Object service = serviceTracker.getService();

        serviceTracker.close();

        return service;
    }

    public static Object getServiceByBeanName(String beanName) {
        Filter filter = null;

        try {
            filter =
                    nanoFrameServiceProxy.getBundleContext()
                            .createFilter("(" + FILTER_NAME_BEAN + "=" + beanName + ")");
        } catch (InvalidSyntaxException ex) {
            throw new RuntimeException(ex);
        }

        ServiceTracker serviceTracker = new ServiceTracker(nanoFrameServiceProxy.getBundleContext(), filter, null);
        serviceTracker.open();

        Object service = serviceTracker.getService();

        serviceTracker.close();

        return service;
    }

    public static Object waitForService(String interfaceName) {
        return waitForService(interfaceName, serviceLookupTimeout);
    }

    public static Object waitForService(String interfaceName, long timeout) {
        ServiceTracker serviceTracker = new ServiceTracker(nanoFrameServiceProxy.getBundleContext(), interfaceName, null);
        serviceTracker.open();

        Object service = null;

        try {
            service = serviceTracker.getService();

            if (service == null) {
                log.info("Waiting for service(" + interfaceName + ") until timeout(" + String.valueOf(timeout) + " milliseconds)");
                service = serviceTracker.waitForService(timeout);

                if (service == null)
                    throw new nanoFrameErrorSignal(ErrorSignal.NoServiceRegistered, interfaceName);
                else
                    log.info("Returning service under interface(" + interfaceName + ")");
            }

            return service;
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            serviceTracker.close();
        }
    }

    public static Object waitForServiceByBeanName(String beanName) {
        return waitForServiceByBeanName(beanName, serviceLookupTimeout);
    }

    public static Object waitForServiceByBeanName(String beanName, long timeout) {
        Filter filter = null;

        try {
            filter = nanoFrameServiceProxy.getBundleContext().createFilter("(" + FILTER_NAME_BEAN + "=" + beanName + ")");

        } catch (InvalidSyntaxException ex) {
            throw new RuntimeException(ex);
        }

        ServiceTracker serviceTracker = new ServiceTracker(nanoFrameServiceProxy.getBundleContext(), filter, null);
        serviceTracker.open();

        try {
            Object service = serviceTracker.getService();

            if (service == null) {
                log.info("Waiting for service under bean name(" + beanName + ") until timeout(" + String.valueOf(timeout) + " milliseconds)");
                service = serviceTracker.waitForService(timeout);

                if (service == null)
                    throw new nanoFrameErrorSignal(ErrorSignal.NoServiceBeanRegistered, beanName);
                else
                    log.info("Returning service under bean name(" + beanName + ")");
            }

            return service;
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            serviceTracker.close();
        }
    }

    public static Object[] getServices(String interfaceName) {
        ServiceTracker serviceTracker =
                new ServiceTracker(nanoFrameServiceProxy.getBundleContext(), interfaceName, null);
        serviceTracker.open();

        Object[] services = serviceTracker.getServices();

        serviceTracker.close();

        return services;
    }

    public static Object getBundleServiceClass(Class clazz) throws nanoFrameErrorSignal {
        return getBundleServiceClass(nanoFrameServiceProxy.getBundleContext(), null, clazz, "", -1);
    }


    public static Object getBundleServiceClass(String className) throws nanoFrameErrorSignal {
        return getBundleServiceClass(nanoFrameServiceProxy.getBundleContext(), null, className, -1);
    }

    public static Object getBundleServiceClass(BundleContext bc, ApplicationContext ac, Class clazz, String beanName)
            throws nanoFrameErrorSignal {
        return getBundleServiceClass(bc, ac, clazz, beanName, -1);
    }

    public static Object getBundleServiceClass(BundleContext bc, ApplicationContext ac, Class clazz, String beanName,
                                               long timeOut) throws nanoFrameErrorSignal {
        try {
            serviceLookupTimeout = Long.parseLong(System.getProperty(nanoFrameProperties.SERVICE_LOOKUP_TIMEOUT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bc != null) {
            Object service = null;
            ServiceTracker serviceTracker = new ServiceTracker(bc, clazz.getName(), null);
            serviceTracker.open();
            try {
                if (timeOut <= -1)
                    service = serviceTracker.waitForService(serviceLookupTimeout);
                else
                    service = serviceTracker.waitForService(timeOut);
                if (service != null) {
                    serviceTracker.close();
                    return service;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (ac != null && beanName != null) {
            if (ac.containsBean(beanName))
                return ac.getBean(beanName);
        }
        throw new nanoFrameErrorSignal(ErrorSignal.NoDefineServiceBean, clazz.getName());
    }

    public static Object getBundleServiceClass(BundleContext bc, ApplicationContext ac, String serviceName, long timeOut)
            throws nanoFrameErrorSignal {
        try {
            serviceLookupTimeout = Long.parseLong(System.getProperty(nanoFrameProperties.SERVICE_LOOKUP_TIMEOUT));
        } catch (Exception e) {
        }
        ServiceTracker serviceTracker = null;
        Object service = null;
        if (bc != null) {
            serviceTracker = new ServiceTracker(bc, serviceName, null);
            serviceTracker.open();
            try {
                if (timeOut <= -1)
                    service = serviceTracker.waitForService(serviceLookupTimeout);
                else
                    service = serviceTracker.waitForService(timeOut);
                if (service != null) {
                    serviceTracker.close();
                    return service;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String names[] = StringUtils.delimitedListToStringArray(serviceName, ".");

        if (bc != null && serviceName.startsWith("kr.co.aim.nanotrack.")) {
            int offset = serviceName.lastIndexOf(".");

            StringBuilder builder = new StringBuilder();
            builder.append(serviceName);
            builder.insert(offset + 1, names[4].substring(0, 1).toUpperCase());
            builder.insert(offset + 2, names[4].substring(1));

            serviceName = builder.toString();
            serviceTracker = new ServiceTracker(bc, serviceName, null);
            serviceTracker.open();
            try {
                if (timeOut <= -1)
                    service = serviceTracker.waitForService(serviceLookupTimeout);
                else
                    service = serviceTracker.waitForService(timeOut);
                if (service != null) {
                    serviceTracker.close();
                    return service;
                }
            } catch (Exception e) {
            }
        }

        if (ac != null) {
            String beanName = names[names.length - 1];
            if (ac.containsBean(beanName))
                return ac.getBean(beanName);
            if (beanName.endsWith("Impl"))
                beanName = beanName.substring(0, beanName.length() - 4);
            if (ac.containsBean(beanName))
                return ac.getBean(beanName);
        }

        // TODO kr.co.aim.nanotrack.machine.management.policy.SetEventPolicy 捞 粮犁窍瘤 臼绰促.
        // kr.co.aim.nanotrack.machine.management.policy.MachineSetEventPolicy
        // 烙矫 规祈 备泅...

        throw new nanoFrameErrorSignal(ErrorSignal.NoDefineServiceBean, serviceName);
    }

    public static Object getBundleServiceClass(BundleContext bc, ApplicationContext ac, String serviceName)
            throws nanoFrameErrorSignal {
        return getBundleServiceClass(bc, ac, serviceName, -1);
    }

    public static List<org.w3c.dom.Document> getBundleBpels(Bundle bundle, String resourcePath) {
        return getBundleBpels(bundle, resourcePath, new ArrayList<File>(), new ArrayList<String>());
    }

    public static List<org.w3c.dom.Document> getBundleBpels(Bundle bundle, String resourcePath, List<File> bpelAllList, List<String> failedList) {
        List<org.w3c.dom.Document> docList = new ArrayList<org.w3c.dom.Document>();
        String entryName = null;

        try {
            BaseData baseData = (BaseData) ((AbstractBundle) bundle).getBundleData();
            String path = baseData.getBundleFile().getBaseFile().getAbsolutePath();
            //path = path + ".jar";
            JarFile jar = new JarFile(path);
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                entryName = jarEntry.getName();

                if (resourcePath.startsWith("/")) {
                    if (!entryName.startsWith("/"))
                        entryName = "/" + entryName;
                }

                if (entryName.startsWith(resourcePath) && entryName.toLowerCase().endsWith(".bpel")) {
                    try {
                        InputStream inputStream = jar.getInputStream(jarEntry);
                        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                        javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();

                        docList.add(builder.parse(inputStream));
                    } catch (Exception ex) {
                        failedList.add(entryName);
                        log.error(ex, ex);
                    } finally {
                        bpelAllList.add(new File(entryName));
                    }
                }
            }
            return docList;
        } catch (Exception e) {
            log.error(e, e);
            return null;
        }
    }

    public static InputStream getBundleResourceFile(Bundle bundle, String resourcePath, String fileDescriptor) {
        try {
            BaseData baseData = (BaseData) ((AbstractBundle) bundle).getBundleData();
            String path = baseData.getBundleFile().getBaseFile().getAbsolutePath();
            //path = path + ".jar";
            File file = new File(path + File.separator + resourcePath + "." + fileDescriptor);
            if (file.exists()) {
                return new BufferedInputStream(new FileInputStream(file.getPath()));
            }
            JarFile jar = new JarFile(path);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                String name = jarEntry.getName();
                if (resourcePath.startsWith("/")) {
                    if (!name.startsWith("/"))
                        name = "/" + name;
                }
                if (name.startsWith(resourcePath) && name.toLowerCase().endsWith("." + fileDescriptor)) {
                    return jar.getInputStream(jarEntry);
                }
            }
            return null;
        } catch (Exception e) {
            log.error(e, e);
            return null;
        }
    }

    public static InputStream getBundleResourceInputStream(Bundle bundle, String resourcePath) {
        try {
            BaseData baseData = (BaseData) ((AbstractBundle) bundle).getBundleData();
            String path = baseData.getBundleFile().getBaseFile().getAbsolutePath();
            //path = path + ".jar";
            JarFile jar = new JarFile(path);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                String name = jarEntry.getName();
                if (resourcePath.startsWith("/"))
                    name = "/" + name;
                if (name.equals(resourcePath)) {
                    InputStream inputStream = jar.getInputStream(jarEntry);
                    return inputStream;
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        return null;
    }

    public static File getBundleResource(Bundle bundle, String resourcePath) {
        try {
            File file = new File(resourcePath);
            if (file.exists())
                return file;

            BaseData baseData = (BaseData) ((AbstractBundle) bundle).getBundleData();
            String path = baseData.getBundleFile().getBaseFile().getAbsolutePath();
            file = new File(path + File.separator + resourcePath);
            if (!file.exists()) {
                log.debug("Could not load bundle resource : " + path);
                return null;
            }
            return file;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            log.error(e, e);
            return null;
        } catch (Exception e) {
            log.error(e, e);
            return null;

        }
    }

    public static File getBundleResourceFile(Bundle bundle, String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists())
                return file;

            BaseData baseData = (BaseData) ((AbstractBundle) bundle).getBundleData();
            String path = baseData.getBundleFile().getBaseFile().getAbsolutePath();
            file = new File(path + File.separator + fileName);
            if (!file.exists()) {
                log.debug("Could not load bundle resource : " + path);
                return null;
            }
            return file;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            log.error(e, e);
            return null;
        }
    }

    public static Bundle getBundle(Bundle[] bundles, String SymbolicName) {
        for (int i = 0; i < bundles.length; i++) {
            // 2009.11.4 ytjung - Bundle-SymbolicName俊 singleton 沥焊啊 乐阑 荐 乐澜
            String symName = (String) bundles[i].getHeaders().get("Bundle-SymbolicName");
            String orgName = StringUtils.delimitedListToStringArray(symName, ";")[0];
            if (orgName.equalsIgnoreCase(SymbolicName))
                return bundles[i];
        }
        log.warn("Could not find bundle [" + SymbolicName + "]");
        return null;
    }

    public static String getState(Bundle bundle) {

        int state = bundle.getState();
        String result = "UNKNOWN";
        switch (state) {
            case Bundle.ACTIVE:
                result = "ACTIVE";
                break;
            case Bundle.INSTALLED:
                result = "INSTALLED";
                break;
            case Bundle.RESOLVED:
                result = "RESOLVED";
                break;
            case Bundle.STARTING:
                result = "STARTING";
                break;
            case Bundle.STOPPING:
                result = "STOPPING";
                break;
            case Bundle.UNINSTALLED:
                result = "UNINSTALLED";
                break;
        }
        return result;
    }

    public static String getBundleName(Bundle bundle) {
        Dictionary headers = bundle.getHeaders();
        Object obj = headers.get(Constants.BUNDLE_NAME);
        if (obj == null) {
            return null;
        }
        return (String) obj;
    }

    public static String getBundleHeader(Bundle bundle, String key) {
        Dictionary headers = bundle.getHeaders();
        Object obj = headers.get(key);
        if (obj == null) {
            return null;
        } else {
            return (String) obj;
        }
    }

    public static FrameworkCommandProvider getFrameworkCommandProvider() {
        Object[] services = getServices(CommandProvider.class.getName());

        for (Object service : services) {
            if (service instanceof FrameworkCommandProvider)
                return (FrameworkCommandProvider) service;
        }

        return null;
    }

    public static String[] getBundleResource(Bundle bundle, String resourcePath, String filter, boolean addResourcePath)
            throws Exception {
        if (!BundleUtil.isExistBundleResource(bundle, resourcePath))
            return null;

        File file = BundleUtil.getBundleResource(bundle, resourcePath);
        //bpelFile = null;
        if (file != null) {
            List fileList = FileUtil.getFileList(file, filter, false);
            String[] files = new String[fileList.size()];
            for (int i = 0; i < fileList.size(); i++) {
                if (resourcePath.endsWith("/"))
                    files[i] = resourcePath + ((File) fileList.get(i)).getName();
                else
                    files[i] = resourcePath + "/" + ((File) fileList.get(i)).getName();
            }
            return files;
        } else {
            List<String> files = new ArrayList<String>();
            BaseData baseData = (BaseData) ((AbstractBundle) bundle).getBundleData();
            String path = baseData.getBundleFile().getBaseFile().getAbsolutePath();
            //path = path + ".jar";
            JarFile jar = new JarFile(path);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                String name = jarEntry.getName();
                if (name.startsWith(resourcePath) && name.endsWith(filter)) {
                    name = StringUtils.replace(name, resourcePath, "");
                    if (name.length() > 0) {
                        if (name.indexOf("/") == 0)
                            name = name.substring(1, name.length());
                        if (name.indexOf("/") < 0) {
                            if (resourcePath.endsWith("/"))
                                files.add(resourcePath + name);
                            else
                                files.add(resourcePath + "/" + name);
                        }
                    }
                }
            }
            String[] strFiles = new String[files.size()];
            for (int i = 0; i < strFiles.length; i++) {
                strFiles[i] = files.get(i);
            }

            return strFiles;
        }
    }

    public static String[] getBundleResource2(Bundle bundle, String resourcePath, String filter, boolean addResourcePath) {
        try {
            URL url = bundle.getResource(resourcePath);

            BaseData baseData = (BaseData) ((AbstractBundle) bundle).getBundleData();
            String resourceDir = baseData.getBundleFile().getBaseFile().getPath() + File.separator + resourcePath;
            List fileList = FileUtil.getFileNameList(bundle, resourceDir, ".XML", false);

            if (addResourcePath) {
                String newValue = "";
                for (int i = 0; i < fileList.size(); i++) {
                    if (!resourcePath.endsWith(File.separator))
                        newValue = resourcePath + File.separator + (String) fileList.get(i);
                    else
                        newValue = resourcePath + (String) fileList.get(i);

                    fileList.set(i, newValue);
                }
            }
            return CollectionUtil.toStringArray(fileList);
        } catch (IllegalArgumentException e) {
            log.error(e, e);
        } catch (Exception e) {
            log.error(e, e);
        }
        return null;
    }

    public static boolean isExistBundleResource(Bundle bundle, String resourcePath) {
        try {
            URL url = bundle.getResource(resourcePath);
            if (url != null)
                return true;
            return false;
        } catch (Exception e) {
            log.error(e, e);
        }
        return false;
    }

}
