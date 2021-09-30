package com.springboot.blog.handler;

import com.springboot.blog.bean.LoginResult;
import com.springboot.blog.bean.Result;
import com.springboot.blog.service.UserService;
import com.springboot.blog.util.ResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zhaofeng Zhou
 * @date 2021/9/27 17:55
 */
@Configuration
public class LoginResultHandler {

    @Inject
    private UserService userService;

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            com.springboot.blog.entity.User loggedUser = userService.getUserByUsername(principal.getUsername());
            loggedUser.setEncryptedPassword("[PROTECT]");

            // 封装需要返回给前端的信息
            Map<String, Object> map = new HashMap<>(5);
            map.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("isLogin", "true");

            // 生成访问 token
//            String accessToken = jwtTokenUtil.generateToken(loggedUser);
//            map.put("access_token", accessToken);
            map.put("currentUser", loggedUser);

            // token 和相应的用户信息存入 redis（后续整合实现）
            Result loginSuccess = LoginResult.success("登录成功！", loggedUser, true);
            ResponseWriter.toJson(response, loginSuccess);
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            Result loginFailure = LoginResult.failure("登录失败，账号或密码有误！");
            ResponseWriter.toJson(response, loginFailure);
        };
    }

}
