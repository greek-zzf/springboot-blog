package com.springboot.blog.controller;

import com.springboot.blog.bean.BlogResult;
import com.springboot.blog.bean.Result;
import com.springboot.blog.entity.Blog;
import com.springboot.blog.service.BlogService;
import com.springboot.blog.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * @author Zhouzf
 * @date 2021/7/13/013 22:24
 */
@RestController
public class BlogController {

    private BlogService blogService;
    private UserService userService;

    @Inject
    public BlogController(BlogService blogService, UserService userService) {
        this.blogService = blogService;
        this.userService = userService;
    }

    @GetMapping("/blog")
    public Result getBlogs(@RequestParam(value = "page") int page, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "userId", required = false) String userId) {
        if (page < 0) {
            page = 1;
        }

        return blogService.getBlogs(page, pageSize, userId);
    }


    @GetMapping("/blog/{blogId}")
    public Result details(@PathVariable("blogId") Integer id) {
        return blogService.getBlogById(id);
    }


    @PostMapping("/blog")
    public BlogResult createBlog(@RequestBody Blog newBlog) {
        return userService.getCurrentUser()
                .map(user -> blogService.createBlog(newBlog))
                .orElse(BlogResult.failure("登录后才能操作"));
    }


    @PutMapping("/blog/{blogId}")
    public Result updateBlog(@RequestBody Blog updateBlog) {
        return userService.getCurrentUser()
                .map(user -> blogService.updateBlog(updateBlog, user))
                .orElse(BlogResult.failure("登录后才能操作"));
    }

    @DeleteMapping("/blog/{blogId}")
    public Result deleteBlog(@PathVariable("blogId") Integer id) {
        return userService.getCurrentUser()
                .map(user -> blogService.deleteBlog(id, user))
                .orElse(BlogResult.failure("登录后才能操作"));
    }
}
