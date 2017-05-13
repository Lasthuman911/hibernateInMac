package ch11;

import static org.junit.Assert.*;

import java.util.List;

import ch11.hibernate.SpitterRepository;
import ch11.hibernate.domain.Spitter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RepositoryTestConfig.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class SpitterRepositoryTest {

  @Autowired
  SpitterRepository spitterRepository;

  @Test
  @Transactional
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
    assertSpitter(0, spitterRepository.findOne("wzm001"));
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
  }

}
