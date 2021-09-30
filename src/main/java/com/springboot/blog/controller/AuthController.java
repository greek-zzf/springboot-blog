package com.springboot.blog.controller;

import com.springboot.blog.bean.LoginResult;
import com.springboot.blog.bean.Result;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;

/**
 * @author Zhouzf
 * @date 2021/7/8/008 11:14
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;
    private AuthenticationManager authenticationManager;

    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{1,15}$";

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return LoginResult.failure("账号或密码不能为空!");
        }

        if (isInvalidUsername(username)) {
            return LoginResult.failure("无效用户名!");
        }

        if (isInvalidPassword(password)) {
            return LoginResult.failure("无效密码!");
        }


        // 使用数据库的唯一值约束，避免重复用户添加，并且规避了高并发下用户注册的问题
        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            return LoginResult.failure("当前用户已存在");
        }

        return LoginResult.success("注册成功", userService.getUserByUsername(username), false);
    }


    @GetMapping("/logout")
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = userService.getUserByUsername(authentication == null ? null : authentication.getName());

        if (Objects.isNull(loggedUser)) {
            return LoginResult.failure("用户尚未登录");
        } else {
            SecurityContextHolder.clearContext();
            return LoginResult.success("注销成功", false);
        }
    }

    private boolean isInvalidUsername(String username) {
        return !username.matches(USERNAME_REGEX);
    }

    private boolean isInvalidPassword(String password) {
        return password.length() < 6 || password.length() > 16;
    }

}
