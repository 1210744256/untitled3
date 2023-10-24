package com.example.a6;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

@Slf4j
public class MyBean implements BeanNameAware, InitializingBean, ApplicationContextAware {

    @Override
    public void setBeanName(String s) {
        log.debug("当前bean" + this + "的名字是" + s);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("当前bean" + this + "初始化");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.debug("当前bean" + this + "的容器是" + applicationContext);
    }

    @PostConstruct
    public void init() {
        log.debug("当前bean" + this +"@PostConstruct的init方法");
    }
    @Autowired
    public void aaa(ApplicationContext applicationContext){
        log.debug("当前bean" + this +" @Autowired"+applicationContext);
    }

}
