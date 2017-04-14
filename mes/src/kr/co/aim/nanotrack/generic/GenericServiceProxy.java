package kr.co.aim.nanotrack.generic;

import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanoframe.transaction.TxDataSourceManager;
import kr.co.aim.nanoframe.util.bundle.BundleUtil;
import kr.co.aim.nanotrack.generic.exception.ExceptionMessages;
import kr.co.aim.nanotrack.generic.info.CommonAttributeService;
import kr.co.aim.nanotrack.generic.master.ConstantMap;
import kr.co.aim.nanotrack.generic.master.EnumMap;
import kr.co.aim.nanotrack.generic.master.GradeMap;
import kr.co.aim.nanotrack.generic.master.ObjectBehaviorPolicyMap;
import kr.co.aim.nanotrack.generic.master.ObjectBehaviorValidationMap;
import kr.co.aim.nanotrack.generic.orm.OrmMesEngine;
import kr.co.aim.nanotrack.generic.orm.SqlMesTemplate;
import kr.co.aim.nanotrack.generic.policy.CommonPolicy;
import kr.co.aim.nanotrack.generic.policy.CommonPolicyUtil;
import kr.co.aim.nanotrack.generic.pos.PosPolicyMap;
import kr.co.aim.nanotrack.generic.state.StateModelManager;
import kr.co.aim.nanotrack.generic.validation.CommonValidate;
import kr.co.aim.nanotrack.generic.validation.CommonValidateExecutor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class GenericServiceProxy implements ApplicationContextAware
{
    private static ApplicationContext	ac;

    public GenericServiceProxy()
    {
    }

    public void setApplicationContext(ApplicationContext arg0) throws BeansException
    {
        this.ac = arg0;
    }

    public static ApplicationContext getApplicationContext() throws BeansException
    {
        return ac;
    }

    public static SqlMesTemplate getSqlMesTemplate()
    {
        //		return (SqlMesTemplate)BundleUtil.getBundleServiceClass(SqlMesTemplate.class);
        return (SqlMesTemplate) BundleUtil.getBundleServiceClass(SqlMesTemplate.class);
    }

    public static ConstantMap getConstantMap()
    {
        return (ConstantMap) BundleUtil.getBundleServiceClass(ConstantMap.class);
    }

    public static EnumMap getEnumMap()
    {
        return (EnumMap) BundleUtil.getBundleServiceClass(EnumMap.class);
    }

    public static ExceptionMessages getExceptionMessages()
    {
        return (ExceptionMessages) BundleUtil.getBundleServiceClass(ExceptionMessages.class);
    }

    public static CommonValidateExecutor getCommonValidateExecutor()
    {
        return (CommonValidateExecutor) BundleUtil.getBundleServiceClass(CommonValidateExecutor.class);
    }

    public static ObjectBehaviorValidationMap getObjectBehaviorValidationMap()
    {
        return (ObjectBehaviorValidationMap) BundleUtil.getBundleServiceClass(ObjectBehaviorValidationMap.class);
    }

    public static CommonValidate getCommonValidate()
    {
        return (CommonValidate) BundleUtil.getBundleServiceClass(CommonValidate.class);
    }

    public static CommonPolicyUtil getCommonPolicyUtil()
    {
        return (CommonPolicyUtil) BundleUtil.getBundleServiceClass(CommonPolicyUtil.class);
    }

    public static OrmMesEngine getOrmMesEngine()
    {
        return (OrmMesEngine) BundleUtil.getBundleServiceClass(OrmMesEngine.class);
    }

    public static GradeMap getGradeMap()
    {
        return (GradeMap) BundleUtil.getBundleServiceClass(GradeMap.class);
    }

    public static CommonPolicy getCommonPolicy()
    {
        return (CommonPolicy) BundleUtil.getBundleServiceClass(CommonPolicy.class);
    }

    public static StateModelManager getStateModelManager()
    {
        return (StateModelManager) BundleUtil.getBundleServiceClass(StateModelManager.class);
    }

    public static ObjectBehaviorPolicyMap getObjectBehaviorPolicyMap()
    {
        return (ObjectBehaviorPolicyMap) BundleUtil.getBundleServiceClass(ObjectBehaviorPolicyMap.class);
    }

    @SuppressWarnings("unchecked")
    public static CommonAttributeService<KeyInfo, DataInfo> getCommonAttributeService()
    {
        return (CommonAttributeService) BundleUtil.getBundleServiceClass(CommonAttributeService.class);
    }

    public static PosPolicyMap getPosPolicyMap()
    {
        return (PosPolicyMap) BundleUtil.getBundleServiceClass(PosPolicyMap.class);
    }

    //	public static NamedParamSqlMesTemplate getNamedParamSqlMesTemplate()
    //	{
    //		return (NamedParamSqlMesTemplate) BundleUtil.getBundleServiceClass(NamedParamSqlMesTemplate.class);
    //	}

    public static TxDataSourceManager getTxDataSourceManager()
    {
        return (TxDataSourceManager) BundleUtil.getBundleServiceClass(TxDataSourceManager.class);
    }

}
