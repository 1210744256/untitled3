package com.example.a5;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            //        论后处理器是如何找到注解扫描注解并扫描包的
            ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
            //                        通过一个工具生成BeanDefinition的名字
            AnnotationBeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
            if (componentScan != null) {
                //            如果componentscan存在
                for (String basePackage : componentScan.basePackages()) {
                    //                System.out.println(basePackage);
                    //                根据路径来找到对应的包
                    String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
                    //                通过meta
                    CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    for (Resource resource : resources) {
                        //                    通过工厂获取资源上的标签数据信息
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
//                        System.out.println(metadataReader.getClassMetadata().getClassName());
//                        System.out.println("是否是@Componet注解"+metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName()));
//                        System.out.println("是否是@Componet的派生注解"+metadataReader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()));
                        System.out.println(resource);
                        //                    如果是component注解或者他的派生注解将这个类加入到bean工厂中
                        if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName()) ||
                                metadataReader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName())) {
                            AbstractBeanDefinition bd = BeanDefinitionBuilder.genericBeanDefinition(metadataReader.getClassMetadata().getClassName()).getBeanDefinition();
                            if (configurableListableBeanFactory instanceof DefaultListableBeanFactory beanFactory) {
//                                DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
                                String name = beanNameGenerator.generateBeanName(bd, beanFactory);
                                beanFactory.registerBeanDefinition(name, bd);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
