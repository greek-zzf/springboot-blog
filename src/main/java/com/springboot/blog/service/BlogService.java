package com.springboot.blog.service;

import com.springboot.blog.bean.BlogResult;
import com.springboot.blog.entity.Blog;
import com.springboot.blog.mapper.BlogMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

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

    public BlogResult getBlogs(Integer page, Integer pageSize, String userId) {
        try {
            List<Blog> blogs = blogMapper.getBlogs(page, pageSize, userId);
            int count = blogMapper.count(userId);
            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogResult.newResult(blogs, count, page, pageCount);

        } catch (Exception e) {
            return BlogResult.failure("系统异常");
        }

    }
}
