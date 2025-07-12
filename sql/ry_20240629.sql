

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

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu values('1', '系统管理', '0', '1', 'system',           null, '', '', 1, 0, 'M', '0', '0', '', 'system',   'admin', sysdate(), '', null, '系统管理目录');
insert into sys_menu values('2', '系统监控', '0', '2', 'monitor',          null, '', '', 1, 0, 'M', '0', '0', '', 'monitor',  'admin', sysdate(), '', null, '系统监控目录');
insert into sys_menu values('3', '系统工具', '0', '3', 'tool',             null, '', '', 1, 0, 'M', '0', '0', '', 'tool',     'admin', sysdate(), '', null, '系统工具目录');
insert into sys_menu values('4', '若依官网', '0', '4', 'http://ruoyi.vip', null, '', '', 0, 0, 'M', '0', '0', '', 'guide',    'admin', sysdate(), '', null, '若依官网地址');
-- 二级菜单
insert into sys_menu values('100',  '用户管理', '1',   '1', 'user',       'system/user/index',        '', '', 1, 0, 'C', '0', '0', 'system:user:list',        'user',          'admin', sysdate(), '', null, '用户管理菜单');
insert into sys_menu values('101',  '角色管理', '1',   '2', 'role',       'system/role/index',        '', '', 1, 0, 'C', '0', '0', 'system:role:list',        'peoples',       'admin', sysdate(), '', null, '角色管理菜单');
insert into sys_menu values('102',  '菜单管理', '1',   '3', 'menu',       'system/menu/index',        '', '', 1, 0, 'C', '0', '0', 'system:menu:list',        'tree-table',    'admin', sysdate(), '', null, '菜单管理菜单');
insert into sys_menu values('105',  '字典管理', '1',   '6', 'dict',       'system/dict/index',        '', '', 1, 0, 'C', '0', '0', 'system:dict:list',        'dict',          'admin', sysdate(), '', null, '字典管理菜单');
insert into sys_menu values('106',  '参数设置', '1',   '7', 'config',     'system/config/index',      '', '', 1, 0, 'C', '0', '0', 'system:config:list',      'edit',          'admin', sysdate(), '', null, '参数设置菜单');
insert into sys_menu values('107',  '通知公告', '1',   '8', 'notice',     'system/notice/index',      '', '', 1, 0, 'C', '0', '0', 'system:notice:list',      'message',       'admin', sysdate(), '', null, '通知公告菜单');
insert into sys_menu values('108',  '日志管理', '1',   '9', 'log',        '',                         '', '', 1, 0, 'M', '0', '0', '',                        'log',           'admin', sysdate(), '', null, '日志管理菜单');
insert into sys_menu values('109',  '在线用户', '2',   '1', 'online',     'monitor/online/index',     '', '', 1, 0, 'C', '0', '0', 'monitor:online:list',     'online',        'admin', sysdate(), '', null, '在线用户菜单');
insert into sys_menu values('110',  '定时任务', '2',   '2', 'job',        'monitor/job/index',        '', '', 1, 0, 'C', '0', '0', 'monitor:job:list',        'job',           'admin', sysdate(), '', null, '定时任务菜单');
insert into sys_menu values('111',  '数据监控', '2',   '3', 'druid',      'monitor/druid/index',      '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list',      'druid',         'admin', sysdate(), '', null, '数据监控菜单');
insert into sys_menu values('112',  '服务监控', '2',   '4', 'server',     'monitor/server/index',     '', '', 1, 0, 'C', '0', '0', 'monitor:server:list',     'server',        'admin', sysdate(), '', null, '服务监控菜单');
insert into sys_menu values('113',  '缓存监控', '2',   '5', 'cache',      'monitor/cache/index',      '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis',         'admin', sysdate(), '', null, '缓存监控菜单');
insert into sys_menu values('114',  '缓存列表', '2',   '6', 'cacheList',  'monitor/cache/list',       '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list',      'redis-list',    'admin', sysdate(), '', null, '缓存列表菜单');
insert into sys_menu values('115',  '表单构建', '3',   '1', 'build',      'tool/build/index',         '', '', 1, 0, 'C', '0', '0', 'tool:build:list',         'build',         'admin', sysdate(), '', null, '表单构建菜单');
insert into sys_menu values('116',  '代码生成', '3',   '2', 'gen',        'tool/gen/index',           '', '', 1, 0, 'C', '0', '0', 'tool:gen:list',           'code',          'admin', sysdate(), '', null, '代码生成菜单');
insert into sys_menu values('117',  '系统接口', '3',   '3', 'swagger',    'tool/swagger/index',       '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list',       'swagger',       'admin', sysdate(), '', null, '系统接口菜单');
-- 三级菜单
insert into sys_menu values('500',  '操作日志', '108', '1', 'operlog',    'monitor/operlog/index',    '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list',    'form',          'admin', sysdate(), '', null, '操作日志菜单');
insert into sys_menu values('501',  '登录日志', '108', '2', 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor',    'admin', sysdate(), '', null, '登录日志菜单');
-- 用户管理按钮
insert into sys_menu values('1000', '用户查询', '100', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1001', '用户新增', '100', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1002', '用户修改', '100', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1003', '用户删除', '100', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1004', '用户导出', '100', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1005', '用户导入', '100', '6',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1006', '重置密码', '100', '7',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd',       '#', 'admin', sysdate(), '', null, '');
-- 角色管理按钮
insert into sys_menu values('1007', '角色查询', '101', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1008', '角色新增', '101', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1009', '角色修改', '101', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1010', '角色删除', '101', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1011', '角色导出', '101', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export',         '#', 'admin', sysdate(), '', null, '');
-- 菜单管理按钮
insert into sys_menu values('1012', '菜单查询', '102', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1013', '菜单新增', '102', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1014', '菜单修改', '102', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1015', '菜单删除', '102', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove',         '#', 'admin', sysdate(), '', null, '');
-- 字典管理按钮
insert into sys_menu values('1025', '字典查询', '105', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1026', '字典新增', '105', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1027', '字典修改', '105', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1028', '字典删除', '105', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1029', '字典导出', '105', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export',         '#', 'admin', sysdate(), '', null, '');
-- 参数设置按钮
insert into sys_menu values('1030', '参数查询', '106', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1031', '参数新增', '106', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1032', '参数修改', '106', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1033', '参数删除', '106', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1034', '参数导出', '106', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export',       '#', 'admin', sysdate(), '', null, '');
-- 通知公告按钮
insert into sys_menu values('1035', '公告查询', '107', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1036', '公告新增', '107', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1037', '公告修改', '107', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1038', '公告删除', '107', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove',       '#', 'admin', sysdate(), '', null, '');
-- 操作日志按钮
insert into sys_menu values('1039', '操作查询', '500', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1040', '操作删除', '500', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove',     '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1041', '日志导出', '500', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export',     '#', 'admin', sysdate(), '', null, '');
-- 登录日志按钮
insert into sys_menu values('1042', '登录查询', '501', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1043', '登录删除', '501', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1044', '日志导出', '501', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1045', '账户解锁', '501', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock',  '#', 'admin', sysdate(), '', null, '');
-- 在线用户按钮
insert into sys_menu values('1046', '在线查询', '109', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1047', '批量强退', '109', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1048', '单条强退', '109', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', sysdate(), '', null, '');
-- 定时任务按钮
insert into sys_menu values('1049', '任务查询', '110', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1050', '任务新增', '110', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1051', '任务修改', '110', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1052', '任务删除', '110', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1053', '状态修改', '110', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1054', '任务导出', '110', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export',         '#', 'admin', sysdate(), '', null, '');
-- 代码生成按钮
insert into sys_menu values('1055', '生成查询', '116', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query',             '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1056', '生成修改', '116', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit',              '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1057', '生成删除', '116', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1058', '导入代码', '116', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1059', '预览代码', '116', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1060', '生成代码', '116', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code',              '#', 'admin', sysdate(), '', null, '');


-- 删除标识&版本号
ALTER TABLE `sys_menu`  ADD COLUMN `del_flag` char(1) default '0' comment '删除标志（0代表存在 2代表删除）';
ALTER TABLE `sys_menu`  ADD COLUMN `version` bigint(20) default 0 comment '版本号';

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

insert into sys_job values(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams',        '0/10 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into sys_job values(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')',  '0/15 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into sys_job values(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)',  '0/20 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');


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