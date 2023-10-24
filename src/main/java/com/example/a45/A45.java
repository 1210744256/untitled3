package com.example.a45;

import org.springframework.aop.framework.Advised;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class A45 {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(A45.class, args);
        Bean1 bean3 = context.getBean(Bean1.class);
        System.out.println(bean3);
        show(bean3);
//        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//        ClassPathBeanDefinitionScanner scanner=new ClassPathBeanDefinitionScanner(beanFactory);
//        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
//        scanner.scan("com.example.a45");
//        for (String name : beanFactory.getBeanDefinitionNames()) {
//            System.out.println(name);
//        }
//        Bean1 bean = beanFactory.getBean(Bean1.class);
//        System.out.println(bean);
//        show(bean);
    }
    public static void show(Bean1 proxy) throws Exception {
        System.out.println(proxy.initlized);
        System.out.println(proxy.bean2);
        if (proxy instanceof Advised advised) {
            Bean1 bean1 = (Bean1) advised.getTargetSource().getTarget();
            System.out.println(bean1.initlized);
            System.out.println(bean1.bean2);
        }
    }
}
