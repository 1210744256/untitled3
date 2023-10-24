package org.springframework.boot;

import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.core.env.PropertySource;

public class Step5_2 {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Step5_2.class);
        ApplicationEnvironment env = new ApplicationEnvironment();
        EventPublishingRunListener publisher = new EventPublishingRunListener(springApplication, args);
        System.out.println("》》》》》》》》》》》》》》增强前");
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }
//        发布事件后就会调用监听器去调用那些环境后处理器
        publisher.environmentPrepared(new DefaultBootstrapContext(),env);
        System.out.println("》》》》》》》》》》》》》增强后");
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }

    }
}
