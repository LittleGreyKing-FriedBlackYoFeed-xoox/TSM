package com.tsm.auth.service.impl;

import com.tsm.auth.dto.LoginRequest;
// 暂时禁用MyBatis
// import com.tsm.auth.mapper.UserMapper;
import com.tsm.auth.service.AuthService;
import com.tsm.auth.vo.LoginResponse;
import com.tsm.common.entity.User;
import com.tsm.common.exception.BusinessException;
import com.tsm.common.result.ResultCode;
import com.tsm.common.util.CaptchaUtil;
import com.tsm.common.util.JwtUtil;
import com.tsm.common.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 * @author TSM
 */
@Service
public class AuthServiceImpl implements AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    
    private static final String CAPTCHA_PREFIX = "captcha:";
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    
    // 暂时禁用MyBatis
    // @Autowired
    // private UserMapper userMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 暂时禁用数据库查询，返回模拟数据
        // 简化登录逻辑，移除验证码验证
        
        // 模拟用户数据
        Long userId = 1L;
        String username = loginRequest.getUsername();
        
        // 生成JWT令牌
        String token = JwtUtil.generateToken(userId, username);
        
        // 构建用户信息
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                userId, username, "测试用户", 
                "test@example.com", "13800138000");
        userInfo.setRoles(List.of("USER"));
        userInfo.setPermissions(List.of("READ"));
        
        // 计算过期时间
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);
        
        // 记录登录日志
        logger.info("用户 {} 登录成功", username);
        
        return new LoginResponse(token, expiresAt, userInfo);
    }
    
    @Override
    public void logout(String token) {
        try {
            // 将令牌加入黑名单
            String username = JwtUtil.getUsernameFromToken(token);
            long expiration = JwtUtil.getExpirationDateFromToken(token).getTime();
            long now = System.currentTimeMillis();
            if (expiration > now) {
                long ttl = expiration - now;
                redisTemplate.opsForValue().set(
                        TOKEN_BLACKLIST_PREFIX + token, 
                        username, 
                        ttl, 
                        TimeUnit.MILLISECONDS);
            }
            logger.info("用户 {} 登出成功", username);
        } catch (Exception e) {
            logger.warn("登出时处理令牌失败: {}", e.getMessage());
        }
    }
    
    @Override
    public LoginResponse refreshToken(String token) {
        // 验证令牌
        if (!validateToken(token)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        
        // 获取用户信息
        String username = JwtUtil.getUsernameFromToken(token);
        Long userId = JwtUtil.getUserIdFromToken(token);
        
        // 生成新令牌
        String newToken = JwtUtil.generateToken(userId, username);
        
        // 将旧令牌加入黑名单
        logout(token);
        
        // 构建用户信息
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                userId, username, "测试用户", 
                "test@example.com", "13800138000");
        userInfo.setRoles(List.of("USER"));
        userInfo.setPermissions(List.of("READ"));
        
        // 计算过期时间
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);
        
        return new LoginResponse(newToken, expiresAt, userInfo);
    }
    
    @Override
    public CaptchaUtil.CaptchaResult generateCaptcha() {
        // 生成验证码
        CaptchaUtil.CaptchaResult captcha = CaptchaUtil.generateCaptcha();
        
        // 生成验证码Key
        String captchaKey = UUID.randomUUID().toString();
        
        // 将验证码存储到Redis，5分钟过期
        redisTemplate.opsForValue().set(
                CAPTCHA_PREFIX + captchaKey, 
                captcha.getCode().toLowerCase(), 
                5, 
                TimeUnit.MINUTES);
        
        // 返回结果（包含captchaKey）
        return new CaptchaUtil.CaptchaResult(captchaKey, captcha.getImageBase64());
    }
    
    @Override
    public boolean validateToken(String token) {
        try {
            // 检查令牌是否在黑名单中
            if (redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token)) {
                return false;
            }
            
            // 验证令牌格式和过期时间
            String username = JwtUtil.getUsernameFromToken(token);
            return !JwtUtil.isTokenExpired(token) && username != null;
        } catch (Exception e) {
            logger.warn("验证令牌失败: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getUsernameFromToken(String token) {
        try {
            return JwtUtil.getUsernameFromToken(token);
        } catch (Exception e) {
            logger.warn("从令牌获取用户名失败: {}", e.getMessage());
            return null;
        }
    }
    
    @Override
    public Long getUserIdFromToken(String token) {
        try {
            return JwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            logger.warn("从令牌获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }
    

}