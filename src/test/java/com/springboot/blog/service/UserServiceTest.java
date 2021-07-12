package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Zhouzf
 * @date 2021/7/9/009 16:54
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    PasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;

    @InjectMocks
    UserService userService;

    @Test
    void testSave() {
        // 使用 mockEncoder，将 "myPassword" 转码成 "encodedPassword"
        // 调用 userService
        // 转发请求给 userMapper
        Mockito.when(mockEncoder.encode("myPassword")).thenReturn("encodedPassword");
        userService.save("myUsername", "myPassword");
        Mockito.verify(mockMapper).save("myUsername", "encodedPassword");
    }

    @Test
    void testGetUserByUsername() {
        userService.getUserByUsername("myUsername");
        Mockito.verify(mockMapper).findUserByUsername("myUsername");
    }

    @Test
    void throwExceptionWhenUserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("myUsername"));
    }

    @Test
    void returnUserDetailsWhenUserFound() {
        Mockito.when(mockMapper.findUserByUsername("myUsername")).thenReturn(new User(123, "myUsername", "encodedPassword"));
        UserDetails userDetails = userService.loadUserByUsername("myUsername");

        Assertions.assertEquals("myUsername", userDetails.getUsername());
        Assertions.assertEquals("encodedPassword", userDetails.getPassword());
    }
}
