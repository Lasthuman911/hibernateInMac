package kr.co.aim.nanotrack.product.management.data;

import java.sql.Timestamp;

import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.access.UdfAccessor;

public class Product extends UdfAccessor implements DataInfo<ProductKey>
{
    private ProductKey	key;

    private String		productionType;
    private String		productSpecName;
    private String		productSpecVersion;
    private String		productSpec2Name;
    private String		productSpec2Version;
    private String		materialLocationName;
    private String		processGroupName;
    private String		transportGroupName;
    private String		productRequestName;
    private String		originalProductName;
    private String		sourceProductName;
    private String		destinationProductName;
    private String		lotName;
    private String		carrierName;
    private long		position;
    private String		productType;
    private String		subProductType;
    private double		subProductUnitQuantity1;
    private double		subProductUnitQuantity2;
    private double		createSubProductQuantity;
    private double		createSubProductQuantity1;
    private double		createSubProductQuantity2;
    private double		subProductQuantity;
    private double		subProductQuantity1;
    private double		subProductQuantity2;
    private String		productGrade;
    private String		subProductGrades1;
    private String		subProductGrades2;
    private Timestamp	dueDate;
    private long		priority;
    private String		factoryName;
    private String		destinationFactoryName;
    private String		areaName;
    private String		serialNo;
    private String		productState;
    private String		productProcessState;
    private String		productHoldState;
    private String		lastEventName;
    private String		lastEventTimeKey;
    private Timestamp	lastEventTime;
    private String		lastEventUser;
    private String		lastEventComment;
    private String		lastEventFlag;
    private Timestamp	createTime;
    private String		createUser;
    private Timestamp	inProductionTime;
    private String		inProductionUser;
    private Timestamp	lastProcessingTime;
    private String		lastProcessingUser;
    private Timestamp	lastIdleTime;
    private String		lastIdleUser;
    private String		reasonCodeType;
    private String		reasonCode;
    private String		processFlowName;
    private String		processFlowVersion;
    private String		processOperationName;
    private String		processOperationVersion;
    private String		nodeStack;
    private String		machineName;
    private String		machineRecipeName;
    private String		reworkState;
    private long		reworkCount;
    private String		reworkNodeId;
    private String		branchEndNodeId;

    public Product()
    {
    }

    public ProductKey getKey()
    {
        return this.key;
    }

    public void setKey(ProductKey key)
    {
        this.key = key;
    }

    public String getProductionType()
    {
        return this.productionType;
    }

    public void setProductionType(String productionType)
    {
        this.productionType = productionType;
    }

    public String getProductSpecName()
    {
        return this.productSpecName;
    }

    public void setProductSpecName(String productSpecName)
    {
        this.productSpecName = productSpecName;
    }

    public String getProductSpecVersion()
    {
        return this.productSpecVersion;
    }

    public void setProductSpecVersion(String productSpecVersion)
    {
        this.productSpecVersion = productSpecVersion;
    }

    public String getProductSpec2Name()
    {
        return this.productSpec2Name;
    }

    public void setProductSpec2Name(String productSpec2Name)
    {
        this.productSpec2Name = productSpec2Name;
    }

    public String getProductSpec2Version()
    {
        return this.productSpec2Version;
    }

    public void setProductSpec2Version(String productSpec2Version)
    {
        this.productSpec2Version = productSpec2Version;
    }

    public String getMaterialLocationName()
    {
        return this.materialLocationName;
    }

    public void setMaterialLocationName(String materialLocationName)
    {
        this.materialLocationName = materialLocationName;
    }

    public String getProcessGroupName()
    {
        return this.processGroupName;
    }

    public void setProcessGroupName(String processGroupName)
    {
        this.processGroupName = processGroupName;
    }

    public String getTransportGroupName()
    {
        return this.transportGroupName;
    }

    public void setTransportGroupName(String transportGroupName)
    {
        this.transportGroupName = transportGroupName;
    }

    public String getProductRequestName()
    {
        return this.productRequestName;
    }

    public void setProductRequestName(String productRequestName)
    {
        this.productRequestName = productRequestName;
    }

    public String getOriginalProductName()
    {
        return this.originalProductName;
    }

    public void setOriginalProductName(String originalProductName)
    {
        this.originalProductName = originalProductName;
    }

    public String getSourceProductName()
    {
        return this.sourceProductName;
    }

    public void setSourceProductName(String sourceProductName)
    {
        this.sourceProductName = sourceProductName;
    }

    public String getDestinationProductName()
    {
        return this.destinationProductName;
    }

    public void setDestinationProductName(String destinationProductName)
    {
        this.destinationProductName = destinationProductName;
    }

    public String getLotName()
    {
        return this.lotName;
    }

    public void setLotName(String lotName)
    {
        this.lotName = lotName;
    }

    public String getCarrierName()
    {
        return this.carrierName;
    }

    public void setCarrierName(String carrierName)
    {
        this.carrierName = carrierName;
    }

    public long getPosition()
    {
        return this.position;
    }

    public void setPosition(long position)
    {
        this.position = position;
    }

    public String getProductType()
    {
        return this.productType;
    }

    public void setProductType(String productType)
    {
        this.productType = productType;
    }

    public String getSubProductType()
    {
        return this.subProductType;
    }

    public void setSubProductType(String subProductType)
    {
        this.subProductType = subProductType;
    }

    public double getSubProductUnitQuantity1()
    {
        return this.subProductUnitQuantity1;
    }

    public void setSubProductUnitQuantity1(double subProductUnitQuantity1)
    {
        this.subProductUnitQuantity1 = subProductUnitQuantity1;
    }

    public double getSubProductUnitQuantity2()
    {
        return this.subProductUnitQuantity2;
    }

    public void setSubProductUnitQuantity2(double subProductUnitQuantity2)
    {
        this.subProductUnitQuantity2 = subProductUnitQuantity2;
    }

    public double getCreateSubProductQuantity()
    {
        return this.createSubProductQuantity;
    }

    public void setCreateSubProductQuantity(double createSubProductQuantity)
    {
        this.createSubProductQuantity = createSubProductQuantity;
    }

    public double getCreateSubProductQuantity1()
    {
        return this.createSubProductQuantity1;
    }

    public void setCreateSubProductQuantity1(double createSubProductQuantity1)
    {
        this.createSubProductQuantity1 = createSubProductQuantity1;
    }

    public double getCreateSubProductQuantity2()
    {
        return this.createSubProductQuantity2;
    }

    public void setCreateSubProductQuantity2(double createSubProductQuantity2)
    {
        this.createSubProductQuantity2 = createSubProductQuantity2;
    }

    public double getSubProductQuantity()
    {
        return this.subProductQuantity;
    }

    public void setSubProductQuantity(double subProductQuantity)
    {
        this.subProductQuantity = subProductQuantity;
    }

    public double getSubProductQuantity1()
    {
        return this.subProductQuantity1;
    }

    public void setSubProductQuantity1(double subProductQuantity1)
    {
        this.subProductQuantity1 = subProductQuantity1;
    }

    public double getSubProductQuantity2()
    {
        return this.subProductQuantity2;
    }

    public void setSubProductQuantity2(double subProductQuantity2)
    {
        this.subProductQuantity2 = subProductQuantity2;
    }

    public String getProductGrade()
    {
        return this.productGrade;
    }

    public void setProductGrade(String productGrade)
    {
        this.productGrade = productGrade;
    }

    public String getSubProductGrades1()
    {
        return this.subProductGrades1;
    }

    public void setSubProductGrades1(String subProductGrades1)
    {
        this.subProductGrades1 = subProductGrades1;
    }

    public String getSubProductGrades2()
    {
        return this.subProductGrades2;
    }

    public void setSubProductGrades2(String subProductGrades2)
    {
        this.subProductGrades2 = subProductGrades2;
    }

    public Timestamp getDueDate()
    {
        return this.dueDate;
    }

    public void setDueDate(Timestamp dueDate)
    {
        this.dueDate = dueDate;
    }

    public long getPriority()
    {
        return this.priority;
    }

    public void setPriority(long priority)
    {
        this.priority = priority;
    }

    public String getFactoryName()
    {
        return this.factoryName;
    }

    public void setFactoryName(String factoryName)
    {
        this.factoryName = factoryName;
    }

    public String getDestinationFactoryName()
    {
        return this.destinationFactoryName;
    }

    public void setDestinationFactoryName(String destinationFactoryName)
    {
        this.destinationFactoryName = destinationFactoryName;
    }

    public String getAreaName()
    {
        return this.areaName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getSerialNo()
    {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }

    public String getProductState()
    {
        return this.productState;
    }

    public void setProductState(String productState)
    {
        this.productState = productState;
    }

    public String getProductProcessState()
    {
        return this.productProcessState;
    }

    public void setProductProcessState(String productProcessState)
    {
        this.productProcessState = productProcessState;
    }

    public String getProductHoldState()
    {
        return this.productHoldState;
    }

    public void setProductHoldState(String productHoldState)
    {
        this.productHoldState = productHoldState;
    }

    public String getLastEventName()
    {
        return this.lastEventName;
    }

    public void setLastEventName(String lastEventName)
    {
        this.lastEventName = lastEventName;
    }

    public String getLastEventTimeKey()
    {
        return this.lastEventTimeKey;
    }

    public void setLastEventTimeKey(String lastEventTimeKey)
    {
        this.lastEventTimeKey = lastEventTimeKey;
    }

    public Timestamp getLastEventTime()
    {
        return this.lastEventTime;
    }

    public void setLastEventTime(Timestamp lastEventTime)
    {
        this.lastEventTime = lastEventTime;
    }

    public String getLastEventUser()
    {
        return this.lastEventUser;
    }

    public void setLastEventUser(String lastEventUser)
    {
        this.lastEventUser = lastEventUser;
    }

    public String getLastEventComment()
    {
        return this.lastEventComment;
    }

    public void setLastEventComment(String lastEventComment)
    {
        this.lastEventComment = lastEventComment;
    }

    public String getLastEventFlag()
    {
        return this.lastEventFlag;
    }

    public void setLastEventFlag(String lastEventFlag)
    {
        this.lastEventFlag = lastEventFlag;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getCreateUser()
    {
        return this.createUser;
    }

    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public Timestamp getInProductionTime()
    {
        return this.inProductionTime;
    }

    public void setInProductionTime(Timestamp inProductionTime)
    {
        this.inProductionTime = inProductionTime;
    }

    public String getInProductionUser()
    {
        return this.inProductionUser;
    }

    public void setInProductionUser(String inProductionUser)
    {
        this.inProductionUser = inProductionUser;
    }

    public Timestamp getLastProcessingTime()
    {
        return this.lastProcessingTime;
    }

    public void setLastProcessingTime(Timestamp lastProcessingTime)
    {
        this.lastProcessingTime = lastProcessingTime;
    }

    public String getLastProcessingUser()
    {
        return this.lastProcessingUser;
    }

    public void setLastProcessingUser(String lastProcessingUser)
    {
        this.lastProcessingUser = lastProcessingUser;
    }

    public Timestamp getLastIdleTime()
    {
        return this.lastIdleTime;
    }

    public void setLastIdleTime(Timestamp lastIdleTime)
    {
        this.lastIdleTime = lastIdleTime;
    }

    public String getLastIdleUser()
    {
        return this.lastIdleUser;
    }

    public void setLastIdleUser(String lastIdleUser)
    {
        this.lastIdleUser = lastIdleUser;
    }

    public String getReasonCodeType()
    {
        return this.reasonCodeType;
    }

    public void setReasonCodeType(String reasonCodeType)
    {
        this.reasonCodeType = reasonCodeType;
    }

    public String getReasonCode()
    {
        return this.reasonCode;
    }

    public void setReasonCode(String reasonCode)
    {
        this.reasonCode = reasonCode;
    }

    public String getProcessFlowName()
    {
        return this.processFlowName;
    }

    public void setProcessFlowName(String processFlowName)
    {
        this.processFlowName = processFlowName;
    }

    public String getProcessFlowVersion()
    {
        return this.processFlowVersion;
    }

    public void setProcessFlowVersion(String processFlowVersion)
    {
        this.processFlowVersion = processFlowVersion;
    }

    public String getProcessOperationName()
    {
        return this.processOperationName;
    }

    public void setProcessOperationName(String processOperationName)
    {
        this.processOperationName = processOperationName;
    }

    public String getProcessOperationVersion()
    {
        return this.processOperationVersion;
    }

    public void setProcessOperationVersion(String processOperationVersion)
    {
        this.processOperationVersion = processOperationVersion;
    }

    public String getNodeStack()
    {
        return this.nodeStack;
    }

    public void setNodeStack(String nodeStack)
    {
        this.nodeStack = nodeStack;
    }

    public String getMachineName()
    {
        return this.machineName;
    }

    public void setMachineName(String machineName)
    {
        this.machineName = machineName;
    }

    public String getMachineRecipeName()
    {
        return this.machineRecipeName;
    }

    public void setMachineRecipeName(String machineRecipeName)
    {
        this.machineRecipeName = machineRecipeName;
    }

    public String getReworkState()
    {
        return this.reworkState;
    }

    public void setReworkState(String reworkState)
    {
        this.reworkState = reworkState;
    }

    public long getReworkCount()
    {
        return this.reworkCount;
    }

    public void setReworkCount(long reworkCount)
    {
        this.reworkCount = reworkCount;
    }

    public String getReworkNodeId()
    {
        return this.reworkNodeId;
    }

    public void setReworkNodeId(String reworkNodeId)
    {
        this.reworkNodeId = reworkNodeId;
    }

    public String getBranchEndNodeId()
    {
        return branchEndNodeId;
    }

    public void setBranchEndNodeId(String branchEndNodeId)
    {
        this.branchEndNodeId = branchEndNodeId;
    }

}