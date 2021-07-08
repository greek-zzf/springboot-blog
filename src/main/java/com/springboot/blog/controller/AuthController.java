package com.springboot.blog.controller;

import com.springboot.blog.bean.Result;
import com.springboot.blog.entity.User;
import com.springboot.blog.mapper.UserMapper;
import com.springboot.blog.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private UserMapper userMapper;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public Result auth() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.getUserByUsername(username);

        if (Objects.isNull(loggedUser)) {
            return Result.loginFailure("ok", false);
        } else {
            return Result.loginSuccess("ok", true, loggedUser);
        }
    }

    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.failure("fail", "用户不存在");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());

        User user = userService.getUserByUsername(username);
        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            return Result.success("ok", "登录成功", user);
        } catch (BadCredentialsException e) {
            return Result.failure("fail", "密码不正确");
        }
    }

    @GetMapping("/logout/success")
    public void logoutSuccessHandle() {
    }


    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        if (isFirstRegister(username)) {
            userService.save(username, password);
            return Result.success("ok", "注册成功", userService.getUserByUsername(username));
        } else {
            return Result.failure("fail", "当前用户已存在");
        }
    }

    private boolean isFirstRegister(String username) {
        return Objects.isNull(userService.getUserByUsername(username));
    }


}
