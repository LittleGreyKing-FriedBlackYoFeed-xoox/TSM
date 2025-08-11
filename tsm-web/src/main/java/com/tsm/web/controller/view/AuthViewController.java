package com.tsm.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 认证视图控制器
 * 负责认证相关页面的跳转
 */
@Controller
@RequestMapping("/auth")
public class AuthViewController {

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String login() {
        return "views/auth/login";
    }

    /**
     * 注册页面
     */
    @GetMapping("/register")
    public String register() {
        return "views/auth/register";
    }

    /**
     * 忘记密码页面
     */
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "views/auth/forgot-password";
    }

    /**
     * 重置密码页面
     */
    @GetMapping("/reset-password")
    public String resetPassword() {
        return "views/auth/reset-password";
    }
}