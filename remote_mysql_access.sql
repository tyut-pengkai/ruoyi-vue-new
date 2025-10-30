-- 为root用户添加远程连接权限
USE mysql;

-- 查看当前用户权限
SELECT user, host FROM user;

-- 创建远程访问用户（建议使用专用用户而非root）
CREATE USER IF NOT EXISTS 'remote_user'@'%' IDENTIFIED BY '123456';

-- 授予远程用户访问权限
GRANT ALL PRIVILEGES ON ry_vue.* TO 'remote_user'@'%' WITH GRANT OPTION;

-- 如果需要让root用户也能远程访问（不推荐）
-- GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;

-- 刷新权限
FLUSH PRIVILEGES;

-- 显示授权结果
SHOW GRANTS FOR 'remote_user'@'%';