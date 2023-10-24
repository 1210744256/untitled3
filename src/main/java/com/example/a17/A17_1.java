package com.example.a17;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

@Slf4j
public class A17_1 {
    public static void main(String[] args) {
//        有循环依赖的情况下在构造和依赖注入直接创建代理对象的
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(Config.class);
        context.refresh();
        Bean1 bean = context.getBean(Bean1.class);
        bean.foo();
    }
    @Configuration
    public static class Config {
        @Bean
        public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator(){
            return new AnnotationAwareAspectJAutoProxyCreator();
        }
//        解析autowired
        @Bean
        public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor(){
            return new AutowiredAnnotationBeanPostProcessor();
        }
//        解析postconstruct
        @Bean
        public CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor(){
            return new CommonAnnotationBeanPostProcessor();
        }
        @Bean
        public MethodInterceptor advice3(){
            return new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    System.out.println("before");
                    Object proceed = invocation.proceed();
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
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }
        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }
    }
    static class Bean1{
        public Bean1(){
            System.out.println("bean1创建");
        }
        public void foo(){

        }
        @Autowired
        public void setBean2(Bean2 bean2){

        }
        @PostConstruct
        public void init(){
            System.out.println("bean1初始化");
        }
    }
    static class Bean2{
        public Bean2(){
            System.out.println("bean2 创建");
        }
        @Autowired
        public void setBean1(Bean1 bean1){
            System.out.println(bean1);
        }
        @PostConstruct
        public void init(){
            System.out.println("bean2初始化");
        }
    }
}
