@echo off
echo 正在执行数据库表结构更新脚本...
mysql -u root -p < sql/update_database_product_table.sql
echo 脚本执行完成！
pause