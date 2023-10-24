package com.example.a12;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class A12 {
    public static void main(String[] args) {
//        两个切面概念
//        aspect:advice1+pointcut1,advice2+pointcut2
//        底层都会转换成一个个advisor
//        advisor:更精细度的切面，包含一个切点和通知

//      准备切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");
//        备好通知
        MethodInterceptor methodInterceptor= invocation -> {
            System.out.println("before");
            Object proceed = invocation.proceed();
            System.out.println("after");
            return proceed;
        };
//        准备好切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut,methodInterceptor);
//        advisor.setPointcut();
//        根据代理工厂创建代理
//        创建代理工厂代理的底层具体实现：a.proxyTargetClass=false,目标实现了接口，用jdk实现
//        b.proxyTargetClass=false,目标没有实现接口，用cglib实现
//        c.proxyTargetClass=true,目标总是用cglib实现
        Target1 target1 = new Target1();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(target1.getClass().getInterfaces());
//        proxyFactory.setProxyTargetClass(true);
        proxyFactory.setTarget(target1);
        proxyFactory.addAdvisor(advisor);
        I1 proxy = (I1) proxyFactory.getProxy();
        System.out.println(proxy.getClass());
        proxy.foo();
        proxy.bar();

    }
    interface I1{
        void foo();
        void bar();
    }
    static class Target1 implements I1{

        @Override
        public void foo() {
            System.out.println("foo");
        }

        @Override
        public void bar() {
            System.out.println("bar");
        }
    }
}
