package ch10;

import static org.junit.Assert.*;

import java.util.List;

import ch10.jdbctemplate.JdbcSpitterRepository;
import ch10.jdbctemplate.config.JdbcConfig;
import ch10.jdbctemplate.domain.Spitter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * @ActiveProfiles(value = "QA") 用于激活 profile=QA 的 dataSource，在集成测试中一般使用此种方式激活
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcConfig.class)
//@ContextConfiguration(locations = {"classpath:persistence.xml"})---TODO 这个测试没通过java.lang.IllegalArgumentException: No DataSource specified
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ActiveProfiles(value = "QA")
public class JdbcSpitterRepositoryTest {


     @Autowired
    JdbcSpitterRepository spitterRepository;

/*    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    JdbcSpitterRepository spitterRepository = new JdbcSpitterRepository(jdbcTemplate);*/

    @Test
    public void count() {
        assertEquals(4, spitterRepository.count());
    }

    @Test
    @Transactional
    public void findAll() {
        List<Spitter> spitters = spitterRepository.findAll();
        assertEquals(4, spitters.size());
        assertSpitter(0, spitters.get(0));
        assertSpitter(1, spitters.get(1));
        assertSpitter(2, spitters.get(2));
        assertSpitter(3, spitters.get(3));
    }

    @Test
    @Transactional
    public void findByUsername() {
        assertSpitter(0, spitterRepository.findByUsername("habuma"));
        assertSpitter(1, spitterRepository.findByUsername("mwalls"));
        assertSpitter(2, spitterRepository.findByUsername("chuck"));
        assertSpitter(3, spitterRepository.findByUsername("artnames"));
    }

    @Test
    @Transactional
    public void findOne() {
/*    Spitter[] SPITTERS = new Spitter[3];
    SPITTERS[0] = new Spitter("wzm001", "wzm001", "Q5gVnq1Fpp8=");*/
        assertSpitter(0, spitterRepository.findOne("wzm001"));
        // assertSpitter(1, spitterRepository.findOne("haha"));
    }

/*  @Test
  @Transactional
  public void save_newSpitter() {
    assertEquals(4, spitterRepository.count());
    Spitter spitter = new Spitter(null, "newbee", "letmein", "New Bee",
        "newbee@habuma.com", true);
    Spitter saved = spitterRepository.save(spitter);
    assertEquals(5, spitterRepository.count());
    assertSpitter(4, saved);
    assertSpitter(4, spitterRepository.findOne(5L));
  }

  @Test
  @Transactional
  public void save_existingSpitter() {
    assertEquals(4, spitterRepository.count());
    Spitter spitter = new Spitter(4L, "arthur", "letmein", "Arthur Names",
        "arthur@habuma.com", false);
    Spitter saved = spitterRepository.save(spitter);
    assertSpitter(5, saved);
    assertEquals(4, spitterRepository.count());
    Spitter updated = spitterRepository.findOne(4L);
    assertSpitter(5, updated);
  }*/

    private static void assertSpitter(int expectedSpitterIndex, Spitter actual) {
        assertSpitter(expectedSpitterIndex, actual, "Newbie");
    }

    private static void assertSpitter(int expectedSpitterIndex, Spitter actual,
                                      String expectedStatus) {
        Spitter expected = SPITTERS[expectedSpitterIndex];
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getPassword(), actual.getPassword());
    }

    private static Spitter[] SPITTERS = new Spitter[3];

    @BeforeClass
    public static void before() {
        SPITTERS[0] = new Spitter("wzm001", "wzm001", "Q5gVnq1Fpp8=");
        // SPITTERS[1] = new Spitter("haha", "haha", "Q5gVnq1Fpp8=");
    }

}
