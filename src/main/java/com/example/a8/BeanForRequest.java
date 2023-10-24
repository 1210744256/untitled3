package com.example.a8;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Scope("request")
@Slf4j
public class BeanForRequest {
    @PreDestroy
    public void distory(){
      log.debug("request域销毁");
    }
}
