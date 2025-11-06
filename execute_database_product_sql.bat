@echo off
echo 正在执行数据库产品管理模块的SQL脚本...

echo 1. 创建数据库产品表...
mysql -u root -p < sql/database_product.sql

echo 2. 创建菜单和权限配置...
mysql -u root -p < sql/database_product_permission_fixed.sql

echo 执行完成！
pause