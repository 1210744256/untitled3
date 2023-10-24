package com.example.a8;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Scope("session")
@Slf4j
public class BeanForSession {
    @PreDestroy
    public void distory(){
      log.debug("session域销毁");
    }
}
