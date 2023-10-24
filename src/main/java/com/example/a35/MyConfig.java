package com.example.a35;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.resource.CachingResourceResolver;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

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

    //静态资源路径映射
    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping(ApplicationContext context) {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
//        这个handlermapping是不会默认加载所有resourceHttpRequestHandler的需要手动加载
        Map<String, ResourceHttpRequestHandler> beansOfType = context.getBeansOfType(ResourceHttpRequestHandler.class);
        handlerMapping.setUrlMap(beansOfType);
        System.out.println(beansOfType);
        return handlerMapping;
    }

    @Bean
    public HttpRequestHandlerAdapter httpRequestHandlerAdapter() {
        return new HttpRequestHandlerAdapter();
    }

    @Bean("/**")
    public ResourceHttpRequestHandler handler1() {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
//        设置映射路径
        handler.setLocations(List.of(new ClassPathResource("static/")));
//        添加资源解析器
        handler.setResourceResolvers(List.of(
//                缓存资源解析器
                new CachingResourceResolver(new ConcurrentMapCache("cache")),
//                可以处理压缩文件的资源解析器
                new EncodedResourceResolver(),
//                基本的资源解析器
                new PathResourceResolver()
        ));
        return handler;
    }

    @Bean("/img/**")
    public ResourceHttpRequestHandler handler2() {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(List.of(new ClassPathResource("images/")));

        return handler;
    }
//    欢迎页
//    @Bean
//   public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext context){
//       Resource resource = context.getResource("classpath:hello.html");
//       return new WelcomePageHandlerMapping(null,context,resource,"/**");
//   }
//   public SimpleControllerHandlerAdapter simpleControllerHandlerAdapter(){
//        return new SimpleControllerHandlerAdapter();
//   }

    @PostConstruct
    @SuppressWarnings("all")
    public void initGzip() throws IOException {
        Resource resource = new ClassPathResource("static");
        File dir = resource.getFile();
        for (File file : dir.listFiles(pathname -> pathname.getName().endsWith(".html"))) {
            System.out.println(file);
            try (FileInputStream fis = new FileInputStream(file); GZIPOutputStream fos = new GZIPOutputStream(new FileOutputStream(file.getAbsoluteFile() + ".gz"))) {
                byte[] bytes = new byte[1024 * 8];
                int len;
                while ((len = fis.read(bytes)) != -1) {
                    System.out.println(1);
                    fos.write(bytes, 0, len);
                }
            }

        }
    }


}

