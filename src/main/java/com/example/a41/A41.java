package com.example.a41;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

public class A41 {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
//        不允许重名bean的覆盖
        context.getDefaultListableBeanFactory().setAllowBeanDefinitionOverriding(false);
        context.registerBean(MyConfig.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
    }

    @Configuration
    @Import({MyImportSelector.class})
    public static class MyConfig {

    }

    public static class MyImportSelector implements DeferredImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>");
            List<String> loadFactoryNames = SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class, null);
            for (String loadFactoryName : loadFactoryNames) {
                System.out.println(loadFactoryName);
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
            List<String> myImportSelectors = SpringFactoriesLoader.loadFactoryNames(MyImportSelector.class, null);
            System.out.println(myImportSelectors);
            return myImportSelectors.toArray(new String[0]);
//            return new String[0];
        }
    }

    //    第三方配置类
    @Configuration
    public static class AutoConfiguration1{
        @Bean
//        当bean缺失时才添加
        @ConditionalOnMissingBean
        public Bean1 bean1(){
            return new Bean1();
        }
    }
    @Configuration
    public static class AutoConfiguration2{

        @Bean
        @ConditionalOnMissingBean
        public Bean2 bean2(){
            return new Bean2();
        }
    }
    static class Bean1 {

    }

    static class Bean2 {

    }
}
