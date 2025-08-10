package com.tsm.common.result;

/**
 * 结果状态码枚举
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    
    // 参数相关
    PARAM_ERROR(400, "参数错误"),
    PARAM_MISSING(401, "参数缺失"),
    PARAM_INVALID(402, "参数无效"),
    
    // 认证相关
    UNAUTHORIZED(403, "未授权"),
    TOKEN_INVALID(404, "Token无效"),
    TOKEN_EXPIRED(405, "Token已过期"),
    LOGIN_REQUIRED(406, "请先登录"),
    
    // 权限相关
    PERMISSION_DENIED(407, "权限不足"),
    BUTTON_PERMISSION_DENIED(408, "按钮权限不足"),
    ROLE_PERMISSION_DENIED(409, "角色权限不足"),
    
    // 用户相关
    USER_NOT_FOUND(410, "用户不存在"),
    USER_DISABLED(411, "用户已禁用"),
    USER_LOCKED(412, "用户已锁定"),
    PASSWORD_ERROR(413, "密码错误"),
    USERNAME_EXISTS(414, "用户名已存在"),
    
    // 角色相关
    ROLE_NOT_FOUND(420, "角色不存在"),
    ROLE_EXISTS(421, "角色已存在"),
    ROLE_IN_USE(422, "角色正在使用中"),
    
    // 权限相关
    PERMISSION_NOT_FOUND(430, "权限不存在"),
    PERMISSION_EXISTS(431, "权限已存在"),
    
    // 按钮相关
    BUTTON_NOT_FOUND(440, "按钮不存在"),
    BUTTON_EXISTS(441, "按钮已存在"),
    
    // 数据相关
    DATA_NOT_FOUND(450, "数据不存在"),
    DATA_EXISTS(451, "数据已存在"),
    DATA_IN_USE(452, "数据正在使用中"),
    
    // 系统相关
    SYSTEM_ERROR(500, "系统错误"),
    DATABASE_ERROR(501, "数据库错误"),
    NETWORK_ERROR(502, "网络错误"),
    FILE_ERROR(503, "文件操作错误");

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