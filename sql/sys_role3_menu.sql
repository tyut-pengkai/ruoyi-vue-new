
-- ------------------------------注册用户角色3 的权限 20250622

-- 初始化注册用户角色
INSERT INTO `sys_role`(`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `menu_check_strictly`, `dept_check_strictly`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) 
VALUES (3, '普通注册用户角色', 'common_register', 3, '1', 1, 1, '0', '0', 'admin',  sysdate(), '', NULL, '');


-- 初始化注册用户角色 的菜单权限
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 5);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 6);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2000);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2001);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2006);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2008);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2017);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2018);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2019);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2023);
