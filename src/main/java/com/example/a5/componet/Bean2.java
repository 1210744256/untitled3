package com.example.a5.componet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class Bean2 {
    public Bean2(){
      log.debug("我被spring管理了");
    }
}
