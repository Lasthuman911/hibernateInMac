package kr.co.aim.nanoframe.util.sys;

/**
 * wzm: 这个类很有用，比如CR，不同系统的分割符是不同的
 */
public class SystemPropHelper
{
    public static String	CR				= System.getProperty("line.separator");
    public static String	JAVA_VERSION	= System.getProperty("java.version");
    public static String	JAVA_HOME		= System.getProperty("java.home");
    public static String	FILE_SEPARATOR	= System.getProperty("file.separator");
    public static String	PATH_SEPARATOR	= System.getProperty("path.separater");
    public static String	USER_HOME		= System.getProperty("user.home");
    public static String	USER_DIR		= System.getProperty("user.dir");

}
