-- 删除旧的菜单项
DELETE FROM sys_menu WHERE menu_name LIKE '%数据库产品%';

-- 插入顶级菜单"数据库产品管理"
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) 
VALUES ('数据库产品管理', 0, 1, 'database', NULL, '', 1, 0, 'M', '0', '0', '', 'database', 'admin', NOW(), '', NULL, '数据库产品管理目录');

-- 获取刚插入的顶级菜单ID
SET @parent_id = LAST_INSERT_ID();

-- 插入子菜单"郑瑜甜"
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) 
VALUES ('郑瑜甜', @parent_id, 1, 'zhengyutian', 'database/product/index', '', 1, 0, 'C', '0', '0', 'database:zhengyutian:list', 'form', 'admin', NOW(), '', NULL, '郑瑜甜菜单');

-- 获取刚插入的子菜单ID
SET @child_id = LAST_INSERT_ID();

-- 插入按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, query, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) 
VALUES 
('郑瑜甜查询', @child_id, 1, '#', '', '', 1, 0, 'F', '0', '0', 'database:zhengyutian:query', '#', 'admin', NOW(), '', NULL, ''),
('郑瑜甜新增', @child_id, 2, '#', '', '', 1, 0, 'F', '0', '0', 'database:zhengyutian:add', '#', 'admin', NOW(), '', NULL, ''),
('郑瑜甜修改', @child_id, 3, '#', '', '', 1, 0, 'F', '0', '0', 'database:zhengyutian:edit', '#', 'admin', NOW(), '', NULL, ''),
('郑瑜甜删除', @child_id, 4, '#', '', '', 1, 0, 'F', '0', '0', 'database:zhengyutian:remove', '#', 'admin', NOW(), '', NULL, ''),
('郑瑜甜导出', @child_id, 5, '#', '', '', 1, 0, 'F', '0', '0', 'database:zhengyutian:export', '#', 'admin', NOW(), '', NULL, '');

-- 将权限分配给管理员角色
INSERT INTO sys_role_menu (role_id, menu_id) 
SELECT 1, menu_id FROM sys_menu WHERE menu_name LIKE '%郑瑜甜%' OR menu_name = '数据库产品管理';