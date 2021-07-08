package com.springboot.blog.bean;

/**
 * @author Zhouzf
 * @date 2021/7/8/008 14:00
 */
public class Result {

    private String status;
    private Object data;
    private String msg;
    private boolean isLogin;


    public static Result failure(String status, String msg) {
        return new Result(status, msg);
    }

    public static Result success(String status, String msg, Object data) {
        return new Result(status, msg, data);
    }


    public String getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    private Result(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private Result(String status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

}
