package com.springboot.blog.config;

import com.springboot.blog.bean.LoginResult;
import com.springboot.blog.filter.JsonLoginFilter;
import com.springboot.blog.filter.JwtAuthenticationFilter;
import com.springboot.blog.util.ResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.inject.Inject;
import javax.inject.Named;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author Zhouzf
 * @date 2021/7/8/008 13:32
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESS_URL = "/auth/login";

    @Named("userService")
    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    AuthenticationManager authenticationManager;
    @Inject
    AuthenticationFailureHandler authenticationFailureHandler;
    @Inject
    AuthenticationSuccessHandler authenticationSuccessHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)

                .and()
                .authorizeRequests().antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .anyRequest().permitAll()

                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(accessDeniedException()).authenticationEntryPoint(authenticationEntryPoint());
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public JsonLoginFilter jsonLoginFilter() {
        JsonLoginFilter jsonLoginFilter = new JsonLoginFilter(authenticationManager);
        jsonLoginFilter.setFilterProcessesUrl(LOGIN_PROCESS_URL);
        jsonLoginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        jsonLoginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return jsonLoginFilter;
    }

    @Bean
    public AccessDeniedHandler accessDeniedException() {
        return (request, response, accessDeniedException) -> ResponseWriter.toJson(response, LoginResult.failure("无权限访问！"));
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> ResponseWriter.toJson(response, LoginResult.failure("账号或密码错误！"));
    }

}
