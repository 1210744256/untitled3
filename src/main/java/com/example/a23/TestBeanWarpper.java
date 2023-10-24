package com.example.a23;

import lombok.Data;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

public class TestBeanWarpper {
    public static void main(String[] args) {
//        BeanWrapperImpl利用反射的方式来为属性赋值,走的是set方法赋值
        MyBean myBean = new MyBean();
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(myBean);
        beanWrapper.setPropertyValue("age","10");
        beanWrapper.setPropertyValue("name","小女孩");
        beanWrapper.setPropertyValue("date","2021/10/8");
        System.out.println(myBean);
    }
    @Data
    static class MyBean{
        private int age;
        private String name;
        private Date date;
    }
}
