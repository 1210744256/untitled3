package com.example.a5.componet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Bean1 {
    public Bean1(){
      log.debug("我被spring管理了");
    }
}
