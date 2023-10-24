package com.example.a45;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class Bean1 {
    public Bean2 bean2;
    public boolean initlized;
    @Autowired
    public void setBean2(Bean2 bean2) {
        log.info("依赖注入:{}",bean2);
        this.bean2 = bean2;
    }
    @PostConstruct
    public void init(){
        log.info("初始化");
        initlized=true;
    }
}
