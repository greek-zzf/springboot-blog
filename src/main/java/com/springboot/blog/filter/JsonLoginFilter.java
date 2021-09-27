package com.springboot.blog.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Zhaofeng Zhou
 * @date 2021/9/27/027 15:55
 */
public class JsonLoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JsonLoginFilter.class);

    private static ThreadLocal<String> passwordThreadLocal = new ThreadLocal<>();

    public JsonLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        JsonNode body = null;
        try {
            body = new ObjectMapper().readValue(request.getInputStream(), JsonNode.class);
        } catch (IOException e) {
            log.error("read request body error");
        }
        passwordThreadLocal.set(body.get(SPRING_SECURITY_FORM_PASSWORD_KEY).asText());
        return body.get(SPRING_SECURITY_FORM_USERNAME_KEY).asText();
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String s = passwordThreadLocal.get();
        passwordThreadLocal.remove();
        return s;
    }
}
