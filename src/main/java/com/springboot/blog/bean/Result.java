package com.springboot.blog.bean;

/**
 * @author Zhouzf
 * @date 2021/7/8/008 14:00
 */
public abstract class Result<T> {

    private String status;
    private T data;
    private String msg;


    public String getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }


    protected Result(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

}
