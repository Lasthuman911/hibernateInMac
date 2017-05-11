package inaction.ch2.soundsystem;
import org.springframework.stereotype.Component;

/**
 * 这个简单的注解表明该类会作为组件类， 并告知Spring要为这个类创建bean。 没有必要显式配置SgtPeppers bean， 因为这个类使用了
 @Component注解， 所以Spring会为你把事情处理妥当,默认情况下这个bean所给定的ID为 sgtPeppers， 也就是将类名的第一个字母变为小写
 */
@Component
public class SgtPeppers implements CompactDisc {

  private String title = "Sgt. Pepper's Lonely Hearts Club Band";  
  private String artist = "The Beatles";
  
  public void play() {
    System.out.println("Playing " + title + " by " + artist);
  }
  
}
