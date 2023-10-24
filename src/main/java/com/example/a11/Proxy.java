package com.example.a11;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

public class Proxy extends Target {
    public Proxy() {
    }

    private MethodInterceptor methodInterceptor;

    public Proxy(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    static Method save0;
    static Method save1;
    static Method save2;
    static MethodProxy methodProxy0;
    static MethodProxy methodProxy1;
    static MethodProxy methodProxy2;

    static {
        try {
            save0 = Target.class.getDeclaredMethod("save");
            save1 = Target.class.getDeclaredMethod("save", int.class);
            save2 = Target.class.getDeclaredMethod("save", long.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
//        目标对象，代理对象，返回值以及参数类型，增强方法名，原始功能方法名
        methodProxy0=MethodProxy.create(Target.class, Proxy.class,"()V","save","saveSuper");
        methodProxy1=MethodProxy.create(Target.class, Proxy.class,"(I)I","save","saveSuper");
        methodProxy2=MethodProxy.create(Target.class, Proxy.class,"(J)J","save","saveSuper");
    }
//    >>>>>>>>>>>>>>>>原始功能的方法
    public void saveSuper(){
        super.save();
    }
    private int saveSuper(int i){
        return super.save(i);
    }
    public long saveSuper(long j){
        return super.save(j);
    }
//    >>>>>>>>>>>>>>>>>>增强功能的方法
    @Override
    public void save() {
        try {
            methodInterceptor.intercept(this, save0, new Object[0], methodProxy0);
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
//        FastClass
    }

    @Override
    public int save(int i) {
        try {
            return (int) methodInterceptor.intercept(this, save1, new Object[]{i}, methodProxy1);
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    @Override
    public long save(long j) {
        try {
            return (long) methodInterceptor.intercept(this, save2, new Object[]{j}, methodProxy2);
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
