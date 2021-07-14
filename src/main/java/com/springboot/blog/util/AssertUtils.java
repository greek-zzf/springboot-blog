package com.springboot.blog.util;

/**
 * @author Zhouzf
 * @date 2021/7/14/014 22:37
 */
public class AssertUtils {
    public static void assertTrue(boolean value, String message) {
        if (!value) {
            throw new IllegalArgumentException(message);
        }
    }

}
