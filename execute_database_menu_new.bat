@echo off
echo 正在执行数据库菜单配置脚本...
mysql -u root -p < sql/database_menu_new.sql
echo 脚本执行完成！
pause