package com.springboot.blog.bean;

import com.springboot.blog.entity.User;

public class LoginResult extends Result<User> {

    boolean isLogin;

    protected LoginResult(String status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static Result success(String msg, User user, boolean isLogin) {
        return new LoginResult("ok", msg, user, isLogin);
    }

    public static Result success(String msg, boolean isLogin) {
        return success(msg, null, isLogin);
    }

    public static Result failure(String msg) {
        return new LoginResult("fail", msg, null, false);
    }

    public boolean isLogin() {
        return isLogin;
    }
}
