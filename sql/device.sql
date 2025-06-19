-- ----------------------------
-- 设备信息表 20250618
-- ----------------------------
drop table if exists device_info;
create table device_info (
   device_id        bigint(20)        not null auto_increment  comment '设备id'
  ,device_code      varchar(100)      not null                 comment '设备编码'
  ,device_name      varchar(200)      not null                 comment '设备名称'
  ,mxc_addr         varchar(100)      not null                 comment 'mxc地址'
  ,ip_addr          varchar(100)      default null             comment 'IP地址'
  ,area             varchar(100)      default null             comment '所属区域'
  ,online_status    char(1)           default 1                comment '在线状态（0线上 1下线）'
  ,ota_version      varchar(100)      default 'v1.0.0'         comment '固件版本'
  ,status           char(1)           default '0'              comment '状态（0正常 1停用）'
  ,del_flag         char(1)           default '0'              comment '删除标志（0代表存在 2代表删除）'
  ,create_time 	    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP                                        comment '创建时间'
  ,update_time      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP            comment '更新时间'
  ,primary key (device_id)
  ,UNIQUE INDEX idx_uni_mxc_addr (mxc_addr)
  ,UNIQUE INDEX idx_uni_device_code (device_code)
) engine=innodb auto_increment=200 comment = '设备信息';


INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) 
VALUES (5, '设备管理', 0, 5, 'device', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2025-06-17 09:17:10', '', NULL, '设备管理目录');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('设备信息', '5', '1', 'info', 'device/info/index', 1, 0, 'C', '0', '0', 'device:info:list', '#', 'admin', sysdate(), '', null, '设备信息菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('设备信息查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'device:info:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('设备信息新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'device:info:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('设备信息修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'device:info:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('设备信息删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'device:info:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('设备信息导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'device:info:export',       '#', 'admin', sysdate(), '', null, '');





-- 初始化注册用户角色
INSERT INTO `sys_role`(`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `menu_check_strictly`, `dept_check_strictly`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) 
VALUES (100, '普通注册用户角色', 'common_register', 3, '1', 1, 1, '0', '0', 'admin', '2025-06-18 20:02:44', '', NULL, NULL);


-- 初始化注册用户角色 的菜单权限
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (100, 5);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (100, 2000);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (100, 2001);




-- ----------------------------
-- 用户设备关联表 20250618
-- ----------------------------
drop table if exists device_user;
create table device_user (
   user_id        bigint(20)     not null   comment '用户id'
  ,device_id      bigint(20)     not null   comment '设备id'
  ,create_time 	  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP                                        comment '创建时间'
  ,update_time    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP            comment '更新时间'
  ,primary key (device_id)
) engine=innodb auto_increment=200 comment = '用户设备关联表';



