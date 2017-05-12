package ch10.jdbctemplate.config;


import ch10.jdbctemplate.JdbcSpitterRepository;
import ch10.jdbctemplate.SpitterRepository;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.sql.DataSource;


@Configuration
@ComponentScan(basePackages = "ch10")
public class JdbcConfig {
  /**
   *   @Profile("QA") 指定QA 环境 数据库连接池配置
   */
  @Bean
  @Profile("QA")
  public DataSource qaDataSource() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
    dataSource.setUrl("jdbc:oracle:thin:@10.16.111.245:1521:testdb1");
    dataSource.setUsername("TRULYMESADM");
    dataSource.setPassword("trulymesadm");
    dataSource.setInitialSize(5);
    dataSource.setMaxActive(10);

    return dataSource;
  }

  /**
   * TODO
   * 未测试过，不一定可行，用的H2 数据库
   * @return
   */
  @Bean
  @Profile("dev")
  public DataSource devDataSource(){
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScripts("classpath:spittr/db/jdbc/schema.sql", "classpath:spittr/db/jdbc/test-data.sql")
            .build();
  }

  /**
   * TODO
   * 生产环境使用jndi，未测试过
   * @return
   */
  @Bean
  @Profile("prd")
  public DataSource prdDataSource(){
    JndiObjectFactoryBean jndiObjectFactoryBean
            = new JndiObjectFactoryBean();
    jndiObjectFactoryBean.setJndiName("jdbc/SpittrDS");
    jndiObjectFactoryBean.setResourceRef(true);
    jndiObjectFactoryBean.setProxyInterface(DataSource.class);
    return (DataSource) jndiObjectFactoryBean.getObject();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public SpitterRepository spitterRepository(JdbcTemplate jdbcTemplate) {
    return new JdbcSpitterRepository(jdbcTemplate);
  }
/*
  @Bean
  public SpittleRepository spittleRepository(JdbcTemplate jdbcTemplate) {
    return new JdbcSpittleRepository(jdbcTemplate);
  }

  @Bean
  public PlatformTransactionManager transactionManager(DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }*/
}
