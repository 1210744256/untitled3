package com.example.a11;

import org.springframework.cglib.core.Signature;

public class MethodFastClass {
    public static void main(String[] args) {
        MethodFastClass methodFastClass = new MethodFastClass();
        int save = methodFastClass.getIndex(new Signature("save", "()V"));
        System.out.println(save);
        Object invoke = methodFastClass.invoke(save, new Target(), new Object[0]);
        System.out.println(invoke);
    }
//    会根据传进来的methodProxy的返回值以及参数类型和方法名生成对应的标签对象
    static Signature signature0;
    static Signature signature1;
    static Signature signature2;
    static {
        signature0=new Signature("save","()V");
        signature1=new Signature("save","(I)I");
        signature2=new Signature("save","(J)J");
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
    public Object invoke(int index, Object target, Object[] args){
        if(index==0){
            ((Target) target).save();
            return null;
        }else if(index==1){
            int save = ((Target) target).save((Integer) args[0]);
            return save;
        }else if(index==2){
            long save = ((Target) target).save((Long) args[0]);
            return save;
        }else {
            throw new RuntimeException("无此方法");
        }
    }
}
