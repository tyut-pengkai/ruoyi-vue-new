-- 插入数据库产品管理子菜单
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('数据库产品列表', (SELECT MAX(menu_id) FROM sys_menu WHERE menu_name = '数据库产品管理'), 1, 'product', 'database/product/index', 1, 0, 'C', '0', '0', 'database:product:list', 'database', 'admin', NOW(), 'admin', NOW(), '数据库产品列表菜单');

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('姓名一', (SELECT MAX(menu_id) FROM sys_menu WHERE menu_name = '数据库产品管理'), 2, 'nameOne', 'database/product/nameOne', 1, 0, 'C', '0', '0', 'database:nameOne:list', '#', 'admin', NOW(), 'admin', NOW(), '姓名一菜单');

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('姓名二', (SELECT MAX(menu_id) FROM sys_menu WHERE menu_name = '数据库产品管理'), 3, 'nameTwo', 'database/product/nameTwo', 1, 0, 'C', '0', '0', 'database:nameTwo:list', '#', 'admin', NOW(), 'admin', NOW(), '姓名二菜单');

-- 插入数据库产品管理按钮权限
SET @parentId = (SELECT MAX(menu_id) FROM sys_menu WHERE menu_name = '数据库产品管理');
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, '', `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES 
('新增', @parentId, 1, '', '', 0, 0, 'F', '0', '0', 'database:product:add', '#', 'admin', NOW(), 'admin', NOW(), '新增按钮'),
('修改', @parentId, 2, '', '', 0, 0, 'F', '0', '0', 'database:product:edit', '#', 'admin', NOW(), 'admin', NOW(), '修改按钮'),
('删除', @parentId, 3, '', '', 0, 0, 'F', '0', '0', 'database:product:remove', '#', 'admin', NOW(), 'admin', NOW(), '删除按钮'),
('导出', @parentId, 4, '', '', 0, 0, 'F', '0', '0', 'database:product:export', '#', 'admin', NOW(), 'admin', NOW(), '导出按钮'),
('查询', @parentId, 5, '', '', 0, 0, 'F', '0', '0', 'database:product:query', '#', 'admin', NOW(), 'admin', NOW(), '查询按钮');

-- 将权限分配给管理员角色
SET @roleId = (SELECT role_id FROM sys_role WHERE role_name = '超级管理员');
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT @roleId, menu_id FROM sys_menu WHERE perms LIKE 'database:%';