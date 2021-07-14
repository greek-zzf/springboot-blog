package com.springboot.blog.controller;

import com.springboot.blog.bean.BlogResult;
import com.springboot.blog.bean.Result;
import com.springboot.blog.entity.Blog;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.BlogService;
import com.springboot.blog.service.UserService;
import com.springboot.blog.util.AssertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

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
    public BlogResult createBlog(@RequestBody Map<String, String> params) {
        return userService.getCurrentUser()
                .map(user -> blogService.createBlog(fromParam(params, user)))
                .orElse(BlogResult.failure("登录后才能操作"));
    }

    @PatchMapping("/blog/{blogId}")
    public Result updateBlog(@PathVariable("blogId") Integer blogId, @RequestBody Map<String, String> params) {
        return userService.getCurrentUser()
                .map(user -> blogService.updateBlog(blogId, fromParam(params, user)))
                .orElse(BlogResult.failure("登录后才能操作"));
    }

    @DeleteMapping("/blog/{blogId}")
    public Result deleteBlog(@PathVariable("blogId") Integer id) {
        return userService.getCurrentUser()
                .map(user -> blogService.deleteBlog(id, user))
                .orElse(BlogResult.failure("登录后才能操作"));
    }


    private Blog fromParam(Map<String, String> params, User user) {
        Blog blog = new Blog();
        String title = params.get("title");
        String content = params.get("content");
        String description = params.get("description");

        AssertUtils.assertTrue(StringUtils.isNotBlank(title) && title.length() < 100, "title is invalid!");
        AssertUtils.assertTrue(StringUtils.isNotBlank(content) && content.length() < 10000, "content is invalid");

        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }

        blog.setTitle(title);
        blog.setContent(content);
        blog.setDescription(description);
        blog.setUser(user);
        return blog;
    }
}
