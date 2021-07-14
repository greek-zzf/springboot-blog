package com.springboot.blog.service;

import com.springboot.blog.bean.BlogListResult;
import com.springboot.blog.bean.BlogResult;
import com.springboot.blog.entity.Blog;
import com.springboot.blog.entity.User;
import com.springboot.blog.mapper.BlogMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * @author Zhouzf
 * @date 2021/7/12/012 15:24
 */
@Service
public class BlogService {
    private BlogMapper blogMapper;

    @Inject
    public BlogService(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    public BlogListResult getBlogs(Integer page, Integer pageSize, String userId) {
        try {
            page = page - 1;
            List<Blog> blogs = blogMapper.getBlogs(page, pageSize, userId);

            int count = blogMapper.count(userId);
            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogListResult.success(blogs, count, page, pageCount);
        } catch (Exception e) {
            return BlogListResult.failure("系统异常");
        }

    }

    public BlogResult getBlogById(Integer id) {
        try {
            Blog blog = blogMapper.getBlogById(id);
            return BlogResult.success("获取成功", blog);
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult deleteBlog(Integer id, User user) {
        Blog toBeDeleteBlog = blogMapper.getBlogById(id);
        if (Objects.isNull(toBeDeleteBlog)) {
            return BlogResult.failure("博客不存在");
        }

        if (!user.getId().equals(toBeDeleteBlog.getUserId())) {
            return BlogResult.failure("无法删除别人的博客");
        }

        try {
            blogMapper.deleteBlogById(id);
            return BlogResult.success("删除成功");
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult updateBlog(Integer updateBlogId, Blog blog) {
        Blog tobeUpdateBlog = blogMapper.getBlogById(updateBlogId);
        if (Objects.isNull(tobeUpdateBlog)) {
            return BlogResult.failure("博客不存在");
        }

        if (!blog.getUser().getId().equals(tobeUpdateBlog.getUserId())) {
            return BlogResult.failure("无法修改别人的博客");
        }

        blog.setId(updateBlogId);
        blog.setUpdatedAt(Instant.now());
        try {
            blogMapper.updateBlog(blog);
            return BlogResult.success("修改成功", blogMapper.getBlogById(blog.getId()));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult createBlog(Blog blog) {
        blog.setCreatedAt(Instant.now());
        try {
            blogMapper.insertBlog(blog);
            return BlogResult.success("创建成功", blogMapper.getBlogById(blog.getId()));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

}
