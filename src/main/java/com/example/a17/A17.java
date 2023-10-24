package org.springframework.aop.framework.autoproxy;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

@Slf4j
public class A17 {
    public static void main(String[] args) {
//创建一个干净的容器
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("aspect1",Aspect1.class);
        context.registerBean("config",Config.class);
//        context.registerBean("t1",T1.class);
//        context.registerBean("t1",T1.class);
//        配置类后处理器
        context.registerBean(ConfigurationClassPostProcessor.class);
//        解析切面类的后处理器
        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);
//
        context.refresh();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
//        findEligibleAdvisors根据目标类型收集所有有资格的advisors，会将高级切面转换为低级切面
        List<Advisor> advisors = creator.findEligibleAdvisors(T1.class, "sa");
        for (Advisor advisor : advisors) {
            System.out.println(advisor);
        }
//        wrapIfNecessary内部会调用findEligibleAdvisors如果返回的advisors不为空则说明需要创建代理
        Object t1 = creator.wrapIfNecessary(new T1(), "t1", "");
        System.out.println(t1.getClass());
        Object t2 = creator.wrapIfNecessary(new T2(), "t2", "");
        System.out.println(t2.getClass());
        ((T1) t1).foo();
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
    //    高级切面类


    //    低级切面类
    @Configuration
//    @Slf4j
    public static class Config {
        @Bean
        public MethodInterceptor advice3(){
            return new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    log.debug("before advice3");
                    Object proceed = invocation.proceed();
                    log.debug("after advice3");
                    return proceed;
                }
            };
        }
        @Bean
        public Advisor advisor(MethodInterceptor advice3){
            AspectJExpressionPointcut asj = new AspectJExpressionPointcut();
            asj.setExpression("execution(* foo())");
            DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(asj,advice3);
            return advisor;
        }
    }

    static class T1 {
        public void foo() {
            System.out.println("foo");
        }

        public void bar() {
            System.out.println("bar");
        }
    }
    static class T2{
        public void kd(){
            System.out.println("kd");
        }
    }
}
