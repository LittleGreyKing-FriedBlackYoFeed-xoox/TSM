package com.tsm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库连接测试类
 */
@Slf4j
@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        testDatabaseConnection();
    }

    /**
     * 测试数据库连接
     */
    public void testDatabaseConnection() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            // 获取数据库连接
            connection = dataSource.getConnection();
            log.info("数据库连接成功！");
            log.info("数据库URL: {}", connection.getMetaData().getURL());
            log.info("数据库用户名: {}", connection.getMetaData().getUserName());
            log.info("数据库产品名称: {}", connection.getMetaData().getDatabaseProductName());
            log.info("数据库版本: {}", connection.getMetaData().getDatabaseProductVersion());
            
            // 测试查询
            statement = connection.prepareStatement("SELECT 1 as test_result, NOW() as current_time");
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                int testResult = resultSet.getInt("test_result");
                String currentTime = resultSet.getString("current_time");
                log.info("测试查询成功 - 结果: {}, 当前时间: {}", testResult, currentTime);
            }
            
            // 测试数据库是否存在TSM数据库
            statement = connection.prepareStatement("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'TSM'");
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                log.info("TSM数据库存在");
            } else {
                log.warn("TSM数据库不存在，请先创建数据库");
            }
            
        } catch (SQLException e) {
            log.error("数据库连接测试失败: {}", e.getMessage(), e);
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                log.error("关闭数据库连接时发生错误: {}", e.getMessage());
            }
        }
    }
}