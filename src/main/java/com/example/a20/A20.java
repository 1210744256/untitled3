package com.example.a20;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class A20 {
    public static void main(String[] args) throws Exception {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(Config.class);
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
//        解析@RequestMapping和其派生注解，生成路径与其控制器方法的映射关系，在初始化bean时就会生成
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((key, value) -> System.out.println(key + " =" + value));
//        模拟请求，获取相应的处理器方法，并执行
//        生成处理器执行链对象会包含拦截器
        HandlerExecutionChain chain = handlerMapping.getHandler(new MockHttpServletRequest("GET", "/test1"));
        System.out.println(chain);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
//        adaptor用来调用处理器方法的
        MyRequestMappingHandlerAdaptor adaptor = context.getBean(MyRequestMappingHandlerAdaptor.class);
//        专门用作测试的请求
//        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test2");
//        request.addParameter("name","尤拉");
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        adaptor.invokeHandlerMethod(request,response, (HandlerMethod) handlerMapping.getHandler(request).getHandler());
        System.out.println("参数解析器>>>>>>>>>>>>>>>>>>>>");
        for (HandlerMethodArgumentResolver resolver : adaptor.getArgumentResolvers()) {
            System.out.println(resolver);
        }
        System.out.println("返回值类型解析器>>>>>>>>>>>>>>>");
        for (HandlerMethodReturnValueHandler returnValueHandler : adaptor.getReturnValueHandlers()) {
            System.out.println(returnValueHandler);
        }
//        自定义解析器的测试
        System.out.println("自定义解析器的测试>>>>>>>>>>>>>>>");
        MockHttpServletRequest request1 = new MockHttpServletRequest("GET", "/test3");
        request1.addHeader("token","某个令牌");
        MockHttpServletResponse response1 = new MockHttpServletResponse();
        adaptor.invokeHandlerMethod(request1,response1, (HandlerMethod) handlerMapping.getHandler(request1).getHandler());
//        检查响应结果
        byte[] contentAsByteArray = response1.getContentAsByteArray();
        System.out.println(new String(contentAsByteArray, StandardCharsets.UTF_8));
    }
}
