package com.example.a39;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.*;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

public class A39_3 {
    public static void main(String[] args) throws Exception {
        Math.ceil(5);
        SpringApplication springApplication = new SpringApplication();
        springApplication.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(ConfigurableApplicationContext applicationContext) {
                System.out.println("执行初始化器增强");
            }
        });
//
        System.out.println(">>>>>>>>>>>>>>>>>2封装启动 args");
        DefaultApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
        System.out.println(">>>>>>>>>>>>>>>>8创建容器");
//        根据传入的应用类型创建容器
        GenericApplicationContext context = createApplicationContext(WebApplicationType.SERVLET);
        System.out.println(">>>>>>>>>>>>>>>9准备容器");
        for (ApplicationContextInitializer initializer : springApplication.getInitializers()) {
            initializer.initialize(context);
        }
        System.out.println(">>>>>>>>>>>>>>>10加载bean定义");
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        annotatedBeanDefinitionReader.register(Config.class);
        xmlBeanDefinitionReader.loadBeanDefinitions(new ClassPathResource("a03.xml"));
        System.out.println(">>>>>>>>>>>>>>>11refresh容器");
        context.refresh();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println("name:"+beanDefinitionName+"\t来源是:"+context.getBeanFactory().getBeanDefinition(beanDefinitionName).getResourceDescription());
        }
        System.out.println(">>>>>>>>>>>>>>>12执行runner");
        for (CommandLineRunner runner : context.getBeansOfType(CommandLineRunner.class).values()) {
            runner.run(args);
        }
        for (ApplicationRunner runner : context.getBeansOfType(ApplicationRunner.class).values()) {
            runner.run(applicationArguments);
        }

    }
    public static GenericApplicationContext createApplicationContext(WebApplicationType type) {
        GenericApplicationContext context = null;
        switch (type){
            case SERVLET -> context=new AnnotationConfigServletWebServerApplicationContext();
            case REACTIVE -> context=new AnnotationConfigReactiveWebServerApplicationContext();
            case NONE -> context=new AnnotationConfigApplicationContext();
        }
        return context;
    }
    static class Bean1{

    }
    static class Bean2{

    }
    static class Bean3{

    }
    static class Bean4{

    }
    static class Bean5{

    }

    static class Config{
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }
        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }
        @Bean
        public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }
        @Bean
        public CommandLineRunner commandLineRunner(){
            return new CommandLineRunner() {
                @Override
                public void run(String... args) throws Exception {
                    System.out.println("CommandLineRunner启动"+ Arrays.toString(args));
                }
            };
        }
        @Bean
        public ApplicationRunner applicationRunner(){
            return new ApplicationRunner() {
                @Override
                public void run(ApplicationArguments args) throws Exception {
                    System.out.println("ApplicationRunner执行"+Arrays.toString(args.getSourceArgs()));
                    System.out.println(args.getOptionNames());
                    System.out.println(args.getNonOptionArgs());
                }
            };
        }
    }
}
