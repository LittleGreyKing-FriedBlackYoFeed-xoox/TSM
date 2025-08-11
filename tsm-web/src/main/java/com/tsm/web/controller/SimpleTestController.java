package com.tsm.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单测试控制器
 */
@RestController
public class SimpleTestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World! TSM Application is working!";
    }

    @GetMapping("/health")
    public String health() {
        return "Application is healthy!";
    }
}