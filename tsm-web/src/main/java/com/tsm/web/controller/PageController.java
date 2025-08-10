package com.tsm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面控制器
 */
@Controller
public class PageController {

    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 主页面
     */
    @GetMapping("/main")
    public String main() {
        return "main";
    }

    /**
     * 用户管理页面
     */
    @GetMapping("/user")
    public String user() {
        return "user/user";
    }

    /**
     * 角色管理页面
     */
    @GetMapping("/role")
    public String role() {
        return "role/role";
    }

    /**
     * 权限管理页面
     */
    @GetMapping("/permission")
    public String permission() {
        return "permission/permission";
    }

    /**
     * 按钮管理页面
     */
    @GetMapping("/button")
    public String button() {
        return "button/button";
    }

    /**
     * 权限分配页面
     */
    @GetMapping("/permission-assign")
    public String permissionAssign() {
        return "permission-assign";
    }

    /**
     * 用户管理页面
     */
    @GetMapping("/user-management")
    public String userManagement() {
        return "user-management";
    }
}