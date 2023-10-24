package com.example.a16;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

public class A16 {
    public static void main(String[] args) throws NoSuchMethodException {
//        切入点表达式的内部具体实现
        AspectJExpressionPointcut ajp1 = new AspectJExpressionPointcut();
        ajp1.setExpression("execution(* bar())");
        System.out.println(ajp1.matches(T1.class.getMethod("foo"), T1.class));
//        如果根据matches找到对应的实现规则，然后进行代理
        System.out.println(ajp1.matches(T1.class.getMethod("bar"), T1.class));
//
        AspectJExpressionPointcut ajp2 = new AspectJExpressionPointcut();
        ajp2.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");
        System.out.println(ajp2.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(ajp2.matches(T1.class.getMethod("bar"), T1.class));
//
        StaticMethodMatcher staticMethodMatcher = new StaticMethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
//                方法上是否有Transactional注解
                MergedAnnotations from = MergedAnnotations.from(method);
                if(from.isPresent(Transactional.class)){
                    return true;
                }
//                从继承树上去找是否有Transactional注解
                MergedAnnotations from1 = MergedAnnotations.from(targetClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
                if(from1.isPresent(Transactional.class)){
                    return true;
                }
                return false;
            }
        };
        System.out.println(staticMethodMatcher.matches(T1.class.getMethod("foo"), T1.class));
    }

    static class T1 {
        @Transactional
        public void foo() {
            System.out.println("foo");
        }

        public void bar() {
            System.out.println("bar");
        }
    }
}
