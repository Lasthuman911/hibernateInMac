package kr.co.aim.nanoframe.orm;

import java.io.Serializable;

public class ObjectAttributeDef implements Serializable
{
    private String	typeName;
    private String	attributeName;
    private int		position;
    private String	primaryKeyFlag;
    private String	attributeType;
    private String	dataType;
    private String	defaultValue;

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getAttributeName()
    {
        return attributeName;
    }

    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public String getPrimaryKeyFlag()
    {
        return primaryKeyFlag;
    }

    public void setPrimaryKeyFlag(String primaryKeyFlag)
    {
        this.primaryKeyFlag = primaryKeyFlag;
    }

    public String getAttributeType()
    {
        return attributeType;
    }

    public void setAttributeType(String attributeType)
    {
        this.attributeType = attributeType;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
}
