package com.tsm.common.core.result;

/**
 * 结果状态码枚举
 * 
 * @author TSM Team
 * @since 1.0.0
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
     * 用户不存在
     */
    USER_NOT_FOUND(1001, "用户不存在"),
    
    /**
     * 用户名已存在
     */
    USERNAME_EXISTS(1002, "用户名已存在"),
    
    /**
     * 邮箱已存在
     */
    EMAIL_EXISTS(1003, "邮箱已存在"),
    
    /**
     * 手机号已存在
     */
    PHONE_EXISTS(1004, "手机号已存在"),
    
    /**
     * 密码错误
     */
    PASSWORD_ERROR(1005, "密码错误"),
    
    /**
     * 角色不存在
     */
    ROLE_NOT_FOUND(2001, "角色不存在"),
    
    /**
     * 角色编码已存在
     */
    ROLE_CODE_EXISTS(2002, "角色编码已存在"),
    
    /**
     * 角色名称已存在
     */
    ROLE_NAME_EXISTS(2003, "角色名称已存在"),
    
    /**
     * 权限不存在
     */
    PERMISSION_NOT_FOUND(3001, "权限不存在"),
    
    /**
     * 权限编码已存在
     */
    PERMISSION_CODE_EXISTS(3002, "权限编码已存在"),
    
    /**
     * 权限名称已存在
     */
    PERMISSION_NAME_EXISTS(3003, "权限名称已存在"),
    
    /**
     * 按钮不存在
     */
    BUTTON_NOT_FOUND(4001, "按钮不存在"),
    
    /**
     * 按钮编码已存在
     */
    BUTTON_CODE_EXISTS(4002, "按钮编码已存在"),
    
    /**
     * 按钮名称已存在
     */
    BUTTON_NAME_EXISTS(4003, "按钮名称已存在"),
    
    /**
     * 数据已存在
     */
    DATA_ALREADY_EXISTS(5001, "数据已存在"),
    
    /**
     * 数据未找到
     */
    DATA_NOT_FOUND(5002, "数据未找到"),
    
    /**
     * 数据正在使用中
     */
    DATA_IN_USE(5003, "数据正在使用中，无法删除");
    
    /**
     * 状态码
     */
    private final int code;
    
    /**
     * 消息
     */
    private final String message;
    
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}