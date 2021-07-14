package com.springboot.blog.service;

import com.springboot.blog.bean.BlogResult;
import com.springboot.blog.bean.Result;
import com.springboot.blog.entity.Blog;
import com.springboot.blog.entity.User;
import com.springboot.blog.mapper.BlogMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @author Zhouzf
 * @date 2021/7/12/012 15:24
 */
@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {

    @Mock
    BlogMapper blogMapper;

    @InjectMocks
    BlogService blogService;

    @Test
    void returnFailureWhenExceptionThrow() {
        when(blogMapper.getBlogs(anyInt(), anyInt(), any())).thenThrow(RuntimeException.class);

        Result result = blogService.getBlogs(1, 10, null);
        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("系统异常", result.getMsg());
    }


    @Test
    void createBlogTest() {
        Blog tobeInsertBlog = new Blog(12, 12, new User(11, "zzh", null));
        blogService.createBlog(tobeInsertBlog);
        verify(blogMapper).insertBlog(tobeInsertBlog);

        doThrow(RuntimeException.class).when(blogMapper).insertBlog(tobeInsertBlog);
        BlogResult blogResultWithException = blogService.createBlog(tobeInsertBlog);
        Assertions.assertEquals("fail", blogResultWithException.getStatus());
    }

    @Test
    void deleteBlogTest() {
        deleteBlogWhenBlogIsNull();
        deleteBlogWhenUserNotMatch();
        deleteBlogSuccess();
        deleteBlogWhenThrowException();
    }

    private void deleteBlogWhenBlogIsNull() {
        BlogResult blogResult;
        when(blogMapper.getBlogById(1)).thenReturn(null);
        blogResult = blogService.deleteBlog(1, new User(2, "zzh", null));
        Assertions.assertEquals("fail", blogResult.getStatus());
        Assertions.assertEquals("博客不存在", blogResult.getMsg());
    }

    private void deleteBlogWhenUserNotMatch() {
        BlogResult blogResult;
        when(blogMapper.getBlogById(1)).thenReturn(new Blog(1, 2, new User(1, "zzh", null)));
        blogResult = blogService.deleteBlog(1, new User(3, "zzh", null));
        Assertions.assertEquals("fail", blogResult.getStatus());
        Assertions.assertEquals("无法删除别人的博客", blogResult.getMsg());
    }

    private void deleteBlogSuccess() {
        BlogResult blogResult;
        when(blogMapper.getBlogById(1)).thenReturn(new Blog(1, 2, new User(2, "zzh", null)));
        blogResult = blogService.deleteBlog(1, new User(2, "zzh", null));
        verify(blogMapper).deleteBlogById(1);
        Assertions.assertEquals("ok", blogResult.getStatus());
        Assertions.assertEquals("删除成功", blogResult.getMsg());
    }

    private void deleteBlogWhenThrowException() {
        BlogResult blogResult;
        when(blogMapper.getBlogById(1)).thenReturn(null);
        blogResult = blogService.deleteBlog(1, new User(2, "zzh", null));
        Assertions.assertEquals("fail", blogResult.getStatus());
        Assertions.assertEquals("博客不存在", blogResult.getMsg());
    }


}
