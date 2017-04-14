package kr.co.aim.nanoframe.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class nanoFrameErrorSignal extends RuntimeException
{
    static final long	serialVersionUID	= 9023275698421312312L;
    protected Log		log					= LogFactory.getLog(this.getClass());

    private String		errorCode;

    public nanoFrameErrorSignal(String errorCode, Throwable throwable)
    {
        super(makeMessage(errorCode, throwable.getMessage()));
        this.errorCode = errorCode;
        log.info(super.getMessage());
    }

    public nanoFrameErrorSignal(String errorCode, String msg)
    {
        super(makeMessage(errorCode, msg));
        this.errorCode = errorCode;
        log.info(super.getMessage());
    }

    public nanoFrameErrorSignal(String errorCode, String msg, Throwable throwable)
    {
        super(makeMessage(errorCode, msg), throwable);
        this.errorCode = errorCode;
        log.info(super.getMessage());
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public static String makeMessage(String errorCode, String msg)
    {
        return String.format("%s: %s", errorCode, msg);
    }
}
