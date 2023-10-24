package org.springframework.boot;

import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.RandomValuePropertySourceEnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.core.env.PropertySource;

public class Step5 {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Step5.class);
        ApplicationEnvironment env = new ApplicationEnvironment();
//        第五部会准备一些后处理器去获取一些properties文件
        System.out.println("》》》》》》》》》》》》》》增强前");
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }
        ConfigDataEnvironmentPostProcessor postProcessor1 = new ConfigDataEnvironmentPostProcessor(new DeferredLogs(),new DefaultBootstrapContext());
        postProcessor1.postProcessEnvironment(env,springApplication);
        System.out.println("》》》》》》》》》》》》》增强后");
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }
        RandomValuePropertySourceEnvironmentPostProcessor postProcessor2 = new RandomValuePropertySourceEnvironmentPostProcessor(springApplication.getApplicationLog());
        postProcessor2.postProcessEnvironment(env,springApplication);
        System.out.println("》》》》》》》》》》》》》增强后");
        for (PropertySource<?> ps : env.getPropertySources()) {
            System.out.println(ps);
        }
        System.out.println("》》》》》》》》》》》");
        System.out.println(env.getProperty("random.int"));
        System.out.println(env.getProperty("random.int"));
        System.out.println(env.getProperty("random.int"));
    }
}
