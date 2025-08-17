package com.tsm.system.controller;

import com.tsm.api.common.Result;
import com.tsm.system.entity.User;
import com.tsm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 
 * @author TSM Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            String captcha = loginRequest.getCaptcha();
            
            // 验证输入参数
            if (username == null || username.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                return Result.error("密码不能为空");
            }
            if (captcha == null || captcha.trim().isEmpty()) {
                return Result.error("验证码不能为空");
            }
            
            // 验证验证码（简单验证，实际项目中应该从session中获取）
            String sessionCaptcha = (String) session.getAttribute("captcha");
            if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
                return Result.error("验证码错误");
            }
            
            // 查找用户
            User user = userService.getUserByUsername(username);
            System.out.println("查找用户: " + username + ", 结果: " + (user != null ? "找到" : "未找到"));
            if (user == null) {
                return Result.error("用户名或密码错误");
            }
            
            // 验证密码
            System.out.println("输入密码: " + password);
            System.out.println("数据库密码: " + user.getPassword());
            boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
            System.out.println("密码匹配结果: " + passwordMatch);
            if (!passwordMatch) {
                return Result.error("用户名或密码错误");
            }
            
            // 检查用户状态
            if (user.getStatus() != 1) {
                return Result.error("账户已被禁用");
            }
            
            // 登录成功，保存用户信息到session
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            
            // 返回用户信息
            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getId());
            data.put("username", user.getUsername());
            data.put("realName", user.getRealName());
            data.put("email", user.getEmail());
            
            return Result.success("登录成功", data);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录失败：" + e.getMessage());
        }
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        try {
            session.invalidate();
            return Result.success("登出成功");
        } catch (Exception e) {
            return Result.error("登出失败：" + e.getMessage());
        }
    }
    

    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public Result<Map<String, Object>> getCurrentUser(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return Result.error("用户未登录");
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getId());
            data.put("username", user.getUsername());
            data.put("realName", user.getRealName());
            data.put("email", user.getEmail());
            
            return Result.success("获取成功", data);
        } catch (Exception e) {
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }
    
    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> generateCaptcha(HttpSession session) {
        try {
            // 生成简单的验证码
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuilder captcha = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                captcha.append(chars.charAt((int) (Math.random() * chars.length())));
            }
            
            String captchaCode = captcha.toString();
            session.setAttribute("captcha", captchaCode);
            
            Map<String, String> data = new HashMap<>();
            data.put("captcha", captchaCode);
            
            return Result.success("验证码生成成功", data);
        } catch (Exception e) {
            return Result.error("验证码生成失败：" + e.getMessage());
        }
    }
    
    /**
     * 登录请求对象
     */
    public static class LoginRequest {
        private String username;
        private String password;
        private String captcha;
        private Boolean remember;
        
        // Getters and Setters
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public String getCaptcha() {
            return captcha;
        }
        
        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }
        
        public Boolean getRemember() {
            return remember;
        }
        
        public void setRemember(Boolean remember) {
            this.remember = remember;
        }
    }
}