-- 删除旧的数据库产品管理菜单（如果存在）
DELETE FROM `sys_role_menu` WHERE menu_id IN (SELECT menu_id FROM sys_menu WHERE menu_name = '数据库产品管理' OR perms LIKE 'database:%');
DELETE FROM `sys_menu` WHERE menu_name = '数据库产品管理' OR perms LIKE 'database:%';

-- 插入数据库产品管理菜单（顶级菜单）
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('数据库产品管理', 0, 6, 'database', '', 1, 0, 'M', '0', '0', '', 'database', 'admin', NOW(), 'admin', NOW(), '数据库产品管理菜单');

-- 获取刚插入的数据库产品管理菜单ID
SET @parentId = (SELECT MAX(menu_id) FROM sys_menu WHERE menu_name = '数据库产品管理');

-- 插入郑瑜甜子菜单
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('郑瑜甜', @parentId, 1, 'zhengyutian', 'database/product/index', 1, 0, 'C', '0', '0', 'database:zhengyutian:list', 'user', 'admin', NOW(), 'admin', NOW(), '郑瑜甜菜单');

-- 获取刚插入的郑瑜甜菜单ID
SET @childId = (SELECT MAX(menu_id) FROM sys_menu WHERE menu_name = '郑瑜甜');

-- 插入郑瑜甜菜单的按钮权限
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES 
('新增', @childId, 1, '', '', 0, 0, 'F', '0', '0', 'database:zhengyutian:add', '#', 'admin', NOW(), 'admin', NOW(), '新增按钮'),
('修改', @childId, 2, '', '', 0, 0, 'F', '0', '0', 'database:zhengyutian:edit', '#', 'admin', NOW(), 'admin', NOW(), '修改按钮'),
('删除', @childId, 3, '', '', 0, 0, 'F', '0', '0', 'database:zhengyutian:remove', '#', 'admin', NOW(), 'admin', NOW(), '删除按钮'),
('导出', @childId, 4, '', '', 0, 0, 'F', '0', '0', 'database:zhengyutian:export', '#', 'admin', NOW(), 'admin', NOW(), '导出按钮'),
('查询', @childId, 5, '', '', 0, 0, 'F', '0', '0', 'database:zhengyutian:query', '#', 'admin', NOW(), 'admin', NOW(), '查询按钮');

-- 将权限分配给管理员角色
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, menu_id FROM sys_menu WHERE perms LIKE 'database:%';