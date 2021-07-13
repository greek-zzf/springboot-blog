package com.springboot.blog.mapper;

import com.springboot.blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {

    /**
     * 获取该用户的博客信息，当 userId 为空时返回所有博客信息
     *
     * @param page     当前页
     * @param pageSize 页面大小
     * @param userId   用户id
     * @return 博客信息
     */
    List<Blog> getBlogs(@Param("offset") Integer page, @Param("limit") Integer pageSize, @Param("userId") String userId);

    int count(String userId);
}
