package com.tsm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * TSM前端Web服务启动类
 * @author TSM
 */
@SpringBootApplication
// @EnableDiscoveryClient  // 暂时禁用服务发现
// @EnableFeignClients     // 暂时禁用Feign客户端
@ComponentScan(basePackages = {"com.tsm.web", "com.tsm.common"})
public class WebApplication {
    
    /**
     * RestTemplate Bean配置
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
        System.out.println("\n" +
                "████████╗███████╗███╗   ███╗    ██╗    ██╗███████╗██████╗ \n" +
                "╚══██╔══╝██╔════╝████╗ ████║    ██║    ██║██╔════╝██╔══██╗\n" +
                "   ██║   ███████╗██╔████╔██║    ██║ █╗ ██║█████╗  ██████╔╝\n" +
                "   ██║   ╚════██║██║╚██╔╝██║    ██║███╗██║██╔══╝  ██╔══██╗\n" +
                "   ██║   ███████║██║ ╚═╝ ██║    ╚███╔███╔╝███████╗██████╔╝\n" +
                "   ╚═╝   ╚══════╝╚═╝     ╚═╝     ╚══╝╚══╝ ╚══════╝╚═════╝ \n" +
                "\n" +
                "TSM前端Web服务启动成功！访问地址：http://localhost:8084\n");
    }
}