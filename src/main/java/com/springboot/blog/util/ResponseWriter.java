package com.springboot.blog.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Zhaofeng Zhou
 * @date 2021/9/27 18:04
 */
public class ResponseWriter<T> {

    public static <T> void toJson(HttpServletResponse response, T data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");

        String responseBody = new ObjectMapper().writeValueAsString(data);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(responseBody);
        printWriter.flush();
        printWriter.close();
    }
}
