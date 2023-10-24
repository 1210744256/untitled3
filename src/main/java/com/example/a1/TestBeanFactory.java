package com.example.a1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

@Slf4j
public class TestBeanFactory {
    public static void main(String[] args) {
//        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
////        创建BeanDefinition加入bean工厂中
//        AbstractBeanDefinition singleton = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
////        注册一个BeanDefinition
//        beanFactory.registerBeanDefinition("config", singleton);
////        给beanfactory加入后处理器
//        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
////        通过类型拿到所有的beanFactory后处理器解析那些注解
////        键就是他的名字我们需要拿到值
////        beanFactory后处理器主要是补充一些bean
//        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().stream().forEach(
//                beanFactoryPostProcessor -> beanFactoryPostProcessor.postProcessBeanFactory(beanFactory)
//        );
////        Bean后处理器作用于bean的生命周期如@Autowired,@Value
//        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanFactory::addBeanPostProcessor);
////        获取所有注册的BeanDefinition的名字
//        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }
////        初始化所有单例bean
//        beanFactory.preInstantiateSingletons();
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println(beanFactory.getBean(Bean1.class).getBean2());
testAnnoatationConfigServletWebServerApplicationContext();
    }
//    较为经典的容器内嵌一个tomact容器，基于java配置类来创建，用于web环境
    private static void testAnnoatationConfigServletWebServerApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

    }
    @Configuration
    public static class WebConfig{
        @Bean
//        servletwebserver的一个工厂
        public ServletWebServerFactory servletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }
        @Bean
//        核心类dispacherservlet
        public DispatcherServlet dispatcherServlet(){
            return new DispatcherServlet();
        }
//        注册dispatcherservlet到容器中
        @Bean
        public DispatcherServletRegistrationBean servletRegistrationBean(){
            return new DispatcherServletRegistrationBean(dispatcherServlet(),"/");
        }
        @Bean("/hello")
        public Controller get(){
            return ((res,rps)->{
                rps.getWriter().print("hello");
                return null;
            });
        }

    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    static class Bean1 {
        @Autowired
        private Bean2 bean2;

        public Bean1() {
            log.debug("bean1正在被创建");
        }

        public Bean2 getBean2() {
            return bean2;
        }
    }

    static class Bean2 {
        public Bean2() {
            log.debug("bean2正在被构建");
        }
    }
}
