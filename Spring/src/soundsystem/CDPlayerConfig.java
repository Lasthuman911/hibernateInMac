package soundsystem;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * /**
 * 组件扫描默认是不启用的。 我们还需要显式配置一下Spring， 从而命令它去寻找带
 * 有@Component注解的类， 并为其创建bean.
 * 如果没有其他配置的话， @ComponentScan默认会扫描与配置类相同的包
 * @ComponentScan(basePackageClasses = {CDPlayerConfig.class,MediaPlayer.class})----推荐使用，便于重构
 * @ComponentScan(basePackages = {"soundsystem","sia.knights"})
 * */
@Configuration
@ComponentScan
public class CDPlayerConfig { 
}
