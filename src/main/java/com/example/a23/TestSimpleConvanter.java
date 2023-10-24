package com.example.a23;

import org.springframework.beans.SimpleTypeConverter;

import java.util.Date;

public class TestSimpleConvanter {
    public static void main(String[] args) {
//        最简单的类型转换只有类型转换的功能
        SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
        Integer integer = simpleTypeConverter.convertIfNecessary("123", int.class);
        System.out.println(integer);
        Date date = simpleTypeConverter.convertIfNecessary("2023/10/5", Date.class);
        System.out.println(date);
    }
}
