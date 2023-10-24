package com.example.a1;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
public class ShowApplication {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        ConfigurableApplicationContext context = SpringApplication.run(ShowApplication.class, args);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
//        ApplicationContext
        singletonObjects.setAccessible(true);
        System.out.println(singletonObjects);
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        map.entrySet().stream().filter(e -> e.getKey().startsWith("chen")).forEach(e -> System.out.println(
                e.getKey() + "=" + e.getValue()
        ));
//        application子接口之messageResource接口处理国际化资源的能力
        String hi = context.getMessage("hi", null, Locale.CHINA);
        System.out.println(hi);
        System.out.println(context.getMessage("hi", null, Locale.JAPANESE));
        System.out.println(context.getMessage("hi", null, Locale.ENGLISH));
//        application子接口之Resource
        Resource[] resources = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource : resources) {
            System.out.println(resource);
        }
//        environment用来获取配置信息
        System.out.println(context.getEnvironment().getProperty("java_home"));
//        最后一个接口用来发送事件接收事件
        context.publishEvent(new UserRegisteredEvent(context));
        Chenge chenge = (Chenge) context.getBean("chenge");
        chenge.adjk();
    }

}
