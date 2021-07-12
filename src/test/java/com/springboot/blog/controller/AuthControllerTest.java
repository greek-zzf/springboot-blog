package com.springboot.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.blog.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();
    }

    @Test
    void returnNotLogin() throws Exception {
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.defaultCharset()).contains("用户没有登陆")));
    }

    @Test
    void returnLogin() throws Exception {
        mockMvc.perform(get("/auth").session((MockHttpSession) getLoginSuccessSession())).andExpect(status().isOk())
                .andExpect(result ->
                        Assertions.assertTrue(result.getResponse().getContentAsString(Charset.defaultCharset()).contains("myUsername")));
    }


    @Test
    void testLoginSuccess() throws Exception {
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.defaultCharset()).contains("用户没有登陆")));

        mockMvc.perform(get("/auth").session((MockHttpSession) getLoginSuccessSession())).andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.defaultCharset()).contains("myUsername")));
    }

    @Test
    void testLoginFailureUsernameNotFound() throws Exception {
        Mockito.when(userService.loadUserByUsername("myUsername")).thenThrow(UsernameNotFoundException.class);
        Assertions.assertTrue(getLoginInterReturn().contains("用户不存在"));
    }

    @Test
    void testLoginFailureBadCredentials() throws Exception {
        Mockito.when(userService.loadUserByUsername("myUsername")).thenReturn(new User("myUsername", encoder.encode("myPassword"), Collections.emptyList()));
        UserDetails userDetails = userService.loadUserByUsername("myUsername");
        Mockito.doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(userDetails, "myPassword", Collections.emptyList()));
        Assertions.assertTrue(getLoginInterReturn().contains("密码不正确"));
    }


    @Test
    void testLogoutFailure() throws Exception {
        mockMvc.perform(get("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString(Charset.defaultCharset()).contains("用户尚未登录"));
    }

    @Test
    void testLogoutSuccess() throws Exception {
        Mockito.when(userService.getUserByUsername("myUsername")).thenReturn(new com.springboot.blog.entity.User(123, "myUsername", encoder.encode("myPassword")));
        mockMvc.perform(get("/auth/logout").session((MockHttpSession) getLoginSuccessSession()))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString(Charset.defaultCharset()).contains("注销成功"));
    }

    @Test
    void testRegister() throws Exception {
        Assertions.assertTrue(getRegisterInterReturn("", "myPassword").contains("账号或密码不能为空!"));
        Assertions.assertTrue(getRegisterInterReturn("myUsername", "").contains("账号或密码不能为空!"));
        Assertions.assertTrue(getRegisterInterReturn(".username", "myPassword").contains("无效用户名!"));
        Assertions.assertTrue(getRegisterInterReturn("username123456789", "myPassword").contains("无效用户名!"));
        Assertions.assertTrue(getRegisterInterReturn("myUsername", "myPassword123456789").contains("无效密码!"));
        Assertions.assertTrue(getRegisterInterReturn("myUsername", "pwd").contains("无效密码!"));
        Assertions.assertTrue(getRegisterInterReturn("myUsername123456789", "myPassword123456789").contains("无效用户名!"));

        Assertions.assertTrue(getRegisterInterReturn("myUsername", "myPassword").contains("注册成功"));
        Mockito.doThrow(DuplicateKeyException.class).when(userService).save("myUsername", "myPassword");
        Assertions.assertTrue(getRegisterInterReturn("myUsername", "myPassword").contains("当前用户已存在"));
    }


    private String getRegisterInterReturn(String username, String password) throws Exception {
        Map<String, String> usernameAndPassword = new HashMap<>();
        usernameAndPassword.put("username", username);
        usernameAndPassword.put("password", password);

        String requestBody = new ObjectMapper().writeValueAsString(usernameAndPassword);
        MvcResult result = mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString(Charset.defaultCharset());
    }


    private HttpSession getLoginSuccessSession() throws Exception {
        Map<String, String> usernameAndPassword = new HashMap<>();
        usernameAndPassword.put("username", "myUsername");
        usernameAndPassword.put("password", "myPassword");

        Mockito.when(userService.loadUserByUsername("myUsername")).thenReturn(new User("myUsername", encoder.encode("myPassword"), Collections.emptyList()));
        Mockito.when(userService.getUserByUsername("myUsername")).thenReturn(new com.springboot.blog.entity.User(123, "myUsername", encoder.encode("myPassword")));

        String requestBody = new ObjectMapper().writeValueAsString(usernameAndPassword);
        MvcResult response = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString(Charset.defaultCharset()).contains("登录成功")))
                .andReturn();

        return response.getRequest().getSession();
    }


    private String getLoginInterReturn() throws Exception {
        Map<String, String> usernameAndPassword = new HashMap<>();
        usernameAndPassword.put("username", "myUsername");
        usernameAndPassword.put("password", "myPassword");

        String requestBody = new ObjectMapper().writeValueAsString(usernameAndPassword);
        MvcResult response = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        return response.getResponse().getContentAsString(Charset.defaultCharset());
    }


}
