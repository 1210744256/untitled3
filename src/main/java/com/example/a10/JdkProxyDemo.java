package com.example.a10;

import java.lang.reflect.Proxy;

public class JdkProxyDemo {
    interface Foo{
        void foo();
    }
    static class Target implements Foo{

        @Override
        public void foo() {
            System.out.println("目标方法foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();
        ClassLoader classLoader = JdkProxyDemo.class.getClassLoader();
        Foo foo = (Foo) Proxy.newProxyInstance(classLoader, new Class[]{Foo.class}, (proxy, method, args1) -> {
            System.out.println("before");
            Object result = method.invoke(target, args1);
            System.out.println("after");
            return result;
        });
        foo.foo();
    }
}
