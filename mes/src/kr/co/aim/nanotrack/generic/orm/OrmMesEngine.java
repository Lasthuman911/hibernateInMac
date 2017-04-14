package kr.co.aim.nanotrack.generic.orm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameDBErrorSignal;
import kr.co.aim.nanoframe.orm.OrmStandardEngine;
import kr.co.aim.nanoframe.orm.info.DataInfo;
import kr.co.aim.nanoframe.orm.info.KeyInfo;
import kr.co.aim.nanoframe.orm.support.OrmStandardEngineUtil;
import kr.co.aim.nanoframe.util.bundle.BundleUtil;
import kr.co.aim.nanoframe.util.object.ObjectUtil;
import kr.co.aim.nanotrack.generic.GenericServiceProxy;
import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.ExceptionKey;
import kr.co.aim.nanotrack.generic.exception.ExceptionNotify;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;
import kr.co.aim.nanotrack.generic.exception.InvalidStateTransitionSignal;
import kr.co.aim.nanotrack.generic.exception.LogLevel;
import kr.co.aim.nanotrack.generic.exception.NotFoundSignal;
import kr.co.aim.nanotrack.generic.info.EventInfo;
import kr.co.aim.nanotrack.generic.info.TransitionInfo;
import kr.co.aim.nanotrack.generic.info.UndoInfoTK;
import kr.co.aim.nanotrack.generic.info.UndoTimeKeys;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OrmMesEngine<KEY extends KeyInfo, DATA extends DataInfo> extends OrmStandardEngine<KEY, DATA>
{
    protected Log	log	= LogFactory.getLog(getClass());

    protected UndoTimeKeys doUndo(KEY keyInfo, EventInfo eventInfo, TransitionInfo transitionInfo)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        UndoInfoTK undoInfoTK = new UndoInfoTK();
        undoInfoTK.setFirst(transitionInfo);

        DATA result = doWork(keyInfo, eventInfo, undoInfoTK, "undo");

        UndoTimeKeys undoTimeKeys = new UndoTimeKeys();
        undoTimeKeys.setCanceledTimeKey(undoInfoTK.getSecond());
        undoTimeKeys.setRemarkedTimeKey((String) ObjectUtil.getFieldValue(undoInfoTK.getFirst(), "eventTimeKey"));

        return undoTimeKeys;
    }

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @param eventInfo
     *            kr.co.aim.nanotrack.generic.info.EventInfo
     * @param transitionInfo
     *            extends kr.co.aim.nanotrack.generic.info.TransitionInfo
     * @param operationName
     *            String
     * @return DataInfo extends kr.co.aim.nanoframe.orm.info.DataInfo
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     * @test
     * @code
     * @endcode
     */
    protected DATA doWork(KEY keyInfo, EventInfo eventInfo, TransitionInfo transitionInfo, String operationName)
            throws FrameworkErrorSignal, NotFoundSignal, DuplicateNameSignal
    {
        DATA oldData;
        DATA newData;

        GenericServiceProxy.getTxDataSourceManager().beginTransaction();

        if (log.isInfoEnabled())
        {
            log.info(String.format("⒑ START %s.%s DataKey=[%s]", this.getClass().getSimpleName(), operationName, ObjectUtil.getString(keyInfo)));
        }

        long begin = System.nanoTime();

        try
        {
            if (operationName.startsWith("undo"))
            {
                oldData = this.selectByKeyForUpdate(keyInfo);
            }
            else
            {
                if (operationName.startsWith("create"))
                    oldData = (DATA) OrmStandardEngineUtil.createDataInfo(keyInfo);
                else
                    oldData = this.selectByKeyForUpdate(keyInfo);
            }

            newData = (DATA) ObjectUtil.copyTo(oldData);

            if (operationName.startsWith("undo"))
            {
                GenericServiceProxy.getCommonPolicy().setUndoInfo(newData, eventInfo,
                        ((UndoInfoTK) transitionInfo).getFirst());
            }
            else
            {
                GenericServiceProxy.getCommonPolicy().setEventInfo(newData, eventInfo, transitionInfo);
                GenericServiceProxy.getCommonPolicy().setUdfs(newData, transitionInfo);
            }

            Object service = BundleUtil.getBundleServiceClass("kr.co.aim.nanotrack.behavior.BehaviorServiceProxy");
            Method method = service.getClass().getDeclaredMethod("getBehaviorExecutor", null);
            Object behaviorExecutor = method.invoke(null, null);

            method =
                    behaviorExecutor.getClass().getDeclaredMethod("executeBehavior", DataInfo.class, DataInfo.class,
                            EventInfo.class, TransitionInfo.class, String.class, String.class);
            DATA result =
                    (DATA) method.invoke(behaviorExecutor, new Object[] {
                            oldData,
                            newData,
                            eventInfo,
                            transitionInfo,
                            newData.getClass().getSimpleName(),
                            operationName });

            boolean isRemoved = false;
            if (operationName.toLowerCase().startsWith("undo"))
            {
                String lastEventTimeKey = (String) ObjectUtil.getFieldValue(newData, "lastEventTimeKey");

                if (StringUtils.isEmpty(lastEventTimeKey))
                {
                    Method removeMethod = this.getClass().getDeclaredMethod("remove", newData.getKey().getClass());
                    removeMethod.invoke(this, newData.getKey());

                    result = null;
                    isRemoved = true;
                }
                else
                    this.update(newData);
            }
            else if (operationName.toLowerCase().startsWith("create") == false)
            {
                if (operationName.toLowerCase().startsWith("remove") == false)
                    update(newData);
                else
                    isRemoved = true;
            }

            if (!isRemoved)
                result = this.selectByKey((KEY) result.getKey());

            if (log.isInfoEnabled())
            {
                long end = System.nanoTime();
                log.info(String.format("⒑ ENDOK %s.%s DataKey=[%s] %s ms", this.getClass().getSimpleName(), operationName, ObjectUtil.getString(keyInfo), (end - begin) / 1000000));
            }
            GenericServiceProxy.getTxDataSourceManager().commitTransaction();
            return result;
        } catch (Throwable e)
        {
            long end = System.nanoTime();
            GenericServiceProxy.getTxDataSourceManager().rollbackTransaction();
            if (log.isInfoEnabled())
            {
                log.info(String.format("⒑ ENDNG %s.%s DataKey=[%s] %s ms", this.getClass().getSimpleName(), operationName, ObjectUtil.getString(keyInfo), (end - begin) / 1000000));
            }
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public List<DATA> transform(List resultList)
    {
        List<DATA> result = super.transform(resultList, this.getClass());

        return result;
    }

    /**
     *
     * @param condition
     *            String
     * @param bindSet
     *            Object[]
     * @return List<DATA> List<extends kr.co.aim.nanoframe.orm.info.DataInfo>
     * @throws NotFoundSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    @SuppressWarnings("unchecked")
    public List<DATA> select(String condition, Object[] bindSet) throws NotFoundSignal, FrameworkErrorSignal
    {
        try
        {
            List<DATA> result = (List<DATA>) super.select(condition, bindSet, this.getClass());
            return result;
        } catch (nanoFrameDBErrorSignal e)
        {
            if (ErrorSignal.NotFoundSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new NotFoundSignal(LogLevel.Debug, e, getClass(), "select", e.getDataKey(), e.getSql());
            }
            else
                throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @return DATA extends kr.co.aim.nanoframe.orm.info.DataInfo
     * @throws NotFoundSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    @SuppressWarnings("unchecked")
    public DATA selectByKeyForUpdate(KEY keyInfo) throws NotFoundSignal, FrameworkErrorSignal
    {
        try
        {
            DATA result = (DATA) super.selectByKeyForUpdate(keyInfo);
            return result;
        } catch (nanoFrameDBErrorSignal e)
        {
            if (ErrorSignal.NotFoundSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new NotFoundSignal(e, getClass(), "selectByKeyForUpdate", e.getDataKey(), e.getSql());
            }
            else
                throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @param keyInfo
     *            extends kr.co.aim.nanoframe.orm.info.KeyInfo
     * @return DATA extends kr.co.aim.nanoframe.orm.info.DataInfo
     * @throws NotFoundSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    @SuppressWarnings("unchecked")
    public DATA selectByKey(KEY keyInfo) throws NotFoundSignal, FrameworkErrorSignal
    {
        try
        {
            DATA result = (DATA) super.selectByKey(keyInfo);
            return result;
        } catch (nanoFrameDBErrorSignal e)
        {
            if (ErrorSignal.NotFoundSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new NotFoundSignal(LogLevel.Debug, e, getClass(), "selectByKey", e.getDataKey(), e.getSql());
            }
            else
                throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @param dataInfo
     *            extends kr.co.aim.nanoframe.orm.info.DataInfo
     * @param condition
     *            String
     * @param bindSet
     *            Object[]
     * @return int
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @test
     * @code
     * @endcode
     */
    @SuppressWarnings("unchecked")
    public int update(DATA dataInfo, String condition, Object[] bindSet) throws FrameworkErrorSignal, NotFoundSignal
    {
        try
        {
            int result = super.update(dataInfo, condition, bindSet);
            return result;
        } catch (nanoFrameDBErrorSignal e)
        {
            if (ErrorSignal.NotFoundSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new NotFoundSignal(LogLevel.Debug, e, getClass(), "update", e.getDataKey(), e.getSql());
            }
            else if (ErrorSignal.DuplicateNameSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new DuplicateNameSignal(e, getClass(), "update", e.getDataKey(), e.getSql());
            }
            else
                throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @param dataInfo
     *            extends kr.co.aim.nanoframe.orm.info.DataInfo
     * @return int
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @test
     * @code
     * @endcode
     */
    @SuppressWarnings("unchecked")
    public int update(DATA dataInfo) throws FrameworkErrorSignal, NotFoundSignal
    {
        try
        {
            int result = super.update(dataInfo);
            return result;
        } catch (nanoFrameDBErrorSignal e)
        {
            if (ErrorSignal.NotFoundSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new NotFoundSignal(LogLevel.Debug, e, getClass(), "update", e.getDataKey(), e.getSql());
            }
            else if (ErrorSignal.DuplicateNameSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new DuplicateNameSignal(e, getClass(), "update", e.getDataKey(), e.getSql());
            }
            else
                throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @param dataInfo
     *            extends kr.co.aim.nanoframe.orm.info.DataInfo
     * @return int
     * @throws DuplicateNameSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    @SuppressWarnings("unchecked")
    public int insert(DATA dataInfo) throws DuplicateNameSignal, FrameworkErrorSignal
    {
        try
        {
            int result = super.insert(dataInfo);
            return result;
        } catch (nanoFrameDBErrorSignal e)
        {
            if (ErrorSignal.DuplicateNameSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new DuplicateNameSignal(e, getClass(), "insert", e.getDataKey(), e.getSql());
            }
            else
                throw ExceptionNotify.getNotifyException(e);
        }
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
    @SuppressWarnings("unchecked")
    public int delete(KEY keyInfo) throws FrameworkErrorSignal, NotFoundSignal
    {
        try
        {
            int result = super.delete(keyInfo);
            return result;
        } catch (nanoFrameDBErrorSignal e)
        {
            if (ErrorSignal.NotFoundSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new NotFoundSignal(LogLevel.Debug, e, getClass(), "delete", e.getDataKey(), e.getSql());
            }
            else
                throw ExceptionNotify.getNotifyException(e);
        }

    }

    /**
     *
     * @param condition
     *            String
     * @param bindSet
     *            Object[]
     * @return int
     * @throws FrameworkErrorSignal
     * @throws NotFoundSignal
     * @test
     * @code
     * @endcode
     */
    public int delete(String condition, Object[] bindSet) throws FrameworkErrorSignal, NotFoundSignal
    {
        try
        {
            int result = super.delete(condition, bindSet, this.getClass());
            return result;
        } catch (nanoFrameDBErrorSignal e)
        {
            if (ErrorSignal.NotFoundSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new NotFoundSignal(LogLevel.Debug, e, getClass(), "delete", e.getDataKey(), e.getSql());
            }
            else
                throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @param queryType
     *            String
     * @param dataObject
     *            Object
     * @return Object
     * @throws NotFoundSignal
     * @throws DuplicateNameSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    protected Object execute(String queryType, Object dataObject)
            throws NotFoundSignal, DuplicateNameSignal, FrameworkErrorSignal
    {
        try
        {
            Object result = super.execute(queryType, dataObject);
            return result;
        } catch (nanoFrameDBErrorSignal e)
        {
            if (ErrorSignal.NotFoundSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new NotFoundSignal(LogLevel.Debug, e, getClass(), queryType, e.getDataKey(), e.getSql());
            }
            else if (ErrorSignal.DuplicateNameSignal.equalsIgnoreCase(e.getErrorCode()))
            {
                throw new DuplicateNameSignal(e, getClass(), queryType, e.getDataKey(), e.getSql());
            }
            else
                throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @param resultList
     *            List
     * @param keyData
     *            String
     * @param returnType
     *            String
     * @return Object
     * @throws NotFoundSignal
     * @throws InvalidStateTransitionSignal
     * @throws DuplicateNameSignal
     * @throws FrameworkErrorSignal
     * @test
     * @code
     * @endcode
     */
    @SuppressWarnings("unchecked")
    protected Object validateResultSet(List resultList, String keyData, String returnType)
            throws NotFoundSignal, InvalidStateTransitionSignal, DuplicateNameSignal, FrameworkErrorSignal
    {
        Object result = OrmStandardEngineUtil.ormExecute(OrmStandardEngineUtil.createDataInfo(getClass()), resultList);
        if (result instanceof List)
        {
            return (List<DataInfo>) result;
        }
        else if (result instanceof DataInfo)
        {
            if (returnType.indexOf("list") >= 0)
            {
                List<DataInfo> resultSet = new ArrayList<DataInfo>();
                resultSet.add((DataInfo) result);
                return resultSet;
            }
            else
            {
                return result;
            }
        }
        else
        {
            throw new FrameworkErrorSignal(ExceptionKey.UnExpected_Exception, keyData);
        }
    }

}
