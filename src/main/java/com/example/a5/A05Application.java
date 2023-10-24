package com.example.a5;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;
import java.util.Set;

public class A05Application {
    public static void main(String[] args) throws IOException {
//        创建一个干净的spring容器
        GenericApplicationContext context = new GenericApplicationContext();
//
        context.registerBean("config", Config.class);
        context.registerBean(ComponentScanPostProcessor.class);
        context.registerBean(MapperPostProcessor.class);
////        添加后处理器
//        context.registerBean(ConfigurationClassPostProcessor.class);
////        mybatis扫描底层都是MapperScannerConfiurer
//        context.registerBean(MapperScannerConfigurer.class, beanDefinition -> {
//            beanDefinition.getPropertyValues().add("basePackage","com.example");
//        });
//@Bean的解析
//        我们可以把每个配置类看成一个bean工厂
//        找到对应的元数据
        CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(new ClassPathResource("com/example/a5/Config.class"));
//        找到方法中有@bean的元数据
        Set<MethodMetadata> methods = metadataReader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
//        遍历methods将其加入bean工厂
        for (MethodMetadata method : methods) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
            String s = method.getAnnotationAttributes(Bean.class.getName()).get("initMethod").toString();
            if (s.length() > 0) {
                builder.setInitMethodName(s);
            }
//            在工厂中添加方法
            builder.setFactoryMethodOnBean(method.getMethodName(), "config");
//            自动装配
            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            context.getDefaultListableBeanFactory().registerBeanDefinition(method.getMethodName(), beanDefinition);
        }
//        后处理器生效以及创建单例bean
        context.refresh();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
    }
}
