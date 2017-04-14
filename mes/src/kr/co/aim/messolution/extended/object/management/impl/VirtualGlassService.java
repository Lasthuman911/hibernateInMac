package kr.co.aim.messolution.extended.object.management.impl;

import java.util.ArrayList;
import java.util.List;

import kr.co.aim.messolution.extended.object.ExtendedObjectProxy;
import kr.co.aim.messolution.extended.object.management.CTORMService;
import kr.co.aim.messolution.extended.object.management.CTORMUtil;
import kr.co.aim.messolution.extended.object.management.data.VirtualGlass;
import kr.co.aim.messolution.generic.errorHandler.CustomException;
import kr.co.aim.nanotrack.generic.info.EventInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VirtualGlassService extends CTORMService<VirtualGlass> {

    public static Log logger = LogFactory.getLog(VirtualGlassService.class);

    private final String historyEntity = "VirtualGlassHistory";

    public List<VirtualGlass> select(String condition, Object[] bindSet)
            throws CustomException
    {
        List<VirtualGlass> result = super.select(condition, bindSet, VirtualGlass.class);

        return result;
    }

    public VirtualGlass selectByKey(boolean isLock, Object[] keySet)
            throws CustomException
    {
        return super.selectByKey(VirtualGlass.class, isLock, keySet);
    }

    public VirtualGlass create(EventInfo eventInfo, VirtualGlass dataInfo)
            throws CustomException
    {
        super.insert(dataInfo);

        super.addHistory(eventInfo, this.historyEntity, dataInfo, logger);

        return selectByKey(false, CTORMUtil.makeKeyParam(dataInfo).toArray());
    }

    public void remove(EventInfo eventInfo, VirtualGlass dataInfo)
            throws CustomException
    {
        super.addHistory(eventInfo, this.historyEntity, dataInfo, logger);

        super.delete(dataInfo);
    }

    public VirtualGlass modify(EventInfo eventInfo, VirtualGlass dataInfo)
            throws CustomException
    {
        super.update(dataInfo);

        super.addHistory(eventInfo, this.historyEntity, dataInfo, logger);

        return selectByKey(false, CTORMUtil.makeKeyParam(dataInfo).toArray());
    }

    public List<VirtualGlass> getProductByCarrier(String carrierName) throws CustomException
    {
        List<VirtualGlass> result;

        try
        {
            result = ExtendedObjectProxy.getVirtualGlassService().select("carrier = ? order by position ", new Object[] {carrierName});
        }
        catch (Exception ex)
        {
            logger.warn("Nothing in carrier");
            result = new ArrayList<VirtualGlass>();
        }

        return result;
    }

}
