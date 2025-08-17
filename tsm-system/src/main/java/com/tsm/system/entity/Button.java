package com.tsm.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 按钮权限实体类
 * 
 * @author TSM Team
 * @since 1.0.0
 */
@TableName("tsm_button")
public class Button implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 按钮ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属权限ID
     */
    @NotNull(message = "所属权限ID不能为空")
    private Long permissionId;

    /**
     * 按钮名称
     */
    @NotBlank(message = "按钮名称不能为空")
    private String buttonName;

    /**
     * 按钮编码
     */
    @NotBlank(message = "按钮编码不能为空")
    private String buttonCode;

    /**
     * 按钮图标
     */
    private String buttonIcon;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 删除标志：0-未删除，1-已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 所属权限（非数据库字段）
     */
    @TableField(exist = false)
    private Permission permission;

    public Button() {
    }

    public Button(Long permissionId, String buttonName, String buttonCode) {
        this.permissionId = permissionId;
        this.buttonName = buttonName;
        this.buttonCode = buttonCode;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonCode() {
        return buttonCode;
    }

    public void setButtonCode(String buttonCode) {
        this.buttonCode = buttonCode;
    }

    public String getButtonIcon() {
        return buttonIcon;
    }

    public void setButtonIcon(String buttonIcon) {
        this.buttonIcon = buttonIcon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "Button{" +
                "id=" + id +
                ", permissionId=" + permissionId +
                ", buttonName='" + buttonName + '\'' +
                ", buttonCode='" + buttonCode + '\'' +
                ", buttonIcon='" + buttonIcon + '\'' +
                ", sortOrder=" + sortOrder +
                ", status=" + status +
                '}';
    }
}