package com.example.a11;

import org.springframework.cglib.core.Signature;

public class ProxyFastClass {
    public static void main(String[] args) {
        ProxyFastClass proxyFastClass = new ProxyFastClass();
        int saveSuper = proxyFastClass.getIndex(new Signature("saveSuper", "()V"));
        System.out.println(saveSuper);
        Object invoke = proxyFastClass.invoke(saveSuper, new Proxy(), new Object[0]);
        System.out.println(invoke);
    }
//    会根据传进来的methodProxy的返回值以及参数类型和方法名生成对应的标签对象
//    这个代理类是根据代理对象调用的原始功能的方法
    static Signature signature0;
    static Signature signature1;
    static Signature signature2;
    static {
        signature0=new Signature("saveSuper","()V");
        signature1=new Signature("saveSuper","(I)I");
        signature2=new Signature("saveSuper","(J)J");
    }

//    根据标签获取对应的方法的索引
    public  int getIndex(Signature signature){
        if(signature0.equals(signature)){
            return 0;
        }else if(signature1.equals(signature)){
            return 1;
        }else if(signature2.equals(signature)){
            return 2;
        }else {
            return -1;
        }
    }
    public Object invoke(int index, Object proxy, Object[] args){
        if(index==0){
            ((Proxy) proxy).saveSuper();
            return null;
        }else if(index==1){
            int save = (int) ((Proxy) proxy).saveSuper((Integer) args[0]);
            return save;
        }else if(index==2){
            long save = ((Proxy) proxy).saveSuper((Long) args[0]);
            return save;
        }else {
            throw new RuntimeException("无此方法");
        }
    }
}
