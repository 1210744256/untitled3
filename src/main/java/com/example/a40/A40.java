package com.example.a40;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class A40 {
    public static void main(String[] args) throws IOException, LifecycleException {
//        1创建Tomcat对象
        Tomcat tomcat = new Tomcat();
//        2创建项目文件夹
        File docbase = Files.createTempDirectory("boot.").toFile();
        docbase.deleteOnExit();
//        3创建tomcat项目在tomcat中称为context
        Context context = tomcat.addContext("", docbase.getAbsolutePath());
        ApplicationContext applicationContext = getContext();

//        4编程添加Servlet
        context.addServletContainerInitializer(new ServletContainerInitializer() {
            @Override
            public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
                HelloServlet helloServlet = new HelloServlet();
                servletContext.addServlet("aaa",helloServlet).addMapping("/hello");
                for (ServletRegistrationBean registrationBean : applicationContext.getBeansOfType(ServletRegistrationBean.class).values()) {
//                    每个注册bean去注册servlet
                    registrationBean.onStartup(servletContext);
                }
            }
        }, Collections.emptySet());
//        5启动tomcat
        tomcat.start();
//        6创建连接器设置监听端口
        Connector connector = new Connector(new Http11Nio2Protocol());
        connector.setPort(8080);
        tomcat.setConnector(connector);
    }
    public static ApplicationContext getContext(){
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(Config.class);
        context.register(Config.MyController.class);
        context.refresh();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        return context;
    }
    public static class Config{
        @Bean
        public DispatcherServlet dispatcherServlet(){
            return new DispatcherServlet();

        }
        @Bean
        public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet){
            DispatcherServletRegistrationBean dispatcherServletRegistrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
            return dispatcherServletRegistrationBean;
        }
        @Bean
          public RequestMappingHandlerMapping requestMappingHandlerMapping(){
            return new RequestMappingHandlerMapping();
        }
        @Bean
        public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
            RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
            adapter.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter()));
            return adapter;
        }
        @RestController
        public static class MyController{
            @GetMapping("hello2")
            public String hello(){
                System.out.println("hedsfjk");
                return "hello2";
            }
        }
    }
    
}
