package kr.co.aim.nanotrack.product;

import kr.co.aim.nanoframe.util.bundle.BundleUtil;
import kr.co.aim.nanotrack.product.management.ProductAttributeHistoryService;
import kr.co.aim.nanotrack.product.management.ProductFutureActionService;
import kr.co.aim.nanotrack.product.management.ProductFutureConditionService;
import kr.co.aim.nanotrack.product.management.ProductHistoryService;
import kr.co.aim.nanotrack.product.management.ProductMultiHoldService;
import kr.co.aim.nanotrack.product.management.ProductService;
import kr.co.aim.nanotrack.product.management.ProductSpecAttributeHistoryService;
import kr.co.aim.nanotrack.product.management.ProductSpecHistoryService;
import kr.co.aim.nanotrack.product.management.ProductSpecPossiblePFService;
import kr.co.aim.nanotrack.product.management.ProductSpecService;
import kr.co.aim.nanotrack.product.management.policy.util.ProductHistoryDataAdaptor;
import kr.co.aim.nanotrack.product.management.policy.util.ProductProcessPolicyUtil;

/**
 * 代理工厂类，获取DAO的实例化对象-比如获取ProductService的实例化对象productServiceImpl
 */
public class ProductServiceProxy
{

    /*
     * Product
     */
    public static ProductService getProductService()
    {
        return (ProductService) BundleUtil.getBundleServiceClass(ProductService.class);
    }

    public static ProductHistoryService getProductHistoryService()
    {
        return (ProductHistoryService) BundleUtil.getBundleServiceClass(ProductHistoryService.class);
    }

    public static ProductAttributeHistoryService getProductAttributeHistoryService()
    {
        return (ProductAttributeHistoryService) BundleUtil.getBundleServiceClass(ProductAttributeHistoryService.class);
    }

    /*
     * Future
     */
    public static ProductFutureActionService getProductFutureActionService()
    {
        return (ProductFutureActionService) BundleUtil.getBundleServiceClass(ProductFutureActionService.class);
    }

    public static ProductFutureConditionService getProductFutureConditionService()
    {
        return (ProductFutureConditionService) BundleUtil.getBundleServiceClass(ProductFutureConditionService.class);
    }

    /*
     * MultiHold
     */
    public static ProductMultiHoldService getProductMultiHoldService()
    {
        return (ProductMultiHoldService) BundleUtil.getBundleServiceClass(ProductMultiHoldService.class);
    }

    /*
     * ProductSpec
     */
    public static ProductSpecService getProductSpecService()
    {
        return (ProductSpecService) BundleUtil.getBundleServiceClass(ProductSpecService.class);
    }

    public static ProductSpecHistoryService getProductSpecHistoryService()
    {
        return (ProductSpecHistoryService) BundleUtil.getBundleServiceClass(ProductSpecHistoryService.class);
    }

    public static ProductSpecAttributeHistoryService getProductSpecAttributeHistoryService()
    {
        return (ProductSpecAttributeHistoryService) BundleUtil.getBundleServiceClass(ProductSpecAttributeHistoryService.class);
    }

    public static ProductSpecPossiblePFService getProductSpecPossiblePFService()
    {
        return (ProductSpecPossiblePFService) BundleUtil.getBundleServiceClass(ProductSpecPossiblePFService.class);
    }

    /*
     * Adaptor
     */
    public static ProductHistoryDataAdaptor getProductHistoryDataAdaptor()
    {
        return (ProductHistoryDataAdaptor) BundleUtil.getBundleServiceClass(ProductHistoryDataAdaptor.class);
    }

    public static ProductProcessPolicyUtil getProductProcessPolicyUtil()
    {
        return (ProductProcessPolicyUtil) BundleUtil.getBundleServiceClass(ProductProcessPolicyUtil.class);
    }
}
