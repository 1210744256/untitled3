package com.example.a4;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication
public class A04Application {
    public static void main(String[] args) {
//        GenericApplicationContext是一个干净的容器
        GenericApplicationContext context=new GenericApplicationContext();
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);
        context.registerBean("bean4", Bean4.class);
//        执行beanFactory后处理器，添加Bean后处理器,创建单例bean
//        加一些后处理器
//        给核心beanfactory加入这个之后@value注解生效
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
//        解析@Autowired注解
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
//        解析@Resource@PostConstruct@PreDestroy注解
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        //用来解析ConfigurtionProperties注解
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());
        context.refresh();
    }
}
