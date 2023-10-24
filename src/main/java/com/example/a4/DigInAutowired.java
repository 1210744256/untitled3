package com.example.a4;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
//        关于autowired的理解
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
//        可以解析#{}${}的后处理器
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);
//        注册单例bean
        beanFactory.registerSingleton("bean2",new Bean2());
        beanFactory.registerSingleton("bean3",new Bean3());
//
        Bean1 bean1 = new Bean1();
        System.out.println(bean1);
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor=new AutowiredAnnotationBeanPostProcessor();
//        给后处理器设置bean工厂
        autowiredAnnotationBeanPostProcessor.setBeanFactory(beanFactory);
        autowiredAnnotationBeanPostProcessor.postProcessProperties(null,bean1,"bean1");
        System.out.println(bean1);
//反射调用方法
//        先根据findAutowiringMetadata找到对象上关于有关注解的信息然后通过injection方法进行调用
        Method findAutowiringMetadata = autowiredAnnotationBeanPostProcessor.getClass().getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
//        获取bean1上的获取autowired,@valued的信息
        InjectionMetadata metadata = (InjectionMetadata) findAutowiringMetadata.invoke(autowiredAnnotationBeanPostProcessor, "bean1", Bean1.class, null);
        System.out.println(metadata);
        metadata.inject(bean1,"bean1",null);
        System.out.println(bean1);
        System.out.println("-----------------------");
//        injection的内部工作
//        case1:属性
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        DependencyDescriptor dependencyDescriptor = new DependencyDescriptor(bean3,false);
        Object o = beanFactory.resolveDependency(dependencyDescriptor, null, null, null);
        System.out.println(o);
//        case2:方法
        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dependencyDescriptor1 = new DependencyDescriptor(new MethodParameter(setBean2,0),false);
        Object o1 = beanFactory.resolveDependency(dependencyDescriptor1, null, null, null);
        System.out.println(o1);
//        @value的注入
        Method setValuese = Bean1.class.getDeclaredMethod("setValue", String.class);
        setValuese.setAccessible(true);
        DependencyDescriptor dependencyDescriptor2 = new DependencyDescriptor(new MethodParameter(setValuese, 0), false);
        Object o2 = beanFactory.resolveDependency(dependencyDescriptor2, null, null, null);
        System.out.println(o2);
    }
}
