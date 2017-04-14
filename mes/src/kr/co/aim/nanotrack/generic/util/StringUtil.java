package kr.co.aim.nanotrack.generic.util;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils
{
    public static boolean in(String value, String... args)
    {
        for (String compValue : args)
        {
            if (StringUtils.equals(value, compValue))
                return true;
        }

        return false;
    }

    public static boolean inStartsWith(String value, String... args)
    {
        for (String compValue : args)
        {
            if (compValue.startsWith(value))
                return true;
        }

        return false;
    }

    public static String getIndexValue(String value, String delimiter, int index)
    {
        String[] values = value.split(delimiter);
        return values[index];
    }

    public static String getFirstValue(String value, String delimiter)
    {
        int index = value.indexOf(delimiter);
        if (index < 0)
            return value;
        else
        {
            return value.substring(0, index);
        }
    }

    public static String getLastValue(String value, String delimiter)
    {
        int index = value.lastIndexOf(delimiter);
        if (index < 0)
            return value;
        else
        {
            return value.substring(index + 1);
        }
    }

}
