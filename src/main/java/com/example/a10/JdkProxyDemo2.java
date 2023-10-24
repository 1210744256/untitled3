package com.example.a10;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JdkProxyDemo2 {
    static class Target implements Foo{

        @Override
        public void foo() {
            System.out.println("目标方法foo");
        }

        @Override
        public int bar() {
            System.out.println("目标方法bar");
            return 100;
        }
    }

    public static void main(String[] args) {
        Target target = new Target();
        $Proxy0 proxy0 = new $Proxy0(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                System.out.println("before");
                Object invoke = null;
                try {
                    invoke = method.invoke(target, args);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("after");
                return invoke;
            }
        });
        System.out.println(proxy0.bar());
        proxy0.foo();
    }
}
