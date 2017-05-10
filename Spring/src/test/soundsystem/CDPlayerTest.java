package soundsystem;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 使用了Spring的SpringJUnit4ClassRunner， 以便在测试开始的时候自动创建Spring的应用上下文
 * 注解 @ContextConfiguration 会告诉它需要在CDPlayerConfig中加载配置
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CDPlayerConfig.class)
public class CDPlayerTest {

  @Rule
  public final StandardOutputStreamLog log = new StandardOutputStreamLog();
/*  public final SystemOutRule log2 = new SystemOutRule();*/


 // private Log log2 = LogFactory.getLog(CDPlayerTest.class);--通常的做法


  @Autowired
  private MediaPlayer player;
  /**
   * 属性带有@Autowired注解， 以便于将CompactDisc bean注入到测试代码之中
   */
  @Autowired
  private CompactDisc cd;
  
  @Test
  public void cdShouldNotBeNull() {
    assertNotNull(cd);
  }

  /**
   * 该规则能够基于控制台的输出编写断言。 在这里， 我们断言SgtPeppers.play()方法的输出被发送到了控制台上
   */
  @Test
  public void play() {
    player.play();
    assertEquals(
        "Playing Sgt. Pepper's Lonely Hearts Club Band by The Beatles"+"\r\n",
        log.getLog());
  }

  /**
   * 我自己加的，StandardOutputStreamLog已经不建议使用，但是这个有点问题，问题可能出在初始化上
   */
/*  @Test
  public void play2() {
    player.play();
    assertEquals(
            "Playing Sgt. Pepper's Lonely Hearts Club Band by The Beatles"+"\r\n",
            log2.getLog());
  }*/

}
