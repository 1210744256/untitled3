package com.example.a44;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

public class A44 {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//        注解扫描的关键组件
        ClassPathBeanDefinitionScanner scanner=new ClassPathBeanDefinitionScanner(beanFactory);
        scanner.scan("com.example.a44");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }
}
