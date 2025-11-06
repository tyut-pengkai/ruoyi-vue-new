# 设置数据库产品管理模块的菜单配置
Write-Host "正在设置数据库产品管理模块的菜单配置..." -ForegroundColor Green

# 提示用户输入MySQL密码
$password = Read-Host "请输入MySQL root用户密码" -AsSecureString
$passwordPlain = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto([System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($password))

# 执行SQL脚本
Write-Host "1. 创建数据库产品表..." -ForegroundColor Yellow
$process = Start-Process -FilePath "mysql" -ArgumentList "-u root -p$passwordPlain", "-e", "source sql/database_product.sql" -Wait -PassThru -NoNewWindow
if ($process.ExitCode -eq 0) {
    Write-Host "数据库产品表创建成功！" -ForegroundColor Green
} else {
    Write-Host "数据库产品表创建失败！" -ForegroundColor Red
}

Write-Host "2. 创建菜单和权限配置..." -ForegroundColor Yellow
$process = Start-Process -FilePath "mysql" -ArgumentList "-u root -p$passwordPlain", "-e", "source sql/create_database_menu.sql" -Wait -PassThru -NoNewWindow
if ($process.ExitCode -eq 0) {
    Write-Host "菜单和权限配置创建成功！" -ForegroundColor Green
} else {
    Write-Host "菜单和权限配置创建失败！" -ForegroundColor Red
}

Write-Host "设置完成！" -ForegroundColor Green
Write-Host "请重启后端服务并刷新前端页面以查看更改。" -ForegroundColor Yellow
Read-Host "按任意键退出"