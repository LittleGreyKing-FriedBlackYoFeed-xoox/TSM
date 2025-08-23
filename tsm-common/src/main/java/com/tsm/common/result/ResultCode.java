package com.tsm.common.result;

/**
 * 响应状态码枚举
 * @author TSM
 */
public enum ResultCode {
    
    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),
    
    /**
     * 失败
     */
    ERROR(500, "操作失败"),
    
    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),
    
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),
    
    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),
    
    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),
    
    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1001, "用户名或密码错误"),
    
    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(1002, "验证码错误"),
    
    /**
     * 账户被禁用
     */
    ACCOUNT_DISABLED(1003, "账户被禁用"),
    
    /**
     * Token无效
     */
    TOKEN_INVALID(1004, "Token无效"),
    
    /**
     * Token过期
     */
    TOKEN_EXPIRED(1005, "Token过期"),
    
    /**
     * 用户不存在
     */
    USER_NOT_FOUND(2001, "用户不存在"),
    
    /**
     * 用户名已存在
     */
    USERNAME_EXISTS(2002, "用户名已存在"),
    
    /**
     * 邮箱已存在
     */
    EMAIL_EXISTS(2003, "邮箱已存在"),
    
    /**
     * 手机号已存在
     */
    PHONE_EXISTS(2004, "手机号已存在"),
    
    /**
     * 角色不存在
     */
    ROLE_NOT_FOUND(3001, "角色不存在"),
    
    /**
     * 角色编码已存在
     */
    ROLE_CODE_EXISTS(3002, "角色编码已存在"),
    
    /**
     * 权限不存在
     */
    PERMISSION_NOT_FOUND(4001, "权限不存在"),
    
    /**
     * 权限编码已存在
     */
    PERMISSION_CODE_EXISTS(4002, "权限编码已存在"),
    
    /**
     * 数据库操作失败
     */
    DATABASE_ERROR(5001, "数据库操作失败"),
    
    /**
     * 系统异常
     */
    SYSTEM_ERROR(9999, "系统异常");
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}