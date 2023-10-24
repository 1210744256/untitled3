package org.springframework.boot;

import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class Step3 {
    public static void main(String[] args) throws IOException {
//        第三步会准备applicationEnvironment
//        环境对象中默认会有系统属性和系统环境变量，系统属性可以通过添加虚拟机参数
        ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment();
//        把命令行的参数加进去
        applicationEnvironment.getPropertySources()
                .addFirst(new SimpleCommandLinePropertySource(args));
        applicationEnvironment.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("application.properties")));

        for (PropertySource<?> propertySource : applicationEnvironment.getPropertySources()) {
            System.out.println(propertySource);
        }
        System.out.println(applicationEnvironment.getProperty("java_home"));
    }
}
