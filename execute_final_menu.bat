@echo off
echo Executing database menu configuration...
mysql -u root -p < sql/final_database_menu.sql
echo Script execution completed!
pause