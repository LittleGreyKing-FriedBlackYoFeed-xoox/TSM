package com.tsm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * TSM系统启动类
 * 
 * @author TSM Team
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.tsm.system.mapper")
@EnableTransactionManagement
public class TsmSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsmSystemApplication.class, args);
        System.out.println("\n" +
                "  ████████╗███████╗███╗   ███╗\n" +
                "  ╚══██╔══╝██╔════╝████╗ ████║\n" +
                "     ██║   ███████╗██╔████╔██║\n" +
                "     ██║   ╚════██║██║╚██╔╝██║\n" +
                "     ██║   ███████║██║ ╚═╝ ██║\n" +
                "     ╚═╝   ╚══════╝╚═╝     ╚═╝\n" +
                "\n" +
                "  TSM后台管理系统启动成功！\n" +
                "  访问地址: http://localhost:8080/tsm\n" +
                "  Druid监控: http://localhost:8080/tsm/druid\n");
    }
}