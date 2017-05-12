package ch10.jdbctemplate;

import ch10.jdbctemplate.domain.Spitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Repository 这表明它将会在组件扫描的时候自动创建
 * Name: admin
 * Date: 2017/5/12
 * Time: 10:11
 */
@Repository
public class JdbcSpitterRepository implements SpitterRepository {
    /**
     * JdbcOperations是一个接口， 定义了JdbcTemplate所
     * 实现的操作。 通过注入JdbcOperations， 而不是具体的JdbcTemplate， 能够保证
     * JdbcSpitterRepository通过JdbcOperations接口达到与JdbcTemplate保持松耦
     * 合
     */
    private JdbcOperations jdbcOperations;
    private static final String INSERT_SPITTER = "insert into Spitter (username, password, fullname, email, updateByEmail) values (?, ?, ?, ?, ?)";
    private static final String SELECT_SPITTER = "select userid, username, password from userprofile";

    /**
     * 它的构造器上使用了@Inject注解， 因此在创建的时候， 会自动获得一个JdbcOperations对象
     *
     * @param jdbcOperations
     */
    @Autowired
    public JdbcSpitterRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Spitter save(Spitter spitter) {
        String id = spitter.getId();
        if (id == null) {
            String spitterId = insertSpitterAndReturnId(spitter);
            return new Spitter(spitterId, spitter.getUsername(), spitter.getPassword());
        } else {
            jdbcOperations.update("update Spitter set username=?, password=? where userid=?",
                    spitter.getUsername(),
                    spitter.getPassword(),
                    id);
        }
        return spitter;
    }

    private String insertSpitterAndReturnId(Spitter spitter) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert((JdbcTemplate) jdbcOperations).withTableName("Spitter");
        jdbcInsert.setGeneratedKeyName("id");
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("username", spitter.getUsername());
        args.put("password", spitter.getPassword());
        String spitterId = String.valueOf(jdbcInsert.executeAndReturnKey(args));
        return spitterId;
    }

    /**
     * String对象， 包含了要从数据库中查找数据的SQL；
     * RowMapper对象， 用来从ResultSet中提取数据并构建域对象（ 本例中为Spitter）
     * 可变参数列表， 列出了要绑定到查询上的索引参数值
     */
    @Override
    public Spitter findOne(String userID) {
        return jdbcOperations.queryForObject(SELECT_SPITTER + " WHERE USERID = ? ", new SpitterRowMapper(), userID);
    }

    @Override
    public Spitter findByUsername(String username) {
        return jdbcOperations.queryForObject("select userid, username, password from userprofile where username=?", new SpitterRowMapper(), username);
    }

    @Override
    public List<Spitter> findAll() {
        return jdbcOperations.query("select userid, username, password  from userprofile order by userid", new SpitterRowMapper());
    }

    /**
     * 对于查询返回的每一行数据， JdbcTemplate将会调用RowMapper的mapRow()方法， 并传入一
     * 个ResultSet和包含行号的整数。 在SpitterRowMapper的mapRow()方法中， 我们创建
     * 了Spitter对象并将ResultSet中的值填充进去
     */
    private static final class SpitterRowMapper implements RowMapper<Spitter> {
        public Spitter mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString("userid");
            String username = rs.getString("username");
            String password = rs.getString("password");
            return new Spitter(id, username, password);//, fullName, email, updateByEmail);
        }
    }


}
