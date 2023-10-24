package com.example.a11;

public class Target {
    public void save(){
        System.out.println("save");
    }
    public int save(int i){
        System.out.println("int save");
        return 100+i;
    }
    public long save(long j){
        System.out.println("long save");
        return j;
    }
}
