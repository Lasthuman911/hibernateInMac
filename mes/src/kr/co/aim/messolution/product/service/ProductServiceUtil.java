/**
 * Histories:
 *

 */


package kr.co.aim.messolution.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import kr.co.aim.nanoframe.nanoFrameServiceProxy;
import kr.co.aim.nanoframe.orm.ObjectAttributeDef;
import kr.co.aim.nanoframe.util.bundle.BundleUtil;
import kr.co.aim.messolution.generic.GenericServiceProxy;
import kr.co.aim.messolution.generic.errorHandler.CustomException;
import kr.co.aim.nanotrack.durable.management.data.Durable;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;
import kr.co.aim.nanotrack.generic.util.StringUtil;
import kr.co.aim.nanotrack.lot.management.data.Lot;
import kr.co.aim.nanotrack.name.NameServiceProxy;
import kr.co.aim.nanotrack.product.ProductServiceProxy;
import kr.co.aim.nanotrack.product.management.data.Product;
import kr.co.aim.nanotrack.product.management.data.ProductKey;
import kr.co.aim.nanotrack.product.management.data.ProductSpec;
import kr.co.aim.nanotrack.product.management.data.ProductSpecKey;
import kr.co.aim.nanotrack.product.management.info.ext.ProductU;

import org.jdom.Element;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author sjlee
 *
 */
public class ProductServiceUtil implements ApplicationContextAware
{
    private static Log log = LogFactory.getLog(ProductServiceUtil.class);

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */

    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        // TODO Auto-generated method stub

    }

    /*
    * Name : getProductListByLotName
    * Desc : This function is getProductListByLotName
    * Author : AIM Systems, Inc
    * Date : 2011.01.13
    */
    public List<Product> getProductListByLotName(String lotName) throws FrameworkErrorSignal, NotFoundSignal, CustomException
    {
        if(log.isInfoEnabled()){
            log.info("lotName = " + lotName);
        }

        List<Product> productList = ProductServiceProxy.getProductService().allUnScrappedProductsByLot(lotName);

        return productList;
    }


    /**
     * appending error handler
     * @author swcho
     * @since 2014.08.27
     * @param productName
     * @return
     * @throws CustomException
     */
    public  Product getProductData(String productName) throws CustomException
    {
        try
        {
            ProductKey productKey = new ProductKey();
            productKey.setProductName(productName);

            Product productData = ProductServiceProxy.getProductService().selectByKey(productKey);

            return productData;
        }
        catch (NotFoundSignal e)
        {
            throw new CustomException("PRODUCT-9001", productName);
        }
        catch (FrameworkErrorSignal fe)
        {
            throw new CustomException("PRODUCT-9999", fe.getMessage());
        }
    }

    /*
    * Name : getProductByProductName
    * Desc : This function is getProductByProductName
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public Product getProductByProductName(String productName) throws CustomException
    {
        ProductKey productKey = new ProductKey();
        productKey.setProductName(productName);

        Product productData = null;
        productData = ProductServiceProxy.getProductService().selectByKey(productKey);

        return productData;
    }

    /*
    * Name : getProductSpecByProductName
    * Desc : This function is getProductSpecByProductName
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public ProductSpec getProductSpecByProductName ( Product productData ) throws CustomException {

        ProductSpecKey productSpecKey = new ProductSpecKey();
        productSpecKey.setFactoryName(productData.getFactoryName());
        productSpecKey.setProductSpecName(productData.getProductSpecName());
        productSpecKey.setProductSpecVersion(productData.getProductSpecVersion());

        ProductSpec productSpecData = null;
        productSpecData = ProductServiceProxy.getProductSpecService().selectByKey(productSpecKey);

        return productSpecData;
    }

    /*
    * Name : checkProductState_InProduction
    * Desc : This function is checkProductState_InProduction
    * Author : AIM Systems, Inc
    * Date : 2011.05.27
    */
    public void checkProductState_InProduction(Product productData) throws CustomException
    {
        String productName  = productData.getKey().getProductName();
        String productState = productData.getProductState();

        if(!StringUtils.equals(productState, GenericServiceProxy.getConstantMap().Prod_InProduction))
        {
            throw new CustomException("PRODUCT-9006", productName, productState);
        }
    }

    public List<ProductU> setProductUSequence(String productName)
            throws FrameworkErrorSignal, NotFoundSignal
    {
        ProductU productU = new ProductU();
        List<ProductU> productUList = new ArrayList<ProductU>();
        productU.setProductName(productName);
        productUList.add(productU);

        return productUList;
    }

    /*
    * Name : setProductUSequence
    * Desc : This function is setProductUSequence
    * Author : JHYEOM
    * Date : 2014.05.07
    */
    public List<ProductU> setProductUSequence(org.jdom.Document doc)
            throws FrameworkErrorSignal, NotFoundSignal {
        if (doc == null) {
            log.error("xml is null");
        }

        List<ProductU> productUList = new ArrayList<ProductU>();
        ProductServiceUtil productServiceUtil = (ProductServiceUtil) BundleUtil
                .getBundleServiceClass(ProductServiceUtil.class);

        Element root = doc.getDocument().getRootElement();

        List<Product> productDatas = new ArrayList<Product>();

        String lotName = root.getChild("Body").getChildText("LOTNAME");
        if (lotName == null || lotName == "") {
            String carrierName = root.getChild("Body").getChildText(
                    "CARRIERNAME");
            if (carrierName != null && carrierName != "") {

                String condition = "WHERE carrierName = ?"
                        + "AND productState != ?" + "AND productState != ?"
                        + "ORDER BY position ";

                Object[] bindSet = new Object[] { carrierName,
                        GenericServiceProxy.getConstantMap().Prod_Scrapped,
                        GenericServiceProxy.getConstantMap().Prod_Consumed };
                try {
                    productDatas = ProductServiceProxy.getProductService()
                            .select(condition, bindSet);
                } catch (Exception e) {

                }
            }
        } else {
            try {
                productDatas = ProductServiceProxy.getProductService()
                        .allUnScrappedProductsByLot(lotName);
            } catch (Exception e) {
                log.error(e);
                productDatas = ProductServiceProxy.getProductService()
                        .allProductsByLot(lotName);
            }

        }

        Element element = root.getChild("Body").getChild("PRODUCTLIST");

        if (element != null) {
            for (Iterator iterator = element.getChildren().iterator(); iterator
                    .hasNext();) {
                Element productE = (Element) iterator.next();
                String productName = productE.getChild("PRODUCTNAME").getText();

                ProductU productU = new ProductU();

                productU.setProductName(productName);
                productU.setUdfs(productServiceUtil.setNamedValueSequence(
                        productName, productE));

                productUList.add(productU);
            }
        } else {

            for (Iterator<Product> iteratorProduct = productDatas.iterator(); iteratorProduct
                    .hasNext();) {
                Product product = iteratorProduct.next();

                ProductU productU = new ProductU();
                productU.setProductName(product.getKey().getProductName());
                productU.setUdfs(product.getUdfs());

                productUList.add(productU);
            }
        }

        return productUList;
    }

    /*
    * Name : setNamedValueSequence
    * Desc : This function is setNamedValueSequence
    * Author : JHYEOM
    * Date : 2014.05.07
    */
    public  Map<String, String> setNamedValueSequence(String productName, Element element) throws FrameworkErrorSignal, NotFoundSignal
    {
        if(log.isInfoEnabled()){
            log.info("productName = " + productName);
        }

        Map<String, String> namedValueMap = new HashMap<String, String>();
        ProductKey productKey = new ProductKey();
        productKey.setProductName(productName);

        try{
            Product product = ProductServiceProxy.getProductService().selectByKey(productKey);
            namedValueMap = product.getUdfs();
        }catch(NotFoundSignal ne){}

        List<ObjectAttributeDef> objectAttributeDefs = nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames("Product", "ExtendedC");

        if ( objectAttributeDefs != null )
        {
            for ( int i = 0; i < objectAttributeDefs.size(); i++ )
            {
                String name = "";
                String value = "";

                if ( element != null )
                {
                    for ( int j = 0; j < element.getContentSize(); j++ )
                    {
                        if ( element.getChildText(objectAttributeDefs.get(i).getAttributeName()) != null )
                        {
                            name  = objectAttributeDefs.get(i).getAttributeName();
                            value = element.getChildText(objectAttributeDefs.get(i).getAttributeName());

                            if (StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(value))
                                namedValueMap.put(name, value);

                            break;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }
        }

        return namedValueMap;
    }

    /*
    * Name : generatePanelName
    * Desc : This function is generatePanelName
    * Author : jhyeom
    * Date : 2015.02.05
    */
    public List<String> generatePanelName(String productName, int x, int y)
    {
        int valueX = x/2;
        int valueY = y/2;

        List<String> panelNames = new ArrayList<String>(valueX*valueY);

        char[] cs = "A".toCharArray();

        for (int i = 0; i < valueY; i++)
        {
            char[] cs1 = "A".toCharArray();
            StringBuilder panelName = new StringBuilder();

            if(cs[0] == 'I' || cs[0] == 'O')
            {
                cs[0] += 1;
            }

            for (int j = 0; j < valueX; j++)
            {

                panelName.setLength(0);

                panelName.append(productName);


                panelName.append(cs);

                if(cs1[0] == 'I' || cs1[0] == 'O')
                {
                    cs1[0] += 1;
                }

                panelName.append(cs1);

                panelNames.add(panelName.toString());
                cs1[0] += 1;
            }

            cs[0] += 1;
        }

        return panelNames;
    }

    /*
    * Name : generateGlassName
    * Desc : This function is generateGlassName
    * Author : jhyeom
    * Date : 2014.04.29
    */
    public List<String> generateGlassName(String productName, int subProductUnitQty)
    {
        List<String> panelNames = new ArrayList<String>(subProductUnitQty);

        //char[] cs = "A".toCharArray();

        for (int i = 0; i < subProductUnitQty; i++)
        {
			/*char[] cs1 = "A".toCharArray();
			StringBuilder panelName = new StringBuilder();

			panelName.setLength(0);

			panelName.append(productName);
			panelName.append(cs);
			if(i < 9)
			{
				panelName.append(i+1);
			}
			else
			{
				panelName.append(cs1);
				cs1[0] += 1;
				if(cs1[0] == 'I' || cs1[0] == 'O')
				cs1[0] += 1;
			}

			panelNames.add(panelName.toString());

			cs[0] += 1;*/

            panelNames.add(new StringBuilder(productName).append(String.valueOf(i+1)).toString());
        }

        return panelNames;
    }

    /*
    * Name : generateProductName
    * Desc : This function is generateProductName
    * Author : AIM Systems, Inc
    * Date : 2014.10.16
    */
    public List<String> generateProductName( String ruleName, String prefix , double quantity ) throws CustomException
    {
        List<String> argSeq = new ArrayList<String>();
        argSeq.add(prefix);

        List<String> names = new ArrayList<String>();
        names = NameServiceProxy.getNameGeneratorRuleDefService().generateName(ruleName, argSeq, (long) quantity);

        return names;

    }

    /**
     * logical slot map in carrier
     * 160426 by swcho : only by CST
     * @since 2015.08.27
     * @author swcho
     * @param durableData
     * @param lotData
     * @return
     * @throws CustomException
     */
    public String getSlotMapInfo(Durable durableData)
            throws CustomException
    {
        StringBuffer normalSlotInfoBuffer = new StringBuffer();

        // Get Durable's Capacity
        long iCapacity = durableData.getCapacity();

        // Get Product's Slot , These are not Scrapped Product.
        List<Product> productList = new ArrayList<Product>();

        try
        {
            productList = ProductServiceProxy.getProductService().select("carrierName = ? AND productState = ?",
                    new Object[] {durableData.getKey().getDurableName(), GenericServiceProxy.getConstantMap().Prod_InProduction});
        }
        catch (Exception ex)
        {
            throw new CustomException("SYS-9999", "Product", "No avaliable Product");
        }

        // Make Durable Normal SlotMapInfo
        for(int i = 0; i < iCapacity; i++)
        {
            normalSlotInfoBuffer.append(GenericServiceProxy.getConstantMap().PRODUCT_NOT_IN_SLOT);
        }

        log.debug("Default Slot Map : " + normalSlotInfoBuffer);

        for(int i = 0; i < productList.size(); i++)
        {
            try
            {
                int index = (int)productList.get(i).getPosition() - 1;

                normalSlotInfoBuffer.replace(index, index+1, GenericServiceProxy.getConstantMap().PRODUCT_IN_SLOT);
            }
            catch (Exception ex)
            {
                log.error("Position conversion failed");
                normalSlotInfoBuffer.replace(i, i+1, GenericServiceProxy.getConstantMap().PRODUCT_NOT_IN_SLOT);
            }
        }

        log.info("Current Slot Map : " + normalSlotInfoBuffer);

        return normalSlotInfoBuffer.toString();
    }

    /**
     * logical slot map in carrier
     * 160426 by swcho : only by CST
     * @since 2015.08.27
     * @author swcho
     * @param durableData
     * @param lotData
     * @return
     * @throws CustomException
     */
    public String getSlotMap(Durable durableData)
            throws CustomException
    {
        StringBuffer normalSlotInfoBuffer = new StringBuffer();

        // Get Durable's Capacity
        long iCapacity = durableData.getCapacity();

        // Get Product's Slot , These are not Scrapped Product.
        List<Product> productList = new ArrayList<Product>();

        try
        {
            productList = ProductServiceProxy.getProductService().select("carrierName = ? AND productState = ?",
                    new Object[] {durableData.getKey().getDurableName(), GenericServiceProxy.getConstantMap().Prod_InProduction});
        }
        catch (Exception ex)
        {
            log.debug("CST Product List is Empty : " + durableData.getKey().getDurableName());
        }

        // Make Durable Normal SlotMapInfo
        for(int i = 0; i < iCapacity; i++)
        {
            normalSlotInfoBuffer.append(GenericServiceProxy.getConstantMap().PRODUCT_NOT_IN_SLOT);
        }

        log.debug("Default Slot Map : " + normalSlotInfoBuffer);

        for(int i = 0; i < productList.size(); i++)
        {
            try
            {
                int index = (int)productList.get(i).getPosition() - 1;

                normalSlotInfoBuffer.replace(index, index+1, GenericServiceProxy.getConstantMap().PRODUCT_IN_SLOT);
            }
            catch (Exception ex)
            {
                log.error("Position conversion failed");
                normalSlotInfoBuffer.replace(i, i+1, GenericServiceProxy.getConstantMap().PRODUCT_NOT_IN_SLOT);
            }
        }

        log.info("Current Slot Map : " + normalSlotInfoBuffer);

        return normalSlotInfoBuffer.toString();
    }
}

