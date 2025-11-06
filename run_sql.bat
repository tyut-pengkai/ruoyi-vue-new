@echo off
echo 正在设置数据库产品管理模块...
echo.
echo 请输入MySQL root密码:
mysql -u root -p < sql/database_product.sql
echo.
echo 数据库产品表创建完成！
echo.
echo 请输入MySQL root密码:
mysql -u root -p < sql/create_database_menu.sql
echo.
echo 菜单和权限配置创建完成！
echo.
echo 设置完成！请重启后端服务并刷新前端页面以查看更改。
pause