package com.example.a42;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

public class A42 {
    public static void main(String[] args) {
//        ConditionalOnClass
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

    //    限制目标类型
    @Target({ElementType.TYPE_USE, ElementType.ANNOTATION_TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Conditional(MyCondition1.class)
    static @interface ConditionOnClass {
        //        是否存在
        boolean exists();

        String className();
    }

    //    自动配置底层
    public static class MyCondition1 implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
//            返回类上的一些属性
            Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionOnClass.class.getName());
            boolean exists = (boolean) attributes.get("exists");
            String className = (String) attributes.get("className");
            boolean present = ClassUtils.isPresent(className, null);
//        检查类路径下是否存在某个类
            return exists == present;
        }
    }

    @Configuration
    @Import({MyImportSelector.class})
    public static class MyConfig {

    }

    public static class MyImportSelector implements DeferredImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            DruidDataSource.class.getName();
            return new String[]{
                    AutoConfiguration1.class.getName(),
                    AutoConfiguration2.class.getName()
            };
        }
    }

    //exists = true,className = "com.alibaba.druid.pool.DruidDataSource"
    //    第三方配置类
    @Configuration
    @ConditionOnClass(className = "com.alibaba.druid.pool.DruidDataSource", exists = false)
    public static class AutoConfiguration1 {
        @Bean
//        当bean缺失时才添加
        @ConditionalOnMissingBean
        public Bean1 bean1() {
            return new Bean1();
        }
    }

    @Configuration
    @ConditionOnClass(className = "com.alibaba.druid.pool.DruidDataSource", exists = true)
    public static class AutoConfiguration2 {

        @Bean
        @ConditionalOnMissingBean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    static class Bean1 {

    }

    static class Bean2 {

    }
}
