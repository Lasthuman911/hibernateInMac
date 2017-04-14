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

package kr.co.aim.nanotrack.generic.exception;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameDBErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameErrorSignal;

import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/*
 ****************************************************************************
 *  PACKAGE : kr.co.aim.nanotrack.generic.exception
 *  NAME    : ExceptionNotify.java
 *  TYPE    : JAVA
 *  DESCRIPTION :
 *
 ****************************************************************************
 */

public class ExceptionNotify
{
    //	public static void notifyException(DataAccessException e, String dataKey, String sql)
    //			throws DuplicateNameSignal, FrameworkErrorSignal
    //	{
    //		// TODO improve later : vendor, code, .......
    //		if (getDataAccessError(e).getErrorCode() == 1)
    //			throw new DuplicateNameSignal(e, dataKey, sql);
    //		else
    //			throw new FrameworkErrorSignal(ExceptionKey.DataAccess_Exception, e, e.getMessage(), sql, dataKey);
    //	}

    //	public static SQLException getDataAccessError(DataAccessException e)
    //	{
    //		return (SQLException) e.getCause();
    //	}

    public static LocalizedException getNotifyException(DataAccessException e, String dataKey, String sql)
    {
        //		Throwable cause = e.getCause();
        Throwable cause = e.getMostSpecificCause();
        if (cause == null)
            cause = e;

        if (cause != null && cause instanceof SQLException)
        {
            return getNotifyException((SQLException) cause, dataKey, sql);
        }
        return new FrameworkErrorSignal(ExceptionKey.DataAccess_Exception, cause, cause.getMessage(), sql, dataKey);
    }

    public static LocalizedException getNotifyException(SQLException e, String dataKey, String sql)
    {
        if (e.getErrorCode() == 1)
            return new DuplicateNameSignal(e, dataKey, sql);
        else
            return new FrameworkErrorSignal(ExceptionKey.DataAccess_Exception, e, e.getMessage(), sql, dataKey);
    }

    public static void notifyException(Throwable e)
            throws DuplicateNameSignal, NotFoundSignal, InvalidStateTransitionSignal, FrameworkErrorSignal
    {
        throw getNotifyException(e);
    }

    public static RuntimeException getNotifyException(Throwable e)
    {
        if (e instanceof DuplicateNameSignal)
            return (DuplicateNameSignal) e;
        else if (e instanceof NotFoundSignal)
            return (NotFoundSignal) e;
        else if (e instanceof InvalidStateTransitionSignal)
            return (InvalidStateTransitionSignal) e;
        else if (e instanceof FrameworkErrorSignal)
            return (FrameworkErrorSignal) e;
        else if (e instanceof nanoFrameDBErrorSignal)
        {
            nanoFrameDBErrorSignal ne = (nanoFrameDBErrorSignal) e;
            if (ErrorSignal.NotFoundSignal.equals(ne.getErrorCode()))
            {
                throw new NotFoundSignal(ne, ne.getDataKey(), ne.getSql());
            }
            else if (ErrorSignal.DuplicateNameSignal.equals(ne.getErrorCode()))
            {
                throw new DuplicateNameSignal(ne, ne.getDataKey(), ne.getSql());
            }
            else if (ErrorSignal.DataAccessException.equals(ne.getErrorCode()))
            {
                Throwable te = ne.getCause();

                if (te instanceof SQLException)
                {
                    throw getNotifyException((SQLException) te, ne.getDataKey(), ne.getSql());
                }
                else if (te instanceof EmptyResultDataAccessException)
                    throw new NotFoundSignal(te, ne.getDataKey(), ne.getSql());
                else if (te instanceof DataAccessException)
                    throw getNotifyException((DataAccessException) te, ne.getDataKey(), ne.getSql());
            }
            else if (ErrorSignal.NullPointKeySignal.equals(ne.getErrorCode()))
            {
                throw new FrameworkErrorSignal(ExceptionKey.NullPointKey_Exception, ne, ne.getDataKey());
            }
            return ne;
            //			return new FrameworkErrorSignal(ExceptionKey.UnExpected_Exception, ne, ne.getMessage());
            //			return new FrameworkErrorSignal(ExceptionKey.UnExpected_Exception, ne, ne.getMessage(), ne.getBindSet(), ne.getSql());
        }
        else if (e instanceof nanoFrameErrorSignal)
        {
            return (nanoFrameErrorSignal) e;
        }
        else if (e instanceof SQLException)
        {
            return getNotifyException((SQLException) e, "", "");
        }
        else if (e instanceof DataAccessException)
        {
            return getNotifyException((DataAccessException) e, "", "");
        }
        else if (e instanceof InvocationTargetException)
        {
            Throwable targetEx = ((InvocationTargetException) e).getTargetException();
            return getNotifyException(targetEx);
        }
        else if (e instanceof NestedRuntimeException)
        {
            Throwable t = ((NestedRuntimeException) e).getMostSpecificCause();
            if (t == null)
                t = e;
            return new FrameworkErrorSignal(ExceptionKey.UnExpected_Exception, t, t.getClass().getSimpleName()
                    + ":"
                    + t.getMessage());
        }
        else
        {
            return new FrameworkErrorSignal(ExceptionKey.UnExpected_Exception, e, e.getClass().getSimpleName()
                    + ":"
                    + e.getMessage());
        }
    }

}
