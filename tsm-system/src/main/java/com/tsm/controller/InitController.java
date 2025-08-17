package com.tsm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RestController
public class InitController {
    
    /**
     * 根路径重定向到登录页面
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login.html";
    }
    
    @Configuration
    @EnableWebSecurity
    public static class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
            .antMatchers("/auth/**").permitAll()
            .antMatchers("/", "/login.html", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
            .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .defaultSuccessUrl("/static/index.html", true)
                .and()
                .csrf().disable()
                .sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(false);
        }
    }
    

}