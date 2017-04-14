/*
 ****************************************************************************
 *
 *  (c) Copyright 2010 AIM Systems, Inc. All rights reserved.
 *
 *  This software is proprietary to and embodies the confidential
 *  technology of AIM Systems, Inc. Possession, use, or copying of this
 *  software and media is authorized only pursuant to a valid written
 *  license from AIM Systems, Inc.
 *
 ****************************************************************************
 */

package kr.co.aim.nanoframe.orm;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import kr.co.aim.nanoframe.orm.info.DataInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

/*
 ****************************************************************************
 *  PACKAGE : kr.co.aim.nanoframe.orm
 *  NAME    : SQLLogUtil.java
 *  TYPE    : JAVA
 *  DESCRIPTION :
 *
 ****************************************************************************
 */

public class SQLLogUtil
{
    private static StringComparator	stringComparator	= new StringComparator(false);

    public static void logBeforeExecuting(String sql, Log log)
    {
        if (log.isDebugEnabled())
            log.debug("SQL [" + sql + "]");
    }

    public static void logAfterQuery(Object result, Log log)
    {
        if (log.isDebugEnabled())
        {
            if (result instanceof List)
                log.debug("SQL query returned " + ((List) result).size() + " rows");
            else if (result instanceof DataInfo)
                log.debug("SQL query returned 1 rows");
            else
                log.debug("SQL query returned " + (Integer) result);
        }
    }

    public static void logAfterUpdate(int result, Log log)
    {
        if (log.isDebugEnabled())
            log.debug("SQL update affected " + result + " rows");
    }

    public static String getLogFormatSqlStatement(String sql, Object args, Log log)
    {
        try
        {
            if (args instanceof Map)
            {
                Map<String, Object> map = (Map<String, Object>) args;

                TreeMap<String, Object> treeMap = new TreeMap<String, Object>(stringComparator);
                treeMap.putAll(map);

                Iterator<Entry<String, Object>> iter = treeMap.entrySet().iterator();
                while (iter.hasNext())
                {
                    Entry<String, Object> entry = iter.next();
                    sql = sql.replace(":" + entry.getKey(), getLogFormatArgument(entry.getValue()));
                }
            }
            else if (args instanceof Object[])
            {
                Object[] objs = (Object[]) args;

                for (Object obj : objs)
                {
                    sql = StringUtils.replaceOnce(sql, "?", getLogFormatArgument(obj));
                }
            }
        } catch (Throwable t)
        {
            if (log.isDebugEnabled())
                log.error(t, t);
            else
                log.error(t);
        }

        return sql;
    }

    public static String getLogFormatArgument(Object arg)
    {
        if (arg instanceof String)
            return "'" + (String)arg + "'";
        else if (arg instanceof Timestamp || arg instanceof Date)
            return "timestamp '" + arg.toString() + "'";
        else if ( arg == null )
            return "null";
        else
            return arg.toString();
    }
}

class StringComparator implements Comparator<String>
{
    private boolean	sortOrderAscending	= true;

    public StringComparator()
    {
    }

    public StringComparator(boolean sortOrderAscending)
    {
        this.sortOrderAscending = sortOrderAscending;
    }

    public int compare(String o1, String o2)
    {
        int result = o1.compareTo(o2);
        return sortOrderAscending ? result : -result;
    }

}