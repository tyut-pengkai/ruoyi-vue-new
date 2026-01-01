@echo off
REM 简化版SQL执行脚本，使用绝对路径和基本错误处理
set "MYSQL_EXE=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
set "SQL_FILE=C:\Users\lenovo\RuoYi-Vue\sql\database_product.sql"

REM 检查MySQL可执行文件
if not exist "%MYSQL_EXE%" (
    echo 错误：未找到MySQL可执行文件
    echo 请确认路径是否正确：%MYSQL_EXE%
    exit /b 1
)

REM 检查SQL文件
if not exist "%SQL_FILE%" (
    echo 错误：未找到SQL文件
    echo 请确认路径是否正确：%SQL_FILE%
    exit /b 1
)

REM 执行SQL脚本（请在提示时输入数据库密码）
echo 正在执行SQL脚本...
"%MYSQL_EXE%" -u root -p ry-vue < "%SQL_FILE%"

REM 检查执行结果
if %errorlevel% equ 0 (
    echo SQL脚本执行成功！
) else (
    echo SQL脚本执行失败，错误代码：%errorlevel%
    exit /b %errorlevel%
)