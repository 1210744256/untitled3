package com.example.a26;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class Config {
    @ControllerAdvice
    static class MyControllerAdvice{
        @ModelAttribute("a")
        public String aa(){
            return "aa";
        }
    }
    @Controller
    static class MyController {
        @ModelAttribute("b")
        public String bb(){
            return "bb";
        }
        @ResponseStatus(HttpStatus.OK)
        public ModelAndView foo(Student student) {
            System.out.println("foo");
            return null;
        }
    }
}
