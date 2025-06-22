-- ---------------------------- 20250622
-- 设备信息表 
-- ----------------------------
drop table if exists device_info;
create table device_info (
   device_id        bigint(20)        not null auto_increment  comment '设备id'
  ,device_code      varchar(100)      not null                 comment '设备编码'
  ,device_name      varchar(200)      not null                 comment '设备名称'
  ,mac_addr         varchar(100)      not null                 comment 'mac地址'
  ,ip_addr          varchar(100)      default null             comment 'IP地址'
  ,area             varchar(100)      default null             comment '所属区域'
  ,online_status    char(1)           default 1                comment '在线状态（0线上 1下线）'
  ,ota_version      varchar(100)                               comment '固件版本'
  ,status           char(1)           default '0'              comment '状态（0正常 1停用）'
  ,del_flag         char(1)           default '0'              comment '删除标志（0代表存在 2代表删除）'
  ,create_time 	    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP                                        comment '创建时间'
  ,update_time      TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP            comment '更新时间'
  ,primary key (device_id)
  ,UNIQUE INDEX idx_uni_mac_addr (mac_addr)
  ,UNIQUE INDEX idx_uni_device_code (device_code)
) engine=innodb auto_increment=200 comment = '设备信息';



-- ----------------------------
-- 用户设备关联表  
-- ----------------------------
drop table if exists device_user;
create table device_user (
   user_id        bigint(20)     not null   comment '用户id'
  ,device_id      bigint(20)     not null   comment '设备id'
  ,create_time 	  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP                                        comment '创建时间'
  ,update_time    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP            comment '更新时间'
  ,primary key (device_id)
) engine=innodb auto_increment=200 comment = '用户设备关联表';


-- ------------- 设备菜单  
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (5   , '设备管理'      , 0   , 5, 'device', NULL               , ''  , '', 1, 0, 'M', '0', '0', ''                        , 'tool', 'admin', sysdate(), '', sysdate(), '设备管理目录');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2000, '设备信息'      , 5   , 1, 'info'  , 'device/info/index', NULL, '', 1, 0, 'C', '0', '0', 'device:info:list'        , '#'   , 'admin', sysdate(), '', sysdate(), '设备信息菜单');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2001, '设备信息查询'  , 2000, 1, '#'     , ''                 , NULL, '', 1, 0, 'F', '0', '0', 'device:info:query'       , '#'   , 'admin', sysdate(), '', sysdate(), '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2002, '设备信息新增'  , 2000, 2, '#'     , ''                 , NULL, '', 1, 0, 'F', '0', '0', 'device:info:add'         , '#'   , 'admin', sysdate(), '', sysdate(), '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2003, '设备信息修改'  , 2000, 3, '#'     , ''                 , NULL, '', 1, 0, 'F', '0', '0', 'device:info:edit'        , '#'   , 'admin', sysdate(), '', sysdate(), '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2004, '设备信息删除'  , 2000, 4, '#'     , ''                 , NULL, '', 1, 0, 'F', '0', '0', 'device:info:remove'      , '#'   , 'admin', sysdate(), '', sysdate(), '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2005, '设备信息导出'  , 2000, 5, '#'     , ''                 , NULL, '', 1, 0, 'F', '0', '0', 'device:info:export'      , '#'   , 'admin', sysdate(), '', sysdate(), '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2006, '添加绑定设备'  , 2000, 6, ''      , NULL               , NULL, '', 1, 0, 'F', '0', '0', 'device:info:bindToUser'  , '#'   , 'admin', sysdate(), '', sysdate(), '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2008, '解绑'          , 2000, 7, ''      , NULL               , NULL, '', 1, 0, 'F', '0', '0', 'device:info:unbindToUser', '#'   , 'admin', sysdate(), '', sysdate(), '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2009, '导入'          , 2000, 8, ''      , NULL               , NULL, '', 1, 0, 'F', '0', '0', 'device:info:import'      , '#'   , 'admin', sysdate(), '', sysdate(), '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2023, '选择音色'      , 2000, 9, '#'     , NULL               , NULL, '', 1, 0, 'F', '0', '0', 'device:info:chooseAgent' , '#'   , 'admin', sysdate(), '', sysdate(), '');


