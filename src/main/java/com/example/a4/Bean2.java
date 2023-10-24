package com.example.a4;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
@Slf4j
public class Bean2 {
    @PostConstruct
    public void init(){
        log.debug("初始化");
    }
}
