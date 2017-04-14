package kr.co.aim.nanotrack.generic.info;

import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;

public interface CommonMaterialAttributeService<KEY extends KeyInfo, DATA extends DataInfo>
        extends CommonMaterialService<KEY, DATA>, CommonAttributeService<KEY, DATA>
{
}