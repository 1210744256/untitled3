package com.example.a21;

import com.example.a20.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class A21 {
    public static void main(String[] args) throws Exception {
        HttpServletRequest httpServletRequest = mockRequest();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
//        要点1，控制器方法封装为HandlerMethod
        HandlerMethod handlerMethod = new HandlerMethod(new Controller(), Controller.class.getMethod("test", String.class, String.class, int.class, String.class, MultipartFile.class, int.class, String.class, String.class, String.class, HttpServletRequest.class, User.class, User.class, User.class));
//        要点2，准备对象绑定与类型转换
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);
//        要点3，准备ModelAndViewContainer用来存储中间Model结果
        ModelAndViewContainer container = new ModelAndViewContainer();
//        要点4，解析每个参数值
        for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
//           false是必须有这个注解
            RequestParamMethodArgumentResolver requestParamMethodArgumentResolver = new RequestParamMethodArgumentResolver(context.getDefaultListableBeanFactory(), false);
//            组合解析器
            HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
            composite.addResolvers(
                    new RequestParamMethodArgumentResolver(context.getDefaultListableBeanFactory(), false),
                    new PathVariableMethodArgumentResolver(),
                    new RequestHeaderMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                    new ExpressionValueMethodArgumentResolver(context.getDefaultListableBeanFactory()),
                    new ServletRequestMethodArgumentResolver(),
//                    必须有modelandview注解
                    new ServletModelAttributeMethodProcessor(false),
                    new ServletModelAttributeMethodProcessor(true),
                    new RequestParamMethodArgumentResolver(context.getDefaultListableBeanFactory(), true)
            );
//            获取参数上的注解
            String annotations = Arrays.stream(parameter.getParameterAnnotations()).map(a -> a.annotationType().getSimpleName()).collect(Collectors.joining());
            String str = annotations.length() > 0 ? "@" + annotations + " " : " ";
//            设置参数名的解析器
            parameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
            if (composite.supportsParameter(parameter)) {
//                如果支持这个参数就进行解析
                Object v = composite.resolveArgument(parameter, container, new ServletWebRequest(httpServletRequest), factory);
//                System.out.println(v.getClass());
                System.out.println("[" + parameter.getParameterIndex() + "]" + str + parameter.getParameterType().getSimpleName() + " " + parameter.getParameterName() + "->" + v);
                System.out.println("模型数据" + container.getModel());
            } else {
                System.out.println("[" + parameter.getParameterIndex() + "]" + str + parameter.getParameterType().getSimpleName() + " " + parameter.getParameterName());

            }
        }
    }

    static class Controller {
        public void test(
                @RequestParam("name1") String name1,
                String name2,
                @RequestParam("age") int age,
                @RequestParam(name = "home", defaultValue = "${JAVA_HOME}") String home1,
                @RequestParam("file") MultipartFile file,
                @PathVariable("id") int id,
                @RequestHeader("Content-Type") String header,
                @CookieValue("token") String token,
                @Value("${JAVA_HOME}") String home2,
                HttpServletRequest request,
                @ModelAttribute User user1,
                User user2,
                @RequestBody User user3

        ) {

        }
    }

    private static HttpServletRequest mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name1",
                "zhangsan");
        request.setParameter("name2",
                "lisi");
        request.addPart(new MockPart("file",
                "abc", "hello".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> uriTemplateVariables = new AntPathMatcher()
                .extractUriTemplateVariables("/test/{id}", "/test/123");
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriTemplateVariables);
        request.setContentType("application/json");
        request.setCookies(new Cookie("token", "123456"));
        request.setParameter("name", "张三 ");
        request.setParameter("age", " 18 ");
        request.setContent("{\"name\": \"李四\", \"age\": 20}".getBytes(StandardCharsets.UTF_8));
        return new StandardServletMultipartResolver().resolveMultipart(request);
    }
}
