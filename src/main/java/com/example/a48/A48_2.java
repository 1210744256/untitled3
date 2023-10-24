package com.example.a48;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
public class A48_2 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A48_2.class);
        MyService myService = context.getBean(MyService.class);
        myService.doBusiness();
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

    @Component
    static class SmsService {
        @EventListener
        public void doSms(MyEvent myEvent){
            log.debug("短信业务");
        }
    }
    @Component
    static class EmailService {
        @EventListener
        public void doSms(MyEvent myEvent){
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
    public SimpleApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor threadPoolTaskExecutor){
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(threadPoolTaskExecutor);
        return eventMulticaster;
    }
}
