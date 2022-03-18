package vane.data.handler.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  private SpringContextUtil() {
    System.out.println("constructor - SpringContextUtil()");
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    System.out.println("applicationContext : " + applicationContext);
    SpringContextUtil.applicationContext = applicationContext;
  }

  public static <T> T getBean(Class<T> clazz) {
    return applicationContext.getBean(clazz);
  }
}
