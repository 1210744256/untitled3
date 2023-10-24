package com.example.a23.tip;

import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Test {
    public static void main(String[] args) {
//        小技巧
//        jdk的方法获取泛型参数
        Type genericSuperclass = StudentDao.class.getGenericSuperclass();
        System.out.println(genericSuperclass);
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
//            如果有泛型进行类型转换后获取泛型
            for (Type actualTypeArgument : parameterizedType.getActualTypeArguments()) {
                System.out.println(actualTypeArgument);
            }
        }
        System.out.println("---------------");
//        小技巧值spring的方法来获取泛型参数
        Class<?> aClass = GenericTypeResolver.resolveTypeArgument(StudentDao.class, BaseDao.class);
        System.out.println(aClass);
    }
}
