package com.springboot.blog.mapper;

import com.springboot.blog.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Zhouzf
 * @date 2021/7/8/008 15:49
 */
@Mapper
public interface UserMapper {

    @Insert("insert into user (username,encrypted_password,created_at,updated_at) values(#{username},#{encryptedPassword},now(),now())")
    void save(@Param("username") String username, @Param("encryptedPassword") String encryptedPassword);

    @Select("select id,username,encrypted_password,avatar,updated_at,created_at from user where username = #{username}")
    User findUserByUsername(String username);

}
