package com.tsm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * TSM网关启动类
 */
@SpringBootApplication
public class TsmGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsmGatewayApplication.class, args);
    }

    /**
     * 配置路由规则
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // TSM Web服务路由
                .route("tsm-web", r -> r.path("/tsm/**")
                        .uri("http://localhost:8080"))
                // API路由
                .route("tsm-api", r -> r.path("/api/**")
                        .uri("http://localhost:8080"))
                .build();
    }
}