package com.springboot.blog.service;

import com.springboot.blog.bean.Result;
import com.springboot.blog.mapper.BlogMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

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
    void getBlogFromDb() {
        blogService.getBlogs(1, 10, null);
        Mockito.verify(blogMapper).getBlogs(1, 10, null);
    }

    @Test
    void returnFailureWhenExceptionThrow() {
        Mockito.when(blogMapper.getBlogs(anyInt(), anyInt(), any())).thenThrow(RuntimeException.class);

        Result result = blogService.getBlogs(1, 10, null);
        Assertions.assertEquals("fail", result.getStatus());
        Assertions.assertEquals("系统异常", result.getMsg());

    }


}
