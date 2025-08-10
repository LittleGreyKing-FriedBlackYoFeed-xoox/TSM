@echo off
echo ========================================
echo TSM分布式后台管理系统启动脚本
echo ========================================

echo.
echo 正在编译项目...
call mvn clean compile -DskipTests

if %errorlevel% neq 0 (
    echo 编译失败，请检查错误信息
    pause
    exit /b 1
)

echo.
echo 编译成功！
echo.
echo 请选择启动模式：
echo 1. 启动Web服务 (端口: 8080)
echo 2. 启动网关服务 (端口: 9000)
echo 3. 同时启动Web和网关服务
echo 4. 运行测试
echo 5. 退出

set /p choice=请输入选择 (1-5): 

if "%choice%"=="1" (
    echo.
    echo 正在启动Web服务...
    cd tsm-web
    call mvn spring-boot:run
) else if "%choice%"=="2" (
    echo.
    echo 正在启动网关服务...
    cd tsm-gateway
    call mvn spring-boot:run
) else if "%choice%"=="3" (
    echo.
    echo 正在同时启动Web和网关服务...
    start "TSM-Web" cmd /c "cd tsm-web && mvn spring-boot:run"
    timeout /t 3 /nobreak > nul
    start "TSM-Gateway" cmd /c "cd tsm-gateway && mvn spring-boot:run"
    echo.
    echo 服务启动中，请稍候...
    echo Web服务: http://localhost:8080/tsm
    echo 网关服务: http://localhost:9000/tsm
) else if "%choice%"=="4" (
    echo.
    echo 正在运行测试...
    cd tsm-test
    call mvn test
) else if "%choice%"=="5" (
    echo 退出脚本
    exit /b 0
) else (
    echo 无效选择，请重新运行脚本
)

echo.
echo 按任意键退出...
pause > nul