package com.example.a20;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class Controller1 {
    @GetMapping("/test1")
    public void test1() {

    }

    @GetMapping("test2")
    public void test2(String name) {
        log.debug("test2.name{}", name);
    }

    @Yml
    @RequestMapping("/test3")
    public User test3(@Token String token) {
        log.debug("token:[{}]", token);
        return new User(token, 18);
    }

    @PutMapping("/test4")
    public void test4() {

    }
}
