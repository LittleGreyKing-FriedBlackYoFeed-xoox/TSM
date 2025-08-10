package com.tsm.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * TSM Web应用启动类
 */
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@ComponentScan(basePackages = {"com.tsm.web", "com.tsm.common", "com.tsm.auth"})
// @MapperScan({"com.tsm.auth.mapper"})
public class TsmWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsmWebApplication.class, args);
    }
}