package com.tsm.auth;

// 暂时禁用MyBatis
// import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * TSM认证服务启动类
 * @author TSM
 */
@SpringBootApplication
// @EnableDiscoveryClient  // 暂时禁用服务发现
// @MapperScan("com.tsm.auth.mapper")
@ComponentScan(basePackages = {"com.tsm.auth", "com.tsm.common"})
public class AuthApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        System.out.println("\n" +
                "████████╗███████╗███╗   ███╗     █████╗ ██╗   ██╗████████╗██╗  ██╗\n" +
                "╚══██╔══╝██╔════╝████╗ ████║    ██╔══██╗██║   ██║╚══██╔══╝██║  ██║\n" +
                "   ██║   ███████╗██╔████╔██║    ███████║██║   ██║   ██║   ███████║\n" +
                "   ██║   ╚════██║██║╚██╔╝██║    ██╔══██║██║   ██║   ██║   ██╔══██║\n" +
                "   ██║   ███████║██║ ╚═╝ ██║    ██║  ██║╚██████╔╝   ██║   ██║  ██║\n" +
                "   ╚═╝   ╚══════╝╚═╝     ╚═╝    ╚═╝  ╚═╝ ╚═════╝    ╚═╝   ╚═╝  ╚═╝\n" +
                "\n" +
                "TSM认证服务启动成功！\n");
    }
}