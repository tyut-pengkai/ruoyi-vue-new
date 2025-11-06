Write-Host "正在创建数据库产品管理菜单..."

# 执行SQL脚本
& mysql -u root -p -e "source create_menu_simple.sql"

Write-Host "菜单创建完成！"
Read-Host "按任意键继续..."