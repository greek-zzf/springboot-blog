package com.springboot.blog.controller;

import com.springboot.blog.bean.Result;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Collections;
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

    @GetMapping
    public Result auth() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.getUserByUsername(username);

        if (Objects.isNull(loggedUser)) {
            return Result.success(false);
        } else {
            return Result.success(true, loggedUser);
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.failure("用户不存在");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());

        User user = userService.getUserByUsername(username);
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            return Result.success("登录成功", user);
        } catch (BadCredentialsException e) {
            return Result.failure("密码不正确");
        }
    }


    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return Result.failure("账号或密码不能为空!");
        }

        if (isInvalidUsername(username)) {
            return Result.failure("无效用户名!");
        }

        if (isInvalidPassword(password)) {
            return Result.failure("无效密码!");
        }


        // 使用数据库的唯一值约束，避免重复用户添加，并且规避了高并发下用户注册的问题
        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            return Result.failure("当前用户已存在");
        }

        return Result.success("注册成功", userService.getUserByUsername(username));
    }


    @GetMapping("/logout")
    public Result logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedUser = userService.getUserByUsername(username);

        if (Objects.isNull(loggedUser)) {
            return Result.failure("用户尚未登录");
        } else {
            SecurityContextHolder.clearContext();
            return Result.success("注销成功");
        }
    }

    private boolean isInvalidUsername(String username) {
        return !username.matches(USERNAME_REGEX);
    }

    private boolean isInvalidPassword(String password) {
        return password.length() < 6 || password.length() > 16;
    }

}
