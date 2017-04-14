package kr.co.aim.nanoframe.orm.info;

public interface DataInfo<KEY extends KeyInfo>
{
    public KEY getKey();

    public void setKey(KEY keyInfo);
}
