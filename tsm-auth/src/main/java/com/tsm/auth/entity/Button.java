package com.tsm.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tsm.common.entity.BaseEntity;

/**
 * 按钮实体类
 * 用于按钮级权限管理
 */
@TableName("sys_button")
public class Button extends BaseEntity {

    /**
     * 按钮名称
     */
    @TableField("button_name")
    private String buttonName;

    /**
     * 按钮编码
     */
    @TableField("button_code")
    private String buttonCode;

    /**
     * 按钮描述
     */
    @TableField("description")
    private String description;

    /**
     * 所属页面/模块
     */
    @TableField("page_code")
    private String pageCode;

    /**
     * 按钮类型（1新增 2编辑 3删除 4查看 5导出 6导入 7其他）
     */
    @TableField("button_type")
    private Integer buttonType;

    /**
     * 按钮样式
     */
    @TableField("button_style")
    private String buttonStyle;

    /**
     * 按钮图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 状态（0正常 1停用）
     */
    @TableField("status")
    private Integer status;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public Integer getButtonType() {
        return buttonType;
    }

    public void setButtonType(Integer buttonType) {
        this.buttonType = buttonType;
    }

    public String getButtonStyle() {
        return buttonStyle;
    }

    public void setButtonStyle(String buttonStyle) {
        this.buttonStyle = buttonStyle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}