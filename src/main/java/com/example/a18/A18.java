package org.springframework.aop.framework;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class A18 {
    public static void main(String[] args) throws Throwable {
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
            }else if(method.isAnnotationPresent(After.class)){
                //切点表达式
                String expression = method.getAnnotation(After.class).value();
                AspectJExpressionPointcut asj = new AspectJExpressionPointcut();
                asj.setExpression(expression);
//                前置通知类
                AspectJAfterAdvice beforeAdvice = new AspectJAfterAdvice(method,asj,factory);
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(asj,beforeAdvice);
                advisors.add(advisor);
            }else if(method.isAnnotationPresent(Around.class)){
                //切点表达式
                String expression = method.getAnnotation(Around.class).value();
                AspectJExpressionPointcut asj = new AspectJExpressionPointcut();
                asj.setExpression(expression);
//                前置通知类
                AspectJAroundAdvice beforeAdvice = new AspectJAroundAdvice(method,asj,factory);
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(asj,beforeAdvice);
                advisors.add(advisor);
            }else if(method.isAnnotationPresent(AfterReturning.class)){
                //切点表达式
                String expression = method.getAnnotation(AfterReturning.class).value();
                AspectJExpressionPointcut asj = new AspectJExpressionPointcut();
                asj.setExpression(expression);
//                前置通知类
                AspectJAfterReturningAdvice beforeAdvice = new AspectJAfterReturningAdvice(method,asj,factory);
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(asj,beforeAdvice);
                advisors.add(advisor);
            }else if(method.isAnnotationPresent(AfterThrowing.class)){
                //切点表达式
                String expression = method.getAnnotation(AfterThrowing.class).value();
                AspectJExpressionPointcut asj = new AspectJExpressionPointcut();
                asj.setExpression(expression);
//                前置通知类
                AspectJAfterThrowingAdvice beforeAdvice = new AspectJAfterThrowingAdvice(method,asj,factory);
                DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(asj,beforeAdvice);
                advisors.add(advisor);

            }
        }
        for (Advisor advisor : advisors) {
            System.out.println(advisor);
        }
        System.out.println(">>>>>>>>>>>>>>>>>");
        //代理工厂
        ProxyFactory proxyFactory = new ProxyFactory();
        T1 target = new T1();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvice(ExposeInvocationInterceptor.INSTANCE);
        proxyFactory.addAdvisors(advisors);
        List<Object> interceptionAdvice = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(T1.class.getMethod("foo"), T1.class);
        for (Object o : interceptionAdvice) {
            System.out.println(o);
        }
//        创建调用链并调用
//        因为内部可能会使用methodinvocation所以需要加入一个公共的环绕通知使用spring暴露在外面的exposeInvocationInterceptor底层使用了ThreadLocal
        System.out.println(">>>>>>>>>>>>>>>>>");
        MethodInvocation methodInvocation=new ReflectiveMethodInvocation
                (null,target,T1.class.getMethod("foo"),new Object[0],T1.class,interceptionAdvice);
        methodInvocation.proceed();
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
        @AfterReturning("execution(* foo())")
        public void afterReturing() {
            log.debug("afterReturing advice3");
        }
        @AfterThrowing("execution(* foo())")
        public void afterThrowing() {
            log.debug("afterThrowing advice4");
        }
    }
    static class T1 {
        public void foo() {
            System.out.println("target foo");
        }

        public void bar() {
            System.out.println("bar");
        }
    }
}
