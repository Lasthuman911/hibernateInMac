
package kr.co.aim.messolution.generic.util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.co.aim.messolution.generic.GenericServiceProxy;
import kr.co.aim.messolution.generic.errorHandler.CustomException;
import kr.co.aim.messolution.generic.esb.ESBService;
import kr.co.aim.nanoframe.nanoFrameServiceProxy;
import kr.co.aim.nanoframe.exception.nanoFrameDBErrorSignal;
import kr.co.aim.nanoframe.orm.ObjectAttributeDef;
import kr.co.aim.nanoframe.util.object.ObjectUtil;
import kr.co.aim.nanotrack.machine.MachineServiceProxy;
import kr.co.aim.nanotrack.machine.management.data.Machine;
import kr.co.aim.nanotrack.machine.management.data.MachineKey;
import kr.co.aim.nanotrack.machine.management.data.MachineSpec;
import kr.co.aim.nanotrack.machine.management.data.MachineSpecKey;
import kr.co.aim.nanotrack.consumable.ConsumableServiceProxy;
import kr.co.aim.nanotrack.consumable.management.data.Consumable;
import kr.co.aim.nanotrack.consumable.management.data.ConsumableKey;
import kr.co.aim.nanotrack.consumable.management.data.ConsumableSpec;
import kr.co.aim.nanotrack.consumable.management.data.ConsumableSpecKey;
import kr.co.aim.nanotrack.durable.DurableServiceProxy;
import kr.co.aim.nanotrack.durable.management.data.Durable;
import kr.co.aim.nanotrack.durable.management.data.DurableKey;
import kr.co.aim.nanotrack.durable.management.data.DurableSpec;
import kr.co.aim.nanotrack.durable.management.data.DurableSpecKey;
import kr.co.aim.nanotrack.port.PortServiceProxy;
import kr.co.aim.nanotrack.port.management.data.Port;
import kr.co.aim.nanotrack.port.management.data.PortKey;
import kr.co.aim.nanotrack.port.management.data.PortSpec;
import kr.co.aim.nanotrack.port.management.data.PortSpecKey;
import kr.co.aim.nanotrack.product.ProductServiceProxy;
import kr.co.aim.nanotrack.product.management.ProductService;
import kr.co.aim.nanotrack.product.management.data.Product;
import kr.co.aim.nanotrack.product.management.data.ProductKey;
import kr.co.aim.nanotrack.product.management.data.ProductSpec;
import kr.co.aim.nanotrack.product.management.data.ProductSpecKey;
import kr.co.aim.nanotrack.product.management.info.ext.ProductPGSRC;
import kr.co.aim.nanotrack.productrequest.ProductRequestServiceProxy;
import kr.co.aim.nanotrack.productrequest.management.data.ProductRequest;
import kr.co.aim.nanotrack.productrequest.management.data.ProductRequestKey;
import kr.co.aim.nanotrack.productrequestplan.ProductRequestPlanServiceProxy;
import kr.co.aim.nanotrack.productrequestplan.management.data.ProductRequestPlan;
import kr.co.aim.nanotrack.processflow.ProcessFlowServiceProxy;
import kr.co.aim.nanotrack.processflow.management.data.Arc;
import kr.co.aim.nanotrack.processflow.management.data.ArcKey;
import kr.co.aim.nanotrack.processflow.management.data.Node;
import kr.co.aim.nanotrack.processflow.management.data.ProcessFlowKey;
import kr.co.aim.nanotrack.processflow.management.iter.NodeStackUtil;
import kr.co.aim.nanotrack.processoperationspec.ProcessOperationSpecServiceProxy;
import kr.co.aim.nanotrack.processoperationspec.management.data.ProcessOperationSpec;
import kr.co.aim.nanotrack.processoperationspec.management.data.ProcessOperationSpecKey;
import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;
import kr.co.aim.nanotrack.generic.util.StringUtil;
import kr.co.aim.nanotrack.generic.util.XmlUtil;
import kr.co.aim.nanotrack.lot.LotServiceProxy;
import kr.co.aim.nanotrack.lot.management.data.Lot;
import kr.co.aim.nanotrack.lot.management.data.LotKey;
import kr.co.aim.nanotrack.name.NameServiceProxy;
import kr.co.aim.nanotrack.name.management.data.NameGeneratorRuleAttrDef;
import kr.co.aim.nanotrack.name.management.data.NameGeneratorRuleDef;
import kr.co.aim.nanotrack.name.management.data.NameGeneratorRuleDefKey;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class CommonUtil implements ApplicationContextAware
{
    private static Log log = LogFactory.getLog(CommonUtil.class);
    private ApplicationContext	applicationContext;


    public void setApplicationContext( ApplicationContext arg0 ) throws BeansException
    {
        // TODO Auto-generated method stub
        applicationContext = arg0;
    }

    /*
    * Name : getRecipeByProduct
    * Desc : This function is getRecipeByProduct
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static String getRecipeByProduct( List<Map<String, Object>> reserveRecipeByProduct, String productName, String lotRecipeName ){
        String recipename = "";

        for( int i = 0; i < reserveRecipeByProduct.size(); i++ ){
            String inputProductName = reserveRecipeByProduct.get(i).get("PRODUCTNAME").toString();
            if( StringUtils.equals(inputProductName, productName)){
                recipename = reserveRecipeByProduct.get(i).get("RECIPENAME").toString();
                break;
            }
        }
        if(StringUtils.isEmpty(recipename))
            recipename = lotRecipeName;

        return recipename;
    }

    /*
    * Name : getValue
    * Desc : This function is getValue
    * Author : AIM Systems, Inc
    * Date : 2011.05.20
    */
    public static String getValue(Document doc, String itemName)
    {
        String value = "";
        Element root = doc.getDocument().getRootElement();
        try
        {
            value  = root.getChild("Body").getChild(itemName).getText();
        }
        catch(Exception e1)
        {
            log.info("Cannot Find Item Name: " + itemName);
        }
        return value;
    }

    /*
    * Name : getIp
    * Desc : This function is getIp
    * Author : AIM Systems, Inc
    * Date : 2011.05.20
    */
    public static String getIp()
    {
        String ip = "";
        try
        {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {}
        return ip;
    }

    /*
    * Name : getEnumDefValueStringByEnumName
    * Desc : This function is getEnumDefValueStringByEnumName
    * Author : AIM Systems, Inc
    * Date : 2011.05.20
    */
    public static String getEnumDefValueStringByEnumName( String enumName ){
        String enumValue = "";
        String sql = "SELECT ENUMVALUE FROM ENUMDEFVALUE WHERE ENUMNAME = :enumName ";

        Map<String, String> bindMap = new HashMap<String, String>();
        bindMap.put("enumName", enumName);

//		List<Map<String, Object>> sqlResult = 
//			nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().queryForList(sql, bindMap);

        List<Map<String, Object>> sqlResult =
                GenericServiceProxy.getSqlMesTemplate().queryForList(sql, bindMap);

        if(sqlResult.size() > 0){
            enumValue = sqlResult.get(0).get("ENUMVALUE").toString();
        }

        return enumValue;
    }
    /*
    * Name : getEnumDefValueByEnumName
    * Desc : This function is getEnumDefValueByEnumName
    * Author : AIM Systems, Inc
    * Date : 2011.05.20
    */
    public static List<Map<String, Object>> getEnumDefValueByEnumName( String enumName ){
        String sql = "SELECT ENUMVALUE FROM ENUMDEFVALUE "
                + "WHERE ENUMNAME = :enumName ";

        Map<String, String> bindMap = new HashMap<String, String>();
        bindMap.put("enumName", enumName);

//		List<Map<String, Object>> sqlResult = 
//			nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().queryForList(sql, bindMap);

        List<Map<String, Object>> sqlResult =
                GenericServiceProxy.getSqlMesTemplate().queryForList(sql, bindMap);

        return sqlResult;
    }
    /*
    * Name : getEnumDefByEnumName
    * Desc : This function is getEnumDefByEnumName
    * Author : AIM Systems, Inc
    * Date : 2011.05.20
    */
    public static List<Map<String, Object>> getEnumDefByEnumName( String enumName ){
        String sql = "SELECT ENUMNAME, CONSTANTFLAG FROM ENUMDEF "
                + "WHERE ENUMNAME = :enumName ";

        Map<String, String> bindMap = new HashMap<String, String>();
        bindMap.put("enumName", enumName);

//		List<Map<String, Object>> sqlResult = 
//			nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().queryForList(sql, bindMap);

        List<Map<String, Object>> sqlResult =
                GenericServiceProxy.getSqlMesTemplate().queryForList(sql, bindMap);

        return sqlResult;
    }

    public static List<Product> getProductList (String carrierName) throws CustomException
    {
        String condition = "WHERE lotName in ( select lotname from lot where carrierName = :carrierName ) "
                + "AND PRODUCTSTATE = :productState " + "ORDER BY POSITION ";

        Object[] bindSet = new Object[] { carrierName, GenericServiceProxy.getConstantMap().Prod_InProduction };

        List<Product> productList = ProductServiceProxy.getProductService().select(condition, bindSet);

        return productList;
    }

    /*
    * Name : getMachineInfo
    * Desc : This function is getMachineInfo
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static Machine getMachineInfo( String machineName ) throws CustomException{
        MachineKey machineKey = new MachineKey();
        machineKey.setMachineName(machineName);

        Machine machineData = null;

        try {
            machineData = MachineServiceProxy.getMachineService().selectByKey(machineKey);
        }catch (Exception e){
            throw new CustomException("MACHINE-9000", machineName);
        }

        return machineData;
    }

    /*
    * Name : getCrateInfo
    * Desc : This function is getCrateInfo
    * Author : AIM Systems, Inc
    * Date : 2011.03.11
    */
    public static Consumable getCrateInfo ( String crateName ) throws CustomException{
        try
        {
            ConsumableKey consumableKey = new ConsumableKey();
            consumableKey.setConsumableName(crateName);

            Consumable consumableData = null;
            consumableData = ConsumableServiceProxy.getConsumableService().selectByKey(consumableKey);

            return consumableData;
        }
        catch (Exception e)
        {
            throw new CustomException("CRATE-9000", crateName);
        }
    }

    /*
    * Name : getLotInfoByLotName
    * Desc : This function is getLotInfoByLotName
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static Lot getLotInfoByLotName ( String lotName ) throws CustomException{
        try {
            LotKey lotKey = new LotKey();
            lotKey.setLotName(lotName);
            Lot lotData = LotServiceProxy.getLotService().selectByKey(lotKey);

            return lotData;

        } catch (Exception e) {
            throw new CustomException("LOT-9000", lotName);
        }
    }

    /*
    * Name : getPortInfo
    * Desc : This function is getPortInfo
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static Port getPortInfo ( String machineName, String portName ) throws CustomException{
        try
        {
            PortKey portKey = new PortKey();
            portKey.setMachineName(machineName);
            portKey.setPortName(portName);


            Port portData = null;

            portData = PortServiceProxy.getPortService().selectByKey(portKey);

            return portData;
        }
        catch (Exception e) {
            throw new CustomException("PORT-9000",portName);
        }
    }


    /**
     * get Machine spec
     * @author swcho
     * @since 2016.11.02
     * @param machineName
     * @return
     */
    public static MachineSpec getMachineSpecByMachineName(String machineName) throws CustomException
    {
        try
        {
            MachineSpecKey machineSpecKey = new MachineSpecKey();
            machineSpecKey.setMachineName(machineName);

            MachineSpec machineSpecData = MachineServiceProxy.getMachineSpecService().selectByKey(machineSpecKey);

            return machineSpecData;
        }
        catch (Exception ex)
        {
            throw new CustomException("SYS-9001", "MachineSpec");
        }
    }

    /**
     * get product request by ID
     * @author swcho
     * @since 2016.07.12
     * @param productRequestName
     * @return
     * @throws CustomException
     */
    public static ProductRequest getProductRequestData(String productRequestName)
            throws CustomException
    {
        try
        {
            ProductRequestKey prKey = new ProductRequestKey();
            prKey.setProductRequestName(productRequestName);

            ProductRequest prData = ProductRequestServiceProxy.getProductRequestService().selectByKey(prKey);

            return prData;
        }
        catch (Exception ex)
        {
            throw new CustomException("SYS-9001", "ProductRequest");
        }
    }

    /**
     * Lot in any carrier, null means empty carrier
     * @author swcho
     * @since 2015.03.09
     * @param carrierName
     * @return
     * @throws CustomException
     */
    public static Lot getLotInfoBydurableName(String carrierName)
            throws CustomException
    {
        String condition = "WHERE carrierName = ? AND lotState = ?";

        Object[] bindSet = new Object[] {carrierName, GenericServiceProxy.getConstantMap().Lot_Released};

        List<Lot> lotList;

        try
        {
            lotList = LotServiceProxy.getLotService().select(condition, bindSet);
        }
        catch(NotFoundSignal ne)
        {
            //throw new CustomException("CARRIER-9002",carrierName);
            lotList = new ArrayList<Lot>();

            return null;
        }
        catch (Exception ex)
        {
            throw new CustomException("", "");
        }

        //one Lot to a carrier
        return lotList.get(0);
    }

    /**
     * get multi-Lot in any carrier
     * @since 2016.04.26
     * @author swcho
     * @param carrierName
     * @param isMandatory
     * @return
     * @throws CustomException
     */
    public static List<Lot> getLotListByCarrier(String carrierName, boolean isMandatory) throws CustomException
    {
        String condition = "WHERE carrierName = ? AND lotState = ?";

        Object[] bindSet = new Object[] {carrierName, GenericServiceProxy.getConstantMap().Lot_Released};

        List<Lot> lotList;

        try
        {
            lotList = LotServiceProxy.getLotService().select(condition, bindSet);
        }
        catch (Exception ex)
        {
            if (isMandatory)
                throw new CustomException("SYS-9999", "Product", "Nothing Lot");
            else
                return lotList = new ArrayList<Lot>();
        }

        return lotList;
    }

    /*
    * Name : getProductSpecByLotName
    * Desc : This function is getProductSpecByLotName
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static ProductSpec getProductSpecByLotName ( String lotName){
        LotKey lotKey = new LotKey();
        lotKey.setLotName(lotName);

        Lot lotData = null;

        lotData = LotServiceProxy.getLotService().selectByKey(lotKey);

        ProductSpecKey productSpecKey = new ProductSpecKey();
        productSpecKey.setFactoryName(lotData.getFactoryName());
        productSpecKey.setProductSpecName(lotData.getProductSpecName());
        productSpecKey.setProductSpecVersion(lotData.getProductSpecVersion());

        ProductSpec productSpecData = null;
        productSpecData = ProductServiceProxy.getProductSpecService().selectByKey(productSpecKey);

        return productSpecData;
    }

    /*
    * Name : getProductSpecByLotName
    * Desc : This function is getProductSpecByLotName
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static ConsumableSpec getConsumableSpecByProductName ( String productName) throws CustomException{

        ProductKey productKey = new ProductKey();
        productKey.setProductName(productName);

        Product productData = ProductServiceProxy.getProductService().selectByKey(productKey);

        ConsumableKey consumableKey = new ConsumableKey();
        consumableKey.setConsumableName(productData.getUdfs().get("CRATENAME").toString());

        Consumable consumableData = ConsumableServiceProxy.getConsumableService().selectByKey(consumableKey);

        ConsumableSpecKey consumableSpecKey = new ConsumableSpecKey();
        consumableSpecKey.setFactoryName(consumableData.getFactoryName());
        consumableSpecKey.setConsumableSpecName(consumableData.getConsumableSpecName());
        consumableSpecKey.setConsumableSpecVersion(consumableData.getConsumableSpecVersion());

        ConsumableSpec consumableSpecData = null;
        consumableSpecData = ConsumableServiceProxy.getConsumableSpecService().selectByKey(consumableSpecKey);

        return consumableSpecData;
    }

    /*
    * Name : getProductSpecByLotName
    * Desc : This function is getProductSpecByLotName
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static ConsumableSpec getConsumableSpec ( String crateName) throws CustomException{

        ConsumableKey consumableKey = new ConsumableKey();
        consumableKey.setConsumableName(crateName);

        Consumable consumableData = ConsumableServiceProxy.getConsumableService().selectByKey(consumableKey);

        ConsumableSpecKey consumableSpecKey = new ConsumableSpecKey();
        consumableSpecKey.setFactoryName(consumableData.getFactoryName());
        consumableSpecKey.setConsumableSpecName(consumableData.getConsumableSpecName());
        consumableSpecKey.setConsumableSpecVersion(consumableData.getConsumableSpecVersion());

        ConsumableSpec consumableSpecData = null;
        consumableSpecData = ConsumableServiceProxy.getConsumableSpecService().selectByKey(consumableSpecKey);

        return consumableSpecData;
    }

    /*
    * Name : getProcessOperationSpec
    * Desc : This function is getProcessOperationSpec
    * Author : AIM Systems, Inc
    * Date : 2011.03.17
    */
    public static ProcessOperationSpec getProcessOperationSpec( String factoryName, String processOperationName ) throws CustomException{

        ProcessOperationSpec processOperationData = new ProcessOperationSpec();
        try{
            ProcessOperationSpecKey processOperationKey = new ProcessOperationSpecKey();

            processOperationKey.setFactoryName(factoryName);
            processOperationKey.setProcessOperationName(processOperationName);
            processOperationKey.setProcessOperationVersion("00001");

            processOperationData
                    = ProcessOperationSpecServiceProxy.getProcessOperationSpecService().selectByKey(processOperationKey);

        } catch( Exception e ){
            throw new CustomException("PROCESSOPERATION-9001", processOperationName);
        }

        return processOperationData;
    }

    /*
    * Name : getDurableSpecByDurableName
    * Desc : This function is getDurableSpecByDurableName
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static DurableSpec getDurableSpecByDurableName( String durableName ){
        DurableKey durableKey = new DurableKey();
        durableKey.setDurableName(durableName);

        Durable durableData = null;

        durableData = DurableServiceProxy.getDurableService().selectByKey(durableKey);

        DurableSpecKey durableSpecKey = new DurableSpecKey();
        durableSpecKey.setFactoryName(durableData.getFactoryName());
        durableSpecKey.setDurableSpecName(durableData.getDurableSpecName());
        durableSpecKey.setDurableSpecVersion(durableData.getDurableSpecVersion());

        DurableSpec durableSpecData = null;
        durableSpecData = DurableServiceProxy.getDurableSpecService().selectByKey(durableSpecKey);

        return durableSpecData;
    }

    /*
    * Name : getDurableInfo
    * Desc : This function is getDurableInfo
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static Durable getDurableInfo( String durableName ) throws CustomException{
        try{
            DurableKey durableKey = new DurableKey();
            durableKey.setDurableName(durableName);

            Durable durableData = null;

            durableData = DurableServiceProxy.getDurableService().selectByKey(durableKey);

            return durableData;
        }
        catch (Exception e)
        {
            throw new CustomException("DURABLE-9000", durableName);
        }
    }

    /*
    * Name : getPortSpecInfo
    * Desc : This function is getPortSpecInfo
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static PortSpec getPortSpecInfo ( String machineName, String portName ) throws CustomException{

        try
        {
            PortSpecKey portSpecKey = new PortSpecKey();
            portSpecKey.setMachineName(machineName);
            portSpecKey.setPortName(portName);

            PortSpec portSpecData = null;

            portSpecData = PortServiceProxy.getPortSpecService().selectByKey(portSpecKey);

            return portSpecData;
        }
        catch (Exception e) {
            throw new CustomException("PORT-9000",portName);
        }
    }

    public static String getAreaName(String factoryName, String processOperationName, String processOperationVersion)
            throws FrameworkErrorSignal, NotFoundSignal
    {
        ProcessOperationSpecKey key = new ProcessOperationSpecKey();
        key.setFactoryName(factoryName);
        key.setProcessOperationName(processOperationName);
        key.setProcessOperationVersion(processOperationVersion);

        ProcessOperationSpec spec = ProcessOperationSpecServiceProxy.getProcessOperationSpecService().selectByKey(key);
        return spec.getDefaultAreaName();
    }

    /*
    * Name : hasText
    * Desc : This function is find Text
    * Author : AIM Systems, Inc
    * Date : 2011.08.10
    */
    public static boolean hasText(String targetText, String searchText)
    {
        boolean hasText = false;
        if (targetText.length() > 0) {
            if (targetText.indexOf(searchText) > 0)
                hasText = true;
        }
        return hasText;
    }

    /*
    * Name : getPrefix
    * Desc : This function is getPrefix
    * Author : AIM Systems, Inc
    * Date : 2011.08.10
    */
    public static String getPrefix(String targetText, String delimeter)
    {
        if (targetText.length() > 0)
            targetText = targetText.substring(0, targetText.indexOf(delimeter));
        return targetText;
    }

    public static int getIsSameCharactorCount( String context, String compareCharator, int compareCLength ){
        int iSameCount = 0;
        try{
            for( int i = 0; i < context.length(); i++ ){
                String tempContext = context.substring(i, compareCLength + i);

                if( StringUtils.equals(tempContext, compareCharator)){
                    iSameCount++;
                }
            }
        }
        catch( Exception e ){

        }
        return iSameCount;
    }

    /*
    * Name : makeListForQueryAndCount
    * Desc : This function is makeListForQueryAndCount
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static List<String> makeListForQueryAndCount(Element element, String listName, String Name) throws CustomException
    {
        int i = 0;
        String list= "'";
        List<String> argSeq = new ArrayList();

        Element ListElement = element.getChild(listName);

        if( ListElement.getChildren().size() > 0 ){
            for( Iterator iterator = ListElement.getChildren().iterator(); iterator.hasNext(); ){
                Element product = (Element) iterator.next();
                String productName = product.getChild(Name).getText();
                list =  list + productName + "', '";
                i = i + 1;
            }
        }
        list = list + "'";
        try{
            if( list.length() > 4){
                list = list.substring(0, list.length() - 4);
            }
            argSeq.add(list);
            argSeq.add(Integer.toString(i));
        }
        catch(Exception e){
            //throw new Exception();
        }
        return argSeq;
    }

    /*
    * Name : strConvent
    * Desc : This function is spiltGlassGrade String Convent
    * Author : AIM Systems, Inc
    * Date : 2011.08.08
    */
    public static int strConvent(String id)
    {
        int num = 0;
        for(int i = 65; i < 91; i++)
        {
            if((int)id.charAt(0) >= 86)
            {
                if((int)id.charAt(0) == i)
                {
                    num = i - 57;
                    break;
                }
            }
            else
            {
                if((int)id.charAt(0) == i)
                {
                    num = i - 55;
                    break;
                }
            }
        }

        return num;
    }

    /**
     * 150311 by swcho : modified with correction
     * to get generic naming rule
     * @author swcho
     * @since 2013.09.23
     * @param ruleName
     * @param nameRuleAttrMap
     * @param quantity
     * @param originalSourceSubjectName
     * @return
     * @throws CustomException
     */
    public static List<String> getNameByNamingRule(String ruleName, Map<String, Object> nameRuleAttrMap, int quantity,
                                                   String sendSubjectName)
            throws CustomException
    {
        List<String> lstResult = new ArrayList<String>();

        try
        {
            //set param for query
            HashMap<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("RULENAME", ruleName);
            paraMap.put("QUANTITY", String.valueOf(quantity));

            Document doc = SMessageUtil.generateQueryMessage("GetNameList",
                    paraMap,
                    (HashMap<String, Object>) nameRuleAttrMap,
                    "", "MES", "");

            //doc = ESBService.sendBySenderReturnMessage(replySubjectName, doc,
            //											doc.getRootElement().getChild(SMessageUtil.Body_Tag),
            //											"QRYSender", "");

            doc = GenericServiceProxy.getESBServive().sendRequestBySender(sendSubjectName, doc, "QRYSender");

            if (doc != null)
            {
                Element returnList = XmlUtil.getNode(doc, "//" + SMessageUtil.Message_Tag + "/" + SMessageUtil.Return_Tag);

                String errorCode = SMessageUtil.getChildText(returnList, SMessageUtil.Result_ReturnCode, true);

                if (!errorCode.equals("0"))
                {
                    String errorString = SMessageUtil.getChildText(returnList, SMessageUtil.Result_ErrorMessage, false);

                    throw new Exception(errorString);
                }

                //parsing result
                Element resultList = XmlUtil.getNode(doc, "//" + SMessageUtil.Message_Tag + "/" + SMessageUtil.Body_Tag + "/" + "DATALIST");

                for (Iterator iterator = resultList.getChildren().iterator(); iterator.hasNext();) {
                    Element resultData = (Element) iterator.next();

                    String name = resultData.getChildText("NAMEVALUE");

                    lstResult.add(name);
                }
            }
        }
        catch (Exception ex)
        {
            throw new CustomException("SYS-0000", ex.getMessage());
        }

        return lstResult;
    }

    /**
     * modified to tune
     * @author swcho
     * @since 2013.09.26
     * @param ruleName
     * @param nameRuleAttrMap
     * @param quantity
     * @return generatedNameList
     */
    public static List<String> generateNameByNamingRule(String ruleName, Map<String, Object> nameRuleAttrMap, int quantity) throws CustomException
    {
        if(log.isInfoEnabled()){
            log.debug("ruleName = " + ruleName);
            log.debug("demand = " + quantity);
        }

        //get single Lot ID
        //${location}.${factory}.${cim}.${mode}.${svr}
        StringBuffer nameServerSubjectName = new StringBuffer(GenericServiceProxy.getESBServive().getSendSubject("QRYsvr"));
		/*StringBuffer nameServerSubjectName = new StringBuffer(System.getProperty("location")).append(".")
													.append(System.getProperty("factory")).append(".")
													.append(System.getProperty("cim")).append(".")
													.append(System.getProperty("mode")).append(".")
													.append(System.getProperty("shop")).append(".")
													.append("PEXsvr");*/

        List<String> lstLotName;

        try {
            lstLotName = CommonUtil.getNameByNamingRule(ruleName, nameRuleAttrMap, quantity, nameServerSubjectName.toString());
        } catch (CustomException e) {
            lstLotName = new ArrayList<String>();
            //throw new CustomException("SYS-0000", e.getMessage());
            throw e;
        }

        return lstLotName;
    }

    /**
     * name generator
     * @author swcho
     * @since 2014.04.16
     * @param ruleName
     * @param nameRuleAttrMap
     * @param quantity
     * @return
     * @throws CustomException
     */
    public static List<String> generateNameByNamingRule(String ruleName, Map<String, Object> nameRuleAttrMap, long quantity)
            throws CustomException
    {
        List<String> argSeq = new ArrayList();

        try
        {
            NameGeneratorRuleDef ruleDef;

            try
            {
                ruleDef = NameServiceProxy.getNameGeneratorRuleDefService().selectByKey(
                        new NameGeneratorRuleDefKey(ruleName));
            }
            catch (NotFoundSignal ne)
            {
                throw new CustomException("SYS-9001", NameGeneratorRuleDef.class.getSimpleName());
            }

            String namingRuleSql = CommonUtil.getValue(ruleDef.getUdfs(), "SQL");

//			List<Map<String, Object>> sqlResult = kr.co.aim.nanotrack.generic.GenericServiceProxy.
//													getSqlMesTemplate().queryForList(namingRuleSql, nameRuleAttrMap);

            List<Map<String, Object>> sqlResult = GenericServiceProxy.getSqlMesTemplate().queryForList(namingRuleSql, nameRuleAttrMap);

            List<NameGeneratorRuleAttrDef> nameGeneratorRuleAttrDefList;

            try
            {
                nameGeneratorRuleAttrDefList = NameServiceProxy.getNameGeneratorRuleAttrDefService()
                        .getAllRuleAttrDefs(ruleName);
            }
            catch (NotFoundSignal ne)
            {
                throw new CustomException("SYS-9001", NameGeneratorRuleAttrDef.class.getSimpleName());
            }

            for (NameGeneratorRuleAttrDef nameGeneratorRuleAttrDef : nameGeneratorRuleAttrDefList)
            {
                if(nameGeneratorRuleAttrDef.getSectionType().equals("Argument"))
                {
                    int position = (int) nameGeneratorRuleAttrDef.getKey().getPosition();

                    try
                    {
                        argSeq.add(sqlResult.get(position).get("SECTIONVALUE").toString());
                    }
                    catch (Exception ex)
                    {
                        //150825 by swcho : get by name
                        String argName = nameGeneratorRuleAttrDef.getSectionName();

                        for (Map<String, Object> row : sqlResult)
                        {
                            if (row.get("SECTIONNAME").toString().equals(argName))
                            {
                                argSeq.add(row.get("SECTIONVALUE").toString());
                            }
                        }
                    }
                }
            }

            List<String> names = NameServiceProxy.getNameGeneratorRuleDefService().generateName(ruleName, argSeq, quantity);

            String[] receiveNames = new String[names.size()];

            List<String> convertedNames = new ArrayList<String>();

            for (int i = 0; i < names.size(); i++)
            {
                String convertedName = convertReceivedNaming(ruleName, names.get(i).toString());

                convertedNames.add(convertedName);
                //receiveNames[i] = names.get(i).toString();
                receiveNames[i] = convertedName;

                log.info("ID LIST = " + i + " : " + receiveNames[i]);
            }

            return convertedNames;
        }
        catch (FrameworkErrorSignal fe)
        {
            throw new CustomException("SYS-9999", "NameGeneratorRuleDefService", fe.getMessage());
        }
    }

    /*
    * Name : getMachineRecipeByTPFO
    * Desc : This function is getMachineRecipeByTPFOM
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static List<Map<String, Object>> getMachineRecipeByTPFO(String lotName, String machineName){
        //log.info("START getMachineRecipeByTPFOM");
        String sql = "SELECT MACHINERECIPENAME " +
                "FROM POSMACHINE PM, TPFOPOLICY TP " +
                "WHERE 1=1 " +
                "AND PM.MACHINENAME = :machineName " +
                "AND PM.CONDITIONID = TP.CONDITIONID " +
                "AND TP.FACTORYNAME = :factoryName " +
                "AND TP.PRODUCTSPECNAME = :productSpecName " +
                "AND TP.PROCESSFLOWNAME = :processFlowName " +
                "AND TP.PROCESSOPERATIONNAME = :procesOperationName ";

        Map<String, String> bindMap = new HashMap<String, String>();

        LotKey lotKey = new LotKey();
        lotKey.setLotName(lotName);

        Lot lotData = null;
        lotData = LotServiceProxy.getLotService().selectByKey(lotKey);

        bindMap.put("machineName", machineName);
        bindMap.put("factoryName", lotData.getFactoryName());
        bindMap.put("productSpecName", lotData.getProductSpecName());
        bindMap.put("processFlowName", lotData.getProcessFlowName());
        bindMap.put("procesOperationName", lotData.getProcessOperationName());

//		List<Map<String, Object>> sqlResult 
//		  = nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().queryForList(sql, bindMap);

        List<Map<String, Object>> sqlResult
                = GenericServiceProxy.getSqlMesTemplate().queryForList(sql, bindMap);

        String machineRecipe = "";
        if(sqlResult.size() > 0)
            machineRecipe = sqlResult.get(0).get("MACHINERECIPENAME").toString();

        log.info("MachineRecipe = " +  machineRecipe);
        log.info("END getMachineRecipeByTPFO");
        return sqlResult;
    }

    /**
     * convert MES name according to rule type
     * @author swcho
     * @since 2014.04.17
     * @param ruleName
     * @param name
     * @return
     */
    public static String convertReceivedNaming(String ruleName, String name)
    {
        String convertedName = name;

        try
        {
            if (ruleName.equals("LotNaming"))
            {
                //handling only by query
				/*int length = 2;
				
				//parse only serial partition
				String serial = StringUtil.substring(name, name.length() - length);
				
				if (!StringUtil.isEmpty(serial) && serial.length() == length)
				{
					//must be numeric with single digit
					String target = StringUtil.substring(serial, 0, 1);
					String suffix = StringUtil.substring(serial, 1, length);
					String alpha = "";
					
					switch (Integer.parseInt(target))
					{
						case 0: alpha = "A";break;
						case 1: alpha = "B";break;
						case 2: alpha = "C";break;
						case 3: alpha = "D";break;
						case 4: alpha = "E";break;
						case 5: alpha = "F";break;
						case 6: alpha = "G";break;
						case 7: alpha = "H";break;
						case 8: alpha = "J";break;
						case 9: alpha = "K";break;
						default: alpha = target;
					}
					
					convertedName = new StringBuilder()
										.append(StringUtil.removeEnd(name, serial))
										.append(alpha).append(suffix).toString();
				}*/
            }
        }
        catch (Exception ex)
        {
            //ignore
        }

        return convertedName;
    }

    /**
     * get value from dictionary by key
     * @author swcho
     * @since 2013.11.06
     * @param map
     * @param keyName
     * @return
     */
    public static String getValue(Map map, String keyName)
    {
        try
        {
            Object value = map.get(keyName);

            if (value != null && value instanceof String)
                return value.toString();
            else if (value != null && value instanceof BigDecimal)
                return value.toString();
        }
        catch(Exception ex)
        {
            log.debug(ex.getMessage());
        }

        return "";
    }

    /**
     * @author smkang
     * @since 2014-02-20
     * @param element
     * @return namedValueSequence
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     */
    public static Map<String, String> setNamedValueSequence(Element element, String typeName)
            throws FrameworkErrorSignal, NotFoundSignal
    {
        Map<String, String> namedValuemap = new HashMap<String, String>();

        List<ObjectAttributeDef> objectAttributeDefs = nanoFrameServiceProxy.getObjectAttributeMap().getAttributeNames(typeName, "ExtendedC");

        log.info("UDF SIZE=" + objectAttributeDefs.size());

        if (objectAttributeDefs != null)
        {
            for (int i = 0; i < objectAttributeDefs.size(); i++)
            {
                String name = "";
                String value = "";

                if (element != null)
                {
                    for (int j = 0; j < element.getContentSize(); j++)
                    {
                        if (element.getChildText(objectAttributeDefs.get(i).getAttributeName()) != null)
                        {
                            name = objectAttributeDefs.get(i).getAttributeName();
                            value = element.getChildText(objectAttributeDefs.get(i).getAttributeName());

                            break;
                        }
                        else
                        {
                            name = objectAttributeDefs.get(i).getAttributeName();
                        }
                    }
                }

                //140821 by swcho : empty value could not be modified in UDF
                if (name.equals("") != true && StringUtil.isNotEmpty(value))
                    namedValuemap.put(name, value);
            }
        }
        else {

        }

        log.info("UDF SIZE=" + namedValuemap.size());
        return namedValuemap;
    }


    /*
    * Name : getProductAttributeByLength
    * Desc : This function is getProductAttributeByLength
    * Author : AIM Systems, Inc
    * Date : 2011.05.20
    */
    public static String getProductAttributeByLength(String machineName, String productName, String fieldName, String itemValue) throws CustomException
    {
        String sql = "SELECT LENGTH(" + fieldName + ") AS LEN, " + fieldName + " FROM PRODUCT WHERE PRODUCTNAME = :productName ";

        Map<String, String> bindMap = new HashMap<String, String>();
        bindMap.put("productName", productName);

        try{

//			List<Map<String,Object>> sqlResult = nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().queryForList(sql, bindMap);

            List<Map<String,Object>> sqlResult = GenericServiceProxy.getSqlMesTemplate().queryForList(sql, bindMap);

            if(sqlResult.size() > 0)
            {
                if(!(StringUtils.isEmpty((String)sqlResult.get(0).get(fieldName))))
                {
                    if(Integer.parseInt((String)sqlResult.get(0).get("LEN").toString()) != itemValue.length() )
                    {
                        itemValue = (String)sqlResult.get(0).get(fieldName);
                        log.debug("Product Info Different In Field: " + fieldName + ". Reported Value: " + itemValue + ". Machine ID: " + machineName);
                    }
                }
                else
                {
                    sql = "UPDATE PRODUCT SET " + fieldName +  " = :itemValue WHERE PRODUCTNAME = :productName";
                    bindMap.put("itemValue", itemValue);

//					nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().update(sql, bindMap);
                    GenericServiceProxy.getSqlMesTemplate().update(sql, bindMap);
                }
            }
        }
        catch(Exception e)
        {
            throw new CustomException("COM-9001", fieldName);
        }
        return itemValue;
    }

    /**
     * convert collection array to String
     * @author swcho
     * @since 2014.04.29
     * @param obj
     * @return
     */
    public static String toStringFromCollection(Object obj)
    {
        StringBuffer temp = new StringBuffer("");

        if (obj instanceof Object[])
        {
            for (Object element : (Object[]) obj)
            {
                if (!temp.toString().isEmpty())
                    temp.append(",");

                try
                {
                    temp.append(element.toString());
                }
                catch (Exception ex)
                {
                    if (log.isDebugEnabled())
                        log.debug(ex.getMessage());
                }
            }
        }
        else if (obj instanceof List)
        {
            for (Object element : (List<Object>) obj)
            {
                if (!temp.toString().isEmpty())
                    temp.append(",");

                try
                {
                    temp.append(element.toString());
                }
                catch (Exception ex)
                {
                    if (log.isDebugEnabled())
                        log.debug(ex.getMessage());
                }
            }
        }

        return temp.toString();
    }

    /*
    * Name : getProductdata
    * Desc : This function is getMachineInfo
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static Product getProductData( String productName ) throws CustomException{
        ProductKey productKey = new ProductKey();
        productKey.setProductName(productName);

        Product productData = null;

        try {
            productData = ProductServiceProxy.getProductService().selectByKey(productKey);
        }catch (Exception e){
            throw new CustomException("PRODUCT-0001", productName);
        }

        return productData;
    }

    /**
     * Added by hykim
     * @param factoryName
     * @param processFlowName
     * @param processFlowVersion
     * @return List<String>
     * @throws
     */
    public static List<String> getOperList(String factoryName, String processFlowName, String processFlowVersion) throws CustomException
    {
        String sql = "SELECT N.NODEATTRIBUTE1, PC.PROCESSOPERATIONTYPE FROM NODE N, ARC A, PROCESSOPERATIONSPEC PC" +
                " WHERE N.FACTORYNAME = :factoryName " +
                " AND N.FACTORYNAME = PC.FACTORYNAME " +
                " AND A.FROMNODEID = N.NODEID " +
                " AND N.NODEATTRIBUTE1 = PC.PROCESSOPERATIONNAME " +
                " AND N.PROCESSFLOWNAME = :processFlowName " +
                " AND N.PROCESSFLOWVERSION = :processFlowVersion " +
                " AND N.NODETYPE = 'ProcessOperation' ";
        //" ORDER BY XCOORDINATE, YCOORDINATE ";

        Map<String, String> bindMap = new HashMap<String, String>();
        bindMap.put("factoryName", factoryName);
        bindMap.put("processFlowName", processFlowName);
        bindMap.put("processFlowVersion", processFlowVersion);

//		List<Map<String, Object>> sqlResult = nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().queryForList(sql, bindMap);
        List<Map<String, Object>> sqlResult = GenericServiceProxy.getSqlMesTemplate().queryForList(sql, bindMap);

        List<String> operList = new ArrayList<String>();

        if( sqlResult.size() > 0)
        {
            for(int i=0; i<sqlResult.size(); i++)
            {
                String operationType = sqlResult.get(i).get("PROCESSOPERATIONTYPE").toString();

                if( operationType.equals("Inspection"))
                {
                    operList.add(sqlResult.get(i).get("NODEATTRIBUTE1").toString());
                }
            }
        }
        return operList;
    }

    /**
     * Added by hykim
     * @param factoryName
     * @param processFlowName
     * @param processFlowVersion
     * @return List<String>
     * @throws
     */
    public static List<String> getProductionOperList(String factoryName, String processFlowName, String processFlowVersion) throws CustomException
    {
        String sql = "SELECT N.NODEATTRIBUTE1, PC.PROCESSOPERATIONTYPE FROM NODE N, ARC A, PROCESSOPERATIONSPEC PC" +
                " WHERE N.FACTORYNAME = :factoryName " +
                " AND N.FACTORYNAME = PC.FACTORYNAME " +
                " AND A.FROMNODEID = N.NODEID " +
                " AND N.NODEATTRIBUTE1 = PC.PROCESSOPERATIONNAME " +
                " AND N.PROCESSFLOWNAME = :processFlowName " +
                " AND N.PROCESSFLOWVERSION = :processFlowVersion " +
                " AND N.NODETYPE = 'ProcessOperation' ";
        //" ORDER BY XCOORDINATE, YCOORDINATE ";

        Map<String, String> bindMap = new HashMap<String, String>();
        bindMap.put("factoryName", factoryName);
        bindMap.put("processFlowName", processFlowName);
        bindMap.put("processFlowVersion", processFlowVersion);

//		List<Map<String, Object>> sqlResult = nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().queryForList(sql, bindMap);
        List<Map<String, Object>> sqlResult = GenericServiceProxy.getSqlMesTemplate().queryForList(sql, bindMap);

        List<String> operList = new ArrayList<String>();

        if( sqlResult.size() > 0)
        {
            for(int i=0; i<sqlResult.size(); i++)
            {
                String operationType = sqlResult.get(i).get("PROCESSOPERATIONTYPE").toString();

                if( operationType.equals("Production"))
                {
                    operList.add(sqlResult.get(i).get("NODEATTRIBUTE1").toString());
                }
            }
        }
        return operList;
    }

    /**
     * Added by hykim
     * @param lotName
     * @param String
     * @return List<String>
     * @throws
     */
    public static List<String> splitString(String regex, String str) throws CustomException
    {
        List<String> strList = new ArrayList<String>();
        String[] strArray = str.split(regex);

        for(String strTemp : strArray)
        {
            strList.add(strTemp);
        }

        return strList;
    }

    /**
     * Added by hykim
     * @param lotName
     * @param double
     * @return String
     * @throws
     */
    public static String makeProductSamplingPositionList(int productSamplingCount) throws CustomException
    {
        String productSamplePositionList = "";

        for(int i=1; i<productSamplingCount+1; i++)
        {
            productSamplePositionList = productSamplePositionList + String.valueOf(i);

            if(i != productSamplingCount)
                productSamplePositionList = productSamplePositionList + ",";
        }

        return productSamplePositionList;
    }

    /**
     * Added by hykim
     * @param lotName
     * @param double
     * @return String
     * @throws
     */
    public static String StringValueOf(double d) throws CustomException
    {
        String str = "";

        if(d == 0)
            str = "0";
        else
        {
            str = String.valueOf(d);

            if(str.indexOf(".") > 0)
            {
                str = str.substring(0, str.indexOf("."));
                //List<String> strList = CommonUtil.splitString(".", str);

                //str = strList.get(0);
            }
        }

        return str;
    }

    /** copyToObjectList
     * Added by hykim
     * @param List<Object>
     * @return List<Object>
     * @throws
     */
    public static List<String> copyToStringList(List<String> strList) throws CustomException
    {
        List<String> returnStrList = new ArrayList<String>();

        for(String str : strList)
        {
            returnStrList.add(str);
        }

        return returnStrList;
    }

    /** toStringWithoutBrackets
     * Added by hykim
     * @param List<Object>
     * @return List<Object>
     * @throws
     */
    public static String toStringWithoutBrackets(List<String> strList) throws CustomException
    {
        String str = strList.toString();

        str = str.replace("[", "");
        str = str.replace("]", "");

        return str;
    }

    /*
    * Name : getMaskInfoBydurableName
    * Desc : This function is getLotInfoBydurableName
    * Author : AIM Systems, Inc
    * Date : 2011.03.07
    */
    public static List<Durable> getMaskInfoBydurableName(String carrierName,String durabletype) throws CustomException{
        //log.info("START getLotInfoBydurableName");
        String condition = "WHERE durabletype = : durabletype and MASKCARRIERNAME = :carrierName ";

        Object[] bindSet = new Object[] {durabletype,carrierName,};
        List<Durable> maskList = new ArrayList<Durable>();
        try
        {
            maskList = DurableServiceProxy.getDurableService().select(condition, bindSet);
        }
        catch(Exception e)
        {
            maskList = null;
            throw new CustomException("MASK-0009",carrierName);
        }
        //log.info("END getLotInfoBydurableName");
        return maskList;
    }

    /**
     * whether Lot has reached end of any flow?
     * @author swcho
     * @since 2015.03.01
     * @param lotData
     * @return
     * @throws CustomException
     */
    public static boolean isLotInEndOperation(Lot lotData, String factoryName, String processFlowName)
            throws CustomException
    {
        try
        {
            Node endNode = ProcessFlowServiceProxy.getProcessFlowService().getEndNode(new ProcessFlowKey(factoryName, processFlowName, "00001"));

            List<Arc> connectArc = ProcessFlowServiceProxy.getArcService().select("toNodeId = ?", new Object[] {endNode.getKey().getNodeId()});

            Node lastOperationNode = ProcessFlowServiceProxy.getNodeService().getNode(connectArc.get(0).getKey().getFromNodeId());

            String targetFactoryName = lastOperationNode.getFactoryName();
            String targetFlowName = lastOperationNode.getProcessFlowName();
            String targetOperationName = lastOperationNode.getNodeAttribute1();

            if (lotData.getFactoryName().equals(targetFactoryName)
                    && lotData.getProcessFlowName().equals(targetFlowName)
                    && lotData.getProcessOperationName().equals(targetOperationName))
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            //all components are mandatory
            log.warn("this Lot might not be located anywhere");
        }

        return false;
    }

    /*
    * Name : isInitialInput
    * Desc : This function is isInitialInput
    * Author : xzquan
    * Date : 2016.02.20
    */
    public static boolean isInitialInput(String machineName)
            throws CustomException
    {
        boolean isInitialInput = false;

        try
        {
            MachineSpecKey mSpecKey = new MachineSpecKey(machineName);
            MachineSpec mSpec = MachineServiceProxy.getMachineSpecService().selectByKey(mSpecKey);
            String constructType = mSpec.getUdfs().get("CONSTRUCTTYPE").toString();

            if(constructType.equals("UPK"))
            {
                isInitialInput = true;
            }
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
        }

        return isInitialInput;
    }

    /*
    * Name : isBpkType
    * Desc : This function is isBpkType
    * Author : Aim System
    * Date : 2016.06.22
    */
    public static boolean isBpkType(String machineName)
            throws CustomException
    {
        boolean isInitialInput = false;

        try
        {
            MachineSpecKey mSpecKey = new MachineSpecKey(machineName);
            MachineSpec mSpec = MachineServiceProxy.getMachineSpecService().selectByKey(mSpecKey);
            String constructType = mSpec.getUdfs().get("CONSTRUCTTYPE").toString();

            if(constructType.equals("BPK"))
            {
                isInitialInput = true;
            }
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
        }

        return isInitialInput;
    }

    /**
     * is inline EQP
     * @author swcho
     * @since 2016-04-27
     * @param machineName
     * @return
     * @throws CustomException
     */
    public static boolean isInineType(String machineName)
            throws CustomException
    {
        boolean isIt = false;

        try
        {
            MachineSpecKey mSpecKey = new MachineSpecKey(machineName);
            MachineSpec mSpec = MachineServiceProxy.getMachineSpecService().selectByKey(mSpecKey);

            String constructType = mSpec.getUdfs().get("CONSTRUCTTYPE").toString();

            if(constructType.equals("EVA"))
            {
                isIt = true;
            }
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
        }

        return isIt;
    }

    //-- COMMENT
    //-- 2016.02.17 LEE HYEON WOO
    public static String getProcessFlowNameByFGCode(Lot lotData, String productSpecName){
        String processFlowName = "";

        ProductSpecKey productSpecKey = new ProductSpecKey();
        productSpecKey.setFactoryName(lotData.getFactoryName());
        productSpecKey.setProductSpecName(productSpecName);
        productSpecKey.setProductSpecVersion("00001");

        ProductSpec productSpecData = ProductServiceProxy.getProductSpecService().selectByKey(productSpecKey);

        processFlowName = productSpecData.getProcessFlowName();

        return processFlowName;
    }

    //-- COMMENT
    //-- 2011.02.16 JUNG SUN KYU // 2016.02.22 LEE HYEON WOO
    public static String getProcessFlowNameByFGCode(String productSpecName){
        String processFlowName = "";

        ProductSpecKey productSpecKey = new ProductSpecKey();
        productSpecKey.setFactoryName("MODULE");
        productSpecKey.setProductSpecName(productSpecName);
        productSpecKey.setProductSpecVersion("00001");

        ProductSpec productSpecData = ProductServiceProxy.getProductSpecService().selectByKey(productSpecKey);

        processFlowName = productSpecData.getProcessFlowName();

        return processFlowName;
    }

    //-- COMMENT
    //-- 2016.02.17 LEE HYEON WOO
    public static String getNodeStack( String factoryName, String processFlowName, String processOperationName){
        String nodeStack = "";
        String sql = "SELECT NODEID FROM NODE WHERE " ;
        sql += " FACTORYNAME = :factoryName ";
        sql += " AND PROCESSFLOWNAME = :processFlowName ";
        sql += " AND PROCESSFLOWVERSION = :processFlowVersion ";
        sql += " AND NODEATTRIBUTE1 = :processOperationName ";
        sql += " AND NODEATTRIBUTE2 = :processOperationVersion ";

        Map<String, String> bindMap = new HashMap<String, String>();
        bindMap.put("factoryName", factoryName);
        bindMap.put("processFlowName", processFlowName);
        bindMap.put("processFlowVersion", "00001");
        bindMap.put("processOperationName", processOperationName);
        bindMap.put("processOperationVersion", "00001");

        List<Map<String, Object>> sqlResult
                = nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().queryForList(sql, bindMap);

        if( sqlResult.size() == 1 ){
            nodeStack = sqlResult.get(0).get("NODEID").toString();
        } else {
            nodeStack = "";
        }
        return nodeStack;
    }

    //-- COMMENT
    //-- 2016.02.17 LEE HYEON WOO
    public static ProcessOperationSpec getProcessOperationSpec( Lot lotData, String stepID ) throws CustomException{

        ProcessOperationSpec processOperationData = new ProcessOperationSpec();
        try{
            ProcessOperationSpecKey processOperationKey = new ProcessOperationSpecKey();

            processOperationKey.setFactoryName(lotData.getFactoryName());
            processOperationKey.setProcessOperationName(stepID);
            processOperationKey.setProcessOperationVersion("00001");

            processOperationData
                    = ProcessOperationSpecServiceProxy.getProcessOperationSpecService().selectByKey(processOperationKey);

        } catch( Exception e ){
            throw new CustomException("PROCESSOPERATION-9000", stepID);
        }

        return processOperationData;
    }

    //-- COMMENT
    //-- 2011.01.28 // 2016.02.23 LEE HYEON WOO
    public static List<Map<String, Object>> trackOutInfoResult ( Lot lotData, String serviceName ){
        // Set Sql
        String sql = "SELECT * FROM CT_TRACKOUTINFO WHERE SERVICENAME = :serviceName AND PROCESSOPERATIONNAME = :processOpreationName";

        // Set bindMap and performs the query.
        Map<String, String> bindMap = new HashMap<String, String>();

        bindMap.put("serviceName", serviceName);
        bindMap.put("processOpreationName", lotData.getProcessOperationName().toString());

        List<Map<String, Object>> sqlResult
                = nanoFrameServiceProxy.getSqlTemplate().getSimpleJdbcTemplate().queryForList(sql, bindMap);

        if( sqlResult.size() == 0 ){
            // Throw
        }
        return sqlResult;
    }

    /**
     * getFirstPlanByMachine
     * 151105 by xzquan : service object changed
     * @author xzquan
     * @since 2015.11.08
     * @param eventInfo
     * @param machineName
     * @throws CustomException
     */
    public static ProductRequestPlan getFirstPlanByMachine(String machineName, boolean flag)
            throws CustomException
    {
        try
        {
            ProductRequestPlan pPlan = new ProductRequestPlan();

            if(flag)
            {
                String condition = "assignedMachineName = ? and productRequestState IN (?, ?, ?) "
                        + "and position = (select min(position) from productRequestPlan Where assignedMachineName = ? and productRequestState IN (?, ?, ?))";
                Object bindSet[] = new Object[]{machineName, "Completing", "Started", "Aborted", machineName, "Completing", "Started", "Aborted"};
                List<ProductRequestPlan> pPlanList = ProductRequestPlanServiceProxy.getProductRequestPlanService().select(condition, bindSet);

                if(pPlanList.size() > 1)
                {
                    throw new CustomException("PRODUCTREQUEST-0022", machineName);
                }

                pPlan = pPlanList.get(0);
            }
            else
            {
                String condition = "assignedMachineName = ? and productRequestState IN (?, ?) "
                        + "and position = (select min(position) from productRequestPlan Where assignedMachineName = ? and productRequestState IN (?, ?))";
                Object bindSet[] = new Object[]{machineName, "Released", "Started", machineName, "Released", "Started"};
                List<ProductRequestPlan> pPlanList = ProductRequestPlanServiceProxy.getProductRequestPlanService().select(condition, bindSet);

                if(pPlanList.size() > 1)
                {
                    throw new CustomException("PRODUCTREQUEST-0022", machineName);
                }

                pPlan = pPlanList.get(0);
            }

            return pPlan;
        }
        //160603 by swcho : error enhanced
        catch (NotFoundSignal ne)
        {
            throw new CustomException("PRODUCTREQUEST-0021", machineName);
        }
        catch (FrameworkErrorSignal fe)
        {
            throw new CustomException("PRODUCTREQUEST-0021", machineName);
        }
    }

    /**
     * getStartPlanByMachine
     * 160503 by Aim System : service object changed
     * @author Aim System
     * @since 2016.05.03
     * @param eventInfo
     * @param machineName
     * @throws CustomException
     */
    public static ProductRequestPlan getStartPlanByMachine(String machineName)
            throws CustomException
    {
        try
        {
            ProductRequestPlan pPlan = new ProductRequestPlan();

            String condition = "assignedMachineName = ? and productRequestState = ? ";
            Object bindSet[] = new Object[]{machineName, "Started"};
            List<ProductRequestPlan> pPlanList = ProductRequestPlanServiceProxy.getProductRequestPlanService().select(condition, bindSet);

            if(pPlanList.size() > 1)
            {
                throw new CustomException("PRODUCTREQUEST-0022", machineName);
            }

            pPlan = pPlanList.get(0);

            return pPlan;
        }
        catch (nanoFrameDBErrorSignal ne)
        {
            throw new CustomException("PRODUCTREQUEST-0021", machineName);
        }
    }

    /**
     * getFactoryNameByMachine
     * 151105 by xzquan : service object changed
     * @author xzquan
     * @since 2015.11.08
     * @param eventInfo
     * @param machineName
     * @throws CustomException
     */
    public static String getFactoryNameByMachine(String machineName)
            throws CustomException
    {
        try
        {
            MachineSpecKey mSpecKey = new MachineSpecKey(machineName);
            MachineSpec mSpec = MachineServiceProxy.getMachineSpecService().selectByKey(mSpecKey);

            String factoryName = mSpec.getFactoryName();

            return factoryName;
        }
        catch (nanoFrameDBErrorSignal ne)
        {
            throw new CustomException("PRODUCTREQUEST-0021", machineName);
        }
    }

    /**
     * get first operation for any flow
     * @author swcho
     * @since 2016.04.06
     * @param factoryName
     * @param processFlowName
     * @return
     * @throws CustomException
     */
    public static ProcessOperationSpec getFirstOperation(String factoryName, String processFlowName)
            throws CustomException
    {
        try
        {
            ProcessFlowKey pfKey = new ProcessFlowKey(factoryName, processFlowName, GenericServiceProxy.getConstantMap().DEFAULT_ACTIVE_VERSION);

            String startNodeStack = ProcessFlowServiceProxy.getProcessFlowService().getStartNode(pfKey).getKey().getNodeId();
            String nodeId = ProcessFlowServiceProxy.getProcessFlowService().getNextNode(startNodeStack, "Normal", "").getKey().getNodeId();

            Node currentNode = ProcessFlowServiceProxy.getNodeService().getNode(nodeId);

            String processOperationName = currentNode.getNodeAttribute1();

            ProcessOperationSpec processOperation = getProcessOperationSpec(factoryName, processOperationName);

            return processOperation;
        }
        catch (CustomException ce)
        {
            throw ce;
        }
        catch (Exception ex)
        {
            throw new CustomException("SYS-9999", "ProcessOperation", ex.getMessage());
        }
    }

    /**
     * get next operation for any flow
     * @author swcho
     * @since 2017.02.09
     * @param factoryName
     * @param processFlowName
     * @param processOperationName
     * @return
     * @throws CustomException
     */
    public static ProcessOperationSpec getNextOperation(String factoryName, String processFlowName, String processOperationName)
            throws CustomException
    {
        try
        {
            String startNodeStack = NodeStack.getNodeID(factoryName, processFlowName, processOperationName);
            String nodeId = ProcessFlowServiceProxy.getProcessFlowService().getNextNode(startNodeStack, "Normal", "").getKey().getNodeId();

            Node currentNode = ProcessFlowServiceProxy.getNodeService().getNode(nodeId);

            String nextOperationName = currentNode.getNodeAttribute1();

            ProcessOperationSpec processOperation = getProcessOperationSpec(factoryName, nextOperationName);

            return processOperation;
        }
        catch (CustomException ce)
        {
            throw ce;
        }
        catch (Exception ex)
        {
            throw new CustomException("SYS-9999", "ProcessOperation", ex.getMessage());
        }
    }

    /**
     * getUnScrappedProducts
     * 160425 AIM System
     * @since 2016.04.25
     * @param carrierName
     * @throws CustomException
     */
    public static List<Product> getUnScrappedProducts(String carrierName)
            throws CustomException
    {
        String condition = "carrierName = ? AND productState != ?" + " AND productState != ? ";

        Object[] bindSet = new Object[] {carrierName, GenericServiceProxy.getConstantMap().Prod_Scrapped, GenericServiceProxy.getConstantMap().Prod_Consumed};
        List<Product> productList = new ArrayList<Product>();
        try
        {
            productList = ProductServiceProxy.getProductService().select(condition, bindSet);
        }
        catch(Exception e)
        {
            productList = null;
            throw new CustomException("CST-0011");
        }

        return productList;
    }

    /*
    * Name : Get ZPL Code
    * Author : AIM Systems, Inc
    * Date : 2016.06.18
    */
    public static String getZplCode(String labelID, String version)
    {
        boolean flag = false;

        String sql = "SELECT L.LABELCODE " +
                "FROM CT_LABEL L " +
                "WHERE 1=1 " +
                "AND L.LABELID = :labelID " +
                "AND L.VERSION = :version ";

        Map<String, String> bindMap = new HashMap<String, String>();

        bindMap.put("labelID", labelID);
        bindMap.put("version", version);

        List<Map<String, Object>> sqlResult
                = GenericServiceProxy.getSqlMesTemplate().queryForList(sql, bindMap);


        if(sqlResult.size() < 1)
        {
            log.info("Not Exist LabelCode");
            flag = true;
        }

        return sqlResult.get(0).get("LABELCODE").toString();
    }
}