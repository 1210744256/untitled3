package com.example.a20;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
@ComponentScan
//绑定指定的配置文件
@PropertySource("classpath:application.properties")
//内部绑定两个bean
@EnableConfigurationProperties({WebMvcProperties.class, ServerProperties.class})
public class Config {
    //    内嵌tomact容器工厂
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(ServerProperties serverProperties) {
        return new TomcatServletWebServerFactory(serverProperties.getPort());
    }

    //    dispacherservlet
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    //    注册DispacherServlet
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean
    (DispatcherServlet dispatcherServlet, WebMvcProperties webMvcProperties) {
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
//        是否在加载tomact容器时初始化servlet
        registrationBean.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
        return registrationBean;
    }
//如果不进行配置，dispacherservlet内部会把他们这些组件作为成员变量，不会加入spring容器中
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }
    @Bean
    public MyRequestMappingHandlerAdaptor myRequestMappingHandlerAdaptor(){
//        将自定义的参数解析器加入到适配器中
        TokenArgumentResolver tokenArgumentResolver = new TokenArgumentResolver();
        MyYmlReturnValueHandler ymlReturnValueHandler = new MyYmlReturnValueHandler();
        MyRequestMappingHandlerAdaptor mappingHandlerAdaptor = new MyRequestMappingHandlerAdaptor();
//        mappingHandlerAdaptor.s
        mappingHandlerAdaptor.setCustomArgumentResolvers(List.of(tokenArgumentResolver));
        mappingHandlerAdaptor.setCustomReturnValueHandlers(List.of(ymlReturnValueHandler));
        return mappingHandlerAdaptor;
    }
}
