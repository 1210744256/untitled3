package com.example.a29;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.List;

public class A29 {
    public static void main(String[] args) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("name","balf");
        request.addParameter("age","15");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //        创建RequestMappingHandlerAdapter
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setApplicationContext(context);
        adapter.afterPropertiesSet();
   //        准备servletInvocableHandlerMethod
        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod
                (new MyController(),MyController.class.getDeclaredMethod("student", Student.class));
//        为handlermethod绑定数据绑定工厂,参数解析组合器,返回值解析组合器,ModelAndView
        handlerMethod.setHandlerMethodArgumentResolvers(getArgumentComposite(context));
        handlerMethod.setDataBinderFactory(new ServletRequestDataBinderFactory(null,null));
        handlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        handlerMethod.setHandlerMethodReturnValueHandlers(getReturnValueComposite(context));
        handlerMethod.invokeAndHandle(new ServletWebRequest(request,response),new ModelAndViewContainer());
        System.out.println(response.getContentAsString());
    }
    public static HandlerMethodArgumentResolverComposite getArgumentComposite(AnnotationConfigApplicationContext context){
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolvers(List.of(
                new RequestParamMethodArgumentResolver(context.getDefaultListableBeanFactory(), false),
                new PathVariableMethodArgumentResolver(),
                new RequestHeaderMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                new ExpressionValueMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                new ServletRequestMethodArgumentResolver(),
//                    必须有modelandview注解
                new ServletModelAttributeMethodProcessor(false),
                new ServletModelAttributeMethodProcessor(true),
                new RequestParamMethodArgumentResolver(context.getDefaultListableBeanFactory(),true)
        ));
        return composite;
    }
    public static HandlerMethodReturnValueHandlerComposite getReturnValueComposite(AnnotationConfigApplicationContext context){
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addHandlers(List.of(
                new ModelAndViewMethodReturnValueHandler(),
                new HttpHeadersReturnValueHandler(),
                new HttpEntityMethodProcessor(List.of(new MappingJackson2HttpMessageConverter())),
                new ServletModelAttributeMethodProcessor(false),
                new RequestResponseBodyMethodProcessor(List.of(new MappingJackson2HttpMessageConverter()),List.of(new MyConfig.MyControllerAdvice())),
                new ServletModelAttributeMethodProcessor(true)
        ));
        return composite;
    }
}
