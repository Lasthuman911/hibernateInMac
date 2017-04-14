package kr.co.aim.nanotrack.generic.common;

import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanotrack.generic.info.CommonService;
import kr.co.aim.nanotrack.generic.orm.OrmMesEngine;

public class CommonServiceDAO<KEY extends KeyInfo, DATA extends DataInfo> extends OrmMesEngine<KEY, DATA>
        implements CommonService<KEY, DATA>
{

    public CommonServiceDAO()
    {

    }

}
