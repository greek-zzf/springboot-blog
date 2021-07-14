package com.springboot.blog.bean;

import com.springboot.blog.entity.Blog;

import java.util.List;

/**
 * @author Zhouzf
 */
public class BlogListResult extends Result<List<Blog>> {

    private int page;
    private int total;
    private int totalPage;

    public static BlogListResult success(List<Blog> blogs, int page, int total, int totalPage) {
        return new BlogListResult(ResultStatus.OK, "获取成功", blogs, page, total, totalPage);
    }

    public static BlogListResult failure(String msg) {
        return new BlogListResult(ResultStatus.FAIL, msg, null, 0, 0, 0);
    }


    protected BlogListResult(ResultStatus status, String msg, List<Blog> data, int page, int total, int totalPage) {
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
