package com.tsm.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tsm.common.entity.BaseEntity;

/**
 * 用户按钮权限关联实体类
 * 实现按钮级权限控制
 */
@TableName("sys_user_button")
public class UserButton extends BaseEntity {

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 按钮ID
     */
    @TableField("button_id")
    private Long buttonId;

    /**
     * 权限类型（1允许 0禁止）
     */
    @TableField("permission_type")
    private Integer permissionType;

    /**
     * 生效时间
     */
    @TableField("effective_time")
    private java.time.LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    @TableField("expiry_time")
    private java.time.LocalDateTime expiryTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getButtonId() {
        return buttonId;
    }

    public void setButtonId(Long buttonId) {
        this.buttonId = buttonId;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public java.time.LocalDateTime getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(java.time.LocalDateTime effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public java.time.LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(java.time.LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}