package org.springframework.boot;

import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

public class Step4 {
    public static void main(String[] args) throws IOException {
//        第三步会准备applicationEnvironment
        ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment();
//        把命令行的参数加进去
        applicationEnvironment.getPropertySources()
                .addFirst(new SimpleCommandLinePropertySource(args));
//        加入ConfiurationProperties的配置
        ConfigurationPropertySources.attach(applicationEnvironment);
        applicationEnvironment.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("application.properties")));

        for (PropertySource<?> propertySource : applicationEnvironment.getPropertySources()) {
            System.out.println(propertySource);
        }
        System.out.println(applicationEnvironment.getProperty("java_home"));
    }
}
