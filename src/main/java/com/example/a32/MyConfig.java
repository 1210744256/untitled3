package com.example.a32;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistrarBeanPostProcessor;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Configuration
public class MyConfig {
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }
    @Bean
    public DispatcherServlet dispatcherServlet(){
        return new DispatcherServlet();
    }
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet){
        return new DispatcherServletRegistrationBean(dispatcherServlet,"/");
    }
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping(){
        return new RequestMappingHandlerMapping();
    }
    @Bean
//    默认的RequestMappingHandlerAdapter是没有消息处理器的
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter()));
        return adapter;
    }
//    tomact错误转发
    @Bean
    public ErrorPageRegistrar errorPageRegistrar(){
        return registry -> registry.addErrorPages(new ErrorPage("/error"));
    }
    //    tomact错误转发后处理器
    @Bean
    public ErrorPageRegistrarBeanPostProcessor errorPageRegistrarBeanPostProcessor(){
        return new ErrorPageRegistrarBeanPostProcessor();
    }
//    springboot提供的错误处理
    @Bean
    public BasicErrorController basicErrorController(){
        ErrorProperties errorProperties = new ErrorProperties();
        errorProperties.setIncludeException(true);
        return new BasicErrorController(new DefaultErrorAttributes(),errorProperties);
    }
    @Bean
    public View error(){
        return new View() {
            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                System.out.println(model);
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().print("""
                        <h3>服务器内部错误<h3>
                        """);
            }
        };
    }
    @Bean
    public ViewResolver viewResolver(){
        return new BeanNameViewResolver();
    }
    @Controller
    public static class MyController {
        @RequestMapping("/test")
        public void test(){
            System.out.println("test");
            System.out.println(1/0);
        }
//        @RequestMapping("/error")
//        @ResponseBody
//        public Map<String,Object> error(HttpServletRequest request){
//            Throwable error = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//            return Map.of("error",error.getMessage());
//        }
    }
}
