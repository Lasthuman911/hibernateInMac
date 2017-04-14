package kr.co.aim.nanotrack.product.management;

import java.util.List;

import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.InvalidStateTransitionSignal;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;
import kr.co.aim.nanotrack.generic.info.CommonMaterialAttributeService;
import kr.co.aim.nanotrack.generic.info.EventInfo;
import kr.co.aim.nanotrack.generic.info.UndoTimeKeys;
import kr.co.aim.nanotrack.product.management.data.Product;
import kr.co.aim.nanotrack.product.management.data.ProductKey;
import kr.co.aim.nanotrack.product.management.info.AssignCarrierInfo;
import kr.co.aim.nanotrack.product.management.info.AssignLotAndCarrierInfo;
import kr.co.aim.nanotrack.product.management.info.AssignLotInfo;
import kr.co.aim.nanotrack.product.management.info.AssignProcessGroupInfo;
import kr.co.aim.nanotrack.product.management.info.AssignTransportGroupInfo;
import kr.co.aim.nanotrack.product.management.info.ChangeGradeInfo;
import kr.co.aim.nanotrack.product.management.info.ChangeSpecInfo;
import kr.co.aim.nanotrack.product.management.info.ConsumeMaterialsInfo;
import kr.co.aim.nanotrack.product.management.info.CreateInfo;
import kr.co.aim.nanotrack.product.management.info.CreateRawInfo;
import kr.co.aim.nanotrack.product.management.info.CreateWithLotInfo;
import kr.co.aim.nanotrack.product.management.info.DeassignCarrierInfo;
import kr.co.aim.nanotrack.product.management.info.DeassignLotAndCarrierInfo;
import kr.co.aim.nanotrack.product.management.info.DeassignLotInfo;
import kr.co.aim.nanotrack.product.management.info.DeassignProcessGroupInfo;
import kr.co.aim.nanotrack.product.management.info.DeassignTransportGroupInfo;
import kr.co.aim.nanotrack.product.management.info.MakeAllocatedInfo;
import kr.co.aim.nanotrack.product.management.info.MakeBranchRecoveryInfo;
import kr.co.aim.nanotrack.product.management.info.MakeCompletedInfo;
import kr.co.aim.nanotrack.product.management.info.MakeConsumedInfo;
import kr.co.aim.nanotrack.product.management.info.MakeIdleInfo;
import kr.co.aim.nanotrack.product.management.info.MakeInProductionInfo;
import kr.co.aim.nanotrack.product.management.info.MakeInReworkInfo;
import kr.co.aim.nanotrack.product.management.info.MakeNotInReworkInfo;
import kr.co.aim.nanotrack.product.management.info.MakeNotOnHoldInfo;
import kr.co.aim.nanotrack.product.management.info.MakeOnHoldInfo;
import kr.co.aim.nanotrack.product.management.info.MakeProcessingInfo;
import kr.co.aim.nanotrack.product.management.info.MakeReceivedInfo;
import kr.co.aim.nanotrack.product.management.info.MakeScrappedInfo;
import kr.co.aim.nanotrack.product.management.info.MakeShippedInfo;
import kr.co.aim.nanotrack.product.management.info.MakeTravelingInfo;
import kr.co.aim.nanotrack.product.management.info.MakeUnScrappedInfo;
import kr.co.aim.nanotrack.product.management.info.MakeUnShippedInfo;
import kr.co.aim.nanotrack.product.management.info.RecreateInfo;
import kr.co.aim.nanotrack.product.management.info.SeparateInfo;
import kr.co.aim.nanotrack.product.management.info.SetAreaInfo;
import kr.co.aim.nanotrack.product.management.info.SetEventInfo;
import kr.co.aim.nanotrack.product.management.info.UndoInfo;

/**
 * DAO 接口，定义了数据库的一系列的原子性操作
 */
public interface ProductService extends CommonMaterialAttributeService<ProductKey, Product>
{
    /**
     * Find all product data by a lot.
     *
     * @param lotName
     *            A lot name assigned to a product. String
     * @return List<Product> Found Product datas.
     *         List<kr.co.aim.nanotrack.product.management.data.Product>
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     *
     * @see <pre>
     * String lotName = args.get(ItemName.lotName);
     *
     * try
     * {
     * 	List&lt;Product&gt; productList = ProductServiceProxy.getProductService().allProductsByLot(lotName);
     * 	System.out.println(productList.size());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public List<Product> allProductsByLot(String lotName) throws FrameworkErrorSignal, NotFoundSignal;

    /**
     * Find all unscrapped product data by a lot.
     *
     * @param lotName
     *            A lot name assigned to a product. String
     * @return List<Product> Found unscrapped Product datas.
     *         List<kr.co.aim.nanotrack.product.management.data.Product>
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     *
     * @see <pre>
     * String lotName = args.get(ItemName.lotName);
     *
     * try
     * {
     * 	List&lt;Product&gt; productList = ProductServiceProxy.getProductService().allUnScrappedProductsByLot(lotName);
     * 	System.out.println(productList.size());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public List<Product> allUnScrappedProductsByLot(String lotName) throws FrameworkErrorSignal, NotFoundSignal;

    /**
     * Find all product data by a carrier.
     *
     * @param carrierName
     *            A carrier name to be assigned to a product. String
     * @return List<Product> Found Product datas.
     *         List<kr.co.aim.nanotrack.product.management.data.Product>
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     *
     * @see <pre>
     * String carrierName = args.get(ItemName.carrierName);
     *
     * try
     * {
     * 	List&lt;Product&gt; productList = ProductServiceProxy.getProductService().allProductsByCarrier(carrierName);
     * 	System.out.println(productList.size());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public List<Product> allProductsByCarrier(String carrierName) throws FrameworkErrorSignal, NotFoundSignal;

    /**
     * Find all product data by a process group.
     *
     * @param processGroupName
     *            A process group name assigned to a product. String
     * @return List<Product> Found Product datas.
     *         List<kr.co.aim.nanotrack.product.management.data.Product>
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     *
     * @see <pre>
     * String processGroupName = args.get(ItemName.processGroupName);
     *
     * try
     * {
     * 	List&lt;Product&gt; productList = ProductServiceProxy.getProductService().allProductsByProcessGroup(processGroupName);
     * 	System.out.println(productList.size());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public List<Product> allProductsByProcessGroup(String processGroupName) throws FrameworkErrorSignal, NotFoundSignal;

    /**
     * Find all product data by a transport group.
     *
     * @param transportGroupName
     *            A transport group name assigned to a product. String
     * @return List<Product> Found Product datas.
     *         List<kr.co.aim.nanotrack.product.management.data.Product>
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     *
     * @see <pre>
     * String transportGroupName = args.get(ItemName.transportGroupName);
     *
     * try
     * {
     * 	List&lt;Product&gt; productList = ProductServiceProxy.getProductService().allProductsByTransportGroup(transportGroupName);
     * 	System.out.println(productList.size());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public List<Product> allProductsByTransportGroup(String transportGroupName)
            throws FrameworkErrorSignal, NotFoundSignal;

    /**
     * Create a Product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param createInfo
     *            A create specific information.
     *            kr.co.aim.nanotrack.product.management.info.CreateInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     * @throws InvalidStateTransitionSignal
     *
     * @see <pre>
     * try
     * {
     * 	String productName = args.get(ItemName.productName);
     * 	String productSpecName = args.get(ItemName.productSpecName);
     * 	String productSpecVersion = args.get(ItemName.productSpecVersion);
     * 	String processFlowName = args.get(ItemName.processFlowName);
     * 	String processFlowVersion = args.get(ItemName.processFlowVersion);
     *
     * 	// If product exist, Remove this.remove(productName);
     *
     * 	// 1. CreateInfo
     * 	CreateInfo createInfo = new CreateInfo();
     * 	createInfo.setProductName(productName);
     *
     * 	createInfo.setProductionType(&quot;Production&quot;);
     * 	if (StringUtils.isEmpty(productSpecName))
     * 		productSpecName = &quot;Semi200mm_Wafer02&quot;;
     * 	createInfo.setProductSpecName(productSpecName);
     * 	if (StringUtils.isEmpty(productSpecVersion))
     * 		productSpecVersion = &quot;00001&quot;;
     * 	createInfo.setProductSpecVersion(productSpecVersion);
     *
     * 	createInfo.setProductType(&quot;Wafer&quot;);
     * 	createInfo.setSubProductType(&quot;Die&quot;);
     * 	createInfo.setSubProductUnitQuantity1(10);
     *
     * 	createInfo.setProductGrade(&quot;G&quot;);
     * 	createInfo.setSubProductGrades1(&quot;GGGGGGGGGG&quot;);
     *
     * 	createInfo.setDueDate(TimeUtils.getTimestamp(&quot;20091231&quot;));
     * 	createInfo.setPriority(1);
     *
     * 	createInfo.setFactoryName(&quot;SEMIFAB&quot;);
     *
     * 	if (StringUtils.isEmpty(processFlowName))
     * 		processFlowName = &quot;PF_03&quot;;
     * 	createInfo.setProcessFlowName(processFlowName);
     * 	if (StringUtils.isEmpty(processFlowVersion))
     * 		processFlowVersion = &quot;00001&quot;;
     * 	createInfo.setProcessFlowVersion(processFlowVersion);
     *
     * 	Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * 	udfs.put(&quot;UDF01&quot;, &quot;create1&quot;);
     * 	udfs.put(&quot;UDF02&quot;, &quot;create2&quot;);
     * 	createInfo.setUdfs(udfs);
     *
     * 	// 2. EventInfo
     * 	EventInfo eventInfo = new EventInfo();
     * 	eventInfo.setEventName(&quot;Create&quot;);
     * 	eventInfo.setEventUser(&quot;ehwoo&quot;);
     * 	eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * 	eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * 	ProductServiceProxy.getProductService().create(new ProductKey(), eventInfo, createInfo);
     * } catch (Exception e)
     * {
     * 	e.printStackTrace();
     * }
     * </pre>
     */
    @Deprecated
    public Product create(ProductKey productKey, EventInfo eventInfo, CreateInfo createInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal;

    public Product create(EventInfo eventInfo, CreateInfo createInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal;

    /**
     * Create a product with a lot.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param createWithLotInfo
     *            A createWithLot specific information.
     *            kr.co.aim.nanotrack.product.management.info.CreateWithLotInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     * @throws InvalidStateTransitionSignal
     *
     * @see <pre>
     * try
     * {
     * 	String productName = args.get(ItemName.productName);
     * 	String lotName = args.get(ItemName.lotName);
     *
     * 	// If product exist, Remove this.remove(productName);
     *
     * 	// 1. CreateInfo
     * 	CreateWithLotInfo createWithLotInfo = new CreateWithLotInfo();
     * 	createWithLotInfo.setProductName(productName);
     *
     * 	createWithLotInfo.setProductionType(&quot;Production&quot;);
     * 	createWithLotInfo.setProductSpecName(&quot;Semi200mm_Wafer02&quot;);
     * 	createWithLotInfo.setProductSpecVersion(&quot;00001&quot;);
     *
     * 	LotKey lotKey = new LotKey();
     * 	lotKey.setLotName(lotName);
     * 	Lot lot = LotServiceProxy.getLotService().selectByKey(lotKey);
     *
     * 	createWithLotInfo.setLotName(lot.getKey().getLotName());
     * 	createWithLotInfo.setCarrierName(lot.getCarrierName());
     *
     * 	createWithLotInfo.setPosition(2);
     * 	createWithLotInfo.setProductType(&quot;Wafer&quot;);
     * 	createWithLotInfo.setSubProductType(&quot;Die&quot;);
     * 	createWithLotInfo.setSubProductUnitQuantity1(10);
     *
     * 	createWithLotInfo.setProductGrade(&quot;G&quot;);
     * 	createWithLotInfo.setSubProductGrades1(&quot;GGGGGGGGGG&quot;);
     *
     * 	createWithLotInfo.setDueDate(TimeUtils.getTimestamp(&quot;20091231&quot;));
     * 	createWithLotInfo.setPriority(1);
     *
     * 	createWithLotInfo.setProductState(GenericServiceProxy.getConstantMap().Prod_InProduction);
     * 	createWithLotInfo.setProductProcessState(GenericServiceProxy.getConstantMap().Prod_Idle);
     * 	createWithLotInfo.setProductHoldState(GenericServiceProxy.getConstantMap().Prod_NotOnHold);
     *
     * 	createWithLotInfo.setFactoryName(&quot;SEMIFAB&quot;); // createWithLotInfo.setAreaName(&quot;THIN&quot;);
     *
     * 	createWithLotInfo.setProcessFlowName(&quot;PF_03&quot;);
     * 	createWithLotInfo.setProcessFlowVersion(&quot;00001&quot;);
     *
     * 	Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * 	udfs.put(&quot;UDF01&quot;, &quot;createWithLot1&quot;);
     * 	udfs.put(&quot;UDF02&quot;, &quot;createWithLot2&quot;);
     * 	createWithLotInfo.setUdfs(udfs);
     *
     * 	// 2. EventInfo
     * 	EventInfo eventInfo = new EventInfo();
     * 	eventInfo.setEventName(&quot;CreateWithLot&quot;);
     * 	eventInfo.setEventUser(&quot;ehwoo&quot;);
     * 	eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * 	eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * 	ProductServiceProxy.getProductService().createWithLot(new ProductKey(), eventInfo, createWithLotInfo);
     * } catch (Exception e)
     * {
     * 	e.printStackTrace();
     * }
     * </pre>
     */
    @Deprecated
    public Product createWithLot(ProductKey productKey, EventInfo eventInfo, CreateWithLotInfo createWithLotInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal;

    public Product createWithLot(EventInfo eventInfo, CreateWithLotInfo createWithLotInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal;

    /**
     * Create a product with a product state as a user wants.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param createRawInfo
     *            A createRaw specific information.
     *            kr.co.aim.nanotrack.product.management.info.CreateRawInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     * @throws InvalidStateTransitionSignal
     *
     * @see <pre>
     * try
     * {
     * 	String productName = args.get(ItemName.productName);
     *
     * 	// If product exist, Remove this.remove(productName);
     *
     * 	// 1. CreateInfo
     * 	CreateRawInfo createRawInfo = new CreateRawInfo();
     * 	createRawInfo.setProductName(productName);
     *
     * 	createRawInfo.setProductionType(&quot;Production&quot;);
     * 	createRawInfo.setProductSpecName(&quot;Semi200mm_Wafer02&quot;);
     * 	createRawInfo.setProductSpecVersion(&quot;00001&quot;);
     *
     * 	createRawInfo.setCarrierName(&quot;Car_Ehwoo_03&quot;);
     * 	createRawInfo.setPosition(1);
     * 	createRawInfo.setProductType(&quot;Wafer&quot;);
     * 	createRawInfo.setSubProductType(&quot;Die&quot;);
     * 	createRawInfo.setSubProductUnitQuantity1(10);
     *
     * 	createRawInfo.setProductGrade(&quot;G&quot;);
     * 	createRawInfo.setSubProductGrades1(&quot;GGGGGGGGGG&quot;);
     *
     * 	createRawInfo.setDueDate(TimeUtils.getTimestamp(&quot;20091231&quot;));
     * 	createRawInfo.setPriority(1);
     *
     * 	createRawInfo.setProductState(GenericServiceProxy.getConstantMap().Prod_NotAllocated);
     * 	createRawInfo.setProductProcessState(GenericServiceProxy.getConstantMap().Prod_Idle);
     * 	createRawInfo.setProductHoldState(GenericServiceProxy.getConstantMap().Prod_NotOnHold);
     *
     * 	createRawInfo.setFactoryName(&quot;SEMIFAB&quot;); // createRawInfo.setAreaName(&quot;THIN&quot;);
     *
     * 	createRawInfo.setProcessFlowName(&quot;PF_03&quot;);
     * 	createRawInfo.setProcessFlowVersion(&quot;00001&quot;);
     *
     * 	createRawInfo.setMachineName(&quot;THIN_MAC11&quot;);
     * 	createRawInfo.setMachineRecipeName(&quot;THIN_MAC_RCP01&quot;);
     *
     * 	Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * 	udfs.put(&quot;UDF01&quot;, &quot;createRaw1&quot;);
     * 	udfs.put(&quot;UDF02&quot;, &quot;createRaw2&quot;);
     * 	createRawInfo.setUdfs(udfs);
     *
     * 	// 2. EventInfo
     * 	EventInfo eventInfo = new EventInfo();
     * 	eventInfo.setEventName(&quot;CreateRaw&quot;);
     * 	eventInfo.setEventUser(&quot;ehwoo&quot;);
     * 	eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * 	eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * 	ProductServiceProxy.getProductService().createRaw(new ProductKey(), eventInfo, createRawInfo);
     * } catch (Exception e)
     * {
     * 	e.printStackTrace();
     * }
     * </pre>
     */
    @Deprecated
    public Product createRaw(ProductKey productKey, EventInfo eventInfo, CreateRawInfo createRawInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal;

    public Product createRaw(EventInfo eventInfo, CreateRawInfo createRawInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal;

    /**
     * Allocate a product to a lot.
     *
     * @param key
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeAllLocatedInfo
     *            A makeAllocated specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeAllocatedInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeAllocatedInfo makeAllocatedInfo = new MakeAllocatedInfo();
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeAllocated1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeAllocated2&quot;);
     * makeAllocatedInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * eventInfo EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeAllocated&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeAllocated(productKey, eventInfo, makeAllocatedInfo);
     * </pre>
     */
    public Product makeAllocated(ProductKey key, EventInfo eventInfo, MakeAllocatedInfo makeAllLocatedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Start production of a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeInProductionInfo
     *            A makeInProduction specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeInProductionInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeInProductionInfo makeInProductionInfo = new MakeInProductionInfo();
     *
     * makeInProductionInfo.setCarrierName(&quot;&quot;); // makeInProductionInfo.setAreaName( &quot;THIN&quot; );
     *
     * makeInProductionInfo.setDueDate(TimeUtils.getTimestamp(&quot;20091231&quot;));
     *
     * Product product = ProductServiceProxy.getProductService().selectByKey(productKey);
     * makeInProductionInfo.setPriority(product.getPriority());
     *
     * makeInProductionInfo.setProcessFlowName(product.getProcessFlowName());
     * makeInProductionInfo.setProcessFlowVersion(product.getProcessFlowVersion());
     * makeInProductionInfo.setProcessOperationName(product.getProcessOperationName());
     * makeInProductionInfo.setProcessOperationVersion(product.getProcessOperationVersion());
     *
     * makeInProductionInfo.setNodeStack(product.getNodeStack());
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeInProduction1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeInProduction2&quot;);
     * makeInProductionInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeInProduction&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeInProduction(productKey, eventInfo, makeInProductionInfo);
     * </pre>
     */
    public Product makeInProduction(ProductKey productKey, EventInfo eventInfo,
                                    MakeInProductionInfo makeInProductionInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Scrap a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeScrappedInfo
     *            A makeScrapped specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeScrappedInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeScrappedInfo makeScrappedInfo = new MakeScrappedInfo(); //
     * makeScrappedInfo.setProductQuantity(2);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeScrapped1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeScrapped2&quot;);
     * makeScrappedInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeScrapped&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeScrapped(productKey, eventInfo, makeScrappedInfo);
     * </pre>
     */
    public Product makeScrapped(ProductKey key, EventInfo eventInfo, MakeScrappedInfo makeScrappedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Unscrap a scrapped product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeUnScrappedInfo
     *            A makeUnScrapped specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeUnScrappedInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeUnScrappedInfo makeUnScrappedInfo = new MakeUnScrappedInfo();
     * // makeUnScrappedInfo.setProductProcessState(GenericServiceProxy.getConstantMap().Prod_Processing );
     * makeUnScrappedInfo.setProductProcessState(GenericServiceProxy.getConstantMap().Prod_Idle);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeUnScrapped1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeUnScrapped2&quot;);
     * makeUnScrappedInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeUnScrapped&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeUnScrapped(productKey, eventInfo, makeUnScrappedInfo);
     * </pre>
     */
    public Product makeUnScrapped(ProductKey productKey, EventInfo eventInfo, MakeUnScrappedInfo makeUnScrappedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Complete a production of a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeCompletedInfo
     *            aMakeCompletedInfo A makeCompleted specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeCompletedInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeCompletedInfo makeCompletedInfo = new MakeCompletedInfo();
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeCompleted1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeCompleted2&quot;);
     * makeCompletedInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeCompleted&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeCompleted(productKey, eventInfo, makeCompletedInfo);
     * </pre>
     */
    public Product makeCompleted(ProductKey productKey, EventInfo eventInfo, MakeCompletedInfo makeCompletedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Ship a product to a destination factory.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeShippedInfo
     *            A makeShipped specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeShippedInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeShippedInfo makeShippedInfo = new MakeShippedInfo();
     * makeShippedInfo.setFactoryName(&quot;RNDFAB&quot;);
     * makeShippedInfo.setAreaName(&quot;DTHIN&quot;); //
     * makeShippedInfo.setDirectShipFlag(&quot;Y&quot;);
     * makeShippedInfo.setDirectShipFlag(&quot;N&quot;);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeShipped1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeShipped2&quot;);
     * makeShippedInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeShipped&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeShipped(productKey, eventInfo, makeShippedInfo);
     * </pre>
     */
    public Product makeShipped(ProductKey productKey, EventInfo eventInfo, MakeShippedInfo makeShippedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Unship a shipped product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeUnShippedInfo
     *            A makeUnShipped specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeUnShippedInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeUnShippedInfo makeUnShippedInfo = new MakeUnShippedInfo();
     * makeUnShippedInfo.setAreaName(&quot;THIN&quot;);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeUnShipped1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeUnShipped2&quot;);
     * makeUnShippedInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeUnShipped&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeUnShipped(productKey, eventInfo, makeUnShippedInfo);
     * </pre>
     */
    public Product makeUnShipped(ProductKey productKey, EventInfo eventInfo, MakeUnShippedInfo makeUnShippedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Receive a product from a source factory.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeReceivedInfo
     *            A makeReceived specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeReceivedInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeReceivedInfo makeReceivedInfo = new MakeReceivedInfo();
     * makeReceivedInfo.setProductionType(&quot;Production&quot;);
     * makeReceivedInfo.setProductSpecName(&quot;Semi200mm_Wafer02&quot;);
     * makeReceivedInfo.setProductSpecVersion(&quot;00001&quot;);
     *
     * makeReceivedInfo.setProductType(&quot;Wafer&quot;);
     * makeReceivedInfo.setSubProductType(&quot;Die&quot;);
     *
     * makeReceivedInfo.setAreaName(&quot;DTHIN&quot;);
     *
     * makeReceivedInfo.setProcessFlowName(&quot;RPF_03&quot;);
     * makeReceivedInfo.setProcessFlowVersion(&quot;00001&quot;);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeReceived1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeReceived2&quot;);
     * makeReceivedInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeReceived&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeReceived(productKey, eventInfo, makeReceivedInfo);
     * </pre>
     */
    public Product makeReceived(ProductKey productKey, EventInfo eventInfo, MakeReceivedInfo makeReceivedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Make a product consumed.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeConsumedInfo
     *            A makeConsumed specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeConsumedInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     * String consumeLotName = args.get(ItemName.consumerLotName);
     * String consumeProductName = args.get(ItemName.consumerProductName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeConsumedInfo makeConsumedInfo = new MakeConsumedInfo();
     * makeConsumedInfo.setConsumerLotName(consumeLotName);
     * makeConsumedInfo.setConsumerProductName(consumeProductName);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeConsumed1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeConsumed2&quot;);
     * makeConsumedInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeConsumed&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeConsumed(productKey, eventInfo, makeConsumedInfo);
     * </pre>
     */
    public Product makeConsumed(ProductKey productKey, EventInfo eventInfo, MakeConsumedInfo makeConsumedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * End processing or traveling.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeIdleInfo
     *            A makeIdle specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeIdleInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     * String machineName = args.get(ItemName.machineName);
     * String machineRecipeName = args.get(ItemName.machineRecipeName);
     * String reworkFlag = args.get(ItemName.reworkFlag);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * Product product = ProductServiceProxy.getProductService().selectByKey(productKey);
     *
     * MakeIdleInfo makeIdleInfo = new MakeIdleInfo();
     *
     * makeIdleInfo.setProcessFlowName(product.getProcessFlowName());
     * makeIdleInfo.setProcessFlowVersion(product.getProcessFlowVersion());
     *
     * makeIdleInfo.setReworkFlag(reworkFlag);
     *
     * makeIdleInfo.setCarrierName(product.getCarrierName());
     * makeIdleInfo.setPosition(product.getPosition());
     *
     * // makeIdleInfo.setAreaName(product.getAreaName());
     * makeIdleInfo.setAreaName(&quot;THIN&quot;);
     *
     * Map&lt;String, Object&gt; bindSet = new HashMap&lt;String, Object&gt;();
     * bindSet.put(&quot;factoryName&quot;, product.getFactoryName());
     * bindSet.put(&quot;processFlowName&quot;, product.getProcessFlowName());
     * bindSet.put(&quot;processFlowVersion&quot;, product.getProcessFlowVersion());
     * bindSet.put(&quot;processOperationName&quot;, product.getProcessOperationName());
     * bindSet.put(&quot;processOperationVersion&quot;, product.getProcessOperationVersion());
     * bindSet.put(&quot;productSpecName&quot;, product.getProductSpecName());
     * bindSet.put(&quot;productSpecVersion&quot;, product.getProductSpecVersion());
     *
     * if (StringUtils.isNotEmpty(machineName))
     * {
     * 	makeIdleInfo.setMachineName(machineName);
     * }
     * else
     * {
     * 	try
     * 	{
     * 		List&lt;PosPolicyData&gt; posMachine =
     * 				GenericServiceProxy.getPosPolicyMap().getPosDataByPolicyName(&quot;Machine&quot;, bindSet);
     * 		makeIdleInfo.setMachineName((String) posMachine.get(0).getPosPolicyData().get(0).get(&quot;machineName&quot;));
     * 	} catch (NotFoundSignal e)
     * 	{}
     * }
     *
     * if (StringUtils.isNotEmpty(machineRecipeName))
     * {
     * 	makeIdleInfo.setMachineRecipeName(machineRecipeName);
     * }
     * else
     * {
     * 	try
     * 	{
     * 		List&lt;PosPolicyData&gt; posMachineRecipe =
     * 				GenericServiceProxy.getPosPolicyMap().getPosDataByPolicyName(&quot;MachineRecipe&quot;, bindSet);
     * 		makeIdleInfo.setMachineRecipeName((String) posMachineRecipe.get(0).getPosPolicyData().get(0).get(
     * 			&quot;machineRecipeName&quot;));
     * 	} catch (NotFoundSignal e)
     * 	{}
     * }
     *
     * makeIdleInfo.setProductGrade(product.getProductGrade());
     * makeIdleInfo.setSubProductGrades1(product.getSubProductGrades1());
     * makeIdleInfo.setSubProductQuantity1(product.getSubProductQuantity1());
     *
     * List&lt;ConsumedMaterial&gt; cms = new ArrayList&lt;ConsumedMaterial&gt;();
     * cms.add(new ConsumedMaterial(&quot;Consumable&quot;, &quot;Gas_Ehwoo_01&quot;, 10));
     * cms.add(new ConsumedMaterial(&quot;Durable&quot;, &quot;Ret_Ehwoo_01&quot;, 1));
     *
     * makeIdleInfo.setCms(cms);
     *
     * // makeIdleInfo.setCompleteFlag(&quot;N&quot;);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;AA&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeIdle2&quot;);
     * makeIdleInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeIdle&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeIdle(productKey, eventInfo, makeIdleInfo);
     * </pre>
     */
    public Product makeIdle(ProductKey productKey, EventInfo eventInfo, MakeIdleInfo makeIdleInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Start processing.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeProcessingInfo
     *            A makeProcessing specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeProcessingInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     * String machineName = args.get(ItemName.machineName);
     * String machineRecipeName = args.get(ItemName.machineRecipeName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeProcessingInfo makeProcessingInfo = new MakeProcessingInfo();
     * makeProcessingInfo.setAreaName(&quot;THIN&quot;);
     *
     * Product product = ProductServiceProxy.getProductService().selectByKey(productKey);
     *
     * Map&lt;String, Object&gt; bindSet = new HashMap&lt;String, Object&gt;();
     * bindSet.put(&quot;factoryName&quot;, product.getFactoryName());
     * bindSet.put(&quot;processFlowName&quot;, product.getProcessFlowName());
     * bindSet.put(&quot;processFlowVersion&quot;, product.getProcessFlowVersion());
     * bindSet.put(&quot;processOperationName&quot;, product.getProcessOperationName());
     * bindSet.put(&quot;processOperationVersion&quot;, product.getProcessOperationVersion());
     * bindSet.put(&quot;productSpecName&quot;, product.getProductSpecName());
     * bindSet.put(&quot;productSpecVersion&quot;, product.getProductSpecVersion());
     *
     * if (StringUtils.isNotEmpty(machineName))
     * {
     * 	makeProcessingInfo.setMachineName(machineName);
     * }
     * else
     * {
     * 	try
     * 	{
     * 		List&lt;PosPolicyData&gt; posMachine =
     * 				GenericServiceProxy.getPosPolicyMap().getPosDataByPolicyName(&quot;Machine&quot;, bindSet);
     * 		makeProcessingInfo.setMachineName((String) posMachine.get(0).getPosPolicyData().get(0).get(&quot;machineName&quot;));
     * 	} catch (NotFoundSignal e)
     * 	{}
     * }
     *
     * if (StringUtils.isNotEmpty(machineRecipeName))
     * {
     * 	makeProcessingInfo.setMachineRecipeName(machineRecipeName);
     * }
     * else
     * {
     * 	try
     * 	{
     * 		List&lt;PosPolicyData&gt; posMachineRecipe =
     * 				GenericServiceProxy.getPosPolicyMap().getPosDataByPolicyName(&quot;MachineRecipe&quot;, bindSet);
     * 		makeProcessingInfo.setMachineRecipeName((String) posMachineRecipe.get(0).getPosPolicyData().get(0).get(
     * 			&quot;machineRecipeName&quot;));
     * 	} catch (NotFoundSignal e)
     * 	{}
     * }
     *
     * List&lt;ConsumedMaterial&gt; cms = new ArrayList&lt;ConsumedMaterial&gt;();
     * cms.add(new ConsumedMaterial(&quot;Consumable&quot;, &quot;Gas_Ehwoo_01&quot;, 10));
     * cms.add(new ConsumedMaterial(&quot;Durable&quot;, &quot;Ret_Ehwoo_01&quot;, 1));
     *
     * makeProcessingInfo.setCms(cms);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;AA&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeProcessing2&quot;);
     * makeProcessingInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeProcessing&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeProcessing(productKey, eventInfo, makeProcessingInfo);
     * </pre>
     */
    public Product makeProcessing(ProductKey productKey, EventInfo eventInfo, MakeProcessingInfo makeProcessingInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Start traveling.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeTravelingInfo
     *            A makeTraveling specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeTravelingInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     * String areaName = args.get(ItemName.areaName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeTravelingInfo makeTravelingInfo = new MakeTravelingInfo();
     * makeTravelingInfo.setAreaName(areaName);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeTraveling1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeTraveling2&quot;);
     * makeTravelingInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeTraveling&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeTraveling(productKey, eventInfo, makeTravelingInfo);
     * </pre>
     */
    public Product makeTraveling(ProductKey productKey, EventInfo eventInfo, MakeTravelingInfo makeTravelingInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Make a product not on hold.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeNotOnHoldInfo
     *            A makeNotOnHold specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeNotOnHoldInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeNotOnHoldInfo makeNotOnHoldInfo = new MakeNotOnHoldInfo();
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeNotOnHold1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeNotOnHold2&quot;);
     * makeNotOnHoldInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeNotOnHold&quot;);
     * eventInfo.setReasonCode(&quot;Reason1&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeNotOnHold(productKey, eventInfo, makeNotOnHoldInfo);
     * </pre>
     */
    public Product makeNotOnHold(ProductKey productKey, EventInfo eventInfo, MakeNotOnHoldInfo makeNotOnHoldInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Make a product on hold.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeOnHoldInfo
     *            A makeInRework specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeOnHoldInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeOnHoldInfo makeOnHoldInfo = new MakeOnHoldInfo();
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeOnHold1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeOnHold2&quot;);
     * makeOnHoldInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeOnHold&quot;);
     * eventInfo.setReasonCode(&quot;Reason1&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeOnHold(productKey, eventInfo, makeOnHoldInfo);
     * </pre>
     */
    public Product makeOnHold(ProductKey productKey, EventInfo eventInfo, MakeOnHoldInfo makeOnHoldInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Start a rework.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. An event generic information.
     *            kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeInReworkInfo
     *            A makeInRework specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeInReworkInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     * String processOperation = args.get(ItemName.processOperationName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeInReworkInfo makeInReworkInfo = new MakeInReworkInfo();
     *
     * Product product = ProductServiceProxy.getProductService().selectByKey(productKey);
     *
     * makeInReworkInfo.setAreaName(product.getAreaName());
     * makeInReworkInfo.setProcessFlowName(product.getProcessFlowName());
     * makeInReworkInfo.setProcessFlowVersion(product.getProcessFlowVersion());
     *
     * // makeInReworkInfo.setProcessOperationName(processOperation);
     *
     * if (StringUtils.isNotEmpty(processOperation))
     * {
     * 	String SELECT_NODE = &quot; WHERE nodeattribute1=? and factoryname=? and processflowname=? and processflowversion=?&quot;;
     * 	Object[] bindSet =
     * 			new Object[] {
     * 					processOperation,
     * 					product.getFactoryName(),
     * 					product.getProcessFlowName(),
     * 					product.getProcessFlowVersion() };
     *
     * 	List&lt;Node&gt; nodeList = ProcessFlowServiceProxy.getNodeService().select(SELECT_NODE, bindSet);
     * 	makeInReworkInfo.setNodeStack(nodeList.get(0).getKey().getNodeId());
     * }
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeInRework1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeInRework2&quot;);
     * makeInReworkInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeInRework&quot;);
     * eventInfo.setReasonCode(&quot;Reason2&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeInRework(productKey, eventInfo, makeInReworkInfo);
     * </pre>
     */
    public Product makeInRework(ProductKey productKey, EventInfo eventInfo, MakeInReworkInfo makeInReworkInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * End a rework.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param makeNotInReworkInfo
     *            A makeNotInRework specific information.
     *            kr.co.aim.nanotrack.product.management.info.MakeNotInReworkInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     * String processOperation = args.get(ItemName.processOperationName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * MakeNotInReworkInfo makeNotInReworkInfo = new MakeNotInReworkInfo();
     *
     * Product product = ProductServiceProxy.getProductService().selectByKey(productKey);
     * makeNotInReworkInfo.setAreaName(product.getAreaName());
     * makeNotInReworkInfo.setProcessFlowName(product.getProcessFlowName());
     * makeNotInReworkInfo.setProcessFlowVersion(product.getProcessFlowVersion());
     *
     * if (StringUtils.isNotEmpty(processOperation))
     * {
     * 	String SELECT_NODE = &quot; WHERE nodeattribute1=? and factoryname=? and processflowname=? and processflowversion=?&quot;;
     * 	Object[] bindSet =
     * 			new Object[] {
     * 					processOperation,
     * 					product.getFactoryName(),
     * 					product.getProcessFlowName(),
     * 					product.getProcessFlowVersion() };
     *
     * 	List&lt;Node&gt; nodeList = ProcessFlowServiceProxy.getNodeService().select(SELECT_NODE, bindSet);
     * 	makeNotInReworkInfo.setNodeStack(nodeList.get(0).getKey().getNodeId());
     * }
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;makeNotInRework1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;makeNotInRework2&quot;);
     * makeNotInReworkInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;MakeNotInRework&quot;);
     * eventInfo.setReasonCode(&quot;Reason2&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().makeNotInRework(productKey, eventInfo, makeNotInReworkInfo);
     * </pre>
     */
    public Product makeNotInRework(ProductKey productKey, EventInfo eventInfo, MakeNotInReworkInfo makeNotInReworkInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Assign a carrier to a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param assignCarrierInfo
     *            A assignCarrier specific information.
     *            kr.co.aim.nanotrack.product.management.info.AssignCarrierInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;AssignCarrier&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventTimeKey(TimeUtils.getCurrentEventTimeKey());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * AssignCarrierInfo assignCarrierInfo = new AssignCarrierInfo();
     * assignCarrierInfo.setCarrierName(args.get(ItemName.carrierName));
     * assignCarrierInfo.setPosition(Long.parseLong(args.get(ItemName.position)));
     *
     * try
     * {
     * 	Product product = ProductServiceProxy.getProductService().assignCarrier(key, eventInfo, assignCarrierInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product assignCarrier(ProductKey productKey, EventInfo eventInfo, AssignCarrierInfo assignCarrierInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Deassign a carrier from a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param deassignCarrierInfo
     *            A deassignCarrier specific information.
     *            kr.co.aim.nanotrack.product.management.info.DeassignCarrierInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;DeassignCarrier&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventTimeKey(TimeUtils.getCurrentEventTimeKey());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * DeassignCarrierInfo deassignCarrierInfo = new DeassignCarrierInfo();
     *
     * try
     * {
     * 	Product product = ProductServiceProxy.getProductService().deassignCarrier(key, eventInfo, deassignCarrierInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product deassignCarrier(ProductKey productKey, EventInfo eventInfo, DeassignCarrierInfo deassignCarrierInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Assign a lot to a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param assignLotInfo
     *            A assignLot specific information.
     *            kr.co.aim.nanotrack.product.management.info.AssignLotInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;AssignLot&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * AssignLotInfo assignLotInfo = new AssignLotInfo();
     * assignLotInfo.setLotName(args.get(ItemName.lotName));
     *
     * try
     * {
     * 	Product product = ProductServiceProxy.getProductService().assignLot(key, eventInfo, assignLotInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product assignLot(ProductKey productKey, EventInfo eventInfo, AssignLotInfo assignLotInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Deassign a lot from a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param deassignLotInfo
     *            A deassignLot specific information.
     *            kr.co.aim.nanotrack.product.management.info.DeassignLotInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;DeassignLot&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * DeassignLotInfo deassignLotInfo = new DeassignLotInfo();
     *
     * try
     * {
     * 	Product product = ProductServiceProxy.getProductService().deassignLot(key, eventInfo, deassignLotInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product deassignLot(ProductKey productKey, EventInfo eventInfo, DeassignLotInfo deassignLotInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Assign a lot and a carrier to a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param assignLotAndCarrierInfo
     *            A assignLotAndCarrier specific information.
     *            kr.co.aim.nanotrack.product.management.info.AssignLotAndCarrierInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;assignLotAndCarrier&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * AssignLotAndCarrierInfo assignLotAndCarrierInfo = new AssignLotAndCarrierInfo();
     * assignLotAndCarrierInfo.setLotName(args.get(ItemName.lotName));
     * assignLotAndCarrierInfo.setCarrierName(args.get(ItemName.carrierName));
     * assignLotAndCarrierInfo.setPosition(Long.parseLong(args.get(ItemName.position)));
     * assignLotAndCarrierInfo.setProductProcessState(args.get(ItemName.productProcessState));
     * assignLotAndCarrierInfo.setGradeFlag(&quot;&quot;);
     *
     * try
     * {
     * 	Product product =
     * 			ProductServiceProxy.getProductService().assignLotAndCarrier(key, eventInfo, assignLotAndCarrierInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product assignLotAndCarrier(ProductKey productKey, EventInfo eventInfo,
                                       AssignLotAndCarrierInfo assignLotAndCarrierInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Deassign a lot and a carrier from a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param deassignLotAndCarrierInfo
     *            A deassignLotAndCarrier specific information.
     *            kr.co.aim.nanotrack.product.management.info.DeassignLotAndCarrierInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;DeassignLotAndCarrier&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * DeassignLotAndCarrierInfo deassignLotAndCarrierInfo = new DeassignLotAndCarrierInfo();
     *
     * try
     * {
     * 	Product product =
     * 			ProductServiceProxy.getProductService().deassignLotAndCarrier(key, eventInfo, deassignLotAndCarrierInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product deassignLotAndCarrier(ProductKey productKey, EventInfo eventInfo,
                                         DeassignLotAndCarrierInfo deassignLotAndCarrierInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Assign a process group to a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param assignProcessGroupInfo
     *            A assignProcessGroup specific information.
     *            kr.co.aim.nanotrack.product.management.info.AssignProcessGroupInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;AssignProcessGroup&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * AssignProcessGroupInfo assignProcessGroupInfo = new AssignProcessGroupInfo();
     * assignProcessGroupInfo.setProcessGroupName(args.get(ItemName.processGroupName));
     *
     * try
     * {
     * 	Product product =
     * 			ProductServiceProxy.getProductService().assignProcessGroup(key, eventInfo, assignProcessGroupInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product assignProcessGroup(ProductKey productKey, EventInfo eventInfo,
                                      AssignProcessGroupInfo assignProcessGroupInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Deassign a process group from a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param deassignProcessGroupInfo
     *            A deassignProcessGroup specific information.
     *            kr.co.aim.nanotrack.product.management.info.DeassignProcessGroupInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;DeassignProcessGroup&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * DeassignProcessGroupInfo deassignProcessGroupInfo = new DeassignProcessGroupInfo();
     *
     * try
     * {
     * 	Product product =
     * 			ProductServiceProxy.getProductService().deassignProcessGroup(key, eventInfo, deassignProcessGroupInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product deassignProcessGroup(ProductKey productKey, EventInfo eventInfo,
                                        DeassignProcessGroupInfo deassignProcessGroupInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Assign a transport group to a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param assignTransportGroupInfo
     *            A assignTransportGroup specific information.
     *            kr.co.aim.nanotrack.product.management.info.AssignTransportGroupInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;AssignTransportGroup&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * AssignTransportGroupInfo assignTransportGroupInfo = new AssignTransportGroupInfo();
     * assignTransportGroupInfo.setTransportGroupName(args.get(ItemName.transportGroupName));
     *
     * try
     * {
     * 	Product product =
     * 			ProductServiceProxy.getProductService().assignTransportGroup(key, eventInfo, assignTransportGroupInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product assignTransportGroup(ProductKey productKey, EventInfo eventInfo,
                                        AssignTransportGroupInfo assignTransportGroupInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Deassign a transport group from a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param deassignTransportGroupInfo
     *            A deassignTransportGroup specific information.
     *            kr.co.aim.nanotrack.product.management.info.DeassignTransportGroupInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;DeassignTransportGroup&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * DeassignTransportGroupInfo deassignTransportGroupInfo = new DeassignTransportGroupInfo();
     *
     * try
     * {
     * 	Product product =
     * 			ProductServiceProxy.getProductService().deassignTransportGroup(key, eventInfo, deassignTransportGroupInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product deassignTransportGroup(ProductKey productKey, EventInfo eventInfo,
                                          DeassignTransportGroupInfo deassignTransportGroupInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Change all specification information.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param changeSpecInfo
     *            A changeSpec specific information.
     *            kr.co.aim.nanotrack.product.management.info.ChangeSpecInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;ChangeSpec&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * ChangeSpecInfo changeSpecInfo = new ChangeSpecInfo();
     * changeSpecInfo.setFactoryName(args.get(ItemName.factoryName));
     * changeSpecInfo.setAreaName(args.get(ItemName.areaName));
     * changeSpecInfo.setPriority(Long.parseLong(args.get(ItemName.priority)));
     * changeSpecInfo.setDueDate(TimeUtils.getTimestamp(args.get(ItemName.dueDate)));
     * changeSpecInfo.setPosition(Long.parseLong(args.get(ItemName.position)));
     * changeSpecInfo.setProductionType(args.get(ItemName.productionType));
     * changeSpecInfo.setProductSpecName(args.get(ItemName.productSpecName));
     * changeSpecInfo.setProductSpecVersion(args.get(ItemName.productSpecVersion));
     * changeSpecInfo.setProcessFlowName(args.get(ItemName.processFlowName));
     * changeSpecInfo.setProcessFlowVersion(args.get(ItemName.processFlowVersion));
     * changeSpecInfo.setProcessOperationName(args.get(ItemName.processOperationName));
     * changeSpecInfo.setProcessOperationVersion(args.get(ItemName.processOperationVersion));
     * changeSpecInfo.setProductState(args.get(ItemName.productState));
     * changeSpecInfo.setProductProcessState(args.get(ItemName.productProcessState));
     * changeSpecInfo.setProductHoldState(args.get(ItemName.productHoldState));
     * changeSpecInfo.setNodeStack(args.get(ItemName.nodeStack));
     *
     * try
     * {
     * 	Product product = ProductServiceProxy.getProductService().changeSpec(key, eventInfo, changeSpecInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product changeSpec(ProductKey productKey, EventInfo eventInfo, ChangeSpecInfo changeSpecInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Change a product grade and sub product grades.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param changeGradeInfo
     *            A changeGrade specific information.
     *            kr.co.aim.nanotrack.product.management.info.ChangeGradeInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;ChangeGrade&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * ChangeGradeInfo changeGradeInfo = new ChangeGradeInfo();
     * changeGradeInfo.setProductGrade(args.get(ItemName.productGrade));
     * changeGradeInfo.setProductProcessState(args.get(ItemName.productProcessState));
     * changeGradeInfo.setPosition(Long.parseLong(args.get(ItemName.position)));
     * changeGradeInfo.setSubProductGrades1(args.get(ItemName.subProductGrades1));
     * changeGradeInfo.setSubProductQuantity1(Long.parseLong(args.get(ItemName.subProductQuantity1)));
     *
     * try
     * {
     * 	Product product = ProductServiceProxy.getProductService().changeGrade(key, eventInfo, changeGradeInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product changeGrade(ProductKey productKey, EventInfo eventInfo, ChangeGradeInfo changeGradeInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Create a new product based on a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param recreateInfo
     *            A recreate specific information.
     *            kr.co.aim.nanotrack.product.management.info.RecreateInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     * String newProductName = args.get(ItemName.newProductName);
     *
     * remove(newProductName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * Product product = ProductServiceProxy.getProductService().selectByKey(productKey);
     *
     * RecreateInfo recreateInfo = new RecreateInfo();
     * recreateInfo.setNewProductName(newProductName);
     *
     * recreateInfo.setProductionType(product.getProductionType());
     * recreateInfo.setProductSpecName(product.getProductSpecName());
     * recreateInfo.setProductSpecVersion(product.getProductSpecVersion());
     * recreateInfo.setProductSpec2Name(product.getProductSpec2Name());
     * recreateInfo.setProductSpec2Version(product.getProductSpec2Version());
     *
     * recreateInfo.setProductRequestName(product.getProductRequestName());
     *
     * recreateInfo.setLotName(product.getLotName());
     * recreateInfo.setCarrierName(product.getCarrierName());
     * recreateInfo.setPosition(product.getPosition());
     *
     * recreateInfo.setProductType(product.getProductType());
     * recreateInfo.setSubProductType(product.getSubProductType());
     *
     * recreateInfo.setSubProductUnitQuantity1(product.getSubProductUnitQuantity1());
     * recreateInfo.setSubProductUnitQuantity2(product.getSubProductUnitQuantity2());
     *
     * recreateInfo.setProductGrade(product.getProductGrade());
     * recreateInfo.setSubProductGrades1(product.getSubProductGrades1());
     * recreateInfo.setSubProductGrades2(product.getSubProductGrades2());
     * recreateInfo.setSubProductQuantity1(product.getSubProductQuantity());
     * recreateInfo.setSubProductQuantity2(product.getSubProductQuantity2());
     *
     * recreateInfo.setDueDate(product.getDueDate());
     * recreateInfo.setPriority(product.getPriority());
     *
     * // recreateInfo.setAreaName( product.getAreaName() );
     *
     * recreateInfo.setProcessFlowName(product.getProcessFlowName());
     * recreateInfo.setProcessFlowVersion(product.getProcessFlowVersion());
     * recreateInfo.setProcessOperationName(product.getProcessOperationName());
     * recreateInfo.setProcessOperationVersion(product.getProcessOperationVersion());
     * recreateInfo.setNodeStack(product.getNodeStack());
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;recreate1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;recreate2&quot;);
     * recreateInfo.setUdfs(udfs);
     *
     * Map&lt;String, String&gt; newUdfs = new HashMap&lt;String, String&gt;();
     * newUdfs.put(&quot;UDF01&quot;, &quot;recreate3&quot;);
     * newUdfs.put(&quot;UDF02&quot;, &quot;recreate4&quot;);
     * recreateInfo.setNewUdfs(newUdfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;Recreate&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().recreate(productKey, eventInfo, recreateInfo);
     * </pre>
     */
    public Product recreate(ProductKey productKey, EventInfo eventInfo, RecreateInfo recreateInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Separate a product to sub products.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param separateInfo
     *            A separate specific information.
     *            kr.co.aim.nanotrack.product.management.info.SeparateInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;Separate&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * SeparateInfo separateInfo = new SeparateInfo();
     *
     * List&lt;ProductPGQS&gt; pgqsList = new ArrayList&lt;ProductPGQS&gt;();
     *
     * for (int i = 1; i &lt;= 10; i++)
     * {
     * 	ProductPGQS pgqs = new ProductPGQS();
     * 	pgqs.setPosition(Long.parseLong(args.get(ItemName.position)));
     * 	pgqs.setProductName(args.get(ItemName.newProductName) + &quot;-0&quot; + i);
     * 	pgqs.setProductGrade(args.get(ItemName.productGrade));
     * 	pgqs.setSubProductGrades1(args.get(ItemName.subProductGrades1));
     * 	pgqs.setSubProductGrades2(&quot;&quot;);
     * 	pgqs.setSubProductQuantity1(Long.parseLong(args.get(ItemName.subProductQuantity1)));
     * 	pgqs.setSubProductQuantity2(0);
     * 	pgqs.setSubProductUnitQuantity1(Long.parseLong(args.get(ItemName.subProductUnitQuantity1)));
     * 	pgqs.setSubProductUnitQuantity2(0);
     *
     * 	pgqsList.add(pgqs);
     * }
     *
     * separateInfo.setSubProductPGQSSequence(pgqsList);
     *
     * try
     * {
     * 	Product product = (Product) ProductServiceProxy.getProductService().separate(key, eventInfo, separateInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product separate(ProductKey productKey, EventInfo eventInfo, SeparateInfo separateInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Consume materials for a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param consumeMaterialsInfo
     *            A consumeMaterials specific information.
     *            kr.co.aim.nanotrack.product.management.info.ConsumeMaterialsInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * String productName = args.get(ItemName.productName);
     *
     * // 1. productKey
     * ProductKey productKey = new ProductKey();
     * productKey.setProductName(productName);
     *
     * // 2. transitionInfo
     * ConsumeMaterialsInfo consumeMaterialsInfo = new ConsumeMaterialsInfo();
     * consumeMaterialsInfo.setProductGrade(&quot;G&quot;);
     * consumeMaterialsInfo.setSubProductQuantity1(10);
     * consumeMaterialsInfo.setSubProductGrades1(&quot;GGGGGGGGGG&quot;);
     *
     * List&lt;ConsumedMaterial&gt; cms = new ArrayList&lt;ConsumedMaterial&gt;();
     * cms.add(new ConsumedMaterial(&quot;Consumable&quot;, &quot;Gas_Ehwoo_01&quot;, 5));
     * cms.add(new ConsumedMaterial(&quot;Durable&quot;, &quot;Ret_Ehwoo_01&quot;, 1));
     *
     * consumeMaterialsInfo.setCms(cms);
     *
     * Map&lt;String, String&gt; udfs = new HashMap&lt;String, String&gt;();
     * udfs.put(&quot;UDF01&quot;, &quot;consumeMaterials1&quot;);
     * udfs.put(&quot;UDF02&quot;, &quot;consumeMaterials2&quot;);
     * consumeMaterialsInfo.setUdfs(udfs);
     *
     * // 3. eventInfo
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;ConsumeMaterials&quot;);
     * eventInfo.setEventUser(&quot;ehwoo&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventComment(&quot;This is test.&quot;);
     *
     * ProductServiceProxy.getProductService().consumeMaterials(productKey, eventInfo, consumeMaterialsInfo);
     * </pre>
     */
    public Product consumeMaterials(ProductKey productKey, EventInfo eventInfo,
                                    ConsumeMaterialsInfo consumeMaterialsInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Set an area of a product.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param setAreaInfo
     *            A setArea specific information.
     *            kr.co.aim.nanotrack.product.management.info.SetAreaInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;SetArea&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * SetAreaInfo setAreaInfo = new SetAreaInfo();
     * setAreaInfo.setAreaName(args.get(ItemName.areaName));
     *
     * try
     * {
     * 	Product product = ProductServiceProxy.getProductService().setArea(key, eventInfo, setAreaInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product setArea(ProductKey productKey, EventInfo eventInfo, SetAreaInfo setAreaInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;

    /**
     * Set an event only.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param setEventInfo
     *            A setEvent specific information.
     *            kr.co.aim.nanotrack.product.management.info.SetEventInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws InvalidStateTransitionSignal
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     *
     * @see <pre>
     * ProductKey key = new ProductKey();
     * key.setProductName(args.get(ItemName.productName));
     *
     * EventInfo eventInfo = new EventInfo();
     * eventInfo.setEventName(&quot;SetEvent&quot;);
     * eventInfo.setEventComment(&quot;Test&quot;);
     * eventInfo.setEventTime(TimeUtils.getCurrentTimestamp());
     * eventInfo.setEventUser(&quot;ytjung&quot;);
     *
     * SetEventInfo setEventInfo = new SetEventInfo();
     *
     * try
     * {
     * 	Product product = ProductServiceProxy.getProductService().setEvent(key, eventInfo, setEventInfo);
     * 	System.out.println(product.getKey().getProductName());
     * } catch (Exception ex)
     * {
     * 	ex.printStackTrace();
     * }
     * </pre>
     */
    public Product setEvent(ProductKey productKey, EventInfo eventInfo, SetEventInfo setEventInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;


    /**
     * Undo a previous operation.
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @param eventInfo
     *            An event generic information. kr.co.aim.nanotrack.generic.info.EventInfo
     * @param undoInfo
     *            UndoInfo A undo specific information.
     *            kr.co.aim.nanotrack.product.management.info.UndoInfo
     * @return Product kr.co.aim.nanotrack.product.management.data.Product
     * @throws FrameworkErrorSignal
     * @throws InvalidStateTransitionSignal
     * @throws DuplicateNameSignal
     * @throws NotFoundSignal
     */
    public UndoTimeKeys undo(ProductKey productKey, EventInfo eventInfo, UndoInfo undoInfo)
            throws FrameworkErrorSignal, InvalidStateTransitionSignal, DuplicateNameSignal, NotFoundSignal;

    public Product makeBranchRecovery(ProductKey productKey, EventInfo eventInfo,
                                      MakeBranchRecoveryInfo makeBranchRecoveryInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal;


    /**
     *
     * @param productKey
     *            kr.co.aim.nanotrack.product.management.data.ProductKey
     * @return N/A
     *
     * @see <pre>
     * try
     * {
     * 	ProductKey key = new ProductKey();
     * 	key.setProductName(productName);
     *
     * 	ProductServiceProxy.getProductService().selectByKey(key);
     *
     * 	ProductServiceProxy.getProductService().remove(key);
     * } catch (NotFoundSignal e)
     * {}
     * </pre>
     */
    public void remove(ProductKey productKey);

}
