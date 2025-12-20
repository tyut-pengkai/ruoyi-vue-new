-- ============================================
-- Bug 修复脚本 - 2025-12-20
-- ============================================

-- Bug 1: 修复基础套餐菜单配置（添加字典管理菜单）
-- 原因：基础套餐缺少字典管理菜单（ID=105）及其子菜单权限

-- 1. 添加字典管理主菜单
INSERT IGNORE INTO sys_package_menu (package_id, menu_id) VALUES (1, 105);

-- 2. 添加字典管理子菜单权限（1025-1029）
INSERT IGNORE INTO sys_package_menu (package_id, menu_id) VALUES (1, 1025);  -- 字典查询
INSERT IGNORE INTO sys_package_menu (package_id, menu_id) VALUES (1, 1026);  -- 字典新增
INSERT IGNORE INTO sys_package_menu (package_id, menu_id) VALUES (1, 1027);  -- 字典修改
INSERT IGNORE INTO sys_package_menu (package_id, menu_id) VALUES (1, 1028);  -- 字典删除
INSERT IGNORE INTO sys_package_menu (package_id, menu_id) VALUES (1, 1029);  -- 字典导出

-- 验证修复结果
SELECT pm.package_id, pm.menu_id, m.menu_name
FROM sys_package_menu pm
LEFT JOIN sys_menu m ON pm.menu_id = m.menu_id
WHERE pm.package_id = 1 AND pm.menu_id IN (105, 1025, 1026, 1027, 1028, 1029);
