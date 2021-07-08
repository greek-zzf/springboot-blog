package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhouzf
 * @date 2021/7/8/008 15:12
 */
@Service
public class UserService implements UserDetailsService {
    private Map<String, com.springboot.blog.entity.User> database = new ConcurrentHashMap<>();

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

        User user = database.get(username);
        return new org.springframework.security.core.userdetails.User(username, user.getEncryptedPassword(), Collections.emptyList());
    }


    public void save(String username, String password) {
        database.put(username, new com.springboot.blog.entity.User(1, username, passwordEncoder.encode(password)));
    }

    public com.springboot.blog.entity.User getUserByUsername(String username) {
        return database.get(username);
    }
}
