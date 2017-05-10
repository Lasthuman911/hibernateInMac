package javaconfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soundsystem.CDPlayer;
import soundsystem.CompactDisc;
import soundsystem.SgtPeppers;

@Configuration
public class CDPlayerConfig {
  /**
   * bean的ID与带有@Bean注解的方法名是一样的
   * @return
   */
  @Bean
  public CompactDisc compactDisc() {
    return new SgtPeppers();
  }
  
  @Bean
  public CDPlayer cdPlayer(CompactDisc compactDisc) {
    return new CDPlayer(compactDisc);
  }

/*  @Bean
  public CDPlayer cdPlayer() {
    return new CDPlayer(compactDisc());
  }*/

}
