package com.springboot.blog.bean;

import com.springboot.blog.entity.Blog;

import java.util.List;

/**
 * @author Zhouzf
 */
public class BlogResult extends Result<List<Blog>> {

    private int page;
    private int total;
    private int totalPage;

    public static BlogResult newResult(List<Blog> blogs, int page, int total, int totalPage) {
        return new BlogResult("ok", "获取成功", blogs, page, total, totalPage);
    }

    public static BlogResult failure(String msg) {
        return new BlogResult("fail", msg, null, 0, 0, 0);
    }


    private BlogResult(String status, String msg, List<Blog> data, int page, int total, int totalPage) {
        super(status, msg, data);
        this.page = page;
        this.total = total;
        this.totalPage = totalPage;
    }

    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
