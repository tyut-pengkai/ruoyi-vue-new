-- 删除已存在的数据库产品管理相关菜单
DELETE FROM sys_role_menu WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE perms LIKE 'database:%');
DELETE FROM sys_menu WHERE perms LIKE 'database:%' OR menu_name = '数据库产品管理';

-- 创建数据库产品管理顶级菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES ('数据库产品管理', 0, 6, 'database', NULL, 'M', '0', '0', '', 'database', 'admin', NOW());

-- 获取刚插入的菜单ID
SET @parentId = LAST_INSERT_ID();

-- 创建子菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES 
('数据库产品列表', @parentId, 1, 'product', 'database/product/index', 'C', '0', '0', 'database:product:list', 'list', 'admin', NOW()),
('姓名一', @parentId, 2, 'nameOne', 'database/product/nameOne', 'C', '0', '0', 'database:nameOne:list', 'user', 'admin', NOW()),
('姓名二', @parentId, 3, 'nameTwo', 'database/product/nameTwo', 'C', '0', '0', 'database:nameTwo:list', 'user', 'admin', NOW());

-- 创建按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time) 
VALUES 
('新增', @parentId, 4, '', '', 'F', '0', '0', 'database:product:add', '#', 'admin', NOW()),
('修改', @parentId, 5, '', '', 'F', '0', '0', 'database:product:edit', '#', 'admin', NOW()),
('删除', @parentId, 6, '', '', 'F', '0', '0', 'database:product:remove', '#', 'admin', NOW()),
('导出', @parentId, 7, '', '', 'F', '0', '0', 'database:product:export', '#', 'admin', NOW()),
('查询', @parentId, 8, '', '', 'F', '0', '0', 'database:product:query', '#', 'admin', NOW());

-- 分配权限给管理员角色
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE perms LIKE 'database:%';