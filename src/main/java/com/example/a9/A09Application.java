package com.example.a9;

import com.example.a9.service.Myservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class A09Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A09Application.class, args);
        Myservice myservice = context.getBean(Myservice.class);
        System.out.println(myservice.getClass());
        myservice.show();
        new Myservice().show();
    }
}
