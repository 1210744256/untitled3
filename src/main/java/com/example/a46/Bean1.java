package com.example.a46;

import org.springframework.beans.factory.annotation.Value;

public class Bean1 {
    @Value("${java_home}")
    private String home;
    @Value("18")
    private int value;
//    根据名字进行注入
    @Value("#{@bean2}")
    private Bean2 bean2;
    @Value("#{'${java_home}'+'hello'}")
    private String home2;
}
