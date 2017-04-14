package kr.co.aim.nanotrack.generic.info;

import java.util.Map;

import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;

public interface CommonAttributeService<KEY extends KeyInfo, DATA extends DataInfo> extends CommonService<KEY, DATA>
{
    public String getAttribute(KEY keyInfo, String attributeName) throws NotFoundSignal, FrameworkErrorSignal;

    public Map<String, String> getAllAttributes(KEY keyInfo) throws NotFoundSignal, FrameworkErrorSignal;

    public int addAttribute(KEY keyInfo, String attributeName, String attributeValue)
            throws DuplicateNameSignal, FrameworkErrorSignal;

    public int setAttribute(KEY keyInfo, String attributeName, String attributeValue)
            throws FrameworkErrorSignal, NotFoundSignal;

    public int removeAttribute(KEY keyInfo, String attributeName) throws FrameworkErrorSignal, NotFoundSignal;

    public int removeAllAttributes(KEY keyInfo) throws FrameworkErrorSignal, NotFoundSignal;
}
