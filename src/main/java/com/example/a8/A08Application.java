package com.example.a8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class A08Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A08Application.class,args);
        E bean = context.getBean(E.class);
        System.out.println(bean.getF1());
        System.out.println(bean.getF1());
        System.out.println(bean.getF2());
        System.out.println(bean.getF2());
        System.out.println(bean.getF3());
        System.out.println(bean.getF3());
        System.out.println(bean.getF4());
        System.out.println(bean.getF4());
    }
}
