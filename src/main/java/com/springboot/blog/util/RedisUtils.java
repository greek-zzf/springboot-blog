package com.springboot.blog.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author Zhaofeng Zhou
 * @date 2021/9/30/030 10:51
 */
public class RedisUtils {

    private static final String TOKEN_KEY = "USER_TOKEN";
    private static final String USER_KEY = "USER_INFO";


    @Inject
    private static StringRedisTemplate redisTemplate;


    public static String getTokenByUsername(String username) {
        Object tokenObject = redisTemplate.opsForHash().get(TOKEN_KEY, username + "_access_token");
        return Optional.ofNullable(tokenObject)
                .map(Object::toString)
                .orElse(null);
    }

}
