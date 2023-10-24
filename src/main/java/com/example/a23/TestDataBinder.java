package com.example.a23;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Date;

public class TestDataBinder {
    public static void main(String[] args) {
        MyBean myBean = new MyBean();
        DataBinder dataBinder = new DataBinder(myBean);
//        DirectFieldAccess用的是属性注入的方式
        dataBinder.initDirectFieldAccess();
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("age",10);
        propertyValues.add("name","zhangsan");
        propertyValues.add("date","2584/4/11");
        dataBinder.bind(propertyValues);
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
