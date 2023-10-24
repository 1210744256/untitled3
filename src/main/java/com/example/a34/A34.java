package com.example.a34;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class A34 {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(MyConfig.class);
    }
}
