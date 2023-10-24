package com.example.a3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Slf4j
public class LifeCycleBean {
    public LifeCycleBean(){
      log.debug("构造");
    }
    @Autowired
    public void autowire(@Value("${java_home}") String name){
        log.debug("依赖注入{}"+name);
    }
    @PostConstruct
    public void init(){
        log.debug("初始化");
    }
    @PreDestroy
    public void destroy(){
        log.debug("销毁");
    }

}
