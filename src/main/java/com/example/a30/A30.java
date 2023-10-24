package com.example.a30;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class A30 {
    public static void main(String[] args) throws NoSuchMethodException {
//
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
//        添加消息处理器
        exceptionResolver.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter()));
//        如果没有就添加默认的参数处理器，返回值类型处理器
        exceptionResolver.afterPropertiesSet();
//        测试json
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(new Controller1(),Controller1.class.getDeclaredMethod("foo"));
        exceptionResolver.resolveException(request,response,handlerMethod,new ArithmeticException("被0除"));
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
//        测试mav
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        MockHttpServletResponse response1 = new MockHttpServletResponse();
        HandlerMethod handlerMethod1 = new HandlerMethod(new Controller2(), Controller2.class.getDeclaredMethod("foo"));
        ModelAndView mav = exceptionResolver.resolveException(request1, response1, handlerMethod1, new ArithmeticException("渴望着0"));
        System.out.println(mav.getViewName()+":"+mav.getModel());
//循环异常
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        HandlerMethod handlerMethod2 = new HandlerMethod(new Controller3(), Controller3.class.getDeclaredMethod("foo"));
        Exception exception = new Exception("e1", new IOException("e2"));
        ModelAndView mav2 = exceptionResolver.resolveException(request2, response2, handlerMethod2, exception);
        System.out.println(mav2.getViewName()+":"+mav2.getModel());

    }
    static class Controller1 {
        public void foo(){

        }
        @ResponseBody
        @ExceptionHandler
        public Map<String, Object> handle(ArithmeticException e){
            return Map.of("error", e.getMessage());
        }
    }
    static class Controller2 {
        public void foo(){

        }

        @ExceptionHandler
        public ModelAndView handle(ArithmeticException e){
            return new ModelAndView("test2",Map.of("error",e.getMessage()));
        }
    }
    static class Controller3 {
        public void foo(){

        }
//        @ExceptionHandler
//        public ModelAndView handle1(ArithmeticException e){
//            return new ModelAndView("test2",Map.of("error",e.getMessage()));
//        }
//        @ExceptionHandler
//        public ModelAndView handle2(Exception e){
//            return new ModelAndView("test3 Exception",Map.of("error",e.getMessage()));
//        }
        @ExceptionHandler
        public ModelAndView handle3(IOException e, HttpServletRequest request){
            System.out.println(request);
            return new ModelAndView("test4 IOException",Map.of("error",e.getMessage()));
        }

    }
}
