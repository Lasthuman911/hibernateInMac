package kr.co.aim.nanotrack.generic.info;

import java.util.List;

import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;

public interface CommonService<KEY extends KeyInfo, DATA extends DataInfo>
{
    public List<DATA> transform(List resultList);

    public List<DATA> select(String condition, Object[] bindSet) throws NotFoundSignal, FrameworkErrorSignal;

    public DATA selectByKey(KEY keyInfo) throws NotFoundSignal, FrameworkErrorSignal;

    public DATA selectByKeyForUpdate(KEY keyInfo) throws NotFoundSignal, FrameworkErrorSignal;

    public int update(DATA dataInfo) throws FrameworkErrorSignal, NotFoundSignal;

    public int update(DATA dataInfo, String condition, Object[] bindSet) throws FrameworkErrorSignal, NotFoundSignal;

//	public int[] updateAll(List<DATA> dataInfo) throws FrameworkErrorSignal, NotFoundSignal;

    public int insert(DATA dataInfo) throws DuplicateNameSignal, FrameworkErrorSignal;

//	public int[] insertAll(List<DATA> dataInfo) throws FrameworkErrorSignal, NotFoundSignal;

    public int delete(KEY keyInfo) throws FrameworkErrorSignal, NotFoundSignal;

    public int delete(String condition, Object[] bindSet) throws FrameworkErrorSignal, NotFoundSignal;

//	public int[] deleteAll(List<KEY> dataInfo) throws FrameworkErrorSignal, NotFoundSignal;
}
