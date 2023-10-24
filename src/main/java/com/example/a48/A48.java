package com.example.a48;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
public class A48 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A48.class);
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
    static class MyService{
//        事件发布器
        @Autowired
        private ApplicationEventPublisher publisher;
        public void doBusiness(){
            log.debug("主流业务");
            publisher.publishEvent(new MyEvent("MyService.doBusiness()"));
        }
    }
    @Component
    static class SmsEventListener implements ApplicationListener<MyEvent>{

        @Override
        public void onApplicationEvent(MyEvent event) {
            System.out.println(event);
            log.debug("短信事件");
        }
    }
    @Component
    static class EmailEventListener implements ApplicationListener<MyEvent>{

        @Override
        public void onApplicationEvent(MyEvent event) {
            log.debug("处理邮件");
        }
    }
}
