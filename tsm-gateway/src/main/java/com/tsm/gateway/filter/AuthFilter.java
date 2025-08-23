package com.tsm.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证过滤器
 * 
 * @author TSM Team
 * @version 1.0.0
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 白名单路径，不需要认证
    private static final List<String> WHITE_LIST = Arrays.asList(
        "/api/auth/login",
        "/api/auth/captcha",
        "/api/auth/logout",
        "/static",
        "/favicon.ico",
        "/actuator"
    );
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        
        // 检查是否在白名单中
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }
        
        // 获取token
        String token = getToken(request);
        if (!StringUtils.hasText(token)) {
            return unauthorizedResponse(exchange, "未提供认证令牌");
        }
        
        // 验证token（这里简化处理，实际应该调用认证服务验证）
        if (!isValidToken(token)) {
            return unauthorizedResponse(exchange, "认证令牌无效或已过期");
        }
        
        // 在请求头中添加用户信息（从token中解析）
        ServerHttpRequest mutatedRequest = request.mutate()
            .header("X-User-Id", getUserIdFromToken(token))
            .header("X-User-Name", getUserNameFromToken(token))
            .build();
        
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }
    
    @Override
    public int getOrder() {
        return -100;
    }
    
    /**
     * 检查路径是否在白名单中
     */
    private boolean isWhiteList(String path) {
        return WHITE_LIST.stream().anyMatch(path::startsWith);
    }
    
    /**
     * 获取token
     */
    private String getToken(ServerHttpRequest request) {
        String authorization = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }
    
    /**
     * 验证token有效性（简化实现）
     */
    private boolean isValidToken(String token) {
        // 这里应该调用认证服务验证token
        // 简化处理：只要不是空就认为有效
        return StringUtils.hasText(token) && token.length() > 10;
    }
    
    /**
     * 从token中获取用户ID（简化实现）
     */
    private String getUserIdFromToken(String token) {
        // 这里应该解析JWT token获取用户信息
        // 简化处理：返回固定值
        return "1";
    }
    
    /**
     * 从token中获取用户名（简化实现）
     */
    private String getUserNameFromToken(String token) {
        // 这里应该解析JWT token获取用户信息
        // 简化处理：返回固定值
        return "admin";
    }
    
    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", message);
        result.put("data", null);
        result.put("timestamp", System.currentTimeMillis());
        
        try {
            String json = objectMapper.writeValueAsString(result);
            DataBuffer buffer = response.bufferFactory().wrap(json.getBytes());
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("序列化响应数据失败", e);
            return response.setComplete();
        }
    }
}