package kr.co.aim.nanoframe.orm;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.exception.nanoFrameDBErrorSignal;
import kr.co.aim.nanoframe.util.object.ObjectUtil;
import kr.co.aim.nanoframe.util.sys.SystemPropHelper;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SqlTemplate implements InitializingBean
{
    private static Log			log				= LogFactory.getLog(SqlTemplate.class);

    private DataSource			dataSource;
    private JdbcTemplate		jdbcTemplate;
    private SimpleJdbcTemplate	simpleJdbcTemplate;

    //ColumnMapRowMapper 是RowMapper的实现类，方法mapRow 此方法用于将每行数据映射为实体类，直接作为jdbcTemplate的一个参数即可
    private RowMapper				rowMapper = new ColumnMapRowMapper();

    private int					queryTimeout	= 0;

    public DataSource getDataSource()
    {
        return this.dataSource;
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public int getQueryTimeout()
    {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout)
    {
        this.queryTimeout = queryTimeout;
    }

    public JdbcTemplate getJdbcTemplate()
    {
        return this.jdbcTemplate;
    }

    @Deprecated
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SimpleJdbcTemplate getSimpleJdbcTemplate()
    {
        return this.simpleJdbcTemplate;
    }

    @Deprecated
    public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate)
    {
        this.simpleJdbcTemplate = simpleJdbcTemplate;
    }

    public void setRowMapper( RowMapper rowMapper )
    {
        this.rowMapper = rowMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        if (this.dataSource == null)
        {
            if (this.jdbcTemplate != null)
            {
                this.dataSource = this.jdbcTemplate.getDataSource();

                if (this.queryTimeout <= 0)
                    this.queryTimeout = this.jdbcTemplate.getQueryTimeout();
            }
        }

        if (this.jdbcTemplate == null)
        {
            this.jdbcTemplate = new JdbcTemplate(this.dataSource);
            this.jdbcTemplate.setQueryTimeout(this.queryTimeout);
        }

        if (this.simpleJdbcTemplate == null)
        {
            this.simpleJdbcTemplate = new SimpleJdbcTemplate(this.jdbcTemplate);
        }
    }

    public SqlCursorItemReader queryByCursor(String sql, final Object... args)
    {
        JdbcCursorItemReader reader = new JdbcCursorItemReader();
        reader.setDataSource(getJdbcTemplate().getDataSource());
        reader.setSql(sql);

        if (args != null)
        {
            PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {
                public void setValues(PreparedStatement preparedStatement) throws SQLException
                {
                    for (int i = 0; i < args.length; i++)
                    {
                        preparedStatement.setObject(i + 1, args[i]);
                    }
                }
            };
            reader.setPreparedStatementSetter(preparedStatementSetter);
        }

        return new SqlCursorItemReader(reader);
    }

    public int[] updateBatch(String... querys) throws nanoFrameDBErrorSignal
    {
        try
        {
            return getJdbcTemplate().batchUpdate(querys);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e);
        }
    }

    public int[] updateBatch(String query, BatchPreparedStatementSetter pss) throws nanoFrameDBErrorSignal
    {
        try
        {
            return getJdbcTemplate().batchUpdate(query, pss);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, query);
        }
    }

    public int[] updateBatch(String query, Map[] args) throws nanoFrameDBErrorSignal
    {
        try
        {
            return getSimpleJdbcTemplate().batchUpdate(query, args);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, query);
        }
    }

    public int[] updateBatch(String query, List<Object[]> args) throws nanoFrameDBErrorSignal
    {
        try
        {
            return getSimpleJdbcTemplate().batchUpdate(query, args);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, query);
        }
    }

    public int[] updateBatchByList(String query, final List<List<Object>> params) throws nanoFrameDBErrorSignal
    {
        try
        {
            return updateBatch(query, new BatchPreparedStatementSetter() {
                public int getBatchSize()
                {
                    return params.size();
                }

                public void setValues(PreparedStatement ps, int i) throws SQLException
                {
                    List param = params.get(i);
                    for (int j = 0; j < (null != param ? param.size() : 0); j++)
                    {
                        Object obj = param.get(j);
                        if (obj instanceof BigDecimal)
                        {
                            if (!obj.toString().contains("."))
                                ps.setObject(j + 1, Long.valueOf(obj.toString()));
                            else
                                ps.setObject(j + 1, Double.valueOf(obj.toString()));

                        }
                        else
                            ps.setObject(j + 1, obj);
                    }
                }
            });
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, query);
        }
    }

    public int[] updateBatch(String query, SqlParameterSource[] args) throws nanoFrameDBErrorSignal
    {
        try
        {
            return getSimpleJdbcTemplate().batchUpdate(query, args);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, query);
        }
    }

    public Map executeProcedure(String procedureName) throws nanoFrameDBErrorSignal
    {
        try
        {
            SimpleJdbcCall procReader = new SimpleJdbcCall(jdbcTemplate).withProcedureName(procedureName);

            Map out = procReader.execute();
            return out;
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, procedureName);
        }
    }

    public Map executeProcedure(String procedureName, Map args) throws nanoFrameDBErrorSignal
    {
        try
        {
            SimpleJdbcCall procReader = new SimpleJdbcCall(jdbcTemplate).withProcedureName(procedureName);

            SqlParameterSource in = new MapSqlParameterSource().addValues(args);

            Map out = procReader.execute(in);
            return out;
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), procedureName);
        }
    }

    public Map executeProcedure(String procedureName, Map args, SqlParameter... sqlParameters)
            throws nanoFrameDBErrorSignal
    {
        try
        {
            SimpleJdbcCall procReader =
                    new SimpleJdbcCall(jdbcTemplate).withoutProcedureColumnMetaDataAccess().withProcedureName(
                            procedureName).declareParameters(sqlParameters);

            Map out = null;
            if (MapUtils.isEmpty(args))
                out = procReader.execute();
            else
            {
                SqlParameterSource in = new MapSqlParameterSource().addValues(args);
                out = procReader.execute(in);
            }
            return out;
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), procedureName);
        }
    }

    public String[][] queryForStringArray(String sql, Object... args) throws nanoFrameDBErrorSignal
    {
        return DataAccessHandler.getStringArrayData(queryForList(sql, args));
    }

    public int queryForInt(String sql, Object... args) throws nanoFrameDBErrorSignal
    {
        int result = 0;
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null)
                result = getJdbcTemplate().queryForInt(sql);
            else
                result = getJdbcTemplate().queryForInt(sql, args);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterQuery(result, log);
        return result;
    }

    public int queryForInt(String sql, Map<String, Object> args) throws nanoFrameDBErrorSignal
    {
        int result = 0;
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null)
                result = this.getSimpleJdbcTemplate().queryForInt(sql);
            else
                result = this.getSimpleJdbcTemplate().queryForInt(sql, args);

        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterQuery(result, log);
        return result;
    }

    /**
     *sql:  "select 字段1，字段2 from tableName where 字段x=？,字段y=？“
     * @return List<Map<String, Object>>, List<ListOrderedMap>
     * @throws nanoFrameDBErrorSignal
     */
    public List queryForList(String sql, Object... args) throws nanoFrameDBErrorSignal
    {
        List resultList = null;
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (ArrayUtils.isEmpty(args))
                //Query given SQL to create a prepared statement from SQL and a list of arguments to bind to the query,
                // mapping each row to a Java object via a RowMapper.
                resultList = getJdbcTemplate().query(sql, this.rowMapper );
            else
            {
                if (args.length == 1 && args[0] == null)
                    resultList = getJdbcTemplate().query(sql, this.rowMapper );
                else
                    resultList = getJdbcTemplate().query(sql, args, this.rowMapper );
            }
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterQuery(resultList, log);
        return resultList;
    }

    /**
     *
     * @return List<Map<String, Object>>, List<ListOrderedMap>
     * @throws nanoFrameDBErrorSignal
     */
    public List queryForList(String sql, Map<String, Object> args) throws nanoFrameDBErrorSignal
    {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);
        List resultList = null;

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if ( this.rowMapper instanceof NullReplaceableRowMapper )
            {
                if (args == null)
                    resultList = this.getSimpleJdbcTemplate().query(sql, (ParameterizedRowMapper<Object>)this.rowMapper);
                else
                    resultList = this.getSimpleJdbcTemplate().query(sql, (ParameterizedRowMapper<Object>)this.rowMapper, args);
            }
            else
            {
                if (args == null)
                    resultList = this.getSimpleJdbcTemplate().queryForList( sql );
                else
                    resultList = this.getSimpleJdbcTemplate().queryForList(sql, args);
            }
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterQuery(resultList, log);
        return resultList;
    }

    public <T> T queryForObject(String sql, Class<T> requiredType, Map args) throws nanoFrameDBErrorSignal
    {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            SingleColumnRowMapper singleRowMapper = getSingleColumnRowMapper();
            singleRowMapper.setRequiredType( requiredType );

            if ( singleRowMapper instanceof NullReplaceableSingleRowMapper )
                return (T)this.getSimpleJdbcTemplate().queryForObject(sql, (ParameterizedRowMapper<Object>)singleRowMapper, args);
            else
                return this.getSimpleJdbcTemplate().queryForObject(sql, requiredType, args);

        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }
    }

    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws nanoFrameDBErrorSignal
    {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            SingleColumnRowMapper singleRowMapper = getSingleColumnRowMapper();
            singleRowMapper.setRequiredType( requiredType );

            if ( singleRowMapper instanceof NullReplaceableSingleRowMapper )
                return (T)this.getSimpleJdbcTemplate().queryForObject(sql, (ParameterizedRowMapper<Object>)singleRowMapper, args);
            else
                return this.getSimpleJdbcTemplate().queryForObject(sql, requiredType, args);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }
    }

    /**
     *
     * @see 抗距绢 : STARTROW, ENDROW, ROWSEQ ==> args狼 Key 捞抚 : STARTROW, ENDROW, 府畔 蔼 : ROWSEQ
     *
     * @return List<Map<String, Object>>, List<ListOrderedMap>
     */
    public List queryForListByPaging(int pageIndex, int pageSize, String sql, Map<String, Object> args)
            throws nanoFrameDBErrorSignal
    {
        String newSql =
                "SELECT * FROM ( SELECT data.*, rownum as ROWSEQ FROM "
                        + SystemPropHelper.CR
                        + "  ("
                        + SystemPropHelper.CR
                        + sql
                        + SystemPropHelper.CR
                        + "  ) data WHERE rownum <= :ENDROW "
                        + SystemPropHelper.CR
                        + ") WHERE ROWSEQ BETWEEN  :STARTROW AND :ENDROW";
        Map<String, Object> newArgs = null;
        int startRow = (pageIndex - 1) * pageSize + 1;
        int endRow = pageIndex * pageSize;

        newArgs = new HashMap<String, Object>(args);
        newArgs.put("STARTROW", startRow);
        newArgs.put("ENDROW", endRow);

        return queryForList(newSql, newArgs);
    }

    /**
     *
     * @return List<Map<String, Object>>, List<ListOrderedMap>
     * @throws nanoFrameDBErrorSignal
     */
    public List queryForListByPaging(int pageIndex, int pageSize, String sql, Object... args)
            throws nanoFrameDBErrorSignal
    {
        String newSql =
                "SELECT * FROM ( SELECT data.*, rownum as ROWSEQ FROM "
                        + SystemPropHelper.CR
                        + "  ("
                        + SystemPropHelper.CR
                        + sql
                        + SystemPropHelper.CR
                        + "  ) data WHERE rownum <= ? "
                        + SystemPropHelper.CR
                        + ") WHERE ROWSEQ BETWEEN  ? AND ?";
        Object[] newArgs = null;

        int startRow = (pageIndex - 1) * pageSize + 1;
        int endRow = pageIndex * pageSize;

        int cnt = args != null ? args.length : 0;
        newArgs = new Object[cnt + 3];
        for (int index = 0; index < cnt; index++)
        {
            newArgs[index] = args[index];
        }

        newArgs[cnt] = endRow;
        newArgs[cnt + 1] = startRow;
        newArgs[cnt + 2] = endRow;

        return queryForList(newSql, newArgs);
    }

    public int update(String sql, Object... args) throws nanoFrameDBErrorSignal
    {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);
        int result = 0;

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null)
                result = getJdbcTemplate().update(sql);
            else
                result = getJdbcTemplate().update(sql, args);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterUpdate(result, log);
        return result;
    }

    public int update(String sql, Map<String, Object> args) throws nanoFrameDBErrorSignal
    {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);

        int result = 0;
        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null)
                result = this.getSimpleJdbcTemplate().update(sql);
            else
                result = this.getSimpleJdbcTemplate().update(sql, args);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterUpdate(result, log);
        return result;
    }

    public int update(String sql, Object[] args, String keyString) throws nanoFrameDBErrorSignal
    {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);
        int result = 0;

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null)
                result = getJdbcTemplate().update(sql);
            else
                result = getJdbcTemplate().update(sql, args);
        } catch (DataAccessException e)
        {
            if (keyString != null)
            {
                throw ErrorSignal.getNotifyException(e, keyString, logFormatSql);
            }
            else
            {
                throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
            }
        }

        SQLLogUtil.logAfterUpdate(result, log);
        return result;
    }

    @Deprecated
    public int queryForIntBySimple(String sql, Object[] args) throws nanoFrameDBErrorSignal
    {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);
        int result = 0;

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null)
                result = getSimpleJdbcTemplate().queryForInt(sql);
            else
                result = getSimpleJdbcTemplate().queryForInt(sql, args);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterUpdate(result, log);
        return result;
    }

    @Deprecated
    public List queryForListBySimple(String sql, Object[] args) throws nanoFrameDBErrorSignal
    {
        List resultList = null;
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null || args.length == 0)
                resultList = getSimpleJdbcTemplate().queryForList(sql);
            else
            {
                if (args.length == 1 && args[0] == null)
                    resultList = getSimpleJdbcTemplate().queryForList(sql);
                else
                    resultList = getSimpleJdbcTemplate().queryForList(sql, args);
            }
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterQuery(resultList, log);
        return resultList;
    }

    @Deprecated
    public int updateBySimple(String sql, Object[] args) throws nanoFrameDBErrorSignal
    {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);
        int result = 0;

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null)
                result = getSimpleJdbcTemplate().update(sql);
            else
                result = getSimpleJdbcTemplate().update(sql, args);
        } catch (DataAccessException e)
        {
            throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterUpdate(result, log);
        return result;
    }

    @Deprecated
    public int updateBySimple(String sql, String keyString, Object[] args) throws nanoFrameDBErrorSignal
    {
        String logFormatSql = SQLLogUtil.getLogFormatSqlStatement(sql, args, log);
        int result = 0;

        try
        {
            SQLLogUtil.logBeforeExecuting(logFormatSql, log);

            if (args == null)
                result = getSimpleJdbcTemplate().update(sql);
            else
                result = getSimpleJdbcTemplate().update(sql, args);
        } catch (DataAccessException e)
        {
            if (keyString != null)
                throw ErrorSignal.getNotifyException(e, keyString, logFormatSql);
            else
                throw ErrorSignal.getNotifyException(e, ObjectUtil.getString(args), logFormatSql);
        }

        SQLLogUtil.logAfterUpdate(result, log);
        return result;
    }

    public SingleColumnRowMapper getSingleColumnRowMapper()
    {
        SingleColumnRowMapper mapper = null;

        if ( rowMapper instanceof NullReplaceableRowMapper )
            mapper = new NullReplaceableSingleRowMapper();
        else
            mapper = new SingleColumnRowMapper();

        return mapper;
    }
}
