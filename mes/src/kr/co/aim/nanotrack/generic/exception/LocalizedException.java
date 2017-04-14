/*
 ****************************************************************************
 *
 *  (c) Copyright 2008 AIM Systems, Inc. All rights reserved.
 *
 *  This software is proprietary to and embodies the confidential
 *  technology of AIM Systems, Inc. Possession, use, or copying of this
 *  software and media is authorized only pursuant to a valid written
 *  license from AIM Systems, Inc.
 *
 ****************************************************************************
 */

package kr.co.aim.nanotrack.generic.exception;

import kr.co.aim.nanotrack.generic.GenericServiceProxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 ****************************************************************************
 *  PACKAGE : kr.co.aim.system.exception
 *  NAME    : LocalizedException.java
 *  TYPE    : JAVA
 *  DESCRIPTION :
 *
 ****************************************************************************
 */

@SuppressWarnings("serial")
public class LocalizedException extends RuntimeException
{
    protected Log		log	= LogFactory.getLog(this.getClass());

    protected String	errorCode;
    protected Object[]	errorArguments;

    public LocalizedException(String errorCode)
    {
        super(GenericServiceProxy.getExceptionMessages().format(errorCode, null));
        this.errorCode = errorCode;
        this.errorArguments = GenericServiceProxy.getExceptionMessages().preAppendOperation(new Object[0]);

        writeLog(LogLevel.Info, makeErrorLogMessage(), null);
    }

    public LocalizedException(String errorCode, Object... args)
    {
        super(GenericServiceProxy.getExceptionMessages().format(errorCode, args));
        this.errorCode = errorCode;
        this.errorArguments = GenericServiceProxy.getExceptionMessages().preAppendOperation(args);

        writeLog(LogLevel.Info, makeErrorLogMessage(), null);
    }

    public LocalizedException(LogLevel logLevel, String errorCode, Object... args)
    {
        super(GenericServiceProxy.getExceptionMessages().format(errorCode, args));
        this.errorCode = errorCode;
        this.errorArguments = GenericServiceProxy.getExceptionMessages().preAppendOperation(args);

        writeLog(logLevel, makeErrorLogMessage(), null);
    }

    public LocalizedException(String errorCode, Throwable throwable)
    {
        //		super(GenericServiceProxy.getExceptionMessages().format(errorCode, null), throwable);
        super(GenericServiceProxy.getExceptionMessages().format(errorCode, new Object[] { throwable.getMessage() }),
                throwable);
        this.errorCode = errorCode;
        this.errorArguments = GenericServiceProxy.getExceptionMessages().preAppendOperation(new Object[0]);

        writeLog(LogLevel.Info, makeErrorLogMessage(), throwable);
    }

    public LocalizedException(String errorCode, Throwable throwable, Object... args)
    {
        super(GenericServiceProxy.getExceptionMessages().format(errorCode, args), throwable);
        this.errorCode = errorCode;
        this.errorArguments = GenericServiceProxy.getExceptionMessages().preAppendOperation(args);

        writeLog(LogLevel.Info, makeErrorLogMessage(), throwable);
    }

    @SuppressWarnings("unchecked")
    public LocalizedException(String errorCode, Class serviceClass, String operation, Object... args)
    {
        super(GenericServiceProxy.getExceptionMessages().format(errorCode, serviceClass, operation, args));
        this.errorCode = errorCode;
        this.errorArguments =
                GenericServiceProxy.getExceptionMessages().preAppendOperation(serviceClass, operation, args);

        writeLog(LogLevel.Info, makeErrorLogMessage(), null);
    }

    @SuppressWarnings("unchecked")
    public LocalizedException(String errorCode, Throwable throwable, Class serviceClass, String operation,
                              Object... args)
    {
        super(GenericServiceProxy.getExceptionMessages().format(errorCode, serviceClass, operation, args), throwable);
        this.errorCode = errorCode;
        this.errorArguments =
                GenericServiceProxy.getExceptionMessages().preAppendOperation(serviceClass, operation, args);

        writeLog(LogLevel.Info, makeErrorLogMessage(), throwable);
    }

    @SuppressWarnings("unchecked")
    public LocalizedException(LogLevel logLevel, String errorCode, Throwable throwable, Class serviceClass, String operation,
                              Object... args)
    {
        super(GenericServiceProxy.getExceptionMessages().format(errorCode, serviceClass, operation, args), throwable);
        this.errorCode = errorCode;
        this.errorArguments =
                GenericServiceProxy.getExceptionMessages().preAppendOperation(serviceClass, operation, args);

        writeLog(logLevel, makeErrorLogMessage(), throwable);
    }

    public String getErrorCode()
    {
        return this.errorCode;
    }

    public Object[] getErrorArguments()
    {
        return errorArguments;
    }

    private String makeErrorLogMessage()
    {
        return String.format("%s: %s", this.errorCode, this.getMessage());
    }

    private void writeLog(LogLevel logLevel, Object logMsg, Throwable e)
    {
        switch (logLevel)
        {
            case Fatal:
//				if (log.isDebugEnabled())
                log.fatal(logMsg, e);
//				else
//					log.fatal(logMsg);
                break;
            case Error:
//				if (log.isDebugEnabled())
                log.error(logMsg, e);
//				else
//					log.error(logMsg);
                break;
            case Warn:
                if (log.isDebugEnabled())
                    log.warn(logMsg, e);
                else
                    log.warn(logMsg);
                break;
            case Info:
                if (log.isDebugEnabled())
                    log.info(logMsg, e);
                else
                    log.info(logMsg);
                break;
            case Debug:
                if (log.isDebugEnabled())
                    log.debug(logMsg, e);
                break;
            case Trace:
                if (log.isTraceEnabled())
                    log.trace(logMsg, e);
                break;
        }
    }
}
