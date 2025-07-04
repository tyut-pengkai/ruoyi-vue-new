-- ----------------------------
-- 1. Table structure for payment_package
-- ----------------------------
DROP TABLE IF EXISTS `payment_package`;
CREATE TABLE `payment_package` (
  `package_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '套餐ID',
  `name` varchar(100) NOT NULL COMMENT '套餐名称',
  `hours` int(11) NOT NULL COMMENT '套餐时长',
  `free_hours` int(11) NOT NULL DEFAULT 0 COMMENT '套餐赠送时长',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `currency` varchar(20) NOT NULL COMMENT '币种',
  `discount_label` varchar(50) DEFAULT NULL COMMENT '折扣标签',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0启用 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `del_flag`  char(1)   DEFAULT '0'  comment '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`package_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='商品套餐表';



-- ----------------------------
-- 2. Initial data for payment_package
-- ----------------------------
INSERT INTO `payment_package`(`package_id`, `name`, `hours`, `price`, `currency`,`discount_label`, `status`, `create_by`, `create_time`, `remark`) VALUES (1, '基础套餐', 1,  1.00, 'USD',NULL, '0', 'admin', sysdate(), '1小时标准套餐');
INSERT INTO `payment_package`(`package_id`, `name`, `hours`, `price`, `currency`,`discount_label`, `status`, `create_by`, `create_time`, `remark`) VALUES (2, '经济套餐', 10, 9.00, 'USD','省$1', '0', 'admin', sysdate(), '10小时优惠套餐');
INSERT INTO `payment_package`(`package_id`, `name`, `hours`, `price`, `currency`,`discount_label`, `status`, `create_by`, `create_time`, `remark`) VALUES (3, '优选套餐', 15, 12.00,'USD', '省$3', '0', 'admin', sysdate(), '15小时特惠套餐');
INSERT INTO `payment_package`(`package_id`, `name`, `hours`, `price`, `currency`,`discount_label`, `status`, `create_by`, `create_time`, `remark`) VALUES (4, '畅享套餐', 30, 24.00,'USD', '省$6', '0', 'admin', sysdate(), '30小时畅享套餐');

-- ----------------------------
-- 3. Table structure for payment_order
-- ----------------------------
DROP TABLE IF EXISTS `payment_order`;
CREATE TABLE `payment_order` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID', 
  `user_name` VARCHAR(30) DEFAULT NULL COMMENT '用户名',
  `device_id` bigint(20) NOT NULL COMMENT '设备id',
  `device_name` varchar(200) NOT NULL COMMENT '设备名称', 
  `package_id` bigint(20) DEFAULT NULL COMMENT '套餐ID',
  `package_name` varchar(100) DEFAULT NULL COMMENT '套餐名称',
  `package_hours` int(11) DEFAULT NULL COMMENT '套餐时长',
  `package_free_hours` int(11) DEFAULT 0 COMMENT '套餐赠送时长',
  `amount` decimal(10, 2) NOT NULL COMMENT '订单金额',
  `currency` varchar(20) NOT NULL COMMENT '币种',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '订单状态（0待支付 1已完成 2已取消 3支付失败）',
  `payment_channel` varchar(20) DEFAULT NULL COMMENT '支付渠道（paypal/wechat/alipay等）',
  `payment_id` varchar(128) DEFAULT NULL COMMENT '支付平台的支付/交易ID',
  `payment_token` varchar(128) DEFAULT NULL COMMENT '支付令牌',
  `payer_id` varchar(128) DEFAULT NULL COMMENT '支付平台用户标识',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `pay_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `del_flag`  char(1)   DEFAULT '0'  comment '删除标志（0存在 2删除）',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表'; 




-- 订单状态
-- payment_order_status（0=待支付, 1=已完成, 2=已取消, 3=支付失败）
INSERT INTO `sys_dict_type`(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (100, '订单状态', 'payment_order_status', '0', 'admin', sysdate(), '', NULL, '订单状态（0=待支付, 1=已完成, 2=已取消, 3=支付失败）');
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (30, 1, '待支付'  , '0', 'payment_order_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '0=待支付');
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (31, 2, '已完成'  , '1', 'payment_order_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '1=已完成');
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (32, 3, '已取消'  , '2', 'payment_order_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '2=已取消');
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (33, 4, '支付失败', '3', 'payment_order_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '3=支付失败');

-- 币种(USD=美金, CNY=人民币)
INSERT INTO `sys_dict_type`(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (103, '币种', 'payment_currency', '0', 'admin', sysdate(), 'admin', sysdate(), 'USD=美金, CNY=人民币');
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (36, 1, '美金'    , 'USD', 'payment_currency', '', 'info', 'Y', '0', 'admin', sysdate(), '', NULL, 'USD=美金');
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (37, 2, '人民币'  , 'CNY', 'payment_currency', '', 'info', 'N', '0', 'admin', sysdate(), '', NULL, 'CNY=人民币');



-- 套餐状态
-- payment_package_status（0=启用, 1=停用）
INSERT INTO `sys_dict_type`(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (102, '套餐状态', 'payment_package_status', '0', 'admin', sysdate(), 'admin', sysdate(), '套餐状态（0=启用, 1=停用）');
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (34, 1, '启用'  , '0', 'payment_package_status', '', 'danger', 'Y', '0', 'admin', sysdate(), '', NULL, '0=启用');
INSERT INTO `sys_dict_data`(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (35, 2, '停用'  , '1', 'payment_package_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '1=停用');



-- 商品套餐中心 菜单 SQL
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2024, '会员管理', 0, 8, 'payment', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2025, '商品套餐中心', 2024, 1, 'package', 'payment/package/index', NULL, '', 1, 0, 'C', '0', '0', 'payment:package:list', '#', 'admin', sysdate(), '', NULL, '套餐菜单');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2026, '商品套餐查询', 2025, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:package:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2027, '商品套餐新增', 2025, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:package:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2028, '商品套餐修改', 2025, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:package:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2029, '商品套餐删除', 2025, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:package:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2030, '商品套餐导出', 2025, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:package:export', '#', 'admin', sysdate(), '', NULL, '');


--  订单 菜单 SQL
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2031, '支付订单', 2024, 1, 'order', 'payment/order/index', NULL, '', 1, 0, 'C', '0', '0', 'payment:order:list', '#', 'admin', sysdate(), '', NULL, '支付订单菜单');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2032, '支付订单查询', 2031, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:order:query', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2033, '支付订单新增', 2031, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:order:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2034, '支付订单修改', 2031, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:order:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2035, '支付订单删除', 2031, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:order:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2036, '支付订单导出', 2031, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'payment:order:export', '#', 'admin', sysdate(), '', NULL, '');


--  时长充值 菜单
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2037, '时长充值', 2024, 1, 'recharge', 'payment/recharge/index', NULL, '', 1, 0, 'C', '0', '0', NULL, '#', 'admin', sysdate(), '', NULL, '时长充值');

-- 注册用户角色 关联 时长充值菜单
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2024);
INSERT INTO `sys_role_menu`(`role_id`, `menu_id`) VALUES (3, 2037);



-- ----------------------------
-- 4. 设备时长表
-- ----------------------------
DROP TABLE IF EXISTS `device_hour`;
CREATE TABLE `device_hour` (
  `device_id` bigint(20) NOT NULL COMMENT '设备id',
  `device_name` varchar(200) NOT NULL COMMENT '设备名称', 
  `avail_free_hours` int(11) NOT NULL COMMENT '可用赠送时长',
  `avail_Pro_hours` int(11) NOT NULL COMMENT '可用Pro时长',
  `used_free_hours` int(11) NOT NULL COMMENT '已用赠送时长',
  `used_Pro_hours` int(11) NOT NULL COMMENT '已用Pro时长',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='设备时长表';


-- 添加一个参数: 设备管理-初始赠送时长
INSERT INTO `sys_config`(`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (100, '设备管理-初始赠送时长', 'device.hour.init_free_hours', '15', 'Y', 'admin', sysdate(), '', NULL, '初始赠送免费试用时长');


-- 用户设备时长 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('设备时长', '5', '1', 'hour', 'device/hour/index', 1, 0, 'C', '0', '0', 'device:hour:list', '#', 'admin', sysdate(), '', null, '设备时长菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户设备时长查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'device:hour:query',        '#', 'admin', sysdate(), '', null, '');

-- insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
-- values('用户设备时长新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'device:hour:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户设备时长修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'device:hour:edit',         '#', 'admin', sysdate(), '', null, '');

-- insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
-- values('用户设备时长删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'device:hour:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户设备时长导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'device:hour:export',       '#', 'admin', sysdate(), '', null, '');


