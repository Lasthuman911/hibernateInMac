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

package kr.co.aim.nanoframe.exception;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;

/*
 ****************************************************************************
 *  PACKAGE : kr.co.aim.nanoframe.exception
 *  NAME    : ErrorSignal.java
 *  TYPE    : JAVA
 *  DESCRIPTION :
 *
 ****************************************************************************
 */

public class ErrorSignal
{
    public final static String	NotActive						= "NotActive";
    public final static String	CouldNotMatchData				= "CouldNotMatchData:DataInfo";
    public final static String	InvalidQueryType				= "InvalidQueryType";
    public final static String	InvalidQueryState				= "InvalidQueryState";
    public final static String	NullPointKeySignal				= "NullPointKeySignal";
    public final static String	UnexprectedSignal				= "UnexprectedSignal";
    public final static String	InvalidArgumentSignal			= "InvalidArgumentSignal";
    public final static String	NoDefineServiceBean				= "NoDefineServiceBean";
    public final static String	NoServiceRegistered				= "NoServiceRegistered";
    public final static String	NoServiceBeanRegistered			= "NoServiceBeanRegistered";
    public final static String	NoBeanDefinition				= "NoBeanDefinition";
    public final static String	NotDefineObjectAttributeSignal	= "NotDefineObjectAttributeSignal";

    public static final String	DataAccessException				= "DataAccessException";
    public final static String	DuplicateNameSignal				= "DuplicateNameSignal";
    public final static String	NotFoundSignal					= "NotFoundSignal";

    public final static String	InvalidMessageType				= "InvalidMessageType";

    public final static String	InvalidParameter				= "InvalidParameter";

    public static nanoFrameErrorSignal getNotifyException(Throwable e)
    {
        if (e instanceof nanoFrameErrorSignal)
            return (nanoFrameErrorSignal) e;
        else if (e instanceof InvocationTargetException)
        {
            Throwable cause = ((InvocationTargetException) e).getTargetException();
            return getNotifyException(cause);
        }
        else if (e instanceof SQLException)
        {
            return getNotifyException((SQLException) e, null, null);
        }
        else if (e instanceof DataAccessException)
        {
            return getNotifyException((DataAccessException) e);
        }
        else if (e instanceof NestedRuntimeException)
        {
            Throwable cause = ((NestedRuntimeException) e).getMostSpecificCause();
            if (cause != null)
                return new nanoFrameErrorSignal(ErrorSignal.UnexprectedSignal, e.getMessage(), e);
            else
            {
                cause = e.getCause();
                if (cause != null)
                    return new nanoFrameErrorSignal(ErrorSignal.UnexprectedSignal, e.getMessage(), e);
            }
        }

        return new nanoFrameErrorSignal(ErrorSignal.UnexprectedSignal, e.getMessage(), e);
    }

    public static void notifyException(Throwable e)
    {
        throw getNotifyException(e);
    }

    public static nanoFrameDBErrorSignal getNotifyException(DataAccessException e)
    {
        Throwable cause = e.getMostSpecificCause();
        //		Throwable cause = e.getCause();
        if (cause != null && cause instanceof SQLException)
        {
            return getNotifyException((SQLException) cause, null, null);
        }
        return new nanoFrameDBErrorSignal(ErrorSignal.DataAccessException, e);
    }

    public static nanoFrameDBErrorSignal getNotifyException(DataAccessException e, String sql)
    {
        Throwable cause = e.getMostSpecificCause();
        //		Throwable cause = e.getCause();
        if (cause != null && cause instanceof SQLException)
        {
            return getNotifyException((SQLException) cause, null, sql);
        }
        return new nanoFrameDBErrorSignal(ErrorSignal.DataAccessException, sql, e);
    }

    public static nanoFrameDBErrorSignal getNotifyException(DataAccessException e, String bindSet, String sql)
    {
        Throwable cause = e.getMostSpecificCause();
        //		Throwable cause = e.getCause();
        if (cause != null && cause instanceof SQLException)
        {
            return getNotifyException((SQLException) cause, bindSet, sql);
        }
        return new nanoFrameDBErrorSignal(ErrorSignal.DataAccessException, bindSet, sql, e);
    }

    public static nanoFrameDBErrorSignal getNotifyException(SQLException e, String bindSet, String sql)
    {
        String errorCode;
        if (e.getErrorCode() == 1)
            errorCode = ErrorSignal.DuplicateNameSignal;
        else
            errorCode = ErrorSignal.DataAccessException;

        if (StringUtils.isNotEmpty(bindSet) && StringUtils.isNotEmpty(sql))
            return new nanoFrameDBErrorSignal(errorCode, bindSet, sql, e);
        else if (StringUtils.isNotEmpty(sql))
            return new nanoFrameDBErrorSignal(errorCode, sql, e);
        else
            return new nanoFrameDBErrorSignal(errorCode, e);
    }

}
