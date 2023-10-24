package com.example.a5;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.IOException;

public class MapperPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanFactory) throws BeansException {
//        扫描mapper所在包生成对应的mapper类
        try {
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources("classpath*:com/example/a5/mapper/**/*.class");
//            生成beanname的类
            AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
//            创建metadata元数据获取类的信息
            CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
            for (Resource resource : resources) {
                MetadataReader metadataReader = factory.getMetadataReader(resource);
                ClassMetadata classMetadata = metadataReader.getClassMetadata();
                if(classMetadata.isInterface()){
//                    如果是接口类型就创造一个beandefinition注入工厂中
                    AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean.class)
//                            给构造方法设置一个参数值
                            .addConstructorArgValue(classMetadata.getClassName())
                            .setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE)
                            .getBeanDefinition();
//                    单独创建一个beanDefinition用来生成名字
                    AbstractBeanDefinition beanDefinition1 = BeanDefinitionBuilder.genericBeanDefinition(classMetadata.getClassName()).getBeanDefinition();
                    String name = beanNameGenerator.generateBeanName(beanDefinition1, beanFactory);
                    beanFactory.registerBeanDefinition(name,beanDefinition);

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
