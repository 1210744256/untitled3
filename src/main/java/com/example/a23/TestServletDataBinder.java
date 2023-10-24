package com.example.a23;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import java.util.Date;

public class TestServletDataBinder {
    public static void main(String[] args) {
        MyBean myBean = new MyBean();
//        适合在web环境下使用的数据绑定类型转换器
        ServletRequestDataBinder servletRequestDataBinder = new ServletRequestDataBinder(myBean);
        servletRequestDataBinder.initDirectFieldAccess();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("age","18");
        request.setParameter("name","65");
        request.setParameter("date","2451/5/8");
        servletRequestDataBinder.bind(new ServletRequestParameterPropertyValues(request));
        System.out.println(myBean);

    }
    static class MyBean{
        private int age;
        private String name;
        private Date date;

        @Override
        public String toString() {
            return "MyBean{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    ", date=" + date +
                    '}';
        }
    }
}
