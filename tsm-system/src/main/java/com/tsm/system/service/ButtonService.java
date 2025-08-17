package com.tsm.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tsm.system.entity.Button;

import java.util.List;

/**
 * 按钮权限服务接口
 *
 * @author TSM
 * @since 2024-01-01
 */
public interface ButtonService extends IService<Button> {

    /**
     * 根据按钮编码查询按钮
     *
     * @param buttonCode 按钮编码
     * @return 按钮信息
     */
    Button getButtonByCode(String buttonCode);

    /**
     * 分页查询按钮列表
     *
     * @param page         分页参数
     * @param buttonName   按钮名称（模糊查询）
     * @param buttonCode   按钮编码（模糊查询）
     * @param permissionId 所属权限ID
     * @param status       状态（0-禁用 1-启用）
     * @return 按钮分页列表
     */
    IPage<Button> getButtonList(Page<Button> page, String buttonName, String buttonCode, Long permissionId, Integer status);

    /**
     * 查询按钮及其关联权限信息
     *
     * @param buttonId 按钮ID
     * @return 按钮及权限信息
     */
    Button getButtonWithPermission(Long buttonId);

    /**
     * 根据权限ID查询按钮列表
     *
     * @param permissionId 权限ID
     * @param status       状态（0-禁用 1-启用）
     * @return 按钮列表
     */
    List<Button> getButtonsByPermissionId(Long permissionId, Integer status);

    /**
     * 根据角色ID查询按钮列表
     *
     * @param roleId 角色ID
     * @param status 状态（0-禁用 1-启用）
     * @return 按钮列表
     */
    List<Button> getButtonsByRoleId(Long roleId, Integer status);

    /**
     * 根据用户ID查询按钮列表
     *
     * @param userId 用户ID
     * @param status 状态（0-禁用 1-启用）
     * @return 按钮列表
     */
    List<Button> getButtonsByUserId(Long userId, Integer status);

    /**
     * 创建按钮
     *
     * @param button 按钮信息
     * @return 是否成功
     */
    boolean createButton(Button button);

    /**
     * 更新按钮
     *
     * @param button 按钮信息
     * @return 是否成功
     */
    boolean updateButton(Button button);

    /**
     * 删除按钮（逻辑删除）
     *
     * @param buttonId 按钮ID
     * @return 是否成功
     */
    boolean deleteButton(Long buttonId);

    /**
     * 批量删除按钮（逻辑删除）
     *
     * @param buttonIds 按钮ID列表
     * @return 是否成功
     */
    boolean deleteButtons(List<Long> buttonIds);

    /**
     * 更新按钮状态
     *
     * @param buttonId 按钮ID
     * @param status   状态（0-禁用 1-启用）
     * @return 是否成功
     */
    boolean updateButtonStatus(Long buttonId, Integer status);

    /**
     * 检查按钮编码是否存在
     *
     * @param buttonCode 按钮编码
     * @param excludeId  排除的按钮ID（用于更新时排除自己）
     * @return 是否存在
     */
    boolean isButtonCodeExists(String buttonCode, Long excludeId);

    /**
     * 检查按钮名称是否存在
     *
     * @param buttonName 按钮名称
     * @param excludeId  排除的按钮ID（用于更新时排除自己）
     * @return 是否存在
     */
    boolean isButtonNameExists(String buttonName, Long excludeId);

    /**
     * 根据权限ID列表查询按钮列表
     *
     * @param permissionIds 权限ID列表
     * @param status        状态（0-禁用 1-启用）
     * @return 按钮列表
     */
    List<Button> getButtonsByPermissionIds(List<Long> permissionIds, Integer status);

    /**
     * 获取所有可用按钮
     *
     * @return 按钮列表
     */
    List<Button> getAllAvailableButtons();

    /**
     * 检查按钮是否被角色使用
     *
     * @param buttonId 按钮ID
     * @return 是否被使用
     */
    boolean isButtonUsedByRole(Long buttonId);

    /**
     * 根据用户权限获取按钮权限编码列表
     *
     * @param userId 用户ID
     * @return 按钮权限编码列表
     */
    List<String> getUserButtonCodes(Long userId);

    /**
     * 检查用户是否有指定按钮权限
     *
     * @param userId     用户ID
     * @param buttonCode 按钮编码
     * @return 是否有权限
     */
    boolean hasButtonPermission(Long userId, String buttonCode);

    /**
     * 批量创建按钮
     *
     * @param buttons 按钮列表
     * @return 是否成功
     */
    boolean createButtons(List<Button> buttons);

    /**
     * 根据权限ID删除所有按钮
     *
     * @param permissionId 权限ID
     * @return 是否成功
     */
    boolean deleteButtonsByPermissionId(Long permissionId);
}