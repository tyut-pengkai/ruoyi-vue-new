@echo off
REM 数据库导入脚本
REM 请确保MySQL已安装并配置正确

set MYSQL_PORT=3307
set DB_NAME=ruoyi
set USERNAME=root
set PASSWORD=123456
set SQL_DIR=c:/Users/zjj/RuoYi-Vue/sql

echo 连接MySQL并创建数据库...
mysql -P %MYSQL_PORT% -u %USERNAME% -p%PASSWORD% -e "CREATE DATABASE IF NOT EXISTS %DB_NAME% CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

if errorlevel 1 (
    echo 创建数据库失败！请检查MySQL连接信息。
    pause
    exit /b 1
)

echo 开始导入SQL脚本...

REM 导入主要初始化脚本
echo 导入 ry_20250522.sql...
mysql -P %MYSQL_PORT% -u %USERNAME% -p%PASSWORD% %DB_NAME% < %SQL_DIR%/ry_20250522.sql

if errorlevel 1 (
    echo 导入 ry_20250522.sql 失败！
    pause
    exit /b 1
)

REM 导入数据库产品相关脚本
echo 导入 database_product.sql...
mysql -P %MYSQL_PORT% -u %USERNAME% -p%PASSWORD% %DB_NAME% < %SQL_DIR%/database_product.sql

echo 导入 database_menu_new.sql...
mysql -P %MYSQL_PORT% -u %USERNAME% -p%PASSWORD% %DB_NAME% < %SQL_DIR%/database_menu_new.sql

echo 导入 database_product_permission.sql...
mysql -P %MYSQL_PORT% -u %USERNAME% -p%PASSWORD% %DB_NAME% < %SQL_DIR%/database_product_permission.sql

echo 导入 database_product_permission_fixed.sql...
mysql -P %MYSQL_PORT% -u %USERNAME% -p%PASSWORD% %DB_NAME% < %SQL_DIR%/database_product_permission_fixed.sql

REM 导入其他必要脚本
echo 导入 quartz.sql...
mysql -P %MYSQL_PORT% -u %USERNAME% -p%PASSWORD% %DB_NAME% < %SQL_DIR%/quartz.sql

echo 导入 student.sql...
mysql -P %MYSQL_PORT% -u %USERNAME% -p%PASSWORD% %DB_NAME% < %SQL_DIR%/student.sql

echo 导入 update_database_product_table.sql...
mysql -P %MYSQL_PORT% -u %USERNAME% -p%PASSWORD% %DB_NAME% < %SQL_DIR%/update_database_product_table.sql

echo 所有SQL脚本导入完成！
pause