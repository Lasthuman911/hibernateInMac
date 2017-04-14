package kr.co.aim.nanotrack.generic.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.aim.nanoframe.util.time.TimeStampUtil;
import kr.co.aim.nanotrack.generic.GenericServiceProxy;
import kr.co.aim.nanotrack.generic.info.EventInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class TimeUtils extends TimeStampUtil implements InitializingBean
{
    public static final String	FORMAT_SEQTIMEKEY	= "yyyyMMddHHmmssSSS";

    private static Log			log					= LogFactory.getLog(TimeUtils.class);
    private static boolean		useSequence4TimeKey	= true;

    public static boolean isUseSequence4TimeKey()
    {
        return useSequence4TimeKey;
    }

    public static void setUseSequence4TimeKey(boolean useSequence4TimeKey)
    {
        TimeUtils.useSequence4TimeKey = useSequence4TimeKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        try
        {
            String useSequenceTimekey = System.getProperty("nanotrack.sequencetimekey", "true");
            useSequence4TimeKey = Boolean.parseBoolean(useSequenceTimekey);
        } catch (Exception ex)
        {
            log.warn(ex);
        }
    }

    public static String getCurrentEventTimeKey()
    {
        String currentTimeKey;
        if (useSequence4TimeKey)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_SEQTIMEKEY);
            currentTimeKey = sdf.format(System.currentTimeMillis());

            int nextVal = getNextTimeSequence();
            currentTimeKey = currentTimeKey + String.format("%03d", nextVal);
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIMEKEY);
            currentTimeKey = sdf.format(System.currentTimeMillis());
        }

        return currentTimeKey;

        //		String sql;
        //		if (useSequence4TimeKey)
        //		{
        //			sql = "select concat(to_char(systimestamp, 'YYYYMMDDHH24MISSFF3'), lpad(TIMEKEYID.nextval, 3, '0') ) as timekey from dual";
        //		}
        //		else
        //		{
        //			sql = "select to_char(systimestamp, 'YYYYMMDDHH24MISSFF6') as timekey from dual";
        //		}
        //
        //		List<Map<String, Object>> result = GenericServiceProxy.getSqlMesTemplate().queryForList(sql);
        //		String currentTimeKey = (String)result.get(0).get("TIMEKEY");
        //
        //		return currentTimeKey;
    }

    public static String getEventTimeKeyFromTimestamp(Timestamp timestamp)
    {
        //		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIMEKEY);
        //		return sdf.format(timestamp);
        String currentTimeKey;
        if (useSequence4TimeKey)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_SEQTIMEKEY);
            currentTimeKey = sdf.format(timestamp);

            int nextVal = getNextTimeSequence();
            currentTimeKey = currentTimeKey + String.format("%03d", nextVal);
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIMEKEY);
            currentTimeKey = sdf.format(timestamp);
        }

        return currentTimeKey;
    }

    public static int getNextTimeSequence()
    {
        String sql = "select TIMEKEYID.nextval from dual";
        int nextVal = GenericServiceProxy.getSqlMesTemplate().queryForInt(sql);
        return nextVal;
    }

    public static Timestamp getTimestampByTimeKey(String timeKey)
    {
        try
        {
            if (useSequence4TimeKey)
            {
                String realTime = timeKey.substring(0, 17);
                SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_SEQTIMEKEY);
                Date date = sdf.parse(realTime);
                return new Timestamp(date.getTime());
            }
            else
            {
                SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIMEKEY);
                Date date = sdf.parse(timeKey);
                return new Timestamp(date.getTime());
            }
        } catch (Throwable ex)
        {
            return null;
        }
    }

    /**
     *
     * @param lastEventtimeKey
     *            : DataInfo 按眉俊 乐绰 lastEventtimeKey 蔼
     * @param eventInfo
     *
     *            EventInfo俊 eventTimeKey, eventTime 蔼捞 绝绰 版快, 秦寸 蔼阑 汲沥秦 淋.
     */
    public static void setTime4EventInfo(String lastEventtimeKey, EventInfo eventInfo)
    {
        Timestamp currentTimeStamp = TimeUtils.getCurrentTimestamp();
        if (StringUtils.isEmpty(eventInfo.getEventTimeKey()))
        {
            String eventTimeKey = TimeUtils.getEventTimeKeyFromTimestamp(currentTimeStamp);
			/*
			 * sequence甫 cycle捞 登档废 积己窃. 鞍篮 Time俊 捞亥飘啊 滴锅 捞惑 惯积茄 版快, Sequence啊 999俊辑 0栏肺 函版登绰 鉴埃阑 困秦
			 * 眉农秦辑, 促矫 timekey甫 积己窍绰 肺流烙..
			 */
            try
            {
                if (TimeUtils.isUseSequence4TimeKey()
                        && StringUtils.isNotEmpty(lastEventtimeKey)
                        && lastEventtimeKey.compareTo(eventTimeKey) >= 0)
                {
                    String lastTime;
                    String lastSeq;
                    if (lastEventtimeKey.length() >= 17)
                    {
                        lastTime = lastEventtimeKey.substring(0, 17);
                        lastSeq = lastEventtimeKey.substring(17);
                    }
                    else
                    {
                        lastTime = lastEventtimeKey;
                        lastSeq = "000";
                    }
                    lastSeq = String.format("%03d", new Integer(lastSeq));

                    String eventTime = eventTimeKey.substring(0, 17);
                    String eventSeq = eventTimeKey.substring(17);
                    if (eventTime.equals(lastTime) && lastSeq.compareTo(eventSeq) >= 0)
                    {
                        // 矫埃 Gab捞 腹篮 版快, 公茄风橇贸烦 焊老荐档 乐澜. 捞俊 茄锅父 Retry 窍绰 巴栏肺 窃.
                        currentTimeStamp = TimeUtils.getCurrentTimestamp();
                        eventTimeKey = TimeUtils.getEventTimeKeyFromTimestamp(currentTimeStamp);
                        //						setTime4EventInfo(lastEventtimeKey, eventInfo);
                        //						return;
                    }
                }
            } catch (Throwable e)
            {
                if (log.isDebugEnabled())
                    log.debug(e);
            }

            eventInfo.setEventTimeKey(eventTimeKey);
        }

        if (eventInfo.getEventTime() == null)
        {
            eventInfo.setEventTime(currentTimeStamp);
        }
    }

    public static Timestamp getTimestamp(String time)
    {
        try
        {
            SimpleDateFormat sdf = null;

            if (time.length() > 20)
                sdf = new SimpleDateFormat(FORMAT_DETAIL); // yyyy-MM-dd HH:mm:ss.SSS
            else if (time.length() == 20)// yyyyMMddHHmmssSSSSSS
            {
                return getTimestampByTimeKey(time);
            }
            else if (time.length() == 19)
                sdf = new SimpleDateFormat(FORMAT_DEFAULT); // yyyy-MM-dd HH:mm:ss
            else if (time.length() == 17)
                sdf = new SimpleDateFormat(FORMAT_SIMPLE_DETAIL); // yyyyMMddHHmmssSSS
            else if (time.length() == 14)
                sdf = new SimpleDateFormat(FORMAT_SIMPLE_DEFAULT); // yyyyMMddHHmmss
            else if (time.length() == 10)
                sdf = new SimpleDateFormat(FORMAT_DAY); // yyyy-MM-dd
            else if (time.length() == 8)
                sdf = new SimpleDateFormat(FORMAT_SIMPLE_DAY); // yyyyMMdd
            else
            {
                log.warn("could not create Timestamp : " + time);
                return null;
            }
            Date date = sdf.parse(time);
            return new Timestamp(date.getTime());
        } catch (Exception e)
        {
            return null;
        }
    }

}
