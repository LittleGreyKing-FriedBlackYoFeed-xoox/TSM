package com.tsm.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关控制器
 * 
 * @author TSM Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/gateway")
public class GatewayController {
    
    @Autowired
    private RouteLocator routeLocator;
    
    /**
     * 网关健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "tsm-gateway");
        result.put("timestamp", System.currentTimeMillis());
        result.put("version", "1.0.0");
        return result;
    }
    
    /**
     * 获取路由信息
     */
    @GetMapping("/routes")
    public Flux<Map<String, Object>> routes() {
        return routeLocator.getRoutes()
            .map(route -> {
                Map<String, Object> routeInfo = new HashMap<>();
                routeInfo.put("id", route.getId());
                routeInfo.put("uri", route.getUri().toString());
                routeInfo.put("predicates", route.getPredicate().toString());
                routeInfo.put("filters", route.getFilters().toString());
                return routeInfo;
            });
    }
    
    /**
     * 网关信息
     */
    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", "TSM API Gateway");
        result.put("description", "TSM系统API网关服务，负责路由转发和负载均衡");
        result.put("version", "1.0.0");
        result.put("author", "TSM Team");
        result.put("startTime", System.currentTimeMillis());
        return result;
    }
}