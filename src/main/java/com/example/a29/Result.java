package com.example.a29;

import lombok.Data;

@Data
public class Result {
    private Object data;
    private String message;
    private boolean status;
    public Result ok(){
        setStatus(true);
        return this;
    }
    public Result ok(Object data){
        setStatus(true);
        setData(data);
        return this;
    }
    public Result ok(Object data,String msg){
        setStatus(true);
        setData(data);
        setMessage(msg);
        return this;
    }
    public Result error(){
        setStatus(false);
        return this;
    }
    public Result error(Object data){
        setStatus(false);
        setData(data);
        return this;
    }
    public Result error(Object data,String msg){
        setStatus(false);
        setData(data);
        setMessage(msg);
        return this;
    }


}
