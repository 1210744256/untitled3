package com.example.a46;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Field;

@SpringBootApplication
public class A46 {
    public static void main(String[] args) throws NoSuchFieldException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(Bean2.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        StandardBeanExpressionResolver resolver1 = new StandardBeanExpressionResolver();
        beanFactory.setBeanExpressionResolver(resolver1);
//自动注入的解析器
        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
//        获取对象的属性然后进行注入
        Field home = Bean1.class.getDeclaredField("home");
//        通过自动注入解析器去获取@value上的内容
//        依赖描述   是否是必须注入
        resolver.setBeanFactory(beanFactory);
        test1(context, resolver, home);
        Field value = Bean1.class.getDeclaredField("value");
        test2(context,resolver,value);
        test3(context,resolver,Bean1.class.getDeclaredField("bean2"));
        test3(context,resolver,Bean1.class.getDeclaredField("home2"));

    }

    private static void test1(AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver, Field home) {
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(home,false);
        Object suggestedValue = resolver.getSuggestedValue(dependencyDescriptor);
        System.out.println(suggestedValue);
        System.out.println(suggestedValue.getClass());
//        通过容器去获取环境对象然后解析@value里面的内容
//        解析${}
        System.out.println(context.getEnvironment().resolvePlaceholders(suggestedValue.toString()));
    }
    private static void test2(AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver, Field home) {
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(home,false);
        Object suggestedValue = resolver.getSuggestedValue(dependencyDescriptor);
        System.out.println(suggestedValue);
        System.out.println(suggestedValue.getClass());
//        通过容器去获取环境对象然后解析@value里面的内容
        System.out.println(context.getEnvironment().resolvePlaceholders(suggestedValue.toString()));
//        用spring提供的converter进行数据绑定
        Object value = context.getBeanFactory().getTypeConverter().convertIfNecessary(suggestedValue, dependencyDescriptor.getDependencyType());
        System.out.println(value.getClass());
        System.out.println(value);
    }
    private static void test3(AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver, Field home) {
//        先获取注解上的值
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(home,false);
        String value = resolver.getSuggestedValue(dependencyDescriptor).toString();
        System.out.println(value);
//     进行${}的解析
         //获取环境对象
        ConfigurableEnvironment env = context.getEnvironment();
        value = env.resolvePlaceholders(value);
        System.out.println(value);
//        进行#{}的解析
            //获取bean表达式解析器进行解析
        System.out.println(context.getBeanFactory().getBeanExpressionResolver());
        Object evaluate = context.getBeanFactory().getBeanExpressionResolver().evaluate(value, new BeanExpressionContext(context.getBeanFactory(), null));
//        看是否需要进行类型转换
        Object result = context.getBeanFactory().getTypeConverter().convertIfNecessary(evaluate, dependencyDescriptor.getDependencyType());
        System.out.println(result);
        System.out.println(result.getClass());
    }
}
