package com.example.a33;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.Controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class MyConfig {
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet) {
        return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
    }
@Component
    //    自定义的handlermapping路径映射
    public static class MyHandlerMapping implements HandlerMapping {
        @Autowired
        private ApplicationContext context;
        private Map<String,Controller> collect;
        @Override
        public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
            String key = request.getRequestURI();
            Controller controller = collect.get(key);
            if(controller==null){
                return null;
            }
            return new HandlerExecutionChain(controller);
        }

        //        初始化应该把所有的路径映射保存起来
        @PostConstruct
        public void init() {
            collect=context.getBeansOfType(Controller.class).entrySet()
                    .stream().filter(e->e.getKey().startsWith("/"))
                    .collect(Collectors.toMap(e->e.getKey(),e->e.getValue()));
            System.out.println(collect);
        }
    }
//    自定义handleradapter
    @Component
    public static class MyHandlerAdapter implements HandlerAdapter{

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof Controller controller) {
            controller.handleRequest(request,response);
        }
        return null;
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return 0;
    }
}

    @Component("/c1")
    public static class Controller1 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("c1");
            return null;
        }
    }

    @Component("c2")
    public static class Controller2 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("c2");
            return null;
        }
    }

    @Component("/c3")
    public static class Controller3 implements Controller {

        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().print("c3");
            return null;
        }
    }

}

