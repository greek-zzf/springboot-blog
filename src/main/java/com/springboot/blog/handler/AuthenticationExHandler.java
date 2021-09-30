package com.springboot.blog.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Zhaofeng Zhou
 * @date 2021/9/30/030 10:36
 */
@Component
public class AuthenticationExHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HashMap<String, String> map = returnMap(authException.getMessage(), request.getRequestURI());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(map);

        try (ServletOutputStream sr = response.getOutputStream()) {
            sr.write(resBody.getBytes());
        }
    }


    private HashMap<String, String> returnMap(String msg, String uri) {
        HashMap<String, String> map = new HashMap<>(3);
        map.put("uri", uri);
        map.put("msg", "未登录禁止访问");
        map.put("code", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));

        if (StringUtils.isNotEmpty(msg)) {
            map.put("msg", msg);
        }

        return map;
    }
}
