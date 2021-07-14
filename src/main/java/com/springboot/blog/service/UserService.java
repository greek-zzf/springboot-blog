package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Zhouzf
 * @date 2021/7/8/008 15:12
 */
@Service
public class UserService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    @Inject
    public UserService(PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getUserByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(username + " 不存在!");
        }
        return new org.springframework.security.core.userdetails.User(username, user.getEncryptedPassword(), Collections.emptyList());
    }


    public void save(String username, String password) {
        userMapper.save(username, passwordEncoder.encode(password));
    }

    public com.springboot.blog.entity.User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(getUserByUsername(authentication == null ? null : authentication.getName()));
    }
}
