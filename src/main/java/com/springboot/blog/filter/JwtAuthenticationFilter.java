package com.springboot.blog.filter;

import com.springboot.blog.util.JwtUtils;
import com.springboot.blog.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Zhaofeng Zhou
 * @date 2021/9/27/027 14:05
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (isAuthenticated()) {
            doNextFilter(request, response, chain);
        }

        String authorizationHeaderMsg = getAuthorizationHeader(request);
        if (isValidAuthorizationHeader(authorizationHeaderMsg)) {
            String token = getTokenFormAuthorizationStr(authorizationHeaderMsg);

            try {
                authenticateToken(token, request);
            } catch (AuthenticationException ex) {
                authenticationEntryPoint.commence(request, response, ex);
            }

        }

        doNextFilter(request, response, chain);
    }

    private boolean isValidAuthorizationHeader(String authorizationHeaderMsg) {
        return StringUtils.isNotBlank(authorizationHeaderMsg) && StringUtils.startsWith(authorizationHeaderMsg, tokenHead);
    }

    private boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }


    private void doNextFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }


    private String getAuthorizationHeader(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private String getTokenFormAuthorizationStr(String authorizationMsg) {
        return authorizationMsg.replace(tokenHead, "");
    }


    private void authenticateToken(String token, HttpServletRequest request) {
        checkToken(token);

        String username = JwtUtils.getUsernameFromToken(token);
        checkUsername(username);

        String accessToken = RedisUtils.getTokenByUsername(username);
        checkAccessTokenThenEqualWithToken(accessToken, token);

        createSuccessAuthentication(username, request);
    }

    private void checkAccessTokenThenEqualWithToken(String accessToken, String token) {
        if (StringUtils.isEmpty(accessToken) || JwtUtils.isExpiredToken(accessToken) || !StringUtils.equals(token, accessToken)) {
            throw new CredentialsExpiredException("登录过期！");
        }
    }


    private void checkUsername(String username) {
        checkTokenOrUsername(username);
    }

    private void checkToken(String token) {
        checkTokenOrUsername(token);
    }

    private void checkTokenOrUsername(String tokenOrUsername) {
        if (null == tokenOrUsername) {
            throw new BadCredentialsException("无效的 token 信息！");
        }
    }

    private void createSuccessAuthentication(String username, HttpServletRequest request) {
        User user = new User(username, "[PROTECTED]", Collections.emptyList());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
