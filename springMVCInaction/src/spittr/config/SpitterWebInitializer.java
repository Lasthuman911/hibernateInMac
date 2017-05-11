package spittr.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


/**
 * 扩
 展AbstractAnnotationConfigDispatcherServletInitializer的任意类都会自
 动地配置DispatcherServlet和Spring应用上下文， Spring的应用上下文会位于应用程序
 的Servlet上下文之中

 DispatcherServlet加载包含Web组件的bean， 如控制器、 视图解析器以及处理
 器映射， 而ContextLoaderListener要加载应用中的其他bean。 这些bean通常是驱动应
 用后端的中间层和数据层组件

 AbstractAnnotationConfigDispatcherServletInitializer会同时创
 建DispatcherServlet和ContextLoaderListener
 */
public class SpitterWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  /**
   * getRootConfigClasses()方法返回的带有@Configuration注解的类将会用来配置ContextLoaderListener创建的应用上下文中的bean
   * @return
   */
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[] { RootConfig.class };
  }

  /**
   * GetServletConfigClasses()方法返回的带有@Configuration注解的类将会用来定义DispatcherServlet应用上下文中的bean
   * @return
   */
  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] { WebConfig.class };
  }

  /**
   * 它会将一个或多个路径映射到DispatcherServlet上。 在本例中， 它映射的是“/”， 这表示它会是应用的默认Servlet。 它会处理进入应用的所有请求。
   * @return
   */
  @Override
  protected String[] getServletMappings() {
    return new String[] { "/" };
  }

}