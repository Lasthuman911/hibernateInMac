package kr.co.aim.nanotrack.generic.common;

import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;
import kr.co.aim.nanotrack.generic.info.CommonMaterialService;
import kr.co.aim.nanotrack.generic.info.EventInfo;
import kr.co.aim.nanotrack.generic.info.SetMaterialLocationInfo;
import kr.co.aim.nanotrack.generic.orm.OrmMesEngine;

public abstract class CommonMaterialServiceDAO<KEY extends KeyInfo, DATA extends DataInfo>
        extends CommonServiceDAO<KEY, DATA> implements CommonMaterialService<KEY, DATA>
{

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @param eventInfo
     *            extends kr.co.aim.nanoframe.orm.info.DataInfo
     * @param setMaterialLocationInfo
     *            kr.co.aim.nanotrack.generic.info.SetMaterialLocationInfo
     * @return DATA extends kr.co.aim.nanoframe.orm.info.DataInfo
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    public abstract DATA setMaterialLocation(KEY keyInfo, EventInfo eventInfo,
                                             SetMaterialLocationInfo setMaterialLocationInfo)
            throws NotFoundSignal, DuplicateNameSignal, FrameworkErrorSignal;

}
