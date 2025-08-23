package com.tsm.auth.service;

import com.tsm.auth.dto.LoginRequest;
import com.tsm.auth.vo.LoginResponse;
import com.tsm.common.util.CaptchaUtil;

/**
 * 认证服务接口
 * @author TSM
 */
public interface AuthService {
    
    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 用户登出
     * @param token JWT令牌
     */
    void logout(String token);
    
    /**
     * 刷新令牌
     * @param token JWT令牌
     * @return 新的登录响应
     */
    LoginResponse refreshToken(String token);
    
    /**
     * 生成验证码
     * @return 验证码结果
     */
    CaptchaUtil.CaptchaResult generateCaptcha();
    
    /**
     * 验证令牌
     * @param token JWT令牌
     * @return 是否有效
     */
    boolean validateToken(String token);
    
    /**
     * 从令牌中获取用户名
     * @param token JWT令牌
     * @return 用户名
     */
    String getUsernameFromToken(String token);
    
    /**
     * 从令牌中获取用户ID
     * @param token JWT令牌
     * @return 用户ID
     */
    Long getUserIdFromToken(String token);
}