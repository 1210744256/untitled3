package com.example.a10;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class CglibDemo {
    interface Foo{
        void foo();
    }
    static class Target implements Foo {

        @Override
        public void foo() {
            System.out.println("目标方法foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();
        Foo foo= (Foo) Enhancer.create(Target.class, (MethodInterceptor) (p, method, objects, methodProxy) -> {
            System.out.println("before");
//            Object result = method.invoke(target, objects);
//            spring采用的方式没有调用反射
            Object result = methodProxy.invoke(target, objects);
//            Object result = methodProxy.invokeSuper(p, objects);
            System.out.println("after");
            return result;
        });
        foo.foo();
    }

}
