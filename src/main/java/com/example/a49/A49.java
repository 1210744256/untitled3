package com.example.a49;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Configuration
@Slf4j
public class A49 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A49.class);
        MyService myService = context.getBean(MyService.class);
        myService.doBusiness();

    }

    static abstract class AbstractApplicationEvent implements ApplicationEventMulticaster {


        @Override
        public void addApplicationListener(ApplicationListener<?> listener) {

        }

        @Override
        public void addApplicationListenerBean(String listenerBeanName) {

        }

        @Override
        public void removeApplicationListener(ApplicationListener<?> listener) {

        }

        @Override
        public void removeApplicationListenerBean(String listenerBeanName) {

        }

        @Override
        public void removeApplicationListeners(Predicate<ApplicationListener<?>> predicate) {

        }

        @Override
        public void removeApplicationListenerBeans(Predicate<String> predicate) {

        }

        @Override
        public void removeAllListeners() {

        }

        @Override
        public void multicastEvent(ApplicationEvent event) {

        }

        @Override
        public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {

        }
    }

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster(ConfigurableApplicationContext context, ThreadPoolTaskExecutor executor) {
        return new AbstractApplicationEvent() {
            private List<GenericApplicationListener> listeners = new ArrayList<>();

            //            添加事件监听器
            @Override
            public void addApplicationListenerBean(String name) {
                System.out.println(name);
//
//                根据名字获取监听器
                ApplicationListener listener = context.getBean(name, ApplicationListener.class);
//                获取该监听器支持的事件类型
                ResolvableType type = ResolvableType.forClass(listener.getClass()).getInterfaces()[0].getGeneric();
                System.out.println(type);
                GenericApplicationListener genericApplicationListener = new GenericApplicationListener() {
                    //是否支持本事件类型
                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
                        listener.onApplicationEvent(event);
                    }

                    @Override
                    public boolean supportsEventType(ResolvableType eventType) {
//                        右边的类型是否能赋值给左边的类型
                        return type.isAssignableFrom(eventType);
                    }
                };
                listeners.add(genericApplicationListener);
            }

            @Override
            public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
//                遍历所有监听器
                for (GenericApplicationListener listener : listeners) {
                    if (listener.supportsEventType(ResolvableType.forClass(event.getClass()))) {
                        executor.submit(() -> {
                            listener.onApplicationEvent(event);
                        });
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

    @Component
    static class SmsEventListener implements ApplicationListener<MyEvent> {

        @Override
        public void onApplicationEvent(MyEvent event) {
            System.out.println(event);
            log.debug("短信事件");
        }
    }

    @Component
    static class EmailEventListener implements ApplicationListener<MyEvent> {

        @Override
        public void onApplicationEvent(MyEvent event) {
            log.debug("处理邮件");
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
//    @Bean
//    public SimpleApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor threadPoolTaskExecutor){
//        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
//        eventMulticaster.setTaskExecutor(threadPoolTaskExecutor);
//        return eventMulticaster;
//    }
}
