package com.example.a47;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Configuration
public class A47_1 {
    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A47_1.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        // 解析属性上的@autowired
        DependencyDescriptor dd1 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean2"), false);
        System.out.println(beanFactory.doResolveDependency(dd1, null, null, null));
        //解析方法参数上的
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(Bean1.class.getDeclaredMethod("setBean2", Bean2.class),0), false);
        System.out.println(beanFactory.doResolveDependency(dd2, null, null, null));
//        解析Optional里面的类型
        DependencyDescriptor dd3 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean3"), false);
//        如果属性是optional类型
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        if(dd3.getDependencyType()==Optional.class){
//            解析里面一层
            dd3.increaseNestingLevel();
            Object result = beanFactory.resolveDependency(dd3, null, null, null);
            System.out.println(Optional.ofNullable(result));
        }
//        解析ObjectFactory里面的类型
        DependencyDescriptor dd4 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean4"), false);
        if(dd4.getDependencyType()==ObjectFactory.class){
            dd4.increaseNestingLevel();
//            System.out.println(1);
            ObjectFactory<Object> objectFactory = new ObjectFactory<>() {

                @Override
                public Object getObject() throws BeansException {
                    return beanFactory.doResolveDependency(dd4, null, null, null);
                }
            };
            System.out.println(objectFactory.getObject());
        }
//        解析带有@lazy属性的
        DependencyDescriptor dd5 = new DependencyDescriptor(Bean1.class.getDeclaredField("bean2"), false);
        ContextAnnotationAutowireCandidateResolver resolver=new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);
        Object bean1 = resolver.getLazyResolutionProxyIfNecessary(dd5, "bean2");
        System.out.println(bean1);
        System.out.println(bean1.getClass());


    }

    @Component
    static class Bean1 {
        @Autowired
        @Lazy
        private Bean2 bean2;

        @Autowired
        public void setBean2(Bean2 bean2) {
            this.bean2 = bean2;
        }
        @Autowired
        private Optional<Bean2> bean3;
        private ObjectFactory<Bean2> bean4;
    }

    @Component

    static class Bean2 {
        @Override
        public String toString() {
            return super.toString();
        }
    }
}
