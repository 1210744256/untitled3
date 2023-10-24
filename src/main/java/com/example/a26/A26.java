package com.example.a26;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

public class A26 {
    public static void main(String[] args) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("name","钟离");
        request.addParameter("age","18");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
//        创建RequestMappingHandlerAdapter
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setApplicationContext(context);
        adapter.afterPropertiesSet();

//        准备servletInvocableHandlerMethod
        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(new Config.MyController(), Config.MyController.class.getMethod("foo", Student.class));
//        设置绑定器
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);
        handlerMethod.setDataBinderFactory(factory);
//        设置参数名解析器
        handlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
//        设置参数解析组合器
        handlerMethod.setHandlerMethodArgumentResolvers(getComposite(context));
        ModelAndViewContainer container = new ModelAndViewContainer();
//        创建模型工厂
        Method method = RequestMappingHandlerAdapter.class.getDeclaredMethod("getModelFactory", HandlerMethod.class, WebDataBinderFactory.class);
        method.setAccessible(true);
        ModelFactory modelFactory = (ModelFactory) method.invoke(adapter, handlerMethod, factory);
//        调用模型工厂的initModel方法补充一些模型数据
        modelFactory.initModel(new ServletWebRequest(request),container,handlerMethod);
//
        handlerMethod.invokeAndHandle(new ServletWebRequest(request),container);
        System.out.println(container.getModel());
    }
    public static HandlerMethodArgumentResolverComposite getComposite(AnnotationConfigApplicationContext context){
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
}
