package com.example.a10;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

public class $Proxy0 implements Foo {
    static Method foo;
    static Method bar;

    static {
        try {
            foo = Foo.class.getDeclaredMethod("foo", (Class[]) null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            bar = Foo.class.getDeclaredMethod("bar", (Class[]) null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private InvocationHandler invocationHandler;

    public $Proxy0(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    @Override
    public void foo() {
        try {
            invocationHandler.invoke(this, foo, new Class[0]);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable t){
            throw new UndeclaredThrowableException(t);
        }

    }

    @Override
    public int bar() {
        try {
            Object invoke = invocationHandler.invoke(this, bar, new Class[0]);
            return (int) invoke;
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable t){
            throw new UndeclaredThrowableException(t);
        }

    }
}
