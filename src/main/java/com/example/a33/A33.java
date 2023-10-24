package com.example.a33;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class A33 {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(MyConfig.class);
//        MyConfig.MyHandlerMapping handlerMapping = context.getBean(MyConfig.MyHandlerMapping.class);
//        handlerMapping.

    }
}
