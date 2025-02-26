@echo off
echo 正在启动点餐系统服务...

:: 启动后端服务
start cmd /k "cd /d %~dp0 && echo 正在启动后端服务... && mvn spring-boot:run"

:: 等待后端服务启动
echo 等待后端服务启动...
timeout /t 10 /nobreak > nul

:: 启动前端服务
start cmd /k "cd /d %~dp0frontend && echo 正在启动前端服务... && npm run dev"

echo 服务启动中，请稍候...
echo 后端服务默认端口: 8082
echo 前端服务默认端口: 5173
echo.
echo 管理员账户:
echo 用户名: admin
echo 密码: admin123
echo.
echo 按任意键退出此窗口...
pause > nul 