@echo off
setlocal enabledelayedexpansion

set "MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
set "SQL_FILE=sql\database_product.sql"
set "DB_NAME=ry-vue"
set "DB_USER=root"

echo ==== 开始执行SQL脚本 ====

echo 检查MySQL可执行文件路径...
if not exist "%MYSQL_PATH%" (
    echo 错误：MySQL可执行文件未找到 at %MYSQL_PATH%
    echo 请验证MySQL安装路径是否正确
    exit /b 1
)

echo 检查SQL文件是否存在...
if not exist "%SQL_FILE%" (
    echo 错误：SQL文件未找到 at %SQL_FILE%
    exit /b 1
)

echo 正在执行SQL命令：
"%MYSQL_PATH%" --version
if %errorlevel% neq 0 (
    echo 错误：无法执行MySQL命令
    exit /b 1
)

echo 正在导入SQL文件到数据库 %DB_NAME%...
echo 请在提示时输入数据库密码
"%MYSQL_PATH%" -u %DB_USER% -p %DB_NAME% < "%SQL_FILE%"

if %errorlevel% equ 0 (
    echo ==== SQL脚本执行成功 ====
) else (
    echo ==== SQL脚本执行失败，错误代码：%errorlevel% ====
    exit /b %errorlevel%
)