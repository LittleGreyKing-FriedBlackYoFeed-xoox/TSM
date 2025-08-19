-- 测试数据库连接
SELECT 1 as connection_test;

-- 查看当前数据库
SELECT DATABASE() as current_database;

-- 查看当前时间
SELECT NOW() as current_time;

-- 查看数据库版本
SELECT VERSION() as mysql_version;

-- 查看所有数据库
SHOW DATABASES;

-- 查看TSM数据库中的表
USE TSM;
SHOW TABLES;