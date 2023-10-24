package com.example.a47;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class A47_2 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A47_2.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        System.out.println("array>>>>>>>>>>>>>");
        testArray(beanFactory);
        System.out.println("list>>>>>>>>>>>>>>");
        testList(beanFactory);
        System.out.println("特殊类型>>>>>>>>>>>>>");
        testTeshu(beanFactory);
        System.out.println("泛型解析>>>>>>>>>>>");
        testGeneric(beanFactory, resolver);
        System.out.println(">>>>>>>>>>@Qualifier的解析");
        DependencyDescriptor dd5 = new DependencyDescriptor(Bean1.class.getDeclaredField("service"), false);
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, dd5.getDependencyType());
        for (String name : names) {
            BeanDefinition bd = beanFactory.getMergedBeanDefinition(name);
            System.out.println(bd);
            System.out.println(beanFactory.getBeanDefinition(name));
//            System.out.println(bd);
            if (resolver.isAutowireCandidate(new BeanDefinitionHolder(bd,name),dd5)) {
                System.out.println(name);
                System.out.println(dd5.resolveCandidate(name,dd5.getDependencyType(),beanFactory));
                break;
            }
        }

    }

    private static void testGeneric(DefaultListableBeanFactory beanFactory, ContextAnnotationAutowireCandidateResolver resolver) throws NoSuchFieldException {
        //先获取DependencyDescriptor
        DependencyDescriptor dd4 = new DependencyDescriptor(Bean1.class.getDeclaredField("teacherDao"), false);
//        获取类型
        Class<?> type = dd4.getDependencyType();
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type);
        for (String name : names) {
            BeanDefinition bd = beanFactory.getBeanDefinition(name);
//            通过对比beandefinition与DependencyDescriptor的泛型是否匹配
            if (resolver.isAutowireCandidate(new BeanDefinitionHolder(bd,name),dd4)) {
                System.out.println(name);
//                获取实例对象
                System.out.println(dd4.resolveCandidate(name,type, beanFactory));
                break;
            }
        }
    }

    private static void testTeshu(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException, IllegalAccessException {
        DependencyDescriptor dd3 = new DependencyDescriptor(Bean1.class.getDeclaredField("applicationContext"), false);
//        特殊类型要在工厂的resolvableDependencies中获取
        Field resolvableDependencies = DefaultListableBeanFactory.class.getDeclaredField("resolvableDependencies");
        resolvableDependencies.setAccessible(true);
        Map<Class<?>, Object> map = (Map<Class<?>, Object>) resolvableDependencies.get(beanFactory);
        map.forEach((k,v)-> System.out.println("key:"+k+"\tvalue:"+v));
        System.out.println("特殊类型>>>>>>>>>>>>>");
        for (Map.Entry<Class<?>, Object> entry : map.entrySet()) {
//            如果左边的类型能赋值给右边的类型
            if (entry.getKey().isAssignableFrom(dd3.getDependencyType())) {
                System.out.println(entry.getKey());
                break;
            }
        }
    }

    private static void testList(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dd2 = new DependencyDescriptor(Bean1.class.getDeclaredField("serviceList"), false);
//        判断是不是list集合
        if (dd2.getDependencyType()== List.class) {
//            获取list里的泛型
            Class<?> resolve = dd2.getResolvableType().getGeneric().resolve();
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, resolve);
            List<Object> list=new ArrayList<>();
            for (String name : names) {
                list.add(dd2.resolveCandidate(name,resolve, beanFactory));
            }
            System.out.println(list);
        }
    }

    private static void testArray(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dd1 = new DependencyDescriptor(Bean1.class.getDeclaredField("services"), false);
//        如果是数组类型
        if(dd1.getDependencyType().isArray()){
            Class<?> componentType = dd1.getDependencyType().getComponentType();
            System.out.println(componentType);
//            用一个spring工具类查找出所有和这个类型相关的bean的名字
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, componentType);
            List<Object> list=new ArrayList<>();
            for (String name : names) {
                Object o = dd1.resolveCandidate(name, componentType, beanFactory);
                list.add(o);
            }
//            将其转换为数组类型
            Object array = beanFactory.getTypeConverter().convertIfNecessary(list, dd1.getDependencyType());
            System.out.println(array);
        }
    }

    @Component
    static class Bean1{
        @Autowired
        private Service[] services;
        @Autowired
        private List<Service> serviceList;
        @Autowired
        private GenericApplicationContext applicationContext;
        @Autowired
        private Dao<Teacher> teacherDao;
        @Autowired
        @Qualifier("service1")
        private Service service;
    }
    interface Service{

    }

    @Component("service1")
    static class Service1 implements Service{

    }
    @Component("service2")
    static class Service2 implements Service{

    }
    @Component("service3")
    static class Service3 implements Service{

    }
    interface Dao<T>{

    }
    static class Student{

    }
    static class Teacher{

    }
    @Component
    static class Dao1 implements Dao<Student>{}
    @Component
    static class Dao2 implements Dao<Teacher>{}
}
