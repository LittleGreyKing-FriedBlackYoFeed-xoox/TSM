package com.tsm.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * TSM用户管理服务启动类
 * @author TSM
 */
@SpringBootApplication
// @EnableDiscoveryClient  // 暂时禁用服务发现
// @EnableFeignClients     // 暂时禁用Feign客户端
@MapperScan("com.tsm.user.mapper")
@ComponentScan(basePackages = {"com.tsm.user", "com.tsm.common"})
public class UserApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        System.out.println("\n" +
                "████████╗███████╗███╗   ███╗    ██╗   ██╗███████╗███████╗██████╗ \n" +
                "╚══██╔══╝██╔════╝████╗ ████║    ██║   ██║██╔════╝██╔════╝██╔══██╗\n" +
                "   ██║   ███████╗██╔████╔██║    ██║   ██║███████╗█████╗  ██████╔╝\n" +
                "   ██║   ╚════██║██║╚██╔╝██║    ██║   ██║╚════██║██╔══╝  ██╔══██╗\n" +
                "   ██║   ███████║██║ ╚═╝ ██║    ╚██████╔╝███████║███████╗██║  ██║\n" +
                "   ╚═╝   ╚══════╝╚═╝     ╚═╝     ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝\n" +
                "\n" +
                "TSM用户管理服务启动成功！\n");
    }
}