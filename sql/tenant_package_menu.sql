-- ============================================
-- 套餐管理菜单 - 插入脚本
-- 版本: 1.0
-- 日期: 2025-12-20
-- ============================================

-- 1. 套餐管理菜单（放在系统管理下，parent_id = 1）
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2006, '套餐管理', 1, 10, 'package', 'system/package/index', NULL, 'Package', 1, 0, 'C', '0', '0', 'system:package:list', 'component', 'admin', NOW(), '', NULL, '租户套餐管理菜单');

-- 2. 套餐管理按钮权限
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2007, '套餐查询', 2006, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'system:package:query', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2008, '套餐新增', 2006, 2, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'system:package:add', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2009, '套餐修改', 2006, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'system:package:edit', '#', 'admin', NOW(), '', NULL, '');

INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2010, '套餐删除', 2006, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'system:package:remove', '#', 'admin', NOW(), '', NULL, '');

-- 3. 给超级管理员角色分配权限（role_id = 1）
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2006);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2007);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2008);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2009);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 2010);

-- 验证
SELECT * FROM sys_menu WHERE menu_id >= 2006;
