package inaction.ch3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MagicConfig {
  /**
   * Spring 4引入了一个新的
   * @Conditional 注解， 它可以用到带有@Bean注解的方法上。 如果给定的条件计算结果
   * 为true， 就会创建这个bean， 否则的话， 这个bean会被忽略
   *
   * @Conditional 的类可以是任意实现了Condition接口的类型。 可以看出来， 这个接口实现起来很简单直接， 只需提供matches()方法的实现即可
   *
   */
  @Bean
  @Conditional(MagicExistsCondition.class)
  public MagicBean magicBean() {
    return new MagicBean();
  }
  
}