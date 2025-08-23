package com.tsm.user.dto;

/**
 * 用户查询DTO
 * @author TSM
 */
public class UserQueryDTO {
    
    /**
     * 当前页码
     */
    private Long current = 1L;
    
    /**
     * 每页大小
     */
    private Long size = 10L;
    
    /**
     * 用户名（模糊查询）
     */
    private String username;
    
    /**
     * 真实姓名（模糊查询）
     */
    private String realName;
    
    /**
     * 邮箱（模糊查询）
     */
    private String email;
    
    /**
     * 手机号（模糊查询）
     */
    private String phone;
    
    /**
     * 状态：1启用，0禁用，null查询所有
     */
    private Integer status;
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    public UserQueryDTO() {
    }
    
    // Getter and Setter
    public Long getCurrent() {
        return current;
    }
    
    public void setCurrent(Long current) {
        this.current = current;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
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
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}