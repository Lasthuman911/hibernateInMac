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

/*
 ****************************************************************************
 *  PACKAGE : generic
 *  NAME    : FrameworkErrorSignal.java
 *  TYPE    : JAVA
 *  DESCRIPTION :
 *
 ****************************************************************************
 */

@SuppressWarnings("serial")
public class FrameworkErrorSignal extends LocalizedException
{

    public FrameworkErrorSignal(String errorCode)
    {
        super(errorCode);
    }

    public FrameworkErrorSignal(String errorCode, Object... args)
    {
        super(errorCode, args);
    }

    //	public FrameworkErrorSignal(String errorCode, Throwable throwable)
    //	{
    //		super(errorCode, throwable);
    //	}

    public FrameworkErrorSignal(String errorCode, Throwable throwable, Object... args)
    {
        super(errorCode, throwable, args);
    }

    @SuppressWarnings("unchecked")
    public FrameworkErrorSignal(String errorCode, Class serviceClass, String operation, Object... args)
    {
        super(errorCode, serviceClass, operation, args);
    }

    @SuppressWarnings("unchecked")
    public FrameworkErrorSignal(String errorCode, Throwable throwable, Class serviceClass, String operation,
                                Object... args)
    {
        super(errorCode, throwable, serviceClass, operation, args);
    }
}
