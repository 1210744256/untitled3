package com.example.a11;

public class ProxyDemo {
    public static void main(String[] args) {
        Target target = new Target();
        Proxy proxy = new Proxy((p, method, objects, methodProxy) -> {
            System.out.println("before");
            return methodProxy.invoke(target, objects);
        });
        proxy.save();
        System.out.println(proxy.save(100l));
    }
}
