package util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Name: admin
 * Date: 2017/3/22
 * Time: 14:03
 */
public class CommonUtil {

    private static Log log = LogFactory.getLog(CommonUtil.class);

    /**
     * 指定了value必须是String 或者 BigDecimal
     *
     * @param map
     * @param keyName
     * @return
     */
    public static String getMapValue(Map map, String keyName) {
        try {
            Object value = map.get(keyName);
            if (value != null && value instanceof String)
                return value.toString();
            else if (value != null && value instanceof BigDecimal)
                return value.toString();
        } catch (Exception e) {
            if (log.isDebugEnabled())
                log.debug(e.getMessage());
        }
        return "";
    }

    /**
     * @return 返回本机IP地址
     */
    public static String getIpAddress() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ip;
    }

    /**
     *
     * @param obj
     * @return 将一个集合以String方式返回，用逗号分隔
     */
    public static String toStringFromCollection(Object obj) {
        StringBuffer temp = new StringBuffer("");

        if (obj instanceof Object[]) {
            for (Object element : (Object[]) obj) {
                if (!temp.toString().isEmpty())
                    temp.append(",");
                try {
                    temp.append(element.toString());
                } catch (Exception e) {
                    if (log.isDebugEnabled())
                        log.debug(e.getMessage());
                }

            }
        } else if (obj instanceof List) {
            for (Object element : (List<Object>) obj) {
                if (!temp.toString().isEmpty())
                    temp.append(",");
                try {
                    temp.append(element.toString());
                } catch (Exception e) {
                    if (log.isDebugEnabled())
                        log.debug(e.getMessage());
                }
            }
        }
        return temp.toString();
    }

}
