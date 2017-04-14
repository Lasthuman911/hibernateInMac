package kr.co.aim.messolution.lot.event;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;

import kr.co.aim.messolution.generic.errorHandler.CustomException;
import kr.co.aim.messolution.generic.eventHandler.AsyncHandler;
import kr.co.aim.messolution.generic.util.EventInfoUtil;
import kr.co.aim.messolution.generic.util.SMessageUtil;
import kr.co.aim.messolution.lot.MESLotServiceProxy;
import kr.co.aim.messolution.product.MESProductServiceProxy;
import kr.co.aim.nanotrack.lot.management.data.Lot;
import kr.co.aim.nanotrack.product.management.data.Product;
import kr.co.aim.nanotrack.generic.info.EventInfo;
import kr.co.aim.nanotrack.product.management.info.ext.ProductPGS;
import kr.co.aim.nanotrack.product.management.info.ext.ProductPGSRC;
import kr.co.aim.nanotrack.product.management.info.ext.ProductU;
import kr.co.aim.nanotrack.lot.management.info.ChangeGradeInfo;
import kr.co.aim.nanotrack.lot.management.info.MakeUnScrappedInfo;

public class GlassLineIn extends AsyncHandler {

    @Override
    public void doWorks(Document doc) throws CustomException
    {
        String machineName = SMessageUtil.getBodyItemValue(doc, "MACHINENAME", true);
        String productName = SMessageUtil.getBodyItemValue(doc, "PRODUCTNAME", true);
        String reasonCode  = SMessageUtil.getBodyItemValue(doc, "SCRAPCODE", false);

        EventInfo eventInfo = EventInfoUtil.makeEventInfo("UnScrap", getEventUser(), getEventComment(), "UNSCRAP", reasonCode);

        Product productData = MESProductServiceProxy.getProductServiceUtil().getProductData(productName);

        this.changeGrade(eventInfo, productData, "G");
    }

    private void changeGrade(EventInfo eventInfo, Product productData, String productGrade) throws CustomException
    {
        Lot lotData = MESLotServiceProxy.getLotInfoUtil().getLotData(productData.getLotName());

        List<ProductPGSRC> productPGSRCSequence = MESProductServiceProxy.getProductInfoUtil().getProductPGSRCSequence(lotData);
        List<ProductPGS> productPGSSequence = new ArrayList<ProductPGS>();

        for (ProductPGSRC productPGSRC : productPGSRCSequence)
        {
            if (productPGSRC.getProductName().equals(productData.getKey().getProductName()))
            {
                productPGSRC.setProductGrade(productGrade);
            }
        }

        String lotGrade = MESLotServiceProxy.getLotServiceUtil().decideLotJudge(lotData, "", productPGSRCSequence);

        ProductPGS productPGS = new ProductPGS();
        productPGS.setProductName(productData.getKey().getProductName());
        productPGS.setProductGrade(productGrade);
        productPGS.setPosition(productData.getPosition());
        productPGS.setSubProductQuantity1(productData.getSubProductQuantity1());

        productPGSSequence.add(productPGS);

        ChangeGradeInfo changeGradeInfo = MESLotServiceProxy.getLotInfoUtil().changeGradeInfo(lotData, lotGrade, productPGSSequence);

        eventInfo.setEventName("ChangeGrade");

        Lot result = MESLotServiceProxy.getLotServiceImpl().ChangeGrade(eventInfo, lotData, changeGradeInfo);
    }
}
