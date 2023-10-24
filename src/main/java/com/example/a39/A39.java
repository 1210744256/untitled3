package com.example.a39;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

@Configuration
//@SpringBootApplication
public class A39 {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        SpringApplication.run(A39.class,args);
//        会调用SpringApplication的构造
        System.out.println("1. 演示获取Bean Definition源");
        SpringApplication springApplication = new SpringApplication(A39.class);
        springApplication.setSources(Set.of("classpath:spring.xml"));
        System.out.println("2. 演示推断应用类型");
        Method deduceFromClasspath = WebApplicationType.class.getDeclaredMethod("deduceFromClasspath");
        deduceFromClasspath.setAccessible(true);
        Object invoke = deduceFromClasspath.invoke(null);
        System.out.println("应用类型为:"+invoke);
        System.out.println("3. 演示ApplicationContext 初始化器");
        springApplication.addInitializers(applicationContext -> {
            if (applicationContext instanceof GenericApplicationContext gnc) {
                gnc.registerBean("bean03", Bean03.class);
            }
        });
        System.out.println("4. 演示监听器与事件");
        springApplication.addListeners(event -> {
            System.out.println("\t事件为:"+event.getClass());
        });
        System.out.println("5. 演示主类推断");
        Method deduceMainApplicationClass = SpringApplication.class.getDeclaredMethod("deduceMainApplicationClass");
        deduceMainApplicationClass.setAccessible(true);
        System.out.println("主类是"+deduceMainApplicationClass.invoke(springApplication));


//      通过调用run方法获取spring容器
        ConfigurableApplicationContext context = springApplication.run(args);
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println("name:"+name+"\t来源"+context.getBeanFactory().getBeanDefinition(name).getResourceDescription());
        }
        context.close();
    }
    @Component
    public static class Bean01{

    }
    @Component
    public static class Bean02{

    }
    public static class Bean03{}
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
        return new TomcatServletWebServerFactory();
    }
}
