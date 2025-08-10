package com.tsm.auth.service;

import com.tsm.auth.entity.Button;
import com.tsm.auth.entity.User;

import java.util.List;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

    /**
     * 检查用户权限
     */
    boolean hasPermission(Long userId, String permissionCode);

    /**
     * 检查用户按钮权限
     */
    boolean hasButtonPermission(Long userId, String buttonCode);

    /**
     * 获取用户权限列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 获取用户按钮权限列表
     */
    List<String> getUserButtonPermissions(Long userId);

    /**
     * 获取用户在指定页面的按钮权限
     */
    List<Button> getUserButtonsByPage(Long userId, String pageCode);

    /**
     * 为用户分配按钮权限
     */
    boolean assignButtonPermission(Long userId, Long buttonId, Integer permissionType);

    /**
     * 批量为用户分配按钮权限
     */
    boolean batchAssignButtonPermissions(Long userId, List<Long> buttonIds);

    /**
     * 移除用户按钮权限
     */
    boolean removeButtonPermission(Long userId, Long buttonId);

    /**
     * 清空用户所有按钮权限
     */
    boolean clearUserButtonPermissions(Long userId);
}