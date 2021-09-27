package com.springboot.blog.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Zhaofeng Zhou
 * @date 2021/9/27/027 14:05
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // 认证通过，直接放行
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
        }

        // 获取 token 信息
        String token = getTokenFormRequestHeader(request);
        // 校验 token
        checkToken(token);
        // 验证通过放行
        chain.doFilter(request, response);
    }

    private void checkToken(String token) {

    }


    private String getTokenFormRequestHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(header) && header.startsWith(tokenHead)) {
            String token = header.replace(tokenHead, "");
            if (StringUtils.hasText(token)) {
                return token;
            }
        }
        throw new RuntimeException("token not found!");
    }
}
