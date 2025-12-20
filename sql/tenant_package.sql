-- ============================================
-- 租户套餐功能 - 数据库脚本
-- 版本: 1.0
-- 日期: 2025-12-19
-- 说明: 实现租户套餐功能，通过套餐控制租户的菜单权限
-- ============================================

-- ============================================
-- 第一步：创建租户套餐表
-- ============================================

CREATE TABLE IF NOT EXISTS sys_tenant_package (
  package_id       BIGINT(20)     NOT NULL AUTO_INCREMENT    COMMENT '套餐ID',
  package_name     VARCHAR(50)    NOT NULL                   COMMENT '套餐名称',
  package_code     VARCHAR(50)    NOT NULL                   COMMENT '套餐编码',
  status           CHAR(1)        DEFAULT '0'                COMMENT '状态(0正常 1停用)',
  del_flag         CHAR(1)        DEFAULT '0'                COMMENT '删除标志(0存在 2删除)',
  create_by        VARCHAR(64)    DEFAULT ''                 COMMENT '创建者',
  create_time      DATETIME       DEFAULT NULL               COMMENT '创建时间',
  update_by        VARCHAR(64)    DEFAULT ''                 COMMENT '更新者',
  update_time      DATETIME       DEFAULT NULL               COMMENT '更新时间',
  remark           VARCHAR(500)   DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (package_id),
  UNIQUE KEY uk_package_code (package_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='租户套餐表';

-- ============================================
-- 第二步：创建套餐菜单关联表
-- ============================================

CREATE TABLE IF NOT EXISTS sys_package_menu (
  package_id       BIGINT(20)     NOT NULL                   COMMENT '套餐ID',
  menu_id          BIGINT(20)     NOT NULL                   COMMENT '菜单ID',
  PRIMARY KEY (package_id, menu_id),
  INDEX idx_package_id (package_id)                         -- 为package_id添加索引（用户要求）
) ENGINE=InnoDB COMMENT='套餐菜单关联表';

-- ============================================
-- 第三步：修改sys_tenant表，增加套餐字段
-- ============================================

-- 检查列是否存在，避免重复执行报错
SELECT COUNT(*) INTO @col_exists
FROM information_schema.columns
WHERE table_schema = DATABASE()
  AND table_name = 'sys_tenant'
  AND column_name = 'package_id';

-- 只有列不存在时才添加
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE sys_tenant ADD COLUMN package_id BIGINT(20) DEFAULT NULL COMMENT ''套餐ID'' AFTER tenant_code',
    'SELECT ''Column package_id already exists'' AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加索引（忽略已存在的情况）
-- 注意：如果索引已存在会报错，可忽略该错误继续执行
SET @index_exists = (SELECT COUNT(*) FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'sys_tenant' AND index_name = 'idx_package_id');
SET @sql = IF(@index_exists = 0, 'ALTER TABLE sys_tenant ADD INDEX idx_package_id (package_id)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ============================================
-- 第四步：初始化套餐数据
-- ============================================

INSERT INTO sys_tenant_package (package_name, package_code, status, create_by, create_time, remark) VALUES
('基础套餐', 'BASIC', '0', 'admin', NOW(), '用户、部门管理'),
('标准套餐', 'STANDARD', '0', 'admin', NOW(), '增加角色、菜单、字典管理'),
('高级套餐', 'PREMIUM', '0', 'admin', NOW(), '全部功能，不限制菜单');

-- ============================================
-- 第五步：初始化套餐菜单关联（基础套餐）
-- ============================================

-- 基础套餐：只包含用户、部门、岗位、字典管理
INSERT INTO sys_package_menu (package_id, menu_id) VALUES
-- 系统管理模块
(1, 1),      -- 系统管理
-- 菜单项
(1, 100),    -- 用户管理
(1, 101),    -- 部门管理
(1, 103),    -- 岗位管理
(1, 104),    -- 字典管理
-- 用户管理按钮
(1, 1000), (1, 1001), (1, 1002), (1, 1003), (1, 1004), (1, 1005), (1, 1006),
-- 部门管理按钮
(1, 1016), (1, 1017), (1, 1018), (1, 1019),
-- 岗位管理按钮
(1, 1020), (1, 1021), (1, 1022), (1, 1023), (1, 1024),
-- 字典管理按钮
(1, 1025), (1, 1026), (1, 1027), (1, 1028), (1, 1029);

-- ============================================
-- 第六步：初始化套餐菜单关联（标准套餐）
-- ============================================

-- 标准套餐：增加角色、菜单管理
INSERT INTO sys_package_menu (package_id, menu_id) VALUES
-- 系统管理模块
(2, 1),      -- 系统管理
-- 菜单项
(2, 100), (2, 101), (2, 102), (2, 103), (2, 104), (2, 105),  -- 用户/部门/角色/岗位/字典/菜单
-- 用户管理按钮
(2, 1000), (2, 1001), (2, 1002), (2, 1003), (2, 1004), (2, 1005), (2, 1006),
-- 角色管理按钮
(2, 1007), (2, 1008), (2, 1009), (2, 1010), (2, 1011),
-- 菜单管理按钮
(2, 1012), (2, 1013), (2, 1014), (2, 1015),
-- 部门管理按钮
(2, 1016), (2, 1017), (2, 1018), (2, 1019),
-- 岗位管理按钮
(2, 1020), (2, 1021), (2, 1022), (2, 1023), (2, 1024),
-- 字典管理按钮
(2, 1025), (2, 1026), (2, 1027), (2, 1028), (2, 1029);

-- ============================================
-- 第七步：高级套餐不插入关联数据
-- ============================================

-- 说明：高级套餐 (package_id=3) 不插入 sys_package_menu 数据
-- 代码逻辑：查询结果为空列表时，表示"不限制菜单"，用户可以看到所有菜单

-- ============================================
-- 第八步：关联现有租户到套餐
-- ============================================

-- 根据实际租户情况调整（使用安全的更新方式）
UPDATE sys_tenant SET package_id = 1 WHERE tenant_code = 'DEFAULT' AND package_id IS NULL;
UPDATE sys_tenant SET package_id = 2 WHERE tenant_code = 'TENANT_A' AND package_id IS NULL;
UPDATE sys_tenant SET package_id = 3 WHERE tenant_code = 'TENANT_B' AND package_id IS NULL;

-- ============================================
-- 验证数据
-- ============================================

-- 验证套餐表
SELECT * FROM sys_tenant_package;

-- 验证套餐菜单关联（基础套餐）
SELECT COUNT(*) AS '基础套餐菜单数' FROM sys_package_menu WHERE package_id = 1;

-- 验证套餐菜单关联（标准套餐）
SELECT COUNT(*) AS '标准套餐菜单数' FROM sys_package_menu WHERE package_id = 2;

-- 验证套餐菜单关联（高级套餐）
SELECT COUNT(*) AS '高级套餐菜单数（应为0）' FROM sys_package_menu WHERE package_id = 3;

-- 验证租户套餐关联
SELECT tenant_id, tenant_name, tenant_code, package_id
FROM sys_tenant
WHERE package_id IS NOT NULL;

-- ============================================
-- 回滚脚本（仅供紧急情况使用）
-- ============================================

/*
-- 警告：回滚将删除所有套餐数据和配置！

-- 1. 清除租户的套餐关联
UPDATE sys_tenant SET package_id = NULL;

-- 2. 删除套餐菜单关联表
DROP TABLE IF EXISTS sys_package_menu;

-- 3. 删除套餐表
DROP TABLE IF EXISTS sys_tenant_package;

-- 4. 删除租户表的套餐字段
ALTER TABLE sys_tenant DROP COLUMN IF EXISTS package_id;
ALTER TABLE sys_tenant DROP INDEX IF EXISTS idx_package_id;
*/

-- ============================================
-- 使用说明
-- ============================================

/*
套餐机制说明：

1. **基础套餐 (BASIC)**
   - 适用于小型租户
   - 只包含基础的用户、部门、岗位、字典管理
   - 不能分配角色和菜单权限

2. **标准套餐 (STANDARD)**
   - 适用于中型租户
   - 包含基础功能 + 角色管理 + 菜单管理
   - 可以自定义角色和权限

3. **高级套餐 (PREMIUM)**
   - 适用于大型租户
   - 不限制任何菜单功能
   - sys_package_menu 表中无记录表示"全部菜单可见"

菜单过滤逻辑：
- 用户最终可见菜单 = 角色菜单 ∩ 套餐菜单
- 超级管理员不受套餐限制
- 未配置套餐的租户默认不限制菜单
*/
