@echo off
mysql -u root -p123456 ry-vue --default-character-set=utf8mb4 -e "source sql/student.sql"
echo SQL execution completed.
pause