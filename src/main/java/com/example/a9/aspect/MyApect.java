package com.example.a9.aspect;

//@Component

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyApect {
   @Before("within(com.example.a9.service.*)")
    public void before() {
        System.out.println("before");
    }
}
