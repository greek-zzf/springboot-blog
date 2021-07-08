package com.springboot.blog.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhouzf
 * @date 2021/7/8/008 15:12
 */
@Service
public class UserService implements UserDetailsService {
    private Map<String, String> database = new ConcurrentHashMap<>();

    private PasswordEncoder passwordEncoder;

    @Inject
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        save("zzf", "123456");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!database.containsKey(username)) {
            throw new UsernameNotFoundException(username + " 不存在!");
        }

        String password = database.get(username);
        return new User(username, password, Collections.emptyList());
    }


    public Map save(String username, String password) {
        database.put(username, passwordEncoder.encode(password));
        Map<String, String> map = new HashMap();
        map.put(username, password);
        return map;
    }

    public boolean getUserByUserName(String username) {
        return database.containsKey(username) && StringUtils.hasText(database.get(username));
    }
}
