package com.tsm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsm.common.core.exception.BusinessException;
import com.tsm.api.common.PageResult;
import com.tsm.common.core.result.ResultCode;
import com.tsm.system.entity.Role;
import com.tsm.system.mapper.RoleMapper;
import com.tsm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色服务实现类
 *
 * @author TSM
 * @since 2024-01-01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role getRoleByCode(String roleCode) {
        if (!StringUtils.hasText(roleCode)) {
            return null;
        }
        return roleMapper.selectByRoleCode(roleCode);
    }

    public IPage<Role> getRoleList(Page<Role> page, String roleName, String roleCode, Integer status) {
        return roleMapper.selectRolePage(page, roleName, roleCode, status);
    }

    @Override
    public PageResult<Role> getRolePage(long current, long size, String roleName, String roleCode, Integer status) {
        Page<Role> page = new Page<>(current, size);
        IPage<Role> result = roleMapper.selectRolePage(page, roleName, roleCode, status);
        return new PageResult<Role>(result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords());
    }

    @Override
    public Role getRoleWithPermissions(Long roleId) {
        if (roleId == null) {
            return null;
        }
        return roleMapper.selectRoleWithPermissions(roleId);
    }

    @Override
    public Role getRoleWithButtons(Long roleId) {
        if (roleId == null) {
            return null;
        }
        return roleMapper.selectRoleWithButtons(roleId);
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        return roleMapper.selectRolesByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(Role role) {
        // 参数校验
        if (role == null || !StringUtils.hasText(role.getRoleName()) || !StringUtils.hasText(role.getRoleCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色名称和角色编码不能为空");
        }

        // 检查角色编码是否已存在
        if (isRoleCodeExists(role.getRoleCode(), null)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "角色编码已存在");
        }

        // 检查角色名称是否已存在
        if (isRoleNameExists(role.getRoleName(), null)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "角色名称已存在");
        }

        // 设置默认值
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        if (role.getStatus() == null) {
            role.setStatus(1); // 默认启用
        }
        if (role.getDeleted() == null) {
            role.setDeleted(0); // 默认未删除
        }

        return this.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Role role) {
        // 参数校验
        if (role == null || role.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }

        // 检查角色是否存在
        Role existRole = this.getById(role.getId());
        if (existRole == null || existRole.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "角色不存在");
        }

        // 检查角色编码是否已存在（排除自己）
        if (StringUtils.hasText(role.getRoleCode()) && isRoleCodeExists(role.getRoleCode(), role.getId())) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "角色编码已存在");
        }

        // 检查角色名称是否已存在（排除自己）
        if (StringUtils.hasText(role.getRoleName()) && isRoleNameExists(role.getRoleName(), role.getId())) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "角色名称已存在");
        }

        // 设置更新时间
        role.setUpdateTime(LocalDateTime.now());

        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long roleId) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }

        // 检查角色是否存在
        Role role = this.getById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "角色不存在");
        }

        // 检查角色是否被用户使用
        if (isRoleInUse(roleId)) {
            throw new BusinessException(ResultCode.DATA_IN_USE.getCode(), "角色正在被用户使用，无法删除");
        }

        // 逻辑删除
        role.setDeleted(1);
        role.setUpdateTime(LocalDateTime.now());

        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchRoles(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID列表不能为空");
        }

        // 检查是否有角色被用户使用
        for (Long roleId : roleIds) {
            if (isRoleInUse(roleId)) {
                throw new BusinessException(ResultCode.DATA_IN_USE.getCode(), "存在角色正在被用户使用，无法删除");
            }
        }

        return roleMapper.deleteBatchByIds(roleIds) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleStatus(Long roleId, Integer status) {
        if (roleId == null || status == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID和状态不能为空");
        }

        // 检查角色是否存在
        Role role = this.getById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "角色不存在");
        }

        return roleMapper.updateStatus(roleId, status) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean assignPermissions(Long roleId, List<Long> permissionIds) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }

        // 检查角色是否存在
        Role role = this.getById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "角色不存在");
        }

        // 先删除原有权限关联
        roleMapper.deleteRolePermissions(roleId);

        // 如果有新的权限，则插入
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleMapper.insertRolePermissions(roleId, permissionIds);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRolePermissions(Long roleId, List<Long> permissionIds) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }

        try {
            // 先删除原有权限关联
            roleMapper.deleteRolePermissions(roleId);

            // 如果有新的权限，则插入
            if (permissionIds != null && !permissionIds.isEmpty()) {
                roleMapper.insertRolePermissions(roleId, permissionIds);
            }

            return true;
        } catch (Exception e) {
            throw new BusinessException(ResultCode.ERROR.getCode(), "分配角色权限失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoleButtons(Long roleId, List<Long> buttonIds) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }

        // 检查角色是否存在
        Role role = this.getById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "角色不存在");
        }

        // 先删除原有按钮权限关联
        roleMapper.deleteRoleButtons(roleId);

        // 如果有新的按钮权限，则插入
        if (buttonIds != null && !buttonIds.isEmpty()) {
            roleMapper.insertRoleButtons(roleId, buttonIds);
        }

        return true;
    }

    public boolean isRoleCodeExists(String roleCode, Long excludeId) {
        if (!StringUtils.hasText(roleCode)) {
            return false;
        }
        return roleMapper.checkRoleCodeExists(roleCode, excludeId) > 0;
    }

    @Override
    public boolean isRoleNameExists(String roleName, Long excludeId) {
        if (!StringUtils.hasText(roleName)) {
            return false;
        }
        return roleMapper.checkRoleNameExists(roleName, excludeId) > 0;
    }

    public List<Role> getAllAvailableRoles() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, 1)
                .eq(Role::getDeleted, 0)
                .orderByAsc(Role::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public boolean isRoleInUse(Long roleId) {
        if (roleId == null) {
            return false;
        }
        // TODO: 这里需要查询用户角色关联表，暂时返回false
        // 实际实现需要查询 sys_user_role 表
        return false;
    }
}