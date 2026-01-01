# 数据库表创建脚本
# 确保MySQL路径和SQL文件路径正确

param(
    [Parameter(Mandatory=$false)]
    [string]$MysqlPath
)

# 配置参数
# 自动检测MySQL可执行文件路径
$MYSQL_EXE = if (-not [string]::IsNullOrEmpty($MysqlPath)) {
    $MysqlPath
} elseif (Get-Command 'mysql' -ErrorAction SilentlyContinue) {
    # 获取完整路径而非仅命令名
    (Get-Command 'mysql').Path
} else {
    # 默认路径列表 - 根据常见安装位置检测
    $defaultPaths = @(
        'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe',
        'C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe',
        'C:\Program Files\MySQL\MySQL Server 8.2\bin\mysql.exe',
        'C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe',
        'C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe',
        'C:\Program Files (x86)\MySQL\MySQL Server 8.1\bin\mysql.exe',
        'C:\Program Files (x86)\MySQL\MySQL Server 5.7\bin\mysql.exe'
    )
    
    $foundPath = $defaultPaths | Where-Object { Test-Path $_ -PathType Leaf } | Select-Object -First 1
    
    if (-not $foundPath) {
        Write-Host "Error: MySQL executable not found in default paths."
        # 添加交互式路径输入
        $userPath = Read-Host "Enter the full path to mysql.exe (e.g. C:\mysql\bin\mysql.exe)"
        Write-Host "Debug: User input path - $userPath"
        if (Test-Path -Path $userPath -PathType Leaf) {
            $foundPath = $userPath
            Write-Host "Using MySQL path: $foundPath"
            Write-Host "Debug: Assigned foundPath to user input"
        } else {
            Write-Host "Invalid path provided. Exiting."
            exit 1
        }
    }
    Write-Host "Debug: Found MySQL path - $foundPath"
    $MYSQL_EXE = $foundPath
}
$SQL_FILE = "C:\Users\lenovo\RuoYi-Vue\sql\database_product.sql"
$DB_USER = "root"
$DB_NAME = "ry-vue"

# 检查MySQL可执行文件是否存在
Write-Host "Debug: Checking MySQL path - $MYSQL_EXE"
if (-not (Test-Path -Path $MYSQL_EXE -PathType Leaf)) {
    Write-Host "Error: MySQL executable not found at specified path"
    Write-Host "Debug: Checking MySQL path - $MYSQL_EXE"
    Write-Host "Debug: Test-Path result - $(Test-Path -Path $MYSQL_EXE -PathType Leaf)"
    Write-Host "Please verify the MySQL installation path and try again."
    
    # 添加交互式路径输入
    $userPath = Read-Host "Enter the full path to mysql.exe (e.g. C:\mysql\bin\mysql.exe)"
    if (Test-Path -Path $userPath -PathType Leaf) {
        $MYSQL_EXE = $userPath
        Write-Host "Using MySQL path: $MYSQL_EXE"
    } else {
        Write-Host "Invalid path provided. Exiting."
        exit 1
    }
}
Write-Host "Please verify the following:
1. The path contains the correct MySQL version number (e.g., 8.0, 8.1, 5.7)
2. The path uses the correct Program Files directory (Program Files or Program Files (x86))"
    Write-Host "Common default paths to try:
- C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe
- C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe
- C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe"
    # 删除此处的exit 1，允许继续执行交互式输入
# 删除多余的闭合括号

# 检查SQL文件是否存在
if (-not (Test-Path -Path $SQL_FILE -PathType Leaf)) {
    Write-Host "Error: SQL file not found - $SQL_FILE"
    exit 1
}

# 执行SQL脚本
Write-Host "Starting to execute SQL script: $SQL_FILE"
Write-Host "Connecting to database: $DB_NAME (user: $DB_USER)"

# 使用调用运算符执行MySQL命令
& "$MYSQL_EXE" -u $DB_USER -p $DB_NAME -e "source $SQL_FILE"

# 检查执行结果
if ($LASTEXITCODE -eq 0) {
    Write-Host "Success: database_product table created"
} else {
    Write-Host "Error: SQL execution failed, exit code: $LASTEXITCODE"
    exit $LASTEXITCODE
}