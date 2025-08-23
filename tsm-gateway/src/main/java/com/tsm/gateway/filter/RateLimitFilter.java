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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流过滤器
 * 
 * @author TSM Team
 * @version 1.0.0
 */
@Slf4j
@Component
public class RateLimitFilter implements GlobalFilter, Ordered {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 存储每个IP的请求计数
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    
    // 存储每个IP的窗口开始时间
    private final Map<String, Long> windowStartTimes = new ConcurrentHashMap<>();
    
    // 限流配置：每分钟最多100个请求
    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private static final long WINDOW_SIZE_IN_MILLIS = 60 * 1000; // 1分钟
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String clientIp = getClientIp(request);
        
        // 检查是否超过限流
        if (isRateLimited(clientIp)) {
            return rateLimitResponse(exchange);
        }
        
        return chain.filter(exchange);
    }
    
    @Override
    public int getOrder() {
        return -50;
    }
    
    /**
     * 检查是否超过限流
     */
    private boolean isRateLimited(String clientIp) {
        long currentTime = System.currentTimeMillis();
        
        // 获取或创建请求计数
        AtomicInteger count = requestCounts.computeIfAbsent(clientIp, k -> new AtomicInteger(0));
        
        // 获取或创建窗口开始时间
        Long windowStart = windowStartTimes.computeIfAbsent(clientIp, k -> currentTime);
        
        // 检查是否需要重置窗口
        if (currentTime - windowStart >= WINDOW_SIZE_IN_MILLIS) {
            // 重置窗口
            windowStartTimes.put(clientIp, currentTime);
            count.set(1);
            return false;
        }
        
        // 增加请求计数
        int currentCount = count.incrementAndGet();
        
        if (currentCount > MAX_REQUESTS_PER_MINUTE) {
            log.warn("Rate limit exceeded for IP: {}, count: {}", clientIp, currentCount);
            return true;
        }
        
        return false;
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddress() != null ? 
            request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
    }
    
    /**
     * 返回限流响应
     */
    private Mono<Void> rateLimitResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 429);
        result.put("message", "请求过于频繁，请稍后再试");
        result.put("data", null);
        result.put("timestamp", System.currentTimeMillis());
        
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(result);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error writing rate limit response", e);
            return response.setComplete();
        }
    }
}