package com.tsm.web.controller;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器 - 简化版本
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        Map<String, Object> result = new HashMap<>();
        
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        // 简单的用户验证（实际项目中应该查询数据库）
        if ("admin".equals(username) && "123456".equals(password)) {
            Map<String, Object> data = new HashMap<>();
            Map<String, Object> user = new HashMap<>();
            user.put("id", 1);
            user.put("username", "admin");
            user.put("nickname", "管理员");
            
            data.put("token", "mock-jwt-token-" + System.currentTimeMillis());
            data.put("user", user);
            
            result.put("code", 200);
            result.put("message", "登录成功");
            result.put("data", data);
        } else {
            result.put("code", 401);
            result.put("message", "用户名或密码错误");
            result.put("data", null);
        }
        
        return result;
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/userinfo")
    public Map<String, Object> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> result = new HashMap<>();
        
        if (token != null && token.startsWith("Bearer ")) {
            Map<String, Object> user = new HashMap<>();
            user.put("id", 1);
            user.put("username", "admin");
            user.put("nickname", "管理员");
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", user);
        } else {
            result.put("code", 401);
            result.put("message", "未授权");
            result.put("data", null);
        }
        
        return result;
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Map<String, Object> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登出成功");
        result.put("data", null);
        return result;
    }
}