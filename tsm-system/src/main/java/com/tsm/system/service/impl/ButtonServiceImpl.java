package com.tsm.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tsm.common.core.exception.BusinessException;
import com.tsm.api.common.PageResult;
import com.tsm.common.core.result.ResultCode;
import com.tsm.system.entity.Button;
import com.tsm.system.entity.Permission;
import com.tsm.system.mapper.ButtonMapper;
import com.tsm.system.service.ButtonService;
import com.tsm.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 按钮权限服务实现类
 *
 * @author TSM
 * @since 2024-01-01
 */
@Service
public class ButtonServiceImpl extends ServiceImpl<ButtonMapper, Button> implements ButtonService {

    @Autowired
    private ButtonMapper buttonMapper;

    @Autowired
    private PermissionService permissionService;

    @Override
    public Button getButtonByCode(String buttonCode) {
        if (!StringUtils.hasText(buttonCode)) {
            return null;
        }
        return buttonMapper.selectButtonByCode(buttonCode);
    }

    @Override
    public IPage<Button> getButtonList(Page<Button> page, String buttonName, String buttonCode, Long permissionId, Integer status) {
        return buttonMapper.selectButtonList(page, buttonName, buttonCode, permissionId, status);
    }

    @Override
    public Button getButtonWithPermission(Long buttonId) {
        if (buttonId == null) {
            return null;
        }
        return buttonMapper.selectButtonWithPermission(buttonId);
    }

    @Override
    public List<Button> getButtonsByPermissionId(Long permissionId, Integer status) {
        if (permissionId == null) {
            return new ArrayList<>();
        }
        return buttonMapper.selectButtonsByPermissionId(permissionId, status);
    }

    @Override
    public List<Button> getButtonsByRoleId(Long roleId, Integer status) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        return buttonMapper.selectButtonsByRoleId(roleId, status);
    }

    @Override
    public List<Button> getButtonsByUserId(Long userId, Integer status) {
        if (userId == null) {
            return new ArrayList<>();
        }
        return buttonMapper.selectButtonsByUserId(userId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createButton(Button button) {
        // 参数校验
        if (button == null || !StringUtils.hasText(button.getButtonName()) || !StringUtils.hasText(button.getButtonCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "按钮名称和按钮编码不能为空");
        }

        if (button.getPermissionId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "所属权限不能为空");
        }

        // 检查所属权限是否存在
        Permission permission = permissionService.getById(button.getPermissionId());
        if (permission == null || permission.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "所属权限不存在");
        }

        // 检查按钮编码是否已存在
        if (isButtonCodeExists(button.getButtonCode(), null)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "按钮编码已存在");
        }

        // 检查按钮名称是否已存在
        if (isButtonNameExists(button.getButtonName(), null)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "按钮名称已存在");
        }

        // 设置默认值
        button.setCreateTime(LocalDateTime.now());
        button.setUpdateTime(LocalDateTime.now());
        if (button.getStatus() == null) {
            button.setStatus(1); // 默认启用
        }
        if (button.getDeleted() == null) {
            button.setDeleted(0); // 默认未删除
        }
        if (button.getSortOrder() == null) {
            button.setSortOrder(0);
        }

        return this.save(button);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateButton(Button button) {
        // 参数校验
        if (button == null || button.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "按钮ID不能为空");
        }

        // 检查按钮是否存在
        Button existButton = this.getById(button.getId());
        if (existButton == null || existButton.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "按钮不存在");
        }

        // 检查所属权限是否存在
        if (button.getPermissionId() != null) {
            Permission permission = permissionService.getById(button.getPermissionId());
            if (permission == null || permission.getDeleted() == 1) {
                throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "所属权限不存在");
            }
        }

        // 检查按钮编码是否已存在（排除自己）
        if (StringUtils.hasText(button.getButtonCode()) && isButtonCodeExists(button.getButtonCode(), button.getId())) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "按钮编码已存在");
        }

        // 检查按钮名称是否已存在（排除自己）
        if (StringUtils.hasText(button.getButtonName()) && isButtonNameExists(button.getButtonName(), button.getId())) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "按钮名称已存在");
        }

        // 设置更新时间
        button.setUpdateTime(LocalDateTime.now());

        return this.updateById(button);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteButton(Long buttonId) {
        if (buttonId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "按钮ID不能为空");
        }

        // 检查按钮是否存在
        Button button = this.getById(buttonId);
        if (button == null || button.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "按钮不存在");
        }

        // 检查按钮是否被角色使用
        if (isButtonUsedByRole(buttonId)) {
            throw new BusinessException(ResultCode.DATA_IN_USE.getCode(), "按钮正在被角色使用，无法删除");
        }

        // 逻辑删除
        button.setDeleted(1);
        button.setUpdateTime(LocalDateTime.now());

        return this.updateById(button);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteButtons(List<Long> buttonIds) {
        if (buttonIds == null || buttonIds.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "按钮ID列表不能为空");
        }

        // 检查是否有按钮被角色使用
        for (Long buttonId : buttonIds) {
            if (isButtonUsedByRole(buttonId)) {
                throw new BusinessException(ResultCode.DATA_IN_USE.getCode(), "存在按钮正在被角色使用，无法删除");
            }
        }

        return buttonMapper.batchDeleteButtons(buttonIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateButtonStatus(Long buttonId, Integer status) {
        if (buttonId == null || status == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "按钮ID和状态不能为空");
        }

        // 检查按钮是否存在
        Button button = this.getById(buttonId);
        if (button == null || button.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND.getCode(), "按钮不存在");
        }

        return buttonMapper.updateButtonStatus(buttonId, status) > 0;
    }

    @Override
    public boolean isButtonCodeExists(String buttonCode, Long excludeId) {
        if (!StringUtils.hasText(buttonCode)) {
            return false;
        }
        return buttonMapper.checkButtonCodeExists(buttonCode, excludeId) > 0;
    }

    @Override
    public boolean isButtonNameExists(String buttonName, Long excludeId) {
        if (!StringUtils.hasText(buttonName)) {
            return false;
        }
        return buttonMapper.checkButtonNameExists(buttonName, excludeId) > 0;
    }

    @Override
    public List<Button> getButtonsByPermissionIds(List<Long> permissionIds, Integer status) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new ArrayList<>();
        }
        return buttonMapper.selectButtonsByPermissionIds(permissionIds, status);
    }

    @Override
    public List<Button> getAllAvailableButtons() {
        return buttonMapper.selectAllAvailableButtons();
    }

    @Override
    public boolean isButtonUsedByRole(Long buttonId) {
        if (buttonId == null) {
            return false;
        }
        return buttonMapper.checkButtonUsedByRole(buttonId) > 0;
    }

    @Override
    public List<String> getUserButtonCodes(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        List<Button> buttons = getButtonsByUserId(userId, 1);
        return buttons.stream()
                .map(Button::getButtonCode)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasButtonPermission(Long userId, String buttonCode) {
        if (userId == null || !StringUtils.hasText(buttonCode)) {
            return false;
        }
        List<String> userButtonCodes = getUserButtonCodes(userId);
        return userButtonCodes.contains(buttonCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createButtons(List<Button> buttons) {
        if (buttons == null || buttons.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "按钮列表不能为空");
        }

        // 批量校验和设置默认值
        for (Button button : buttons) {
            if (!StringUtils.hasText(button.getButtonName()) || !StringUtils.hasText(button.getButtonCode())) {
                throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "按钮名称和按钮编码不能为空");
            }

            if (button.getPermissionId() == null) {
                throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "所属权限不能为空");
            }

            // 检查按钮编码是否已存在
            if (isButtonCodeExists(button.getButtonCode(), null)) {
                throw new BusinessException(ResultCode.DATA_ALREADY_EXISTS.getCode(), "按钮编码 " + button.getButtonCode() + " 已存在");
            }

            // 设置默认值
            button.setCreateTime(LocalDateTime.now());
            button.setUpdateTime(LocalDateTime.now());
            if (button.getStatus() == null) {
                button.setStatus(1);
            }
            if (button.getDeleted() == null) {
            button.setDeleted(0);
            }
            if (button.getSortOrder() == null) {
            button.setSortOrder(0);
            }
        }

        return this.saveBatch(buttons);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteButtonsByPermissionId(Long permissionId) {
        if (permissionId == null) {
            return false;
        }

        LambdaQueryWrapper<Button> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Button::getPermissionId, permissionId)
                .eq(Button::getDeleted, 0);

        List<Button> buttons = this.list(wrapper);
        if (buttons.isEmpty()) {
            return true;
        }

        // 检查是否有按钮被角色使用
        for (Button button : buttons) {
            if (isButtonUsedByRole(button.getId())) {
                throw new BusinessException(ResultCode.DATA_IN_USE.getCode(), "存在按钮正在被角色使用，无法删除");
            }
        }

        // 批量逻辑删除
        for (Button button : buttons) {
            button.setDeleted(1);
            button.setUpdateTime(LocalDateTime.now());
        }

        return this.updateBatchById(buttons);
    }
}