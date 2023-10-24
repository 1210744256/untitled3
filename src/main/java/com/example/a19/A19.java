package org.springframework.aop.framework.autoproxy;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

public class A19 {
    public static void main(String[] args) throws Throwable {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(Config.class);
        //        解析切面类的后处理器
//        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
        context.refresh();
        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        List<Advisor> advisors = creator.findEligibleAdvisors(Target.class, "");
        for (Advisor advisor : advisors) {
            System.out.println(advisor);
        }
        Target target = new Target();
        Object proxy = creator.wrapIfNecessary(target, "", "");
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvisors(advisors);
        List<Object> methodInterceptors = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getMethod("foo", int.class), Target.class);
        for (Object methodInterceptor : methodInterceptors) {
            System.out.println(methodInterceptor);
        }
//        通过调用链来调用
        MethodInvocation methodInvocation=new ReflectiveMethodInvocation
                (proxy,target,Target.class.getMethod("foo", int.class),new Object[]{10},Target.class,methodInterceptors){};
        methodInvocation.proceed();
    }

    @org.aspectj.lang.annotation.Aspect
    static class Aspect {
        @Before("execution(* foo(..))")
        public void before1() {
            System.out.println("before1");
        }
//动态通知调用
        @Before("execution(* foo(..)) && args(x)")
        public void before2(int x) {
            System.out.println("before2:" + x);
        }

    }
    static class Target{
        public void foo(int x) {
            System.out.println("foo target"+x);
        }
    }

    @Configuration
    static class Config {
        @Bean
        public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
            return new AnnotationAwareAspectJAutoProxyCreator();
        }

        @Bean
        public Aspect aspect() {
            return new Aspect();
        }
    }
}
