package com.springboot.blog.controller;

import com.springboot.blog.bean.Result;
import com.springboot.blog.service.BlogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author Zhouzf
 * @date 2021/7/13/013 22:24
 */
@RestController
public class BlogController {

    private BlogService blogService;

    @Inject
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    public Result getBlogs(@RequestParam("page") int page, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "userId", required = false) String userId) {
        return blogService.getBlogs(page, pageSize, userId);
    }
}
