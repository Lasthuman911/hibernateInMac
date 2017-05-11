package spittr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Name: admin
 * Date: 2017/4/14
 * Time: 16:00
 */
@Configuration
@EnableWebMvc//启用SpringMVC
@ComponentScan("spittr.web")//启用组件扫描
public class WebConfig
        extends WebMvcConfigurerAdapter {
    /**
     * 配置JSP视图解析器
     * 它会查找JSP文件， 在查找的时候， 它会在视图名称上加一个特定的前缀和
     * 后缀（ 例如， 名为home的视图将会解析为/WEB-INF/views/home.jsp）
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");
        resolver.setExposeContextBeansAsAttributes(true);
        return resolver;
    }

    /**
     * 配置静态资源的处理
     * 我们要求DispatcherServlet将对静态资源的请求转发到Servlet容器中默认的Servlet上，
     * 而不是使用DispatcherServlet本身来处理此类请求。
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
