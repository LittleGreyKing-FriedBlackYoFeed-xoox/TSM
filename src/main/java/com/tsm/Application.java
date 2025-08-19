package com.tsm;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * TSM系统启动类
 */
@Slf4j
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@MapperScan("com.tsm.dao")
public class Application {
    
    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
            log.info("TSM系统启动成功！");
            log.info("访问地址: http://localhost:8080/api");
            log.info("Druid监控: http://localhost:8080/api/druid");
            log.info("健康检查: http://localhost:8080/api/actuator/health");
        } catch (Exception e) {
            log.error("TSM系统启动失败: {}", e.getMessage(), e);
        }
    }
}