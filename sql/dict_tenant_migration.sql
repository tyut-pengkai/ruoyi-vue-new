-- =============================================
-- 字典表混合模式租户隔离迁移脚本
-- 执行前请备份数据库
-- =============================================

-- 1. 为 sys_dict_type 添加 tenant_id 字段
ALTER TABLE sys_dict_type
ADD COLUMN tenant_id BIGINT(20) NOT NULL DEFAULT 0 COMMENT '租户ID（0=系统级）' AFTER dict_id;

-- 2. 为 sys_dict_data 添加 tenant_id 字段
ALTER TABLE sys_dict_data
ADD COLUMN tenant_id BIGINT(20) NOT NULL DEFAULT 0 COMMENT '租户ID（0=系统级）' AFTER dict_code;

-- 3. 删除旧的唯一约束
ALTER TABLE sys_dict_type DROP INDEX dict_type;

-- 4. 创建新的复合唯一约束（包含 tenant_id）
ALTER TABLE sys_dict_type ADD UNIQUE INDEX uk_tenant_dict_type (tenant_id, dict_type);
ALTER TABLE sys_dict_data ADD UNIQUE INDEX uk_tenant_dict_data (tenant_id, dict_type, dict_value);

-- 5. 添加索引优化查询
CREATE INDEX idx_dict_type_tenant ON sys_dict_type(tenant_id);
CREATE INDEX idx_dict_data_tenant ON sys_dict_data(tenant_id);
