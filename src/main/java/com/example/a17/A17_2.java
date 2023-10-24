package com.example.a17;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class A17_2 {
    public static void main(String[] args) {
        List<Advisor> advisors=new ArrayList<>();
//        高级切面如何转换为低级切面
//        先拿到对应切面类上的所有方法
        AspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect1());
        for (Method method : Aspect1.class.getDeclaredMethods()) {
//            拿到方法上所有的和切点有关的注解上的信息
            if (method.isAnnotationPresent(Before.class)) {
//切点表达式
                String expression = method.getAnnotation(Before.class).value();
                AspectJExpressionPointcut asj = new AspectJExpressionPointcut();
                asj.setExpression(expression);
//                前置通知类
                AspectJMethodBeforeAdvice beforeAdvice = new AspectJMethodBeforeAdvice(method,asj,factory);
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(asj,beforeAdvice);
                advisors.add(advisor);
            }
        }
        for (Advisor advisor : advisors) {
            System.out.println(advisor);
        }
    }

    @Aspect
//    @Slf4j
    public static class Aspect1 {
        @Before("execution(* foo())")
        public void before() {
            log.debug("before advice1");
        }

        @After("execution(* foo())")
        public void after() {
            log.debug("after advice2");
        }
    }
}
