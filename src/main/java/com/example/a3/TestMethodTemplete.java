package com.example.a3;

import java.util.ArrayList;
import java.util.List;

public class TestMethodTemplete {
    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public void inject(Object o) {
                System.out.println("解析@Autowired");
            }
        });
        beanFactory.getBean();
    }
//    模板方法
    static class BeanFactory{
        private List<BeanPostProcessor> beanPostProcessors=new ArrayList<>();
        public Object getBean(){
            Object bean = new Object();
            System.out.println("构造"+bean);
            System.out.println("依赖注入"+bean);
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                beanPostProcessor.inject(bean);
            }
            System.out.println("初始化"+bean);
            return bean;
        }
//        添加bean的后处理器
        public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
            beanPostProcessors.add(beanPostProcessor);
        }
    }
    static interface BeanPostProcessor{
//        对依赖注入阶段的扩展
        void inject(Object o);
    }
}
