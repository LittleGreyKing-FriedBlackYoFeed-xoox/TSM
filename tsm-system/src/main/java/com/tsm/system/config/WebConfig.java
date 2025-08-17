package com.tsm.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 
 * @author TSM Team
 * @since 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/", "file:D:/Trae_workspace_EN/TSM/static/");
        
        // 配置根目录下的静态文件
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "file:D:/Trae_workspace_EN/TSM/static/");
    }
}