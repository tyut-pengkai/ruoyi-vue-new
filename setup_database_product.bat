@echo off
echo 正在设置数据库产品管理模块...
echo.
echo 1. 创建数据库产品表...
mysql -u root -p -e "source sql/database_product.sql"
echo.
echo 2. 创建菜单和权限配置...
mysql -u root -p -e "source sql/create_database_menu.sql"
echo.
echo 设置完成！
echo.
echo 请重启后端服务并刷新前端页面以查看更改。
pause