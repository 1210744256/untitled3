package com.example.a48;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Configuration
@Slf4j
public class A48_3 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A48_3.class);
        MyService myService = context.getBean(MyService.class);
//        根据容器获得所有bean的名字

//        根据类型找到类里的所有方法看是否有@MyListener注解

        myService.doBusiness();
        context.close();
    }

    //    将处理@MyListener注解的方法封装到后处理器中
//    当所有的单例bean初始化好之后这个方法会被回调
    @Bean
    public SmartInitializingSingleton smartInitializingSingleton(ConfigurableApplicationContext context) {
        return new SmartInitializingSingleton() {
            @Override
            public void afterSingletonsInstantiated() {
                for (String name : context.getBeanDefinitionNames()) {
//            获取每个实例bean
                    Object bean = context.getBean(name);
//            看每个实例bean中的方法是否有@MyListener注解
                    for (Method method : bean.getClass().getMethods()) {
                        if (method.isAnnotationPresent(MyListener.class)) {
                            ApplicationListener<ApplicationEvent> applicationListener = new ApplicationListener<>() {
                                @Override
                                public void onApplicationEvent(ApplicationEvent event) {
                                    System.out.println(event);
//                        进行类型筛选
//                        获取到方法的参数
                                    Class<?> parameterType = method.getParameterTypes()[0];
//                        如果右边的值能赋值给左边的值
                                    if (parameterType.isAssignableFrom(event.getClass())) {
                                        try {
                                            method.invoke(bean, event);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            };
                            context.addApplicationListener(applicationListener);
                        }
                    }
                }
            }
        };
    }

    //    定义一个自己的事件
    static class MyEvent extends ApplicationEvent {

        public MyEvent(Object source) {
            super(source);
        }
    }

    @Component
    static class MyService {
        //        事件发布器
        @Autowired
        private ApplicationEventPublisher publisher;

        public void doBusiness() {
            log.debug("主流业务");
            publisher.publishEvent(new MyEvent("MyService.doBusiness()"));
        }
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyListener {

    }

    @Component
    static class SmsService {
        @MyListener
        public void doSms(MyEvent myEvent) {
            log.debug("短信业务");
        }
    }

    @Component
    static class EmailService {
        @MyListener
        public void doSms(MyEvent myEvent) {
            log.debug("邮件业务");
        }
    }

    //    定义一个线程池来异步处理事件
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(5);
        pool.setMaxPoolSize(10);
        pool.setQueueCapacity(20);
        return pool;
    }

    @Bean
    public SimpleApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(threadPoolTaskExecutor);
        return eventMulticaster;
    }
}
