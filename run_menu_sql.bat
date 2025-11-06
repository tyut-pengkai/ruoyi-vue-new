@echo off
echo 请输入MySQL密码后按回车键执行SQL脚本...
mysql -u root -p < create_database_menu.sql
echo 菜单创建完成！
pause