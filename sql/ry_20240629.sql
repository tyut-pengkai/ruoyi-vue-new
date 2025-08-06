

-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  user_id           bigint(20)      not null auto_increment    comment '用户ID',
  user_name         varchar(30)     not null                   comment '用户账号',
  nick_name         varchar(30)     not null                   comment '用户昵称',
  user_type         varchar(2)      default '00'               comment '用户类型（00系统用户）',
  email             varchar(50)     default ''                 comment '用户邮箱',
  phonenumber       varchar(11)     default ''                 comment '手机号码',
  sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
  avatar            varchar(256)    default ''                 comment '头像地址',
  password          varchar(100)    default ''                 comment '密码',
  status            char(1)         default '0'                comment '帐号状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  login_ip          varchar(128)    default ''                 comment '最后登录IP',
  login_date        datetime                                   comment '最后登录时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  version           bigint(20)      default 0                  comment  '版本号',
  primary key (user_id),
  key `idx_user_name` (`user_name`) USING BTREE,
  key `idx_phonenumber` (`phonenumber`) USING BTREE,
  key `idx_email` (`email`) USING BTREE
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values(1,  'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', null, '管理员', 0);


-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id              bigint(20)      not null auto_increment    comment '角色ID',
  role_name            varchar(30)     not null                   comment '角色名称',
  role_key             varchar(100)    not null                   comment '角色权限字符串',
  role_sort            int(4)          not null                   comment '显示顺序',
  store_id             bigint(20)      default null               comment '档口ID',
  status               char(1)         not null                   comment '角色状态（0正常 1停用）',
  del_flag             char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by            varchar(64)     default ''                 comment '创建者',
  create_time          datetime                                   comment '创建时间',
  update_by            varchar(64)     default ''                 comment '更新者',
  update_time          datetime                                   comment '更新时间',
  remark               varchar(500)    default null               comment '备注',
  version              bigint(20)      default 0                  comment  '版本号',
  primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values(1, '超级管理员',  'admin',  1, null, '0', '0', 'admin', sysdate(), '', null, '超级管理员', 0);
insert into sys_role values(2, '管理员',    'general_admin', 2, null, '0', '0', 'admin', sysdate(), '', null, '管理员', 0);
insert into sys_role values(3, '档口供应商',    'store', 3, null, '0', '0', 'admin', sysdate(), '', null, '档口供应商', 0);
insert into sys_role values(4, '电商卖家',    'seller', 4, null, '0', '0', 'admin', sysdate(), '', null, '电商卖家', 0);
insert into sys_role values(5, '代发',    'agent', 5, null, '0', '0', 'admin', sysdate(), '', null, '代发', 0);


-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
  menu_name         varchar(50)     not null                   comment '菜单名称',
  parent_id         bigint(20)      default 0                  comment '父菜单ID',
  order_num         int(4)          default 0                  comment '显示顺序',
  path              varchar(200)    default ''                 comment '路由地址',
  component         varchar(255)    default null               comment '组件路径',
  query             varchar(255)    default null               comment '路由参数',
  route_name        varchar(50)     default ''                 comment '路由名称',
  is_frame          int(1)          default 1                  comment '是否为外链（0是 1否）',
  is_cache          int(1)          default 0                  comment '是否缓存（0缓存 1不缓存）',
  menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
  visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
  status            char(1)         default 0                  comment '菜单状态（0正常 1停用）',
  perms             varchar(100)    default null               comment '权限标识',
  icon              varchar(100)    default '#'                comment '菜单图标',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- 删除标识&版本号
ALTER TABLE `sys_menu`  ADD COLUMN `del_flag` char(1) default '0' comment '删除标志（0代表存在 2代表删除）';
ALTER TABLE `sys_menu`  ADD COLUMN `version` bigint(20) default 0 comment '版本号';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '步橘网菜单', 0, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-14 22:54:40', 'admin', '2025-07-14 22:54:40', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1001, '商品管理', 1, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:01:20', 'admin', '2025-07-15 16:01:20', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1002, '商品列表', 1001, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:01:38', 'admin', '2025-07-15 16:01:38', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1003, '发布商品', 1001, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:01:50', 'admin', '2025-07-15 16:01:50', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1004, '打印条码', 1001, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:02:02', 'admin', '2025-07-15 16:02:02', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1005, '图片空间', 1001, 4, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:02:16', 'admin', '2025-07-15 16:02:16', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1006, '条码一键迁移', 1001, 5, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:02:31', 'admin', '2025-07-15 16:02:31', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1007, '进货车列表', 1001, 6, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:02:46', 'admin', '2025-07-15 16:02:46', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1008, '我的收藏', 1001, 7, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:02:55', 'admin', '2025-07-15 16:02:55', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1009, '推广营销', 1, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:03:11', 'admin', '2025-07-15 16:03:11', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1010, '推广订购', 1009, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:03:26', 'admin', '2025-07-15 16:03:26', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1011, '已购推广', 1009, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:03:35', 'admin', '2025-07-15 16:03:35', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1012, '代发管理', 1, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:03:52', 'admin', '2025-07-15 16:03:52', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1013, '档口代发', 1012, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:04:04', 'admin', '2025-07-15 16:04:04', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1014, '代发订单', 1012, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:04:15', 'admin', '2025-07-15 16:04:15', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1015, '销售出库', 1, 4, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:04:39', 'admin', '2025-07-15 16:04:39', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1016, '销售/退货', 1015, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:04:48', 'admin', '2025-07-15 16:04:48', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1017, '销售出库列表', 1015, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:04:59', 'admin', '2025-07-15 16:04:59', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1018, '入库管理', 1, 5, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:05:16', 'admin', '2025-07-15 16:05:16', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1019, '入库单列表', 1018, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:05:28', 'admin', '2025-07-15 16:05:28', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1020, '生产入库', 1018, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:05:39', 'admin', '2025-07-15 16:05:39', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1021, '其它入库', 1018, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:05:50', 'admin', '2025-07-15 16:05:50', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1022, '维修入库', 1018, 4, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:06:00', 'admin', '2025-07-15 16:06:00', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1023, '库存管理', 1, 6, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:06:15', 'admin', '2025-07-15 16:06:15', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1024, '库存查询', 1023, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:06:24', 'admin', '2025-07-15 16:06:24', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1025, '库存盘点', 1023, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:06:35', 'admin', '2025-07-15 16:06:35', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1026, '库存一键迁移', 1023, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:06:47', 'admin', '2025-07-15 16:06:47', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1027, '生产需求', 1, 7, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:07:05', 'admin', '2025-07-15 16:07:05', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1028, '提交生产需求', 1027, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:07:15', 'admin', '2025-07-15 16:07:15', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1029, '生产需求列表', 1027, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:07:24', 'admin', '2025-07-15 16:07:24', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1030, '生产模板管理', 1027, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:07:34', 'admin', '2025-07-15 16:07:34', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1031, '店铺管理', 1, 8, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:07:49', 'admin', '2025-07-15 16:07:49', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1032, '基本信息', 1031, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:07:59', 'admin', '2025-07-15 16:07:59', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1033, '认证信息', 1031, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:08:06', 'admin', '2025-07-15 16:08:06', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1034, '工厂管理', 1031, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:08:17', 'admin', '2025-07-15 16:08:17', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1035, '子账号管理', 1031, 4, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:08:28', 'admin', '2025-07-15 16:08:28', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1036, '子角色管理', 1031, 5, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:08:38', 'admin', '2025-07-15 16:08:38', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1037, '店铺装修', 1031, 6, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:08:49', 'admin', '2025-07-15 16:08:49', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1038, '我的关注', 1031, 7, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:09:16', 'admin', '2025-07-15 16:09:16', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1039, '客户管理', 1, 9, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:09:33', 'admin', '2025-07-15 16:09:33', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1040, '客户列表', 1039, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:09:55', 'admin', '2025-07-15 16:10:39', '', '0', 1);
INSERT INTO `sys_menu` VALUES (1041, '客户销售管理', 1039, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:10:32', 'admin', '2025-07-15 16:10:32', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1042, '资产管理', 1, 10, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:10:54', 'admin', '2025-07-15 16:10:54', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1043, '我的资产', 1042, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:11:08', 'admin', '2025-07-15 16:11:30', '', '0', 1);
INSERT INTO `sys_menu` VALUES (1044, '推广管理', 1, 11, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:12:20', 'admin', '2025-07-15 16:12:20', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1045, '投放管理', 1044, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:12:30', 'admin', '2025-07-15 16:12:30', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1046, '推广管理', 1044, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:12:44', 'admin', '2025-07-15 16:12:44', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1047, '推广图管理', 1044, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:12:54', 'admin', '2025-07-15 16:12:54', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1048, '档口管理', 1, 12, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:13:06', 'admin', '2025-07-15 16:13:06', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1049, '档口商品列表', 1048, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:13:28', 'admin', '2025-07-15 16:13:28', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1050, '档口入驻审核', 1048, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:13:43', 'admin', '2025-07-15 16:13:43', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1051, '档口列表', 1048, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:13:55', 'admin', '2025-07-15 16:13:55', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1052, '待介入代发单', 1048, 4, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:14:16', 'admin', '2025-07-15 16:14:16', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1053, '档口会员列表', 1048, 5, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:14:31', 'admin', '2025-07-15 16:14:31', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1054, '代发人员', 1, 13, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:14:54', 'admin', '2025-07-15 16:14:54', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1055, '代发入驻审核', 1054, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:15:11', 'admin', '2025-07-15 16:15:11', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1056, '代发人员列表', 1054, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:15:30', 'admin', '2025-07-15 16:15:30', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1057, '用户中心', 1, 14, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:15:58', 'admin', '2025-07-15 16:15:58', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1058, '账户与安全', 1057, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:16:09', 'admin', '2025-07-15 16:16:09', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1059, '系统消息', 1057, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:16:28', 'admin', '2025-07-15 16:16:28', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1060, '代发认证', 1057, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:16:42', 'admin', '2025-07-15 16:16:42', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1061, '系统设置', 1, 15, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:16:59', 'admin', '2025-07-15 16:16:59', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1062, '字典管理', 1061, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:17:12', 'admin', '2025-07-15 16:17:12', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1063, '账号管理', 1061, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:17:22', 'admin', '2025-07-15 16:17:22', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1064, '菜单管理', 1061, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:17:32', 'admin', '2025-07-15 16:17:32', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1065, '角色管理', 1061, 4, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:17:42', 'admin', '2025-07-15 16:17:42', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1066, '商品分类', 1061, 5, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:17:52', 'admin', '2025-07-15 16:17:52', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1067, '定时任务', 1061, 6, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:18:02', 'admin', '2025-07-15 16:18:02', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1068, '快递费管理', 1061, 6, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:18:14', 'admin', '2025-07-15 16:18:14', '', '0', 0);

-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
  user_id   bigint(20) not null comment '用户ID',
  role_id   bigint(20) not null comment '角色ID',
  store_id  bigint(20) default null comment '档口ID',
  primary key(user_id, role_id),
  key `idx_role_id` (`role_id`) USING BTREE
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values (1, 1, null);


-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
  role_id   bigint(20) not null comment '角色ID',
  menu_id   bigint(20) not null comment '菜单ID',
  primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1062);
INSERT INTO `sys_role_menu` VALUES (2, 1063);
INSERT INTO `sys_role_menu` VALUES (2, 1064);
INSERT INTO `sys_role_menu` VALUES (2, 1065);
INSERT INTO `sys_role_menu` VALUES (2, 1066);
INSERT INTO `sys_role_menu` VALUES (2, 1067);
INSERT INTO `sys_role_menu` VALUES (2, 1068);
INSERT INTO `sys_role_menu` VALUES (3, 1002);
INSERT INTO `sys_role_menu` VALUES (3, 1003);
INSERT INTO `sys_role_menu` VALUES (3, 1004);
INSERT INTO `sys_role_menu` VALUES (3, 1005);
INSERT INTO `sys_role_menu` VALUES (3, 1006);
INSERT INTO `sys_role_menu` VALUES (3, 1010);
INSERT INTO `sys_role_menu` VALUES (3, 1011);
INSERT INTO `sys_role_menu` VALUES (3, 1013);
INSERT INTO `sys_role_menu` VALUES (3, 1016);
INSERT INTO `sys_role_menu` VALUES (3, 1017);
INSERT INTO `sys_role_menu` VALUES (3, 1019);
INSERT INTO `sys_role_menu` VALUES (3, 1020);
INSERT INTO `sys_role_menu` VALUES (3, 1021);
INSERT INTO `sys_role_menu` VALUES (3, 1022);
INSERT INTO `sys_role_menu` VALUES (3, 1024);
INSERT INTO `sys_role_menu` VALUES (3, 1025);
INSERT INTO `sys_role_menu` VALUES (3, 1026);
INSERT INTO `sys_role_menu` VALUES (3, 1028);
INSERT INTO `sys_role_menu` VALUES (3, 1029);
INSERT INTO `sys_role_menu` VALUES (3, 1030);
INSERT INTO `sys_role_menu` VALUES (3, 1032);
INSERT INTO `sys_role_menu` VALUES (3, 1033);
INSERT INTO `sys_role_menu` VALUES (3, 1034);
INSERT INTO `sys_role_menu` VALUES (3, 1035);
INSERT INTO `sys_role_menu` VALUES (3, 1036);
INSERT INTO `sys_role_menu` VALUES (3, 1037);
INSERT INTO `sys_role_menu` VALUES (3, 1040);
INSERT INTO `sys_role_menu` VALUES (3, 1041);
INSERT INTO `sys_role_menu` VALUES (3, 1043);
INSERT INTO `sys_role_menu` VALUES (3, 1058);
INSERT INTO `sys_role_menu` VALUES (3, 1059);
INSERT INTO `sys_role_menu` VALUES (4, 1007);
INSERT INTO `sys_role_menu` VALUES (4, 1008);
INSERT INTO `sys_role_menu` VALUES (4, 1014);
INSERT INTO `sys_role_menu` VALUES (4, 1038);
INSERT INTO `sys_role_menu` VALUES (4, 1043);
INSERT INTO `sys_role_menu` VALUES (4, 1058);
INSERT INTO `sys_role_menu` VALUES (4, 1059);
INSERT INTO `sys_role_menu` VALUES (5, 1007);
INSERT INTO `sys_role_menu` VALUES (5, 1008);
INSERT INTO `sys_role_menu` VALUES (5, 1014);
INSERT INTO `sys_role_menu` VALUES (5, 1038);
INSERT INTO `sys_role_menu` VALUES (5, 1043);
INSERT INTO `sys_role_menu` VALUES (5, 1058);
INSERT INTO `sys_role_menu` VALUES (5, 1059);
INSERT INTO `sys_role_menu` VALUES (5, 1060);


-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log (
  oper_id           bigint(20)      not null auto_increment    comment '日志主键',
  title             varchar(50)     default ''                 comment '模块标题',
  business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
  method            varchar(200)    default ''                 comment '方法名称',
  request_method    varchar(10)     default ''                 comment '请求方式',
  operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
  oper_name         varchar(50)     default ''                 comment '操作人员',
  oper_url          varchar(255)    default ''                 comment '请求URL',
  oper_ip           varchar(128)    default ''                 comment '主机地址',
  oper_location     varchar(255)    default ''                 comment '操作地点',
  oper_param        varchar(2000)   default ''                 comment '请求参数',
  json_result       varchar(2000)   default ''                 comment '返回参数',
  status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
  error_msg         varchar(2000)   default ''                 comment '错误消息',
  oper_time         datetime                                   comment '操作时间',
  cost_time         bigint(20)      default 0                  comment '消耗时间',
  primary key (oper_id),
  key idx_sys_oper_log_bt (business_type),
  key idx_sys_oper_log_s  (status),
  key idx_sys_oper_log_ot (oper_time)
) engine=innodb auto_increment=100 comment = '操作日志记录';


-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
  dict_id          bigint(20)      not null auto_increment    comment '字典主键',
  dict_name        varchar(100)    default ''                 comment '字典名称',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  `version`        bigint UNSIGNED NULL DEFAULT NULL COMMENT '版本号',
  `del_flag`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_id),
  unique (dict_type)
) engine=innodb auto_increment=200 comment = '字典类型表';

INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', '0', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '登录状态列表');


-- ----------------------------
-- 12、字典数据表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
`dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
`dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
`dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
`dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
`status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
`create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
`create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
`update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
`remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
`version` bigint UNSIGNED NULL DEFAULT NULL COMMENT '版本号',
`del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 600 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '性别男', 0, '0');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '性别女', 0, '0');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '性别未知', 0, '0');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '显示菜单', 0, '0');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '隐藏菜单', 0, '0');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '正常状态', 0, '0');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '停用状态', 0, '0');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '正常状态', 0, '0');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '停用状态', 0, '0');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '默认分组', 0, '0');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '系统分组', 0, '0');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '系统默认是', 0, '0');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '系统默认否', 0, '0');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '通知', 0, '0');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '公告', 0, '0');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '正常状态', 0, '0');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '关闭状态', 0, '0');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '其他操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '新增操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '修改操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '删除操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '授权操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '导出操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '导入操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '强退操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '生成操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '清空操作', 0, '0');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '正常状态', 0, '0');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '停用状态', 0, '0');


-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  config_id         int(5)          not null auto_increment    comment '参数主键',
  config_name       varchar(100)    default ''                 comment '参数名称',
  config_key        varchar(100)    default ''                 comment '参数键名',
  config_value      varchar(500)    default ''                 comment '参数键值',
  config_type       char(1)         default 'N'                comment '系统内置（Y是 N否）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

insert into sys_config values(1, '主框架页-默认皮肤样式名称',     'sys.index.skinName',            'skin-blue',     'Y', 'admin', sysdate(), '', null, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow' );
insert into sys_config values(2, '用户管理-账号初始密码',         'sys.user.initPassword',         '123456',        'Y', 'admin', sysdate(), '', null, '初始化密码 123456' );
insert into sys_config values(3, '主框架页-侧边栏主题',           'sys.index.sideTheme',           'theme-dark',    'Y', 'admin', sysdate(), '', null, '深色主题theme-dark，浅色主题theme-light' );
insert into sys_config values(4, '账号自助-验证码开关',           'sys.account.captchaEnabled',    'true',          'Y', 'admin', sysdate(), '', null, '是否开启验证码功能（true开启，false关闭）');
insert into sys_config values(5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser',      'false',         'Y', 'admin', sysdate(), '', null, '是否开启注册用户功能（true开启，false关闭）');
insert into sys_config values(6, '用户登录-黑名单列表',           'sys.login.blackIPList',         '',              'Y', 'admin', sysdate(), '', null, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');


-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
  info_id        bigint(20)     not null auto_increment   comment '访问ID',
  user_name      varchar(50)    default ''                comment '用户账号',
  ipaddr         varchar(128)   default ''                comment '登录IP地址',
  login_location varchar(255)   default ''                comment '登录地点',
  browser        varchar(50)    default ''                comment '浏览器类型',
  os             varchar(50)    default ''                comment '操作系统',
  status         char(1)        default '0'               comment '登录状态（0成功 1失败）',
  msg            varchar(255)   default ''                comment '提示消息',
  login_time     datetime                                 comment '访问时间',
  primary key (info_id),
  key idx_sys_logininfor_s  (status),
  key idx_sys_logininfor_lt (login_time)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 15、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  job_id              bigint(20)    not null auto_increment    comment '任务ID',
  job_name            varchar(64)   default ''                 comment '任务名称',
  job_group           varchar(64)   default 'DEFAULT'          comment '任务组名',
  invoke_target       varchar(500)  not null                   comment '调用目标字符串',
  cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
  misfire_policy      varchar(20)   default '3'                comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent          char(1)       default '1'                comment '是否并发执行（0允许 1禁止）',
  status              char(1)       default '0'                comment '状态（0正常 1暂停）',
  `version`           bigint        UNSIGNED NOT NULL          COMMENT '版本号',
  `del_flag`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  create_by           varchar(64)   default ''                 comment '创建者',
  create_time         datetime                                 comment '创建时间',
  update_by           varchar(64)   default ''                 comment '更新者',
  update_time         datetime                                 comment '更新时间',
  remark              varchar(500)  default ''                 comment '备注信息',
  primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';


-- ----------------------------
-- 16、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  job_log_id          bigint(20)     not null auto_increment    comment '任务日志ID',
  job_name            varchar(64)    not null                   comment '任务名称',
  job_group           varchar(64)    not null                   comment '任务组名',
  invoke_target       varchar(500)   not null                   comment '调用目标字符串',
  job_message         varchar(500)                              comment '日志信息',
  status              char(1)        default '0'                comment '执行状态（0正常 1失败）',
  exception_info      varchar(2000)  default ''                 comment '异常信息',
  create_time         datetime                                  comment '创建时间',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';


-- ----------------------------
-- 17、通知公告表
-- ----------------------------
drop table if exists notice;
create table notice (
  id                int(4)          not null  auto_increment   comment '公告ID',
  notice_title      varchar(50)     not null                   comment '公告标题',
  notice_type       int(1)      not null                comment '公告类型（1通知 2公告）',
  owner_type        char(1)         not null                   comment '公告拥有者（1系统 2档口）',
  notice_content    longblob        default null               comment '公告内容',
  effect_start      datetime                                   comment '公告生效时间(yyyy-MM-dd HH:mm)',
  effect_end        datetime                                   comment '公告失效时间(yyyy-MM-dd HH:mm)',
  perpetuity        tinyint(1)                                 comment '是否永久生效',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (id)
) engine=innodb auto_increment=10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------


-- ----------------------------
-- 18、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  table_id          bigint(20)      not null auto_increment    comment '编号',
  table_name        varchar(200)    default ''                 comment '表名称',
  table_comment     varchar(500)    default ''                 comment '表描述',
  sub_table_name    varchar(64)     default null               comment '关联子表的表名',
  sub_table_fk_name varchar(64)     default null               comment '子表关联的外键名',
  class_name        varchar(100)    default ''                 comment '实体类名称',
  tpl_category      varchar(200)    default 'crud'             comment '使用的模板（crud单表操作 tree树表操作）',
  tpl_web_type      varchar(30)     default ''                 comment '前端模板类型（element-ui模版 element-plus模版）',
  package_name      varchar(100)                               comment '生成包路径',
  module_name       varchar(30)                                comment '生成模块名',
  business_name     varchar(30)                                comment '生成业务名',
  function_name     varchar(50)                                comment '生成功能名',
  function_author   varchar(50)                                comment '生成功能作者',
  gen_type          char(1)         default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path          varchar(200)    default '/'                comment '生成路径（不填默认项目路径）',
  options           varchar(1000)                              comment '其它生成选项',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 19、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  column_id         bigint(20)      not null auto_increment    comment '编号',
  table_id          bigint(20)                                 comment '归属表编号',
  column_name       varchar(200)                               comment '列名称',
  column_comment    varchar(500)                               comment '列描述',
  column_type       varchar(100)                               comment '列类型',
  java_type         varchar(500)                               comment 'JAVA类型',
  java_field        varchar(200)                               comment 'JAVA字段名',
  is_pk             char(1)                                    comment '是否主键（1是）',
  is_increment      char(1)                                    comment '是否自增（1是）',
  is_required       char(1)                                    comment '是否必填（1是）',
  is_insert         char(1)                                    comment '是否为插入字段（1是）',
  is_edit           char(1)                                    comment '是否编辑字段（1是）',
  is_list           char(1)                                    comment '是否列表字段（1是）',
  is_query          char(1)                                    comment '是否查询字段（1是）',
  query_type        varchar(200)    default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         varchar(200)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         varchar(200)    default ''                 comment '字典类型',
  sort              int                                        comment '排序',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';

DROP TABLE IF EXISTS `store_order`;
CREATE TABLE `store_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
	`store_id` bigint(20) NOT NULL COMMENT '档口ID',
  `order_user_id` bigint(20) NOT NULL COMMENT '下单用户ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
	`order_type` tinyint(4) NOT NULL COMMENT '订单类型[1:销售订单 2:退货订单]',
	`order_status` tinyint(4) NOT NULL COMMENT '订单状态（1开头为销售订单状态，2开头为退货订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]',
	`pay_status` tinyint(4) NOT NULL COMMENT '支付状态[1:初始 2:支付中 3:已支付]',
	`pay_channel` tinyint(4) NOT NULL COMMENT '支付渠道[1:支付宝]',
	`pay_trade_no` varchar(255) DEFAULT NULL COMMENT '支付交易号',
	`order_remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
	`goods_quantity` int(11) NOT NULL COMMENT '商品数量',
	`goods_amount` decimal(18,2) NOT NULL COMMENT '商品金额',
	`express_fee` decimal(18,2) NOT NULL COMMENT '快递费',
	`total_amount` decimal(18,2) NOT NULL COMMENT '总金额（商品金额+快递费）',
	`real_total_amount` decimal(18,2) DEFAULT NULL COMMENT '实际总金额（总金额-支付渠道服务费）',
	`origin_order_id` bigint(20) DEFAULT NULL COMMENT '退货原订单ID',
	`refund_reason_code` varchar(255) DEFAULT NULL COMMENT '退货原因',
	`refund_reject_reason` varchar(255) DEFAULT NULL COMMENT '退货拒绝原因',
	`express_id` bigint(20) DEFAULT NULL COMMENT '物流ID',
	`origin_contact_name` varchar(32) DEFAULT NULL COMMENT '发货人-名称',
	`origin_contact_phone_number` varchar(32) DEFAULT NULL COMMENT '发货人-电话',
	`origin_province_code` varchar(8) DEFAULT NULL COMMENT '发货人-省编码',
	`origin_city_code` varchar(8) DEFAULT NULL COMMENT '发货人-市编码',
	`origin_county_code` varchar(8) DEFAULT NULL COMMENT '发货人-区县编码',
	`origin_detail_address` varchar(255) DEFAULT NULL COMMENT '发货人-详细地址',
	`destination_contact_name` varchar(32) DEFAULT NULL COMMENT '收货人-名称',
	`destination_contact_phone_number` varchar(32) DEFAULT NULL COMMENT '收货人-电话',
	`destination_province_code` varchar(8) DEFAULT NULL COMMENT '收货人-省编码',
	`destination_city_code` varchar(8) DEFAULT NULL COMMENT '收货人-市编码',
	`destination_county_code` varchar(8) DEFAULT NULL COMMENT '收货人-区县编码',
	`destination_detail_address` varchar(255) DEFAULT NULL COMMENT '收货人-详细地址',
	`delivery_type` tinyint(4) DEFAULT NULL COMMENT '发货方式[1:货其再发 2:有货先发]',
	`delivery_end_time` datetime DEFAULT NULL COMMENT '最晚发货时间',
	`auto_end_time` datetime DEFAULT NULL COMMENT '自动完成时间',
	`voucher_date` date DEFAULT NULL COMMENT '凭证日期',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
	`version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`),
	UNIQUE KEY `uk_order_no` (`order_no`) USING BTREE,
	KEY `idx_sid_ot` (`store_id`,`order_type`) USING BTREE,
	KEY `idx_origin_order_id` (`origin_order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代发订单';

ALTER TABLE `store_order`
MODIFY COLUMN `refund_reject_reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货拒绝原因' AFTER `refund_reason_code`,
ADD COLUMN `platform_involve_reason` varchar(512) NULL COMMENT '平台介入原因' AFTER `refund_reject_reason`,
ADD COLUMN `platform_involve_result` varchar(512) NULL COMMENT '平台介入结果' AFTER `platform_involve_reason`;

DROP TABLE IF EXISTS `store_order_detail`;
CREATE TABLE `store_order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单明细ID',
	`store_order_id` bigint(20) NOT NULL COMMENT '订单ID',
	`store_prod_color_size_id` bigint(20) NOT NULL COMMENT '商品颜色尺码ID',
	`store_prod_id` bigint(20) NOT NULL COMMENT '商品ID',
	`prod_name` varchar(64) DEFAULT NULL COMMENT '商品名称',
	`prod_art_num` varchar(64) DEFAULT NULL COMMENT '商品货号',
	`prod_title` varchar(64) DEFAULT NULL COMMENT '商品标题',
	`store_color_id` bigint(20) DEFAULT NULL COMMENT '档口颜色ID',
	`color_name` varchar(64) DEFAULT NULL COMMENT '颜色名称',
	`size` tinyint(4) DEFAULT NULL COMMENT '商品尺码',
	`detail_status` tinyint(4) NOT NULL COMMENT '订单明细状态（同订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]',
	`pay_status` tinyint(4) NOT NULL COMMENT '支付状态[1:初始 2:支付中 3:已支付]',
	`express_id` bigint(20) DEFAULT NULL COMMENT '物流ID',
	`express_type` tinyint(4) DEFAULT NULL COMMENT '物流类型[1:平台物流 2:档口物流]',
	`express_status` tinyint(4) NOT NULL COMMENT '物流状态[1:初始 2:下单中 3:已下单 4:取消中 5:已揽件 6:拦截中 99:已结束]',
	`express_req_no` varchar(32) DEFAULT NULL COMMENT '物流请求单号',
	`express_waybill_no` varchar(255) DEFAULT NULL COMMENT '物流运单号（快递单号），档口/用户自己填写时可能存在多个，使用“,”分割',
	`goods_price` decimal(18,2) NOT NULL COMMENT '商品单价',
	`goods_quantity` int(11) NOT NULL COMMENT '商品数量',
	`goods_amount` decimal(18,2) NOT NULL COMMENT '商品金额（商品单价*商品数量）',
	`express_fee` decimal(18,2) NOT NULL COMMENT '快递费',
	`total_amount` decimal(18,2) NOT NULL COMMENT '总金额（商品金额+快递费）',
	`real_total_amount` decimal(18,2) DEFAULT NULL COMMENT '实际总金额（总金额-支付渠道服务费）',
	`origin_order_detail_id` bigint(20) DEFAULT NULL COMMENT '退货原订单明细ID',
	`refund_reason_code` varchar(255) DEFAULT NULL COMMENT '退货原因',
	`refund_reject_reason` varchar(255) DEFAULT NULL COMMENT '退货拒绝原因',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
	`version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`),
	KEY `idx_soid_spcsid` (`store_order_id`,`store_prod_color_size_id`) USING BTREE,
	KEY `idx_origin_order_detail_id` (`origin_order_detail_id`) USING BTREE,
	KEY `idx_eid_ewno` (`express_id`,`express_waybill_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代发订单明细';

DROP TABLE IF EXISTS `store_order_operation_record`;
CREATE TABLE `store_order_operation_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '代发订单操作记录ID',
	`target_id` bigint(20) NOT NULL COMMENT '订单ID/订单明细ID，根据类型确定',
	`target_type` tinyint(4) NOT NULL COMMENT '类型[1:订单 2:订单明细]',
	`action` tinyint(4) NOT NULL COMMENT '节点事件[1:下单 2:支付 3:取消 4:发货 5:完成 6:申请售后 7:寄回 8:售后拒绝 9:平台介入 10:售后完成]',
	`operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
	`operation_time` datetime DEFAULT NULL COMMENT '操作时间',
	`remark` varchar(255) DEFAULT NULL COMMENT '备注',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
	KEY `idx_tid_ttype` (`target_id`,`target_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代发订单操作记录';

DROP TABLE IF EXISTS `express_track_record`;
CREATE TABLE `express_track_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物流轨迹记录ID',
	`express_waybill_no` varchar(255) NOT NULL COMMENT '物流运单号',
	`express_id` bigint(20) DEFAULT NULL COMMENT '物流ID',
	`sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
	`action` varchar(128) DEFAULT NULL COMMENT '节点事件',
	`description` varchar(5000) DEFAULT NULL COMMENT '描述',
	`remark` varchar(5000) DEFAULT NULL COMMENT '备注',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
	KEY `idx_express_waybill_no` (`express_waybill_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流轨迹记录';

DROP TABLE IF EXISTS `express`;
CREATE TABLE `express` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物流ID',
	`express_code` varchar(16) NOT NULL COMMENT '物流编码',
	`express_name` varchar(32) NOT NULL COMMENT '物流名称',
	`system_deliver_access` bit(1) NOT NULL DEFAULT 0 COMMENT '系统发货可选',
	`store_deliver_access` bit(1) NOT NULL DEFAULT 0 COMMENT '档口发货可选',
	`user_refund_access` bit(1) NOT NULL DEFAULT 0 COMMENT '用户退货可选',
	`system_config` varchar(5000) DEFAULT NULL COMMENT '系统配置',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
	`version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`),
	UNIQUE KEY `uk_express_code` (`express_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流信息';

DROP TABLE IF EXISTS `express_region`;
CREATE TABLE `express_region` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`region_code` varchar(8) NOT NULL COMMENT '地区编码，基于行政区划代码做扩展，唯一约束',
  `region_name` varchar(32) NOT NULL COMMENT '地区名称',
  `region_level` tinyint NOT NULL COMMENT '地区级别[1:省 2:市 3:区县]',
  `parent_region_code` varchar(8) DEFAULT NULL COMMENT '上级地区编码，没有上级的默认空',
  `parent_region_name` varchar(32) DEFAULT NULL COMMENT '上级地区名称，冗余',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
	`version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`),
	UNIQUE KEY `uk_region_code` (`region_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流行政区划';

DROP TABLE IF EXISTS `express_fee_config`;
CREATE TABLE `express_fee_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`express_id` bigint(20) NOT NULL COMMENT '物流ID',
	`region_code` varchar(8) NOT NULL COMMENT '地区编码，基于行政区划代码做扩展，唯一约束',
  `parent_region_code` varchar(8) DEFAULT NULL COMMENT '上级地区编码，没有上级的默认空',
	`first_item_amount` decimal(18,2) NOT NULL COMMENT '首件运费',
	`next_item_amount` decimal(18,2) NOT NULL COMMENT '续费',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
	`version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`),
	KEY `idx_express_id` (`express_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流费用配置';

DROP TABLE IF EXISTS `express_shipping_label` ;
CREATE TABLE `express_shipping_label` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`express_waybill_no` varchar(64) NOT NULL COMMENT '运单号',
	`express_id` bigint(20) DEFAULT NULL COMMENT '物流ID',
	`vas_type` varchar(32) DEFAULT NULL COMMENT '服务类型',
	`mark` varchar(32) DEFAULT NULL COMMENT '转运代码',
	`short_mark` varchar(32) DEFAULT NULL COMMENT '短转运代码',
	`bag_addr` varchar(32) DEFAULT NULL COMMENT '集包地',
	`last_print_time` datetime DEFAULT NULL COMMENT '最后打印时间',
	`print_count` int(11) DEFAULT NULL COMMENT '打印次数',
	`goods_info` varchar(512) DEFAULT NULL COMMENT '商品信息',
	`remark` varchar(512) DEFAULT NULL COMMENT '备注',
	`origin_contact_name` varchar(32) DEFAULT NULL COMMENT '发货人-名称',
	`origin_contact_phone_number` varchar(32) DEFAULT NULL COMMENT '发货人-电话',
	`origin_province_name` varchar(8) DEFAULT NULL COMMENT '发货人-省',
	`origin_city_name` varchar(8) DEFAULT NULL COMMENT '发货人-市',
	`origin_county_name` varchar(8) DEFAULT NULL COMMENT '发货人-区县',
	`origin_detail_address` varchar(255) DEFAULT NULL COMMENT '发货人-详细地址',
	`destination_contact_name` varchar(32) DEFAULT NULL COMMENT '收货人-名称',
	`destination_contact_phone_number` varchar(32) DEFAULT NULL COMMENT '收货人-电话',
	`destination_province_name` varchar(8) DEFAULT NULL COMMENT '收货人-省',
	`destination_city_name` varchar(8) DEFAULT NULL COMMENT '收货人-市',
	`destination_county_name` varchar(8) DEFAULT NULL COMMENT '收货人-区县',
	`destination_detail_address` varchar(255) DEFAULT NULL COMMENT '收货人-详细地址',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
	UNIQUE KEY `uk_express_waybill_no` (`express_waybill_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='快递面单';

DROP TABLE IF EXISTS `internal_account`;
CREATE TABLE `internal_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '内部账户ID',
	`owner_type` tinyint(4) NOT NULL COMMENT '归属[1:平台 2:档口 3:用户]',
	`owner_id` bigint(20) NOT NULL COMMENT '归属ID（平台=-1，档口=store_id）',
	`account_status` tinyint(4) NOT NULL COMMENT '账户状态[1:正常 2:冻结]',
	`transaction_password` varchar(128) DEFAULT NULL COMMENT '交易密码',
	`phone_number` varchar(32) DEFAULT NULL COMMENT '归属人手机号',
	`balance` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `usable_balance` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '可用余额',
  `payment_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '支付中金额',
	`remark` varchar(255) DEFAULT NULL COMMENT '备注',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
	UNIQUE KEY `uk_oid_otype` (`owner_id`,`owner_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COMMENT='内部账户';

DROP TABLE IF EXISTS `internal_account_trans_detail`;
CREATE TABLE `internal_account_trans_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '内部账户交易明细ID',
  `internal_account_id` bigint(20) NOT NULL COMMENT '内部账户ID',
  `src_bill_id` bigint(20) DEFAULT NULL COMMENT '来源单据ID',
  `src_bill_type` tinyint(4) DEFAULT NULL COMMENT '来源单据类型[1:收款 2:付款 3:转移]',
  `loan_direction` tinyint(4) NOT NULL COMMENT '借贷方向[1:借(D) 2:贷(C)]',
  `trans_amount` decimal(18,2) NOT NULL COMMENT '交易金额',
  `trans_time` datetime NOT NULL COMMENT '交易时间',
  `handler_id` bigint(20) DEFAULT NULL COMMENT '经办人ID',
  `entry_status` tinyint(4) NOT NULL COMMENT '入账状态 [1:已入账 2:未入账]',
  `entry_executed` tinyint(4) NOT NULL COMMENT '入账执行标识[1:已执行 2:未执行]',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
	`version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_internal_account_id` (`internal_account_id`) USING BTREE,
  KEY `idx_sbid_sbtype` (`src_bill_id`,`src_bill_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内部账户交易明细';

DROP TABLE IF EXISTS `external_account`;
CREATE TABLE `external_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '外部账户ID',
	`owner_type` tinyint(4) NOT NULL COMMENT '归属[1:平台 2:档口 3:用户]',
	`owner_id` bigint(20) NOT NULL COMMENT '归属ID（平台=-1，档口=store_id，用户=user_id）',
	`account_status` tinyint(4) NOT NULL COMMENT '账户状态[1:正常 2:冻结]',
	`account_type` tinyint(4) NOT NULL COMMENT '账户类型[1:支付宝账户]',
	`account_owner_number` varchar(32) DEFAULT NULL COMMENT '归属人实际账户',
	`account_owner_name` varchar(32) DEFAULT NULL COMMENT '归属人真实姓名',
	`account_owner_phone_number` varchar(32) DEFAULT NULL COMMENT '归属人手机号',
	`account_auth_access` bit(1) NOT NULL DEFAULT 0 COMMENT '归属人认证通过',
	`remark` varchar(255) DEFAULT NULL COMMENT '备注',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
	UNIQUE KEY `uk_oid_otype_atype` (`owner_id`,`owner_type`,`account_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COMMENT='外部账户';

DROP TABLE IF EXISTS `external_account_trans_detail`;
CREATE TABLE `external_account_trans_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '外部账户交易明细ID',
  `external_account_id` bigint(20) NOT NULL COMMENT '外部账户ID',
  `src_bill_id` bigint(20) DEFAULT NULL COMMENT '来源单据ID',
  `src_bill_type` tinyint(4) DEFAULT NULL COMMENT '来源单据类型[2:付款]',
  `loan_direction` tinyint(4) NOT NULL COMMENT '借贷方向[1:借(D) 2:贷(C)]',
  `trans_amount` decimal(18,2) NOT NULL COMMENT '交易金额',
  `trans_time` datetime NOT NULL COMMENT '交易时间',
  `handler_id` bigint(20) DEFAULT NULL COMMENT '经办人ID',
  `entry_status` tinyint(4) NOT NULL COMMENT '入账状态 [1:已入账 2:未入账]',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
	`version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_external_account_id` (`external_account_id`) USING BTREE,
  KEY `idx_sbid_sbtype` (`src_bill_id`,`src_bill_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外部账户交易明细';

DROP TABLE IF EXISTS `finance_bill`;
CREATE TABLE `finance_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付单据ID',
	`bill_no` varchar(32) NOT NULL COMMENT '单号',
	`bill_type` tinyint(4) NOT NULL COMMENT '单据类型[1:收款 2:付款 3:转移]',
	`bill_status` tinyint(4) NOT NULL COMMENT '单据状态[1:初始 2:执行中 3:执行成功 4:执行失败]',
	`src_type` tinyint(4) DEFAULT NULL COMMENT '来源类型[1:代发订单支付 2:代发订单完成 3:提现]',
	`src_id` bigint(20) DEFAULT NULL COMMENT '来源ID',
	`rel_type` tinyint(4) DEFAULT NULL COMMENT '关联类型[1:代发订单]',
	`rel_id` bigint(20) DEFAULT NULL COMMENT '关联ID',
	`business_unique_key` varchar(64) DEFAULT NULL COMMENT '业务唯一键',
	`input_internal_account_id` bigint(20) DEFAULT NULL COMMENT '收入内部账户ID',
	`output_internal_account_id` bigint(20) DEFAULT NULL COMMENT '支出内部账户ID',
	`input_external_account_id` bigint(20) DEFAULT NULL COMMENT '收入外部账户ID',
	`output_external_account_id` bigint(20) DEFAULT NULL COMMENT '支出外部账户ID',
	`business_amount` decimal(18,2) NOT NULL COMMENT '业务金额',
	`trans_amount` decimal(18,2) NOT NULL COMMENT '交易金额',
	`remark` varchar(255) DEFAULT NULL COMMENT '备注',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
	`version` int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`),
	UNIQUE KEY `uk_bill_no` (`bill_no`) USING BTREE,
	UNIQUE KEY `uk_business_unique_key` (`business_unique_key`) USING BTREE,
	KEY `idx_sid_stype` (`src_id`,`src_type`) USING BTREE,
	KEY `idx_rid_rtype` (`rel_id`,`rel_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务单据';

DROP TABLE IF EXISTS `finance_bill_detail`;
CREATE TABLE `finance_bill_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付单据明细ID',
	`finance_bill_id` bigint(20) DEFAULT NULL COMMENT '支付单据ID',
	`rel_type` tinyint(4) DEFAULT NULL COMMENT '关联类型[1:代发订单明细]',
	`rel_id` bigint(20) DEFAULT NULL COMMENT '关联ID',
	`business_amount` decimal(18,2) NOT NULL COMMENT '业务金额',
	`trans_amount` decimal(18,2) NOT NULL COMMENT '交易金额',
	`remark` varchar(255) DEFAULT NULL COMMENT '备注',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
	KEY `idx_finance_bill_id` (`finance_bill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务单据明细';

DROP TABLE IF EXISTS `alipay_callback`;
CREATE TABLE `alipay_callback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`biz_type` tinyint(4) DEFAULT NULL COMMENT '业务类型',
	`notify_type` varchar(64) DEFAULT NULL COMMENT '通知的类型',
	`notify_id` varchar(64) DEFAULT NULL COMMENT '通知校验 ID',
	`app_id` varchar(64) DEFAULT NULL COMMENT '支付宝分配给开发者的应用 ID',
	`charset` varchar(128) DEFAULT NULL COMMENT '编码格式，如 utf-8、gbk、gb2312 等',
	`version` varchar(32) DEFAULT NULL COMMENT '调用的接口版本，固定为：1.0',
	`sign_type` varchar(10) DEFAULT NULL COMMENT '商家生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2',
	`sign` varchar(3) DEFAULT NULL COMMENT '详情可查看 异步返回结果的验签',
	`trade_no` varchar(64) DEFAULT NULL COMMENT '支付宝交易凭证号',
	`out_trade_no` varchar(512) DEFAULT NULL COMMENT '原支付请求的商户订单号',
	`out_biz_no` varchar(64) DEFAULT NULL COMMENT '商户业务 ID',
	`buyer_id` varchar(128) DEFAULT NULL COMMENT '买家支付宝用户号',
	`buyer_logon_id` varchar(100) DEFAULT NULL COMMENT '买家支付宝账号',
	`seller_id` varchar(30) DEFAULT NULL COMMENT '卖家支付宝用户号',
	`seller_email` varchar(100) DEFAULT NULL COMMENT '卖家支付宝账号',
	`trade_status` varchar(32) DEFAULT NULL COMMENT '交易目前所处的状态[WAIT_BUYER_PAY	交易创建，等待买家付款 TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款 TRADE_SUCCESS	交易支付成功 TRADE_FINISHED	交易结束，不可退款]',
	`total_amount` decimal(18,2) DEFAULT NULL COMMENT '本次交易支付的订单金额，单位为人民币（元）',
	`receipt_amount` decimal(18,2) DEFAULT NULL COMMENT '商家在收益中实际收到的款项，单位人民币（元）',
	`invoice_amount` decimal(18,2) DEFAULT NULL COMMENT '用户在交易中支付的可开发票的金额',
	`buyer_pay_amount` decimal(18,2) DEFAULT NULL COMMENT '用户在交易中支付的金额',
	`point_amount` decimal(18,2) DEFAULT NULL COMMENT '使用集分宝支付的金额',
	`refund_fee` decimal(18,2) DEFAULT NULL COMMENT '退款通知中，返回总退款金额，单位为人民币（元），支持两位小数',
	`subject` varchar(256) DEFAULT NULL COMMENT '商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来',
	`body` varchar(400) DEFAULT NULL COMMENT '订单的备注、描述、明细等。对应请求时的 body 参数，原样通知回来',
	`gmt_create` varchar(32) DEFAULT NULL COMMENT '该笔交易创建的时间',
	`gmt_payment` varchar(32) DEFAULT NULL COMMENT '该笔交易 的买家付款时间',
	`gmt_refund` varchar(32) DEFAULT NULL COMMENT '该笔交易的退款时间',
	`gmt_close` varchar(32) DEFAULT NULL COMMENT '该笔交易结束时间',
	`fund_bill_list` varchar(512) DEFAULT NULL COMMENT '支付成功的各个渠道金额信息',
	`passback_params` varchar(512) DEFAULT NULL COMMENT '公共回传参数，如果请求时传递了该参数，则返回给商家时会在异步通知时将该参数原样返回',
	`voucher_detail_list` varchar(512) DEFAULT NULL COMMENT '本交易支付时所有优惠券信息',
	`process_status` tinyint(4) NOT NULL COMMENT '回调处理状态[1:初始 2:处理中 3:处理成功 4:处理失败]',
	`del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
	`create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
	UNIQUE KEY `uk_notify_id` (`notify_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='阿里支付回调信息';

-- 上线前补全
INSERT INTO internal_account ( `id`, `owner_type`, `owner_id`, `account_status`, `transaction_password`, `balance`, `usable_balance`, `payment_amount`, `remark`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1, 1, -1, 1, NULL, 0.00, 0.00, 0.00, '平台账户', '0', 'SYSTEM', NOW(), 'SYSTEM', NOW());

INSERT INTO external_account ( `id`, `owner_type`, `owner_id`, `account_status`, `account_type`, `account_owner_number`, `account_owner_name`, `account_owner_phone_number`, `account_auth_access`, `remark`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1, 1, -1, 1, 1, '', '', '', 1, '平台支付宝账户', '0', 'SYSTEM', NOW(), 'SYSTEM', NOW());

-- 快递?
INSERT INTO `express`(`id`, `express_code`, `express_name`, `system_deliver_access`, `store_deliver_access`, `user_refund_access`, `system_config`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`) VALUES (1, 'ZTO', '中通', b'1', b'1', b'1', NULL, '0', '', NULL, '', NULL, 0);
INSERT INTO `express`(`id`, `express_code`, `express_name`, `system_deliver_access`, `store_deliver_access`, `user_refund_access`, `system_config`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `version`) VALUES (2, 'YTO', '圆通', b'1', b'1', b'1', NULL, '0', '', NULL, '', NULL, 0);


DROP TABLE IF EXISTS `voucher_sequence`;
CREATE TABLE `voucher_sequence`  (
     `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '单据编号ID',
     `store_id` bigint UNSIGNED NOT NULL COMMENT '档口ID',
     `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据类型',
     `date_format` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据格式化类型',
     `next_sequence` int NOT NULL COMMENT '下一个单据编号',
     `prefix` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据编号前缀',
     `sequence_format` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '格式化类型',
     `version` bigint UNSIGNED NOT NULL COMMENT '版本号',
     `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
     `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
     `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
     `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
     `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统code生成规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of voucher_sequence
-- ----------------------------
INSERT INTO `voucher_sequence` VALUES (1, 1, 'STORE_SALE', 'yyyy-MM-dd', 1, 'SD', '%04d', 0, '0', '', '2025-03-30 16:09:23', '', '2025-03-30 16:09:26');
INSERT INTO `voucher_sequence` VALUES (2, 1, 'STORAGE', 'yyyy-MM-dd', 1, 'RK', '%04d', 0, '0', '', '2025-03-30 16:09:23', '', '2025-03-30 16:09:26');
INSERT INTO `voucher_sequence` VALUES (3, 1, 'DEMAND', 'yyyy-MM-dd', 1, 'XQ', '%04d', 0, '0', '', '2025-03-30 16:09:23', '', '2025-03-30 16:09:26');
INSERT INTO `voucher_sequence` VALUES (4, 1, 'STORE_ORDER', 'yyyy-MM-dd', 1, 'DF', '%04d', 0, '0', '', '2025-03-30 16:09:23', '', '2025-05-15 01:54:15');

SET FOREIGN_KEY_CHECKS = 1;


DROP TABLE IF EXISTS `sys_product_category`;
CREATE TABLE `sys_product_category`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `parent_id` bigint UNSIGNED NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int UNSIGNED NULL DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类ICON路径',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `version` bigint UNSIGNED NOT NULL COMMENT '版本号',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_product_category
-- ----------------------------
INSERT INTO `sys_product_category` VALUES (1, '鞋库通商品分类', 0, 3, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '最顶层商品分类', 2, '0', 'admin', '2025-04-15 19:42:19', '', '2025-04-15 19:42:23');
INSERT INTO `sys_product_category` VALUES (2, '靴子', 1, 2, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:46:49', '', '2025-04-15 19:46:49');
INSERT INTO `sys_product_category` VALUES (3, '单鞋', 1, 1, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:47:06', '', '2025-04-15 19:47:06');
INSERT INTO `sys_product_category` VALUES (4, '凉鞋', 1, 3, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:47:23', '', '2025-04-15 19:47:23');
INSERT INTO `sys_product_category` VALUES (5, '休闲鞋', 1, 4, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:47:33', '', '2025-04-15 19:47:33');
INSERT INTO `sys_product_category` VALUES (6, '雪地靴', 1, 11, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:47:44', '', '2025-04-15 19:47:44');
INSERT INTO `sys_product_category` VALUES (7, '帆布鞋', 1, 11, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:48:20', '', '2025-04-15 19:48:20');
INSERT INTO `sys_product_category` VALUES (8, '拖鞋', 1, 5, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:48:26', '', '2025-04-15 19:48:26');
INSERT INTO `sys_product_category` VALUES (9, '高帮鞋', 1, 11, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:48:32', '', '2025-04-15 19:48:32');
INSERT INTO `sys_product_category` VALUES (10, '汉服鞋', 1, 11, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:48:39', '', '2025-04-15 19:48:39');
INSERT INTO `sys_product_category` VALUES (11, '绣花鞋', 1, 11, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:48:46', '', '2025-04-15 19:48:46');
INSERT INTO `sys_product_category` VALUES (12, '传统布鞋', 1, 11, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 2, '0', '', '2025-04-15 19:48:52', '', '2025-04-15 19:48:52');
INSERT INTO `sys_product_category` VALUES (13, '弹力靴/袜靴', 2, 1, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:51:18', '', '2025-04-15 19:51:18');
INSERT INTO `sys_product_category` VALUES (14, '马丁靴', 2, 2, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:51:31', '', '2025-04-15 19:51:31');
INSERT INTO `sys_product_category` VALUES (15, '骑士靴', 2, 3, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:51:44', '', '2025-04-15 19:51:44');
INSERT INTO `sys_product_category` VALUES (16, '切尔西靴', 2, 4, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:51:51', '', '2025-04-15 19:51:51');
INSERT INTO `sys_product_category` VALUES (17, '时装靴', 2, 5, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:51:57', '', '2025-04-15 19:51:57');
INSERT INTO `sys_product_category` VALUES (18, '西部靴', 2, 6, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:52:03', '', '2025-04-15 19:52:03');
INSERT INTO `sys_product_category` VALUES (19, '烟筒靴', 2, 7, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:52:09', '', '2025-04-15 19:52:09');
INSERT INTO `sys_product_category` VALUES (20, '勃肯鞋/软木鞋', 3, 1, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:52:36', '', '2025-04-15 19:52:36');
INSERT INTO `sys_product_category` VALUES (21, '乐福鞋（豆豆鞋）', 3, 2, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:52:56', '', '2025-04-15 19:52:56');
INSERT INTO `sys_product_category` VALUES (22, '玛丽珍鞋', 3, 3, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:53:07', '', '2025-04-15 19:53:07');
INSERT INTO `sys_product_category` VALUES (23, '穆勒鞋', 3, 4, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:53:13', '', '2025-04-15 19:53:13');
INSERT INTO `sys_product_category` VALUES (24, '婚鞋', 3, 5, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:53:21', '', '2025-04-15 19:53:21');
INSERT INTO `sys_product_category` VALUES (25, '浅口单鞋', 3, 6, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:53:37', '', '2025-04-15 19:53:37');
INSERT INTO `sys_product_category` VALUES (26, '深口单鞋', 3, 7, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:53:44', '', '2025-04-15 19:53:44');
INSERT INTO `sys_product_category` VALUES (27, '时尚芭蕾鞋', 3, 8, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:53:51', '', '2025-04-15 19:53:51');
INSERT INTO `sys_product_category` VALUES (28, '牛津鞋/布洛克鞋/德比鞋', 3, 9, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:54:07', '', '2025-04-15 19:54:07');
INSERT INTO `sys_product_category` VALUES (29, '松糕（摇摇）鞋', 3, 10, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:54:20', '', '2025-04-15 19:54:20');
INSERT INTO `sys_product_category` VALUES (30, '渔夫鞋', 3, 11, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:54:26', '', '2025-04-15 19:54:26');
INSERT INTO `sys_product_category` VALUES (31, '洞洞鞋', 4, 1, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:54:48', '', '2025-04-15 19:54:48');
INSERT INTO `sys_product_category` VALUES (32, '罗马凉鞋', 4, 2, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:54:55', '', '2025-04-15 19:54:55');
INSERT INTO `sys_product_category` VALUES (33, '沙滩鞋', 4, 3, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:55:00', '', '2025-04-15 19:55:00');
INSERT INTO `sys_product_category` VALUES (34, '时装凉鞋', 4, 4, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:55:06', '', '2025-04-15 19:55:06');
INSERT INTO `sys_product_category` VALUES (35, '休闲凉鞋', 4, 5, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:55:13', '', '2025-04-15 19:55:13');
INSERT INTO `sys_product_category` VALUES (36, '一字带凉鞋', 4, 6, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:55:19', '', '2025-04-15 19:55:19');
INSERT INTO `sys_product_category` VALUES (37, '德训鞋', 5, 1, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:55:37', '', '2025-04-15 19:55:37');
INSERT INTO `sys_product_category` VALUES (38, '工装鞋', 5, 2, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:55:43', '', '2025-04-15 19:55:43');
INSERT INTO `sys_product_category` VALUES (39, '健步鞋', 5, 3, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:55:50', '', '2025-04-15 19:55:50');
INSERT INTO `sys_product_category` VALUES (40, '老爹鞋', 5, 4, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:55:56', '', '2025-04-15 19:55:56');
INSERT INTO `sys_product_category` VALUES (41, '棉鞋', 5, 5, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:56:01', '', '2025-04-15 19:56:01');
INSERT INTO `sys_product_category` VALUES (42, '时尚休闲鞋', 5, 6, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:56:07', '', '2025-04-15 19:56:07');
INSERT INTO `sys_product_category` VALUES (43, '网面鞋', 5, 7, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:56:13', '', '2025-04-15 19:56:13');
INSERT INTO `sys_product_category` VALUES (44, '休闲板鞋', 5, 8, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:56:19', '', '2025-04-15 19:56:19');
INSERT INTO `sys_product_category` VALUES (45, '包头拖', 8, 1, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:56:44', '', '2025-04-15 19:56:44');
INSERT INTO `sys_product_category` VALUES (46, '毛毛鞋', 8, 2, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:56:51', '', '2025-04-15 19:56:51');
INSERT INTO `sys_product_category` VALUES (47, '人字拖', 8, 3, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:56:58', '', '2025-04-15 19:56:58');
INSERT INTO `sys_product_category` VALUES (48, '一字拖', 8, 4, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:57:04', '', '2025-04-15 19:57:04');
INSERT INTO `sys_product_category` VALUES (49, '其他拖鞋', 8, 5, '0', 'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013', '', 0, '0', '', '2025-04-15 19:57:12', '', '2025-04-15 19:57:12');

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '档口负责人ID',
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档口名称',
  `store_weight` int NULL DEFAULT NULL COMMENT '权重',
  `store_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档口logo',
  `brand_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `contact_back_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备选联系电话',
  `wechat_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信账号',
  `qq_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'QQ账号',
  `alipay_account` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `operate_years` int UNSIGNED NULL DEFAULT NULL COMMENT '经营年限',
  `store_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档口地址',
  `fac_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工厂地址',
  `prod_scale` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '生产规模',
  `integrity_gold` decimal(10, 2) NULL DEFAULT NULL COMMENT '保证金',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `trial_end_time` date NULL DEFAULT NULL COMMENT '试用截止时间',
  `storage_usage` decimal(10, 3) NULL DEFAULT NULL COMMENT '已使用文件大小',
  `template_num` int UNSIGNED NULL DEFAULT NULL COMMENT '档口模板ID',
  `store_status` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '档口状态',
  `reject_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拒绝理由',
  `version` bigint UNSIGNED NOT NULL COMMENT '版本号',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;