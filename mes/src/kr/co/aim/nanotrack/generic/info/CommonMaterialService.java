package kr.co.aim.nanotrack.generic.info;

import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;

public interface CommonMaterialService<KEY extends KeyInfo, DATA extends DataInfo> extends CommonService<KEY, DATA>
{

    public DATA setMaterialLocation(KEY keyInfo, EventInfo eventInfo, SetMaterialLocationInfo setMaterialLocationInfo)
            throws NotFoundSignal, DuplicateNameSignal, FrameworkErrorSignal;

}
