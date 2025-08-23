package com.tsm.permission;

// 暂时禁用MyBatis
// import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * TSM权限管理服务启动类
 * @author TSM
 */
@SpringBootApplication
// @EnableDiscoveryClient  // 暂时禁用服务发现
// @EnableFeignClients     // 暂时禁用Feign客户端
// @MapperScan("com.tsm.permission.mapper")
@ComponentScan(basePackages = {"com.tsm.permission", "com.tsm.common"})
public class PermissionApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
        System.out.println("\n" +
                "████████╗███████╗███╗   ███╗    ██████╗ ███████╗██████╗ ███╗   ███╗\n" +
                "╚══██╔══╝██╔════╝████╗ ████║    ██╔══██╗██╔════╝██╔══██╗████╗ ████║\n" +
                "   ██║   ███████╗██╔████╔██║    ██████╔╝█████╗  ██████╔╝██╔████╔██║\n" +
                "   ██║   ╚════██║██║╚██╔╝██║    ██╔═══╝ ██╔══╝  ██╔══██╗██║╚██╔╝██║\n" +
                "   ██║   ███████║██║ ╚═╝ ██║    ██║     ███████╗██║  ██║██║ ╚═╝ ██║\n" +
                "   ╚═╝   ╚══════╝╚═╝     ╚═╝    ╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝\n" +
                "\n" +
                "TSM权限管理服务启动成功！\n");
    }
}