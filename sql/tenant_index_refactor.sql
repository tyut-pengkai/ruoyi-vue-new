-- ============================================
-- 若依多租户 - 唯一索引改造脚本
-- 版本: 1.0
-- 日期: 2025-12-19
-- 警告: 执行前务必备份数据库！
-- ============================================

-- ============================================
-- 第一步：检查是否存在重复数据（必须先执行）
-- ============================================

-- 检查 sys_user 表重复数据
SELECT tenant_id, user_name, COUNT(*)
FROM sys_user
WHERE del_flag = '0'
GROUP BY tenant_id, user_name
HAVING COUNT(*) > 1;
-- 如有输出，需先处理重复数据

-- 检查 sys_role 表重复数据
SELECT tenant_id, role_key, COUNT(*)
FROM sys_role
WHERE del_flag = '0'
GROUP BY tenant_id, role_key
HAVING COUNT(*) > 1;
-- 如有输出，需先处理重复数据

-- 检查 sys_post 表重复数据
-- 注意：sys_post 表没有 del_flag 字段
SELECT tenant_id, post_code, COUNT(*)
FROM sys_post
GROUP BY tenant_id, post_code
HAVING COUNT(*) > 1;
-- 如有输出，需先处理重复数据

-- ============================================
-- 第二步：执行唯一索引改造
-- ============================================

-- 1. sys_user表：用户名改为租户内唯一
-- 说明：不同租户可以有同名用户（如都有 admin）
-- 安全删除旧索引（兼容MySQL 5.7）
SET @index_exists = (SELECT COUNT(*) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'sys_user' AND index_name = 'uk_user_name');
SET @sql = IF(@index_exists > 0, 'ALTER TABLE sys_user DROP INDEX uk_user_name', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
-- 创建新的联合唯一索引
ALTER TABLE sys_user ADD UNIQUE KEY uk_tenant_user_name (tenant_id, user_name);

-- 2. sys_role表：角色标识改为租户内唯一
-- 说明：不同租户可以有同标识角色（如都有 role_admin）
SET @index_exists = (SELECT COUNT(*) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'sys_role' AND index_name = 'uk_role_key');
SET @sql = IF(@index_exists > 0, 'ALTER TABLE sys_role DROP INDEX uk_role_key', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
ALTER TABLE sys_role ADD UNIQUE KEY uk_tenant_role_key (tenant_id, role_key);

-- 3. sys_post表：岗位编码改为租户内唯一
-- 说明：不同租户可以有同编码岗位（如都有 ceo）
SET @index_exists = (SELECT COUNT(*) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'sys_post' AND index_name = 'uk_post_code');
SET @sql = IF(@index_exists > 0, 'ALTER TABLE sys_post DROP INDEX uk_post_code', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
ALTER TABLE sys_post ADD UNIQUE KEY uk_tenant_post_code (tenant_id, post_code);

-- ============================================
-- 第三步：验证索引创建
-- ============================================

-- 验证 sys_user 索引
SHOW INDEX FROM sys_user WHERE Key_name = 'uk_tenant_user_name';

-- 验证 sys_role 索引
SHOW INDEX FROM sys_role WHERE Key_name = 'uk_tenant_role_key';

-- 验证 sys_post 索引
SHOW INDEX FROM sys_post WHERE Key_name = 'uk_tenant_post_code';

-- ============================================
-- 执行结果说明
-- ============================================
-- 每个表应该显示索引的两条记录（tenant_id 和对应的业务字段）
-- Seq_in_index = 1: tenant_id
-- Seq_in_index = 2: user_name / role_key / post_code

-- ============================================
-- 回滚脚本（仅供紧急情况使用）
-- ============================================
/*
-- 警告：回滚将恢复到单租户模式，可能导致数据冲突！

-- 回滚 sys_user 索引
ALTER TABLE sys_user DROP INDEX IF EXISTS uk_tenant_user_name;
ALTER TABLE sys_user ADD UNIQUE KEY uk_user_name (user_name);

-- 回滚 sys_role 索引
ALTER TABLE sys_role DROP INDEX IF EXISTS uk_tenant_role_key;
ALTER TABLE sys_role ADD UNIQUE KEY uk_role_key (role_key);

-- 回滚 sys_post 索引
ALTER TABLE sys_post DROP INDEX IF EXISTS uk_tenant_post_code;
ALTER TABLE sys_post ADD UNIQUE KEY uk_post_code (post_code);
*/

-- ============================================
-- 执行注意事项
-- ============================================
-- 1. 选择业务低峰期执行（预计停机10-30分钟）
-- 2. 执行前完整备份数据库：mysqldump -u root -p database_name > backup.sql
-- 3. 务必先执行第一步的重复数据检查
-- 4. 如有重复数据，需先清理或合并
-- 5. 大表索引创建较慢，耐心等待不要中断
-- 6. 准备回滚脚本以备不时之需
-- 7. 执行完毕后验证应用功能正常
