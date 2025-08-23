package com.tsm.common.util;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 密码工具类
 * @author TSM
 */
public class PasswordUtil {
    
    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }
    
    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
    
    /**
     * 生成随机密码
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }
    
    /**
     * 验证密码强度
     * @param password 密码
     * @return 密码强度等级（1-4）
     */
    public static int checkPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return 1; // 弱
        }
        
        int score = 0;
        
        // 长度检查
        if (password.length() >= 8) {
            score++;
        }
        
        // 包含小写字母
        if (password.matches(".*[a-z].*")) {
            score++;
        }
        
        // 包含大写字母
        if (password.matches(".*[A-Z].*")) {
            score++;
        }
        
        // 包含数字
        if (password.matches(".*[0-9].*")) {
            score++;
        }
        
        // 包含特殊字符
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':,.<>?].*")) {
            score++;
        }
        
        if (score <= 2) {
            return 2; // 中等
        } else if (score <= 4) {
            return 3; // 强
        } else {
            return 4; // 很强
        }
    }
}