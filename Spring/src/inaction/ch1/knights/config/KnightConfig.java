package inaction.ch1.knights.config;

import inaction.ch1.knights.Knight;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import inaction.ch1.knights.BraveKnight;
import inaction.ch1.knights.Quest;
import inaction.ch1.knights.SlayDragonQuest;

@Configuration
public class KnightConfig {

  @Bean
  public Knight knight() {
    return new BraveKnight(quest());
  }
  
  @Bean
  public Quest quest() {
    return new SlayDragonQuest(System.out);
  }

}
