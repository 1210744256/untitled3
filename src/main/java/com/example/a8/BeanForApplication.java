package com.example.a8;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Scope("application")
@Slf4j
public class BeanForApplication {
    @PreDestroy
    public void distory(){
      log.debug("application域销毁");
    }
}
