@echo off
echo Starting TSM Application...

echo Building project...
call mvn clean compile -DskipTests

echo Starting TSM Web Application...
cd tsm-web
start "TSM-Web" mvn spring-boot:run -Dspring-boot.run.profiles=dev

echo TSM Application is starting...
echo Please wait for the application to fully start before accessing it.
echo Access URL: http://localhost:8080/tsm
pause