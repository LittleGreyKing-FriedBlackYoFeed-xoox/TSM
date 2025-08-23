package com.tsm.auth.controller;

import com.tsm.auth.dto.LoginRequest;
import com.tsm.auth.service.AuthService;
import com.tsm.auth.vo.LoginResponse;
import com.tsm.common.result.Result;
import com.tsm.common.util.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 * @author TSM
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return Result.success("登录成功", response);
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            authService.logout(token);
        }
        return Result.success("登出成功");
    }
    
    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    public Result<LoginResponse> refreshToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return Result.error("令牌不能为空");
        }
        LoginResponse response = authService.refreshToken(token);
        return Result.success("刷新成功", response);
    }
    
    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    public Result<CaptchaUtil.CaptchaResult> generateCaptcha() {
        CaptchaUtil.CaptchaResult captcha = authService.generateCaptcha();
        return Result.success(captcha);
    }
    
    /**
     * 验证令牌
     */
    @GetMapping("/validate")
    public Result<Boolean> validateToken(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            return Result.success(false);
        }
        boolean valid = authService.validateToken(token);
        return Result.success(valid);
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/userinfo")
    public Result<Object> getUserInfo(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !authService.validateToken(token)) {
            return Result.error("令牌无效");
        }
        
        String username = authService.getUsernameFromToken(token);
        Long userId = authService.getUserIdFromToken(token);
        
        return Result.success(new Object() {
            public String getUsername() { return username; }
            public Long getUserId() { return userId; }
        });
    }
    
    /**
     * 从请求中获取令牌
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}