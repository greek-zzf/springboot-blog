package com.springboot.blog.bean;

import com.springboot.blog.entity.Blog;

/**
 * @author Zhouzf
 * @date 2021/7/14/014 13:57
 */
public class BlogResult extends Result<Blog> {
    protected BlogResult(ResultStatus status, String msg, Blog data) {
        super(status, msg, data);
    }

    public static BlogResult failure(String message) {
        return new BlogResult(ResultStatus.FAIL, message, null);
    }

    public static BlogResult failure(Exception e) {
        return new BlogResult(ResultStatus.FAIL, e.getMessage(), null);
    }

    public static BlogResult success(String msg) {
        return new BlogResult(ResultStatus.OK, msg, null);
    }

    public static BlogResult success(String msg, Blog blog) {
        return new BlogResult(ResultStatus.OK, msg, blog);
    }

}
