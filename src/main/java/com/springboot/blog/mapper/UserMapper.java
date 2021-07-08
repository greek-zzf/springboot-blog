package com.springboot.blog.mapper;

import com.springboot.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Zhouzf
 * @date 2021/7/8/008 15:49
 */
@Mapper
public interface UserMapper {

    @Select("select * from user limit 1")
    User getUser();

}
