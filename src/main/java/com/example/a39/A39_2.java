package com.example.a39;

import org.springframework.boot.DefaultBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class A39_2 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        事件监听
        SpringApplication app = new SpringApplication(A39_2.class);
        app.addListeners(event -> {
            System.out.println(event.getClass());
        });
//        事件发布器
//        根据事件发布器获得类名
//        SpringFactoriesLoader专门读取spring.factories的配置文件
        List<String> names = SpringFactoriesLoader.loadFactoryNames(SpringApplicationRunListener.class, A39_2.class.getClassLoader());
        for (String name : names) {
            System.out.println(name);
            Class<?> clazz = Class.forName(name);
            Constructor<?> constructor = clazz.getConstructor(SpringApplication.class, String[].class);
//            反射调用构造器获得事件发布器对象
            SpringApplicationRunListener publisher = (SpringApplicationRunListener) constructor.newInstance(app, args);
            DefaultBootstrapContext bootstrapContext = new DefaultBootstrapContext();
            publisher.starting(bootstrapContext);//springboot 开始启动
            publisher.environmentPrepared(bootstrapContext,new StandardEnvironment());//环境信息准备完毕
            GenericApplicationContext context = new GenericApplicationContext();
            publisher.contextPrepared(context);//在spring容器创建，并调用初始化器之后，发送此事件
            publisher.contextLoaded(context);//所有bean definition加载完毕
            context.refresh();
            publisher.started(context);//springboot 容器初始化完成(refresh方法调用完毕)
            publisher.running(context);//springboot 启动完成
            publisher.failed(context,new Exception("出错了"));//springboot 启动出错
        }
    }
}
