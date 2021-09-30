package com.springboot.blog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author Zhaofeng Zhou
 * @date 2021/9/27 18:04
 */
public class ResponseWriter<T> {

    public static <T> void toJson(HttpServletResponse response, T data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");

        byte[] responseBody = new ObjectMapper().registerModule(new JavaTimeModule())
                .writeValueAsBytes(data);
        try (ServletOutputStream so = response.getOutputStream()) {
            so.write(responseBody);
        }

    }
}
