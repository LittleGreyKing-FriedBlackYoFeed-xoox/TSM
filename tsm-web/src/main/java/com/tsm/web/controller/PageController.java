package com.tsm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面控制器
 * @author TSM
 */
@Controller
public class PageController {
    
    /**
     * 登录页面
     */
    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }
    
    /**
     * 主控制台页面
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }
    
    /**
     * 用户列表页面
     */
    @GetMapping("/user/list")
    public String userList() {
        return "user/list";
    }
    
    /**
     * 新增用户页面
     */
    @GetMapping("/user/add")
    public String userAdd() {
        return "user/add";
    }
    
    /**
     * 编辑用户页面
     */
    @GetMapping("/user/edit")
    public String userEdit() {
        return "user/edit";
    }
    
    /**
     * 角色管理页面
     */
    @GetMapping("/permission/role")
    public String roleManage() {
        return "permission/role";
    }
    
    /**
     * 权限分配页面
     */
    @GetMapping("/permission/assign")
    public String permissionAssign() {
        return "permission/assign";
    }
    
    /**
     * 个人中心页面
     */
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
    
    /**
     * 用户测试页面
     */
    @GetMapping("/user/test")
    public String userTest() {
        return "user/test";
    }
    
    /**
     * 用户简单测试页面
     */
    @GetMapping("/user/simple-test")
    public String userSimpleTest() {
        return "user/simple-test";
    }
}