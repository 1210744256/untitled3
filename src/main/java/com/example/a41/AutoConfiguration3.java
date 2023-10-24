package com.example.a41;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration3 {
    @Bean
    public A41.Bean2 bean2(){
        return new A41.Bean2();
    }
}
