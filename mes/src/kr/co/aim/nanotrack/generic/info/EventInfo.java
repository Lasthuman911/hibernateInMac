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

package kr.co.aim.nanotrack.generic.info;

import java.sql.Timestamp;

/*
 ****************************************************************************
 *  PACKAGE : global
 *  NAME    : EventInfo.java
 *  TYPE    : JAVA
 *  DESCRIPTION :
 *
 ****************************************************************************
 */

public class EventInfo
{
    private String		eventName;
    private String		eventTimeKey;
    private Timestamp	eventTime;
    private String		eventUser;
    private String		eventComment;
    private String		lastEventTimekey;
    private String		reasonCodeType;
    private String		reasonCode;
    private String		behaviorName;
    private boolean		checkTimekeyValidation	= true;

    public EventInfo()
    {
        this.eventName = "";
        this.eventTimeKey = "";
        this.eventTime = null;
        this.eventUser = "";
        this.eventComment = "";
        this.lastEventTimekey = "";
        this.reasonCodeType = "";
        this.reasonCode = "";
        this.behaviorName = "";
        this.checkTimekeyValidation = true;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getEventTimeKey()
    {
        return eventTimeKey;
    }

    public void setEventTimeKey(String eventTimeKey)
    {
        this.eventTimeKey = eventTimeKey;
    }

    public Timestamp getEventTime()
    {
        return eventTime;
    }

    public void setEventTime(Timestamp eventTime)
    {
        this.eventTime = eventTime;
    }

    public String getEventUser()
    {
        return eventUser;
    }

    public void setEventUser(String eventUser)
    {
        this.eventUser = eventUser;
    }

    public String getEventComment()
    {
        return eventComment;
    }

    public void setEventComment(String eventComment)
    {
        this.eventComment = eventComment;
    }

    public String getLastEventTimekey()
    {
        return lastEventTimekey;
    }

    public void setLastEventTimekey(String lastEventTimekey)
    {
        this.lastEventTimekey = lastEventTimekey;
    }

    public String getReasonCodeType()
    {
        return reasonCodeType;
    }

    public void setReasonCodeType(String reasonCodeType)
    {
        this.reasonCodeType = reasonCodeType;
    }

    public String getReasonCode()
    {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode)
    {
        this.reasonCode = reasonCode;
    }

    public String getBehaviorName()
    {
        return behaviorName;
    }

    public void setBehaviorName(String behaviorName)
    {
        this.behaviorName = behaviorName;
    }

    public boolean isCheckTimekeyValidation()
    {
        return checkTimekeyValidation;
    }

    public void setCheckTimekeyValidation(boolean checkTimekeyValidation)
    {
        this.checkTimekeyValidation = checkTimekeyValidation;
    }

}
