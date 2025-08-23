package com.tsm.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户VO
 * @author TSM
 */
public class UserVO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 状态：1启用，0禁用
     */
    private Integer status;
    
    /**
     * 状态描述
     */
    private String statusText;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 角色列表
     */
    private List<RoleInfo> roles;
    
    /**
     * 角色名称（逗号分隔）
     */
    private String roleNames;
    
    /**
     * 角色信息内部类
     */
    public static class RoleInfo {
        private Long id;
        private String roleName;
        private String roleCode;
        
        public RoleInfo() {
        }
        
        public RoleInfo(Long id, String roleName, String roleCode) {
            this.id = id;
            this.roleName = roleName;
            this.roleCode = roleCode;
        }
        
        // Getter and Setter
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getRoleName() {
            return roleName;
        }
        
        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
        
        public String getRoleCode() {
            return roleCode;
        }
        
        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }
    }
    
    public UserVO() {
    }
    
    // Getter and Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
        this.statusText = status == 1 ? "启用" : "禁用";
    }
    
    public String getStatusText() {
        return statusText;
    }
    
    public void setStatusText(String statusText) {
        this.statusText = statusText;
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
    
    public List<RoleInfo> getRoles() {
        return roles;
    }
    
    public void setRoles(List<RoleInfo> roles) {
        this.roles = roles;
    }
    
    public String getRoleNames() {
        return roleNames;
    }
    
    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }
}