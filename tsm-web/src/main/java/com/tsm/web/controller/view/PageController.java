package com.tsm.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面控制器
 * 负责主要页面的路由和导航
 */
@Controller
public class PageController {

    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    /**
     * 主页面
     */
    @GetMapping("/index")
    public String home() {
        return "index";
    }

    /**
     * 仪表板
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "views/dashboard/dashboard";
    }

    /**
     * 系统设置
     */
    @GetMapping("/settings")
    public String settings() {
        return "views/system/settings";
    }

    /**
     * 个人资料
     */
    @GetMapping("/profile")
    public String profile() {
        return "views/user/profile";
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
    @GetMapping("/user-management")
    public String userManagement() {
        return "user-management";
    }

    /**
     * 添加用户页面
     */
    @GetMapping("/add-user")
    public String addUser() {
        return "add-user";
    }

    /**
     * 批量导入页面
     */
    @GetMapping("/batch-import")
    public String batchImport() {
        return "batch-import";
    }

    /**
     * 测试页面
     */
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}