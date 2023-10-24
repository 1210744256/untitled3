package com.example.a29;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Configuration
public class MyConfig {
    //    使用ControllerAdvice注解去为返回值做一些扩展处理
    @ControllerAdvice
    static class MyControllerAdvice implements ResponseBodyAdvice<Object> {

        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//            先看方法上是否有ResponseBody注解再看类上是否有ResponseBody注解
            if (returnType.getMethodAnnotation(ResponseBody.class) != null ||
                    AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseBody.class) != null) {
                return true;
            }
            return false;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//            如果返回值就是Result直接返回就行
            if (body instanceof Result) {
                return body;
            }
//            如果不是则需要进行转换
            return new Result().ok(body);
        }
    }
}
