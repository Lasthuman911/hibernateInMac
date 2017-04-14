package kr.co.aim.nanotrack.product.management.impl;

import java.util.List;

import kr.co.aim.nanotrack.generic.GenericServiceProxy;
import kr.co.aim.nanotrack.generic.common.CommonMaterialAttributeServiceDAO;
import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.ExceptionNotify;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.InvalidStateTransitionSignal;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;
import kr.co.aim.nanotrack.generic.info.EventInfo;
import kr.co.aim.nanotrack.generic.info.SetMaterialLocationInfo;
import kr.co.aim.nanotrack.generic.info.UndoTimeKeys;
import kr.co.aim.nanotrack.product.management.ProductService;
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
 * DAO接口的真实实现类
 */
public class ProductServiceImpl extends CommonMaterialAttributeServiceDAO<ProductKey, Product>
        implements ProductService
{
    public List<Product> allProductsByLot(String lotName) throws FrameworkErrorSignal, NotFoundSignal
    {
        String condition = " WHERE lotName = ? ORDER BY position";

        Object bindSet[] = new Object[1];
        bindSet[0] = lotName;

        return super.select(condition, bindSet);//加上super比较直观点
    }

    public List<Product> allUnScrappedProductsByLot(String lotName) throws FrameworkErrorSignal, NotFoundSignal
    {
        String condition =
                " WHERE lotName = ? " + " AND productState != ?" + " AND productState != ?" + " ORDER BY position";

        Object bindSet[] = new Object[3];
        bindSet[0] = lotName;
        bindSet[1] = GenericServiceProxy.getConstantMap().Prod_Scrapped;
        bindSet[2] = GenericServiceProxy.getConstantMap().Prod_Consumed;

        return select(condition, bindSet);
    }

    public List<Product> allProductsByCarrier(String carrierName) throws FrameworkErrorSignal, NotFoundSignal
    {
        String condition = " WHERE carrierName = ? ORDER BY position";

        Object bindSet[] = new Object[1];
        bindSet[0] = carrierName;

        return select(condition, bindSet);
    }

    public List<Product> allProductsByProcessGroup(String processGroupName) throws FrameworkErrorSignal, NotFoundSignal
    {
        String condition = " WHERE processGroupName = ? ORDER BY position";

        Object bindSet[] = new Object[1];
        bindSet[0] = processGroupName;

        return select(condition, bindSet);
    }

    public List<Product> allProductsByTransportGroup(String transportGroupName)
            throws FrameworkErrorSignal, NotFoundSignal
    {
        String condition = " WHERE transportGroupName = ? ORDER BY position";

        Object bindSet[] = new Object[1];
        bindSet[0] = transportGroupName;

        return select(condition, bindSet);
    }

    // Factory Interface
    @Deprecated
    public Product create(ProductKey productKey, EventInfo eventInfo, CreateInfo createInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal
    {
        if (productKey == null)
            productKey = new ProductKey();
        productKey.setProductName(createInfo.getProductName());
        return doWork(productKey, eventInfo, createInfo, "create");
    }

    @Override
    public Product create(EventInfo eventInfo, CreateInfo createInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal
    {
        return doWork(new ProductKey(createInfo.getProductName()), eventInfo, createInfo, "create");
    }

    @Deprecated
    public Product createRaw(ProductKey productKey, EventInfo eventInfo, CreateRawInfo createRawInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal
    {
        if (productKey == null)
            productKey = new ProductKey();
        productKey.setProductName(createRawInfo.getProductName());
        return doWork(productKey, eventInfo, createRawInfo, "createRaw");
    }

    @Override
    public Product createRaw(EventInfo eventInfo, CreateRawInfo createRawInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal
    {
        return doWork(new ProductKey(createRawInfo.getProductName()), eventInfo, createRawInfo, "createRaw");
    }

    @Deprecated
    public Product createWithLot(ProductKey productKey, EventInfo eventInfo, CreateWithLotInfo createWithLotInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal
    {
        if (productKey == null)
            productKey = new ProductKey();
        productKey.setProductName(createWithLotInfo.getProductName());

        return (Product) doWork(productKey, eventInfo, createWithLotInfo, "createWithLot");
    }

    @Override
    public Product createWithLot(EventInfo eventInfo, CreateWithLotInfo createWithLotInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal, InvalidStateTransitionSignal
    {
        return (Product) doWork(new ProductKey(createWithLotInfo.getProductName()), eventInfo, createWithLotInfo,
                "createWithLot");
    }

    // Product Interface
    public Product makeAllocated(ProductKey productKey, EventInfo eventInfo, MakeAllocatedInfo makeAllLocatedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeAllLocatedInfo, "makeAllocated");
    }

    public Product makeInProduction(ProductKey productKey, EventInfo eventInfo,
                                    MakeInProductionInfo makeInProductionInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeInProductionInfo, "makeInProduction");
    }

    public Product makeScrapped(ProductKey productKey, EventInfo eventInfo, MakeScrappedInfo makeScrappedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeScrappedInfo, "makeScrapped");
    }

    public Product makeUnScrapped(ProductKey productKey, EventInfo eventInfo, MakeUnScrappedInfo makeUnScrappedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeUnScrappedInfo, "makeUnScrapped");
    }

    public Product makeCompleted(ProductKey productKey, EventInfo eventInfo, MakeCompletedInfo makeCompletedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeCompletedInfo, "makeCompleted");
    }

    public Product makeShipped(ProductKey productKey, EventInfo eventInfo, MakeShippedInfo makeShippedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeShippedInfo, "makeShipped");
    }

    public Product makeUnShipped(ProductKey productKey, EventInfo eventInfo, MakeUnShippedInfo makeUnShippedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeUnShippedInfo, "makeUnShipped");
    }

    public Product makeReceived(ProductKey productKey, EventInfo eventInfo, MakeReceivedInfo makeReceivedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeReceivedInfo, "makeReceived");
    }

    public Product makeConsumed(ProductKey productKey, EventInfo eventInfo, MakeConsumedInfo makeConsumedInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeConsumedInfo, "makeConsumed");
    }

    public Product makeIdle(ProductKey productKey, EventInfo eventInfo, MakeIdleInfo makeIdleInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeIdleInfo, "makeIdle");
    }

    public Product makeProcessing(ProductKey productKey, EventInfo eventInfo, MakeProcessingInfo makeProcessingInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeProcessingInfo, "makeProcessing");
    }

    public Product makeTraveling(ProductKey productKey, EventInfo eventInfo, MakeTravelingInfo makeTravelingInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeTravelingInfo, "makeTraveling");
    }

    public Product makeNotOnHold(ProductKey productKey, EventInfo eventInfo, MakeNotOnHoldInfo makeNotOnHoldInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeNotOnHoldInfo, "makeNotOnHold");
    }

    public Product makeOnHold(ProductKey productKey, EventInfo eventInfo, MakeOnHoldInfo makeOnHoldInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeOnHoldInfo, "makeOnHold");
    }

    public Product makeInRework(ProductKey productKey, EventInfo eventInfo, MakeInReworkInfo makeInReworkInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeInReworkInfo, "makeInRework");
    }

    public Product makeNotInRework(ProductKey productKey, EventInfo eventInfo, MakeNotInReworkInfo makeNotInReworkInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeNotInReworkInfo, "makeNotInRework");
    }

    public Product assignCarrier(ProductKey productKey, EventInfo eventInfo, AssignCarrierInfo assignCarrierInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, assignCarrierInfo, "assignCarrier");
    }

    public Product deassignCarrier(ProductKey productKey, EventInfo eventInfo, DeassignCarrierInfo deassignCarrierInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, deassignCarrierInfo, "deassignCarrier");
    }

    public Product assignLot(ProductKey productKey, EventInfo eventInfo, AssignLotInfo assignLotInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, assignLotInfo, "assignLot");
    }

    public Product deassignLot(ProductKey productKey, EventInfo eventInfo, DeassignLotInfo deassignLotInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, deassignLotInfo, "deassignLot");
    }

    public Product assignLotAndCarrier(ProductKey productKey, EventInfo eventInfo,
                                       AssignLotAndCarrierInfo assignLotAndCarrierInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, assignLotAndCarrierInfo, "assignLotAndCarrier");
    }

    public Product deassignLotAndCarrier(ProductKey productKey, EventInfo eventInfo,
                                         DeassignLotAndCarrierInfo deassignLotAndCarrierInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, deassignLotAndCarrierInfo, "deassignLotAndCarrier");
    }

    public Product assignProcessGroup(ProductKey productKey, EventInfo eventInfo,
                                      AssignProcessGroupInfo assignProcessGroupInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, assignProcessGroupInfo, "assignProcessGroup");
    }

    public Product deassignProcessGroup(ProductKey productKey, EventInfo eventInfo,
                                        DeassignProcessGroupInfo deassignProcessGroupInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, deassignProcessGroupInfo, "deassignProcessGroup");
    }

    public Product assignTransportGroup(ProductKey productKey, EventInfo eventInfo,
                                        AssignTransportGroupInfo assignTransportGroupInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, assignTransportGroupInfo, "assignTransportGroup");
    }

    public Product deassignTransportGroup(ProductKey productKey, EventInfo eventInfo,
                                          DeassignTransportGroupInfo deassignTransportGroupInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, deassignTransportGroupInfo, "deassignTransportGroup");
    }

    public Product changeSpec(ProductKey productKey, EventInfo eventInfo, ChangeSpecInfo changeSpecInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, changeSpecInfo, "changeSpec");
    }

    public Product changeGrade(ProductKey productKey, EventInfo eventInfo, ChangeGradeInfo changeGradeInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, changeGradeInfo, "changeGrade");
    }

    public Product recreate(ProductKey productKey, EventInfo eventInfo, RecreateInfo recreateInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, recreateInfo, "recreate");
    }

    public Product separate(ProductKey productKey, EventInfo eventInfo, SeparateInfo separateInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, separateInfo, "separate");
    }

    public Product consumeMaterials(ProductKey productKey, EventInfo eventInfo,
                                    ConsumeMaterialsInfo consumeMaterialsInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, consumeMaterialsInfo, "consumeMaterials");
    }

    public Product setArea(ProductKey productKey, EventInfo eventInfo, SetAreaInfo setAreaInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, setAreaInfo, "setArea");
    }

    public Product setEvent(ProductKey productKey, EventInfo eventInfo, SetEventInfo setEventInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, setEventInfo, "setEvent");
    }

    public UndoTimeKeys undo(ProductKey productKey, EventInfo eventInfo, UndoInfo undoInfo)
            throws FrameworkErrorSignal, InvalidStateTransitionSignal, DuplicateNameSignal, NotFoundSignal
    {
        return doUndo(productKey, eventInfo, undoInfo);
    }

    public Product makeBranchRecovery(ProductKey productKey, EventInfo eventInfo,
                                      MakeBranchRecoveryInfo makeBranchRecoveryInfo)
            throws InvalidStateTransitionSignal, FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        return doWork(productKey, eventInfo, makeBranchRecoveryInfo, "makeBranchRecovery");
    }

    @Override
    public Product setMaterialLocation(ProductKey productKey, EventInfo eventInfo,
                                       SetMaterialLocationInfo setMaterialLocationInfo)
            throws NotFoundSignal, DuplicateNameSignal, FrameworkErrorSignal
    {
        return doWork(productKey, eventInfo, setMaterialLocationInfo, "setMaterialLocation");
    }

    // Removable Interface
    public void remove(ProductKey productKey)
    {
        GenericServiceProxy.getTxDataSourceManager().beginTransaction();

        try
        {
            // 1. Remove All Attributes
            try
            {
                removeAllAttributes(productKey);
            } catch (NotFoundSignal e)
            {}

            // 2. Call Base's Remove
            this.delete(productKey);

            GenericServiceProxy.getTxDataSourceManager().commitTransaction();
        } catch (Throwable e)
        {
            GenericServiceProxy.getTxDataSourceManager().rollbackTransaction();
            ExceptionNotify.notifyException(e);
        }
    }

}
