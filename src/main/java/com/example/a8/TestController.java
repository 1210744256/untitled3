package com.example.a8;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("test")
public class TestController {
    @Lazy
    @Autowired
    private BeanForRequest beanForRequest;
    @Lazy
    @Autowired
    private BeanForSession beanForSession;
    @Lazy
    @Autowired
    private BeanForApplication beanForApplication;
    @GetMapping(produces = "text/html")
    public String ada(HttpServletRequest request) throws IOException {
//        ServletContext servletContext = request.getServletContext();
        String sb="<ul>"+
                "<li>"+"request scope"+beanForRequest+"</li>"+
                "<li>"+"session scope"+beanForSession+"</li>"+
                "<li>"+"application scope"+beanForApplication+"</li>"+
                "</ul>";
        return sb;
    }
}
