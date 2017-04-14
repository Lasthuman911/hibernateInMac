package kr.co.aim.nanotrack.product.management.data;

import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanoframe.orm.info.access.FieldAccessor;

public class ProductKey extends FieldAccessor implements KeyInfo
{
    private String	productName;

    public ProductKey()
    {
    }

    public ProductKey(String productName)
    {
        this.productName = productName;
    }

    public String getProductName()
    {
        return this.productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }
}