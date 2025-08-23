package com.tsm.common.entity;

// 暂时禁用MyBatis Plus注解
// import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限实体类
 * @author TSM
 */
// @TableName("tsm_permission")
public class Permission {
    
    /**
     * 权限ID
     */
    // @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    private String permissionName;
    
    /**
     * 权限编码
     */
    @NotBlank(message = "权限编码不能为空")
    @Pattern(regexp = "^[A-Z_]{2,50}$", message = "权限编码只能包含大写字母和下划线，长度2-50位")
    private String permissionCode;
    
    /**
     * 菜单URL
     */
    private String menuUrl;
    
    /**
     * 父权限ID
     */
    private Long parentId;
    
    /**
     * 权限类型：1菜单，2按钮
     */
    private Integer permissionType;
    
    /**
     * 创建时间
     */
    // @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 子权限列表（非数据库字段）
     */
    // @TableField(exist = false)
    private List<Permission> children;
    
    public Permission() {
    }
    
    public Permission(String permissionName, String permissionCode, String menuUrl, Long parentId, Integer permissionType) {
        this.permissionName = permissionName;
        this.permissionCode = permissionCode;
        this.menuUrl = menuUrl;
        this.parentId = parentId;
        this.permissionType = permissionType;
    }
    
    // Getter and Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPermissionName() {
        return permissionName;
    }
    
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    
    public String getPermissionCode() {
        return permissionCode;
    }
    
    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
    
    public String getMenuUrl() {
        return menuUrl;
    }
    
    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public Integer getPermissionType() {
        return permissionType;
    }
    
    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public List<Permission> getChildren() {
        return children;
    }
    
    public void setChildren(List<Permission> children) {
        this.children = children;
    }
}