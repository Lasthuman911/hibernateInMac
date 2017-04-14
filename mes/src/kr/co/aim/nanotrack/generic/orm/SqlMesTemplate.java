package kr.co.aim.nanotrack.generic.orm;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import kr.co.aim.nanoframe.nanoFrameServiceProxy;
import kr.co.aim.nanoframe.exception.nanoFrameDBErrorSignal;
import kr.co.aim.nanoframe.orm.SqlCursorItemReader;
import kr.co.aim.nanoframe.orm.SqlTemplate;
import kr.co.aim.nanotrack.generic.exception.DuplicateNameSignal;
import kr.co.aim.nanotrack.generic.exception.ExceptionNotify;
import kr.co.aim.nanotrack.generic.exception.FrameworkErrorSignal;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SqlMesTemplate implements InitializingBean
{
    private SqlTemplate	sqlTemplate	= null;

    public void setSqlTemplate(SqlTemplate sqlTemplate)
    {
        this.sqlTemplate = sqlTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        if (this.sqlTemplate == null)
            this.sqlTemplate = nanoFrameServiceProxy.getSqlTemplate();
    }

    public JdbcTemplate getJdbcTemplate()
    {
        return this.sqlTemplate.getJdbcTemplate();
    }

    public SimpleJdbcTemplate getSimpleJdbcTemplate()
    {
        return this.sqlTemplate.getSimpleJdbcTemplate();
    }

    public DataSource getDataSource()
    {
        return sqlTemplate.getDataSource();
    }

    public SqlCursorItemReader queryByCursor(String sql, final Object... args)
    {
        return this.sqlTemplate.queryByCursor(sql, args);
    }

    public int[] updateBatch(String... querys)
    {
        try
        {
            return this.sqlTemplate.updateBatch(querys);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public int[] updateBatch(String query, BatchPreparedStatementSetter pss)
            throws FrameworkErrorSignal, DuplicateNameSignal
    {
        try
        {
            return this.sqlTemplate.updateBatch(query, pss);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public int[] updateBatch(String query, List<Object[]> args) throws FrameworkErrorSignal, DuplicateNameSignal
    {
        try
        {
            return this.sqlTemplate.updateBatch(query, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public int[] updateBatchByList(String query, final List<List<Object>> args)
            throws FrameworkErrorSignal, DuplicateNameSignal
    {
        try
        {
            return this.sqlTemplate.updateBatchByList(query, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public int[] updateBatch(String query, Map[] args) throws FrameworkErrorSignal, DuplicateNameSignal
    {
        try
        {
            return this.sqlTemplate.updateBatch(query, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public int[] updateBatch(String query, SqlParameterSource[] args) throws FrameworkErrorSignal, DuplicateNameSignal
    {
        try
        {
            return this.sqlTemplate.updateBatch(query, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public Map executeProcedure(String procedureName) throws FrameworkErrorSignal, DuplicateNameSignal
    {
        try
        {
            return this.sqlTemplate.executeProcedure(procedureName);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public Map executeProcedure(String procedureName, Map args) throws FrameworkErrorSignal, DuplicateNameSignal
    {
        try
        {
            return this.sqlTemplate.executeProcedure(procedureName, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public Map executeProcedure(String procedureName, Map args, SqlParameter... sqlParameters)
            throws FrameworkErrorSignal, DuplicateNameSignal
    {
        try
        {
            return this.sqlTemplate.executeProcedure(procedureName, args, sqlParameters);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public String[][] queryForStringArray(String sql, Object... args) throws DuplicateNameSignal, FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.queryForStringArray(sql, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public int queryForInt(String sql, Object... args) throws FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.queryForInt(sql, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public int queryForInt(String sql, Map<String, Object> args) throws FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.queryForInt(sql, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *sql:  "select 字段1，字段2 from tableName where 字段x=？,字段y=？“
     * @return List<Map<String, Object>>, List<ListOrderedMap>
     *
     * @throws FrameworkErrorSignal
     */
    @SuppressWarnings("unchecked")
    public List queryForList(String sql, Object... args) throws FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.queryForList(sql, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @return List<Map<String, Object>>, List<ListOrderedMap>
     *
     * @throws FrameworkErrorSignal
     */
    public List queryForList(String sql, Map<String, Object> args) throws FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.queryForList(sql, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public <T> T queryForObject(String sql, Class<T> requiredType, Map args) throws FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.queryForObject(sql, requiredType, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.queryForObject(sql, requiredType, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @return List<Map<String, Object>>, List<ListOrderedMap>
     *
     * @throws FrameworkErrorSignal
     */
    public List queryForListByPaging(int pageIndex, int pageSize, String sql, Map<String, Object> args)
            throws FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.queryForListByPaging(pageIndex, pageSize, sql, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    /**
     *
     * @return List<Map<String, Object>>, List<ListOrderedMap>
     *
     * @throws FrameworkErrorSignal
     */
    public List queryForListByPaging(int pageIndex, int pageSize, String sql, Object... args)
            throws FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.queryForListByPaging(pageIndex, pageSize, sql, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public int update(String sql, Object... args) throws DuplicateNameSignal, FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.update(sql, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

    public int update(String sql, Map<String, Object> args) throws DuplicateNameSignal, FrameworkErrorSignal
    {
        try
        {
            return this.sqlTemplate.update(sql, args);
        } catch (nanoFrameDBErrorSignal e)
        {
            throw ExceptionNotify.getNotifyException(e);
        }
    }

}

