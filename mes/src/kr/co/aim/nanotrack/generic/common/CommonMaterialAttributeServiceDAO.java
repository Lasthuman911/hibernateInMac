package kr.co.aim.nanotrack.generic.common;

import java.util.Map;

import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;
import kr.co.aim.nanotrack.generic.info.CommonMaterialAttributeService;

public abstract class CommonMaterialAttributeServiceDAO<KEY extends KeyInfo, DATA extends DataInfo>
        extends CommonMaterialServiceDAO<KEY, DATA> implements CommonMaterialAttributeService<KEY, DATA>
{

    private CommonAttributeServiceDAO	commonAttributeServiceDAO;

    public CommonMaterialAttributeServiceDAO()
    {
        commonAttributeServiceDAO = new CommonAttributeServiceDAO();
    }

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @param attributeName
     *            String
     * @param attributeValue
     *            String
     * @return int
     * @throws DuplicateNameSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    public int addAttribute(KEY keyInfo, String attributeName, String attributeValue)
            throws DuplicateNameSignal, FrameworkErrorSignal
    {
        return this.commonAttributeServiceDAO.addAttribute(keyInfo, attributeName, attributeValue);
    }

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @return Map<String, String>
     * @throws NotFoundSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    public Map<String, String> getAllAttributes(KEY keyInfo) throws NotFoundSignal, FrameworkErrorSignal
    {
        return this.commonAttributeServiceDAO.getAllAttributes(keyInfo);
    }

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @param attributeName
     *            String
     * @return String
     * @throws NotFoundSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    public String getAttribute(KEY keyInfo, String attributeName) throws NotFoundSignal, FrameworkErrorSignal
    {
        return this.commonAttributeServiceDAO.getAttribute(keyInfo, attributeName);
    }

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @return int
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @test
     * @code
     * @endcode
     */
    public int removeAllAttributes(KEY keyInfo) throws FrameworkErrorSignal, NotFoundSignal
    {
        return this.commonAttributeServiceDAO.removeAllAttributes(keyInfo);
    }

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @param attributeName
     *            String
     * @return int
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @test
     * @code
     * @endcode
     */
    public int removeAttribute(KEY keyInfo, String attributeName) throws FrameworkErrorSignal, NotFoundSignal
    {
        return this.commonAttributeServiceDAO.removeAttribute(keyInfo, attributeName);
    }

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @param attributeName
     *            String
     * @param attributeValue
     *            String
     * @return int
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @test
     * @code
     * @endcode
     */
    public int setAttribute(KEY keyInfo, String attributeName, String attributeValue)
            throws FrameworkErrorSignal, NotFoundSignal
    {
        return this.commonAttributeServiceDAO.setAttribute(keyInfo, attributeName, attributeValue);
    }

}
