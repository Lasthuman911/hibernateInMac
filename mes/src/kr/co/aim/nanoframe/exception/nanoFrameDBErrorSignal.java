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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 ****************************************************************************
 *  PACKAGE : kr.co.aim.nanoframe.exception
 *  NAME    : nanoFrameErrorSignal.java
 *  TYPE    : JAVA
 *  DESCRIPTION :
 *
 ****************************************************************************
 */

public class nanoFrameDBErrorSignal extends nanoFrameErrorSignal //RuntimeException
{
    static final long	serialVersionUID	= 9023275698421312312L;

    private String		sql					= "";
    // 老馆 SQL狼 版快 BindSet狼 String, ORM狼 版快 DataKey狼 String
    private String		dataKey				= "";

    public nanoFrameDBErrorSignal(String errorCode, String msg)
    {
        super(errorCode, msg);
    }

    public nanoFrameDBErrorSignal(String errorCode, Throwable throwable)
    {
        super(errorCode, throwable.getMessage(), throwable);
    }

    public nanoFrameDBErrorSignal(String errorCode, String sql, Throwable throwable)
    {
        super(errorCode, String.format("%s [Sql=%s]", throwable.getMessage(), sql), throwable);
        this.sql = sql;
    }

    public nanoFrameDBErrorSignal(String errorCode, String dataKey, String sql)
    {
        super(errorCode, String.format("[DataKey=%s] [Sql=%s]", dataKey, sql));
        this.dataKey = dataKey;
        this.sql = sql;
    }

    public nanoFrameDBErrorSignal(String errorCode, String dataKey, String sql, Throwable throwable)
    {

        super(errorCode, String.format("%s [DataKey=%s] [Sql=%s]", throwable.getMessage(), dataKey, sql), throwable);
        this.dataKey = dataKey;
        this.sql = sql;
    }

    public nanoFrameDBErrorSignal(String errorCode, String dataKey, String sql, String msg)
    {
        super(errorCode, String.format("%s [DataKey=%s] [Sql=%s]", msg, dataKey, sql));
        this.dataKey = dataKey;
        this.sql = sql;
    }

    public nanoFrameDBErrorSignal(String errorCode, String dataKey, String sql, String msg, Throwable throwable)
    {
        super(errorCode, String.format("%s [DataKey=%s] [Sql=%s]", msg, dataKey, sql), throwable);
        this.dataKey = dataKey;
        this.sql = sql;
    }

    public String getSql()
    {
        return sql;
    }

    public String getDataKey()
    {
        return dataKey;
    }

}
