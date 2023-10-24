package com.example.a18;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class A18_2 {
    public static void main(String[] args) throws Throwable {
        List<MethodInterceptor> methodInterceptorList=List.of(new Advice1(),new Advice2());
        Target target = new Target();
        MyInvocation invocation = new MyInvocation(Target.class.getMethod("foo"), new Object[0], target, methodInterceptorList);
        invocation.proceed();

    }

    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("before advice1");
            Object result = invocation.proceed();
            System.out.println("after advice1");
            return result;
        }
    }

    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("before advice2");
            Object result = invocation.proceed();
            System.out.println("after advice2");
            return result;
        }
    }

    static class MyInvocation implements MethodInvocation {
        private Method method;
        private Object[] args;
        private Object target;
        private List<MethodInterceptor> methodInterceptorList;
        private int size=1;

        public MyInvocation(Method method, Object[] args, Object target, List<MethodInterceptor> methodInterceptorList) {
            this.method = method;
            this.args = args;
            this.target = target;
            this.methodInterceptorList = methodInterceptorList;
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return new Object[0];
        }

        @Override
        public Object proceed() throws Throwable {
            if(size>methodInterceptorList.size()){
//                如果环绕通知以及调用完了就调用目标方法
                return method.invoke(target,new Object[0]);
            }
            MethodInterceptor methodInterceptor = methodInterceptorList.get((size++) - 1);
            return methodInterceptor.invoke(this);
        }

        @Override
        public Object getThis() {
            return target;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return method;
        }
    }
    static class Target {
        public void foo() {
            System.out.println("target foo");
        }

        public void bar() {
            System.out.println("bar");
        }
    }
}
