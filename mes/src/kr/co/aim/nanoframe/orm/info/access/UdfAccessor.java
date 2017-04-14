package kr.co.aim.nanoframe.orm.info.access;

import java.util.HashMap;
import java.util.Map;

import kr.co.aim.nanoframe.util.object.ObjectUtil;

public class UdfAccessor extends FieldAccessor
{
    protected Map<String, String>	udfs;

    public UdfAccessor()
    {
        udfs = new HashMap<String, String>();
    }

    public Map<String, String> getUdfs()
    {
        return this.udfs;
    }

    public void setUdfs(UdfAccessor udfAccessor)
    {
        udfs = udfAccessor.getUdfs();
    }

    public void setUdfs(Map<String, String> udfs)
    {
        ObjectUtil.setUdfs(this.udfs, udfs);
    }
}