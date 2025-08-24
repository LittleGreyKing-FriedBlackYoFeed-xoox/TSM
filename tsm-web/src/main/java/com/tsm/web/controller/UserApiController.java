package com.tsm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Enumeration;

/**
 * 用户API代理控制器
 * 将前端的API请求转发到用户服务
 * @author TSM
 */
@RestController
@RequestMapping("/api/user")
public class UserApiController {
    
    @Value("${tsm.services.user:http://localhost:8082}")
    private String userServiceUrl;
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 代理所有用户相关的GET请求
     */
    @GetMapping("/**")
    public ResponseEntity<String> proxyGet(HttpServletRequest request) {
        return proxyRequest(request, HttpMethod.GET, null);
    }
    
    /**
     * 代理所有用户相关的POST请求
     */
    @PostMapping("/**")
    public ResponseEntity<String> proxyPost(HttpServletRequest request, @RequestBody(required = false) String body) {
        return proxyRequest(request, HttpMethod.POST, body);
    }
    
    /**
     * 代理所有用户相关的PUT请求
     */
    @PutMapping("/**")
    public ResponseEntity<String> proxyPut(HttpServletRequest request, @RequestBody(required = false) String body) {
        return proxyRequest(request, HttpMethod.PUT, body);
    }
    
    /**
     * 代理所有用户相关的DELETE请求
     */
    @DeleteMapping("/**")
    public ResponseEntity<String> proxyDelete(HttpServletRequest request) {
        return proxyRequest(request, HttpMethod.DELETE, null);
    }
    
    /**
     * 执行代理请求
     */
    private ResponseEntity<String> proxyRequest(HttpServletRequest request, HttpMethod method, String body) {
        try {
            // 构建目标URL
            String path = request.getRequestURI().substring("/api/user".length());
            String queryString = request.getQueryString();
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(userServiceUrl + "/api/user" + path);
            if (queryString != null) {
                builder.query(queryString);
            }
            URI targetUri = builder.build().toUri();
            
            // 复制请求头
            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (!"host".equalsIgnoreCase(headerName) && !"content-length".equalsIgnoreCase(headerName)) {
                    headers.add(headerName, request.getHeader(headerName));
                }
            }
            
            // 设置Content-Type
            if (body != null && !headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
                headers.setContentType(MediaType.APPLICATION_JSON);
            }
            
            // 创建请求实体
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(targetUri, method, entity, String.class);
            
            // 返回响应
            return ResponseEntity.status(response.getStatusCode())
                    .headers(response.getHeaders())
                    .body(response.getBody());
                    
        } catch (Exception e) {
            // 返回错误响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"success\":false,\"message\":\"代理请求失败: " + e.getMessage() + "\"}");
        }
    }
}