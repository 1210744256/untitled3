package com.example.a23;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Date;

public class TestServletDataBinderFactory {
    public static void main(String[] args) throws Exception {
        MyBean myBean = new MyBean();
//        适合在web环境下使用的数据绑定类型转换器
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("age","18");
        request.setParameter("name","65");
        request.setParameter("date","2451|5|8");
        request.setParameter("address.name","天津");
//    用工厂来创建绑定器对象
//       1 无扩展功能的工厂
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);
//        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), myBean, "skjda");

//       2 用initBinder来扩展 本质上是使用PropertyEditorRegistry配合PropertyEditor使用的，用了适配器把一套接口转换为另外一套接口
//        InvocableHandlerMethod invocableHandlerMethod = new InvocableHandlerMethod(new Controller(),Controller.class.getMethod("aaa", WebDataBinder.class));
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(List.of(invocableHandlerMethod),null);
//        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), myBean, "skjda");

//        3用ConversionService来扩展
//        FormattingConversionService service = new FormattingConversionService();
//        service.addFormatter(new MyDataFormatter(">>>>>>>>使用conversionService来进行扩展"));
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        initializer.setConversionService(service);
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,initializer);
//        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), myBean, "skjda");

//       4 使用默认的ConversionService进行解析
        DefaultFormattingConversionService service = new DefaultFormattingConversionService();
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,initializer);
        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), myBean, "skjda");

        dataBinder.bind(new ServletRequestParameterPropertyValues(request));
        System.out.println(myBean);

    }
    static class Controller{
        @InitBinder
        public void aaa(WebDataBinder binder){
            binder.addCustomFormatter(new MyDataFormatter(">>>>>>使用InitBinder的方式进行扩展"));
        }
    }
    @Data
    static class MyBean{
        private int age;
        private String name;
        @DateTimeFormat(fallbackPatterns= "yyyy|MM|dd")
        private Date date;
        private Address address;
    }
    @Data
    static class Address{
        private String name;
    }
}
