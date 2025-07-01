

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
  avatar            varchar(100)    default ''                 comment '头像地址',
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
INSERT INTO `sys_dict_type` VALUES (113, '帮面材质', 'upper_material', '0', '0', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质');
INSERT INTO `sys_dict_type` VALUES (114, '内里材质', 'lining_material', '0', '0', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质');
INSERT INTO `sys_dict_type` VALUES (115, '鞋垫材质', 'insole_material', '0', '0', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质');
INSERT INTO `sys_dict_type` VALUES (116, '后跟高', 'heel_height', '0', '0', '0', 'admin', '2025-04-15 22:34:09', '', '2025-04-15 22:34:09', '后跟高');
INSERT INTO `sys_dict_type` VALUES (117, '跟底款式', 'heel_type', '0', '0', '0', 'admin', '2025-04-15 22:35:30', '', '2025-04-15 22:35:30', '跟底款式');
INSERT INTO `sys_dict_type` VALUES (118, '鞋头款式', 'toe_style', '0', '0', '0', 'admin', '2025-04-15 22:36:46', '', '2025-04-15 22:36:46', '鞋头款式');
INSERT INTO `sys_dict_type` VALUES (119, '适合季节', 'suitable_season', '0', '0', '0', 'admin', '2025-04-15 22:38:31', '', '2025-04-15 22:38:31', '适合季节');
INSERT INTO `sys_dict_type` VALUES (120, '开口深度', 'collar_depth', '0', '0', '0', 'admin', '2025-04-15 22:40:31', '', '2025-04-15 22:40:31', '开口深度');
INSERT INTO `sys_dict_type` VALUES (121, '鞋底材质', 'outsole_material', '0', '0', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质');
INSERT INTO `sys_dict_type` VALUES (122, '风格', 'style', '0', '0', '0', 'admin', '2025-04-15 22:43:34', '', '2025-04-15 22:43:34', '风格');
INSERT INTO `sys_dict_type` VALUES (123, '款式', 'design', '0', '0', '0', 'admin', '2025-04-15 22:45:07', '', '2025-04-15 22:45:07', '款式');
INSERT INTO `sys_dict_type` VALUES (124, '皮质特征', 'leather_features', '0', '0', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征');
INSERT INTO `sys_dict_type` VALUES (125, '制作工艺', 'manufacturing_process', '0', '0', '0', 'admin', '2025-04-15 22:48:24', '', '2025-04-15 22:48:24', '制作工艺');
INSERT INTO `sys_dict_type` VALUES (126, '图案', 'pattern', '0', '0', '0', 'admin', '2025-04-15 22:49:48', '', '2025-04-15 22:49:48', '图案');
INSERT INTO `sys_dict_type` VALUES (127, '闭合方式', 'closure_type', '0', '0', '0', 'admin', '2025-04-15 22:51:45', '', '2025-04-15 22:51:45', '闭合方式');
INSERT INTO `sys_dict_type` VALUES (128, '适用场景', 'occasion', '0', '0', '0', 'admin', '2025-04-15 22:53:44', '', '2025-04-15 22:53:44', '适用场景');
INSERT INTO `sys_dict_type` VALUES (129, '适用年龄', 'suitable_age', '0', '0', '0', 'admin', '2025-04-15 22:55:33', '', '2025-04-15 22:55:33', '适用年龄');
INSERT INTO `sys_dict_type` VALUES (130, '厚薄', 'thickness', '0', '0', '0', 'admin', '2025-04-15 22:57:30', '', '2025-04-15 22:57:30', '厚薄');
INSERT INTO `sys_dict_type` VALUES (131, '流行元素', 'fashion_elements', '0', '0', '0', 'admin', '2025-04-15 22:59:19', '', '2025-04-15 22:59:19', '流行元素');



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
INSERT INTO `sys_dict_data` VALUES (209, 1, '头层牛皮', '头层牛皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (210, 2, '羊皮(除羊反绒/羊猄)', '羊皮(除羊反绒/羊猄)', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (211, 3, '牛皮', '牛皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (212, 4, '羊反绒(羊猄)', '羊反绒(羊猄)', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (213, 5, '聚氨酯', '聚氨酯', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (214, 6, '弹力布', '弹力布', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (215, 7, '反毛皮', '反毛皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (216, 8, '蛇蜥皮', '蛇蜥皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (217, 9, '驼乌皮', '驼乌皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (218, 10, '麻', '麻', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (219, 11, '漆皮', '漆皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (220, 12, '树脂', '树脂', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (221, 13, '鸵鸟皮', '鸵鸟皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (222, 14, '鹿皮', '鹿皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (223, 15, '裘皮', '裘皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (224, 16, '袋鼠皮', '袋鼠皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (225, 17, '蜥蜴皮', '蜥蜴皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (226, 18, '聚亚胺酯', '聚亚胺酯', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (227, 19, '羊驼皮', '羊驼皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (228, 20, '锦纶', '锦纶', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (229, 21, '牛二层皮覆膜', '牛二层皮覆膜', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (230, 22, '多种材质拼接', '多种材质拼接', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (231, 23, '绵羊皮毛', '绵羊皮毛', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (232, 24, '牛皮剖层绒面革', '牛皮剖层绒面革', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (233, 25, '牛皮网', '牛皮网', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (234, 26, '绒面', '绒面', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (235, 27, '超细纤维', '超细纤维', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (236, 28, '牛皮/羊皮', '牛皮/羊皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (237, 29, '合成革', '合成革', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (238, 30, 'PU', 'PU', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (239, 31, '二层羊皮', '二层羊皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (240, 32, '布', '布', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (241, 33, '绸缎', '绸缎', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (242, 34, '牛反绒(磨砂皮)', '牛反绒(磨砂皮)', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (243, 35, '马毛', '马毛', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (244, 36, '亮片布', '亮片布', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (245, 37, '织物', '织物', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (246, 38, '网布', '网布', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (247, 39, '马皮', '马皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (248, 40, '西施绒', '西施绒', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (249, 41, '缎面', '缎面', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (250, 42, '贴皮', '贴皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (251, 43, '蛇皮', '蛇皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (252, 44, '二层牛皮', '二层牛皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (253, 45, '人造革', '人造革', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (254, 46, '牛皮革+织物', '牛皮革+织物', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (255, 47, '棉麻', '棉麻', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (256, 48, '麂皮', '麂皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (257, 49, '涤纶', '涤纶', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (258, 50, '羊皮毛一体', '羊皮毛一体', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (259, 51, '貂毛', '貂毛', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (260, 52, '格利特皮革', '格利特皮革', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (261, 53, '超纤皮', '超纤皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (262, 54, '牛皮革', '牛皮革', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (263, 55, '天鹅绒', '天鹅绒', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (264, 56, '牛皮漆膜革', '牛皮漆膜革', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (265, 57, '人造毛绒', '人造毛绒', 'upper_material', '0', 'admin', '2025-04-15 22:24:43', '', '2025-04-15 22:24:43', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (266, 58, '呢子', '呢子', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (267, 59, 'PVC化纤', 'PVC化纤', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (268, 60, '羊绒面革', '羊绒面革', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (269, 61, '灯芯绒', '灯芯绒', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (270, 62, '皮质', '皮质', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (271, 63, '绒面革', '绒面革', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (272, 64, '猪皮', '猪皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (273, 65, '亚麻', '亚麻', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (274, 66, '羊皮漆膜革', '羊皮漆膜革', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (275, 67, '二层牛皮(除牛反绒)', '二层牛皮(除牛反绒)', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (276, 68, '二层猪皮', '二层猪皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (277, 69, '羊皮革', '羊皮革', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (278, 70, '绒面革拼漆皮', '绒面革拼漆皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (279, 71, '棉', '棉', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (280, 72, '羊毛皮', '羊毛皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (281, 73, '织锦', '织锦', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (282, 74, '乙纶', '乙纶', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (283, 75, '聚酯纤维', '聚酯纤维', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (284, 76, '鱼皮', '鱼皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (285, 77, '羊羔皮', '羊羔皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (286, 78, '粘胶纤维', '粘胶纤维', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (287, 79, '牛毛皮', '牛毛皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (288, 80, '鳄鱼纹皮', '鳄鱼纹皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (289, 81, '鳄鱼皮', '鳄鱼皮', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (290, 82, '太空革', '太空革', 'upper_material', '0', 'admin', '2025-04-15 22:24:44', '', '2025-04-15 22:24:44', '帮面材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (291, 1, '头层猪皮', '头层猪皮', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (292, 2, '羊皮', '羊皮', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (293, 3, '超细纤维', '超细纤维', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (294, 4, '合成革', '合成革', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (295, 5, 'PU', 'PU', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (296, 6, '牛皮/猪皮内里', '牛皮/猪皮内里', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (297, 7, '兔毛', '兔毛', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (298, 8, '头层牛皮', '头层牛皮', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (299, 9, '二层猪皮', '二层猪皮', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (300, 10, '仿猪皮', '仿猪皮', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (301, 11, '马皮', '马皮', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (302, 12, '牛皮/羊皮', '牛皮/羊皮', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (303, 13, '羊皮革', '羊皮革', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (304, 14, '织物', '织物', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (305, 15, '无内里', '无内里', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (306, 16, '牛皮', '牛皮', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (307, 17, '网纱', '网纱', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (308, 18, '皮革', '皮革', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (309, 19, '人造短毛绒', '人造短毛绒', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (310, 20, '羊皮毛一体', '羊皮毛一体', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (311, 21, '混合皮革', '混合皮革', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (312, 22, '皮革/织物', '皮革/织物', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (313, 23, '聚酯纤维', '聚酯纤维', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (314, 24, '牛皮革', '牛皮革', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (315, 25, '牛二层皮', '牛二层皮', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (316, 26, '山羊皮革', '山羊皮革', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (317, 27, '棉', '棉', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (318, 28, '羊毛羊绒混纺', '羊毛羊绒混纺', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (319, 29, '狐狸毛', '狐狸毛', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (320, 30, '帆布', '帆布', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (321, 31, '人造长毛绒', '人造长毛绒', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (322, 32, '皮革/织物', '皮革/织物', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (323, 33, '纯羊毛', '纯羊毛', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (324, 34, '涤沦', '涤沦', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (325, 35, '布', '布', 'lining_material', '0', 'admin', '2025-04-15 22:28:07', '', '2025-04-15 22:28:07', '内里材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (326, 1, '羊皮', '羊皮', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (327, 2, '头层牛皮', '头层牛皮', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (328, 3, '头层猪皮', '头层猪皮', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (329, 4, 'PU', 'PU', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (330, 5, '超纤皮', '超纤皮', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (331, 6, '合成革', '合成革', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (332, 7, '二层猪皮', '二层猪皮', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (333, 8, '布', '布', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (334, 9, '人造短毛绒', '人造短毛绒', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (335, 10, '纯羊毛', '纯羊毛', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (336, 11, '羊皮毛一体', '羊皮毛一体', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (337, 12, '羊毛羊绒混纺', '羊毛羊绒混纺', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (338, 13, '人造长毛绒', '人造长毛绒', 'insole_material', '0', 'admin', '2025-04-15 22:31:43', '', '2025-04-15 22:31:43', '鞋垫材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (339, 1, '平跟(小于等于1cm)', '平跟(小于等于1cm)', 'heel_height', '0', 'admin', '2025-04-15 22:34:10', '', '2025-04-15 22:34:10', '后跟高子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (340, 2, '低跟(1-3cm)', '低跟(1-3cm)', 'heel_height', '0', 'admin', '2025-04-15 22:34:10', '', '2025-04-15 22:34:10', '后跟高子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (341, 3, '中跟(3-5cm)', '中跟(3-5cm)', 'heel_height', '0', 'admin', '2025-04-15 22:34:10', '', '2025-04-15 22:34:10', '后跟高子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (342, 4, '高跟(5-8cm)', '高跟(5-8cm)', 'heel_height', '0', 'admin', '2025-04-15 22:34:10', '', '2025-04-15 22:34:10', '后跟高子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (343, 5, '超高跟(大于8cm)', '超高跟(大于8cm)', 'heel_height', '0', 'admin', '2025-04-15 22:34:10', '', '2025-04-15 22:34:10', '后跟高子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (344, 1, '粗跟', '粗跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (345, 2, '平跟', '平跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (346, 3, '细跟', '细跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (347, 4, '内增高', '内增高', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (348, 5, '平底', '平底', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (349, 6, '坡跟', '坡跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (350, 7, '方跟', '方跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (351, 8, '松糕底', '松糕底', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (352, 9, '异型跟', '异型跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (353, 10, '酒杯跟', '酒杯跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (354, 11, '猫跟', '猫跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (355, 12, '摇摇底', '摇摇底', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (356, 13, '镂空跟', '镂空跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (357, 14, '锥形跟', '锥形跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (358, 15, '马蹄跟', '马蹄跟', 'heel_type', '0', 'admin', '2025-04-15 22:35:31', '', '2025-04-15 22:35:31', '跟底款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (359, 1, '方头', '方头', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (360, 2, '圆头', '圆头', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (361, 3, '尖头', '尖头', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (362, 4, '杏头', '杏头', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (363, 5, '蝴蝶结头', '蝴蝶结头', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (364, 6, '斜头', '斜头', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (365, 7, '分趾', '分趾', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (366, 8, '露趾', '露趾', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (367, 9, '鱼嘴', '鱼嘴', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (368, 10, '翘头', '翘头', 'toe_style', '0', 'admin', '2025-04-15 22:36:47', '', '2025-04-15 22:36:47', '鞋头款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (369, 1, '春秋', '春秋', 'suitable_season', '0', 'admin', '2025-04-15 22:38:33', '', '2025-04-15 22:38:33', '适合季节子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (370, 2, '夏季', '夏季', 'suitable_season', '0', 'admin', '2025-04-15 22:38:33', '', '2025-04-15 22:38:33', '适合季节子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (371, 3, '冬季', '冬季', 'suitable_season', '0', 'admin', '2025-04-15 22:38:33', '', '2025-04-15 22:38:33', '适合季节子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (372, 4, '四季通用', '四季通用', 'suitable_season', '0', 'admin', '2025-04-15 22:38:33', '', '2025-04-15 22:38:33', '适合季节子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (373, 1, '浅口', '浅口', 'collar_depth', '0', 'admin', '2025-04-15 22:40:31', '', '2025-04-15 22:40:31', '开口深度子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (374, 2, '中口', '中口', 'collar_depth', '0', 'admin', '2025-04-15 22:40:32', '', '2025-04-15 22:40:32', '开口深度子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (375, 3, '深口', '深口', 'collar_depth', '0', 'admin', '2025-04-15 22:40:32', '', '2025-04-15 22:40:32', '开口深度子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (376, 1, 'TPR(牛筋)', 'TPR(牛筋)', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (377, 2, '橡胶', '橡胶', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (378, 3, '聚氨酯', '聚氨酯', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (379, 4, '橡胶发泡', '橡胶发泡', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (380, 5, 'TPR', 'TPR', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (381, 6, 'tpu', 'tpu', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (382, 7, '真皮', '真皮', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (383, 8, '泡沫', '泡沫', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (384, 9, 'EVA发泡胶', 'EVA发泡胶', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (385, 10, '木', '木', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (386, 11, 'EVA', 'EVA', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (387, 12, '橡胶组合底', '橡胶组合底', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (388, 13, '皮革', '皮革', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (389, 14, '千层底', '千层底', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (390, 15, '复合底', '复合底', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (391, 16, 'PVC', 'PVC', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (392, 17, '麻', '麻', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (393, 18, '塑胶', '塑胶', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (394, 19, '塑料', '塑料', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (395, 20, 'MD', 'MD', 'outsole_material', '0', 'admin', '2025-04-15 22:42:21', '', '2025-04-15 22:42:21', '鞋底材质子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (396, 1, '甜美', '甜美', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (397, 2, '日系', '日系', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (398, 3, '学院', '学院', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (399, 4, '公主', '公主', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (400, 5, '森女', '森女', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (401, 6, '田园', '田园', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (402, 7, '洛丽塔', '洛丽塔', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (403, 8, 'ins风', 'ins风', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (404, 9, '欧美', '欧美', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (405, 10, '休闲', '休闲', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (406, 11, '朋克', '朋克', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (407, 12, '军装', '军装', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (408, 13, '嘻哈', '嘻哈', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (409, 14, '中性', '中性', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (410, 15, '波普', '波普', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (411, 16, '通勒', '通勒', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (412, 17, '韩版', '韩版', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (413, 18, '简约', '简约', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (414, 19, '英伦', '英伦', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (415, 20, '优雅', '优雅', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (416, 21, '民族风', '民族风', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (417, 22, '性感', '性感', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (418, 23, '舒适', '舒适', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (419, 24, '复古风', '复古风', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (420, 25, '时尚', '时尚', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (421, 26, '法式', '法式', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (422, 27, '工装', '工装', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (423, 28, '可爱', '可爱', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (424, 29, '轻熟', '轻熟', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (425, 30, '街头', '街头', 'style', '0', 'admin', '2025-04-15 22:43:35', '', '2025-04-15 22:43:35', '风格子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (426, 1, '单鞋', '单鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (427, 2, '玛丽珍鞋', '玛丽珍鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (428, 3, '高跟鞋', '高跟鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (429, 4, '芭蕾舞平底鞋', '芭蕾舞平底鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (430, 5, '奶奶鞋', '奶奶鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (431, 6, '懒人鞋', '懒人鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (432, 7, '穆勒鞋', '穆勒鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (433, 8, '网面鞋', '网面鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (434, 9, '松糕鞋', '松糕鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (435, 10, '休闲', '休闲', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (436, 11, '乐福鞋', '乐福鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (437, 12, '丁字鞋', '丁字鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (438, 13, '运动休闲鞋', '运动休闲鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (439, 14, '豆豆鞋', '豆豆鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (440, 15, '鱼嘴鞋', '鱼嘴鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (441, 16, '布洛克鞋', '布洛克鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (442, 17, '护士鞋', '护士鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (443, 18, '专业鞋', '专业鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (444, 19, '帆船鞋', '帆船鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (445, 20, '布鞋', '布鞋', 'design', '0', 'admin', '2025-04-15 22:45:08', '', '2025-04-15 22:45:08', '款式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (446, 1, '水染皮', '水染皮', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (447, 2, '修面皮', '修面皮', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (448, 3, '漆皮', '漆皮', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (449, 4, '磨砂', '磨砂', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (450, 5, '覆膜', '覆膜', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (451, 6, '软面皮', '软面皮', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (452, 7, '植鞣', '植鞣', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (453, 8, '打蜡', '打蜡', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (454, 9, '压花皮', '压花皮', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (455, 10, '纳帕纹', '纳帕纹', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (456, 11, '油鞣', '油鞣', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (457, 12, '印花', '印花', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (458, 13, '摔纹皮', '摔纹皮', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (459, 14, '擦色皮', '擦色皮', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (460, 15, '反绒皮', '反绒皮', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (461, 16, '裂紋', '裂紋', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (462, 17, '醛鞣', '醛鞣', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (463, 18, '开边珠', '开边珠', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (464, 19, '疯马皮', '疯马皮', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (465, 20, '铬鞣', '铬鞣', 'leather_features', '0', 'admin', '2025-04-15 22:46:48', '', '2025-04-15 22:46:48', '皮质特征子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (466, 1, '胶粘工艺', '胶粘工艺', 'manufacturing_process', '0', 'admin', '2025-04-15 22:48:25', '', '2025-04-15 22:48:25', '制作工艺子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (467, 2, '缝制鞋', '缝制鞋', 'manufacturing_process', '0', 'admin', '2025-04-15 22:48:25', '', '2025-04-15 22:48:25', '制作工艺子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (468, 3, '硫化工艺', '硫化工艺', 'manufacturing_process', '0', 'admin', '2025-04-15 22:48:25', '', '2025-04-15 22:48:25', '制作工艺子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (469, 4, '注压工艺', '注压工艺', 'manufacturing_process', '0', 'admin', '2025-04-15 22:48:25', '', '2025-04-15 22:48:25', '制作工艺子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (470, 1, '纯色', '纯色', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (471, 2, '植物花卉', '植物花卉', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (472, 3, '条纹', '条纹', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (473, 4, '格子', '格子', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (474, 5, '拼色', '拼色', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (475, 6, '波点', '波点', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (476, 7, '卡通动漫', '卡通动漫', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (477, 8, '心形', '心形', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (478, 9, '涂鸦', '涂鸦', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (479, 10, '碎花', '碎花', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (480, 11, '手绘', '手绘', 'pattern', '0', 'admin', '2025-04-15 22:49:49', '', '2025-04-15 22:49:49', '图案子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (481, 1, '套脚', '套脚', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (482, 2, '一字式扣带', '一字式扣带', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (483, 3, '一脚蹬', '一脚蹬', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (484, 4, '丁字式扣带', '丁字式扣带', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (485, 5, '系带', '系带', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (486, 6, '交叉扣带', '交叉扣带', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (487, 7, '松紧带', '松紧带', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (488, 8, '按扣式', '按扣式', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (489, 9, '旋钮', '旋钮', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (490, 10, '脚腕扣带', '脚腕扣带', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (491, 11, '魔术贴', '魔术贴', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (492, 12, '拉链', '拉链', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (493, 13, '滑扣调节', '滑扣调节', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (494, 14, '翻扣式', '翻扣式', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (495, 15, '侧拉链', '侧拉链', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (496, 16, '前拉链', '前拉链', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (497, 17, '前系带', '前系带', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (498, 18, '后拉链', '后拉链', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (499, 19, '后系带', '后系带', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (500, 20, '松紧带', '松紧带', 'closure_type', '0', 'admin', '2025-04-15 22:51:46', '', '2025-04-15 22:51:46', '闭合方式子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (501, 1, '日常', '日常', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (502, 2, '宴会', '宴会', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (503, 3, '戏剧表演', '戏剧表演', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (504, 4, '旅行', '旅行', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (505, 5, '跑步', '跑步', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (506, 6, '健身', '健身', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (507, 7, '面试', '面试', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (508, 8, '音乐会', '音乐会', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (509, 9, '约会', '约会', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (510, 10, '婚宴', '婚宴', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (511, 11, '跳舞', '跳舞', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (512, 12, '办公室', '办公室', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (513, 13, '婚礼', '婚礼', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (514, 14, 'JK制服搭配', 'JK制服搭配', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (515, 15, '古风搭配', '古风搭配', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (516, 16, '运动', '运动', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (517, 17, 'Lolita搭配', 'Lolita搭配', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (518, 18, '上班', '上班', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (519, 19, '沙滩', '沙滩', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (520, 20, '居家', '居家', 'occasion', '0', 'admin', '2025-04-15 22:53:45', '', '2025-04-15 22:53:45', '适用场景子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (521, 1, '18-40岁', '18-40岁', 'suitable_age', '0', 'admin', '2025-04-15 22:55:34', '', '2025-04-15 22:55:34', '适用年龄子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (522, 2, '40-60岁', '40-60岁', 'suitable_age', '0', 'admin', '2025-04-15 22:55:34', '', '2025-04-15 22:55:34', '适用年龄子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (523, 1, '加厚', '加厚', 'thickness', '0', 'admin', '2025-04-15 22:57:30', '', '2025-04-15 22:57:30', '厚薄子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (524, 2, '常规', '常规', 'thickness', '0', 'admin', '2025-04-15 22:57:30', '', '2025-04-15 22:57:30', '厚薄子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (525, 3, '薄款', '薄款', 'thickness', '0', 'admin', '2025-04-15 22:57:30', '', '2025-04-15 22:57:30', '厚薄子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (526, 1, '浅口', '浅口', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (527, 2, '拼接', '拼接', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (528, 3, '搭扣', '搭扣', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (529, 4, '串珠', '串珠', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (530, 5, '亮丝/亮片', '亮丝/亮片', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (531, 6, '绣花 ', '绣花 ', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (532, 7, '粗跟', '粗跟', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (533, 8, '拼色', '拼色', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (534, 9, '坡跟', '坡跟', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (535, 10, '铆钉', '铆钉', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (536, 11, '格子', '格子', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (537, 12, '花卉', '花卉', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (538, 13, '金属装饰', '金属装饰', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (539, 14, '罗马风格', '罗马风格', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (540, 15, '珍珠', '珍珠', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (541, 16, '皮带装饰', '皮带装饰', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (542, 17, '丝带', '丝带', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (543, 18, '亮丝', '亮丝', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (544, 19, '一字扣', '一字扣', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (545, 20, '金属', '金属', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (546, 21, '交叉绑带', '交叉绑带', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (547, 22, '脚环绑带', '脚环绑带', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (548, 23, '蕾丝', '蕾丝', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (549, 24, '毛线扣', '毛线扣', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (550, 25, '蝴蝶结', '蝴蝶结', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (551, 26, '花朵', '花朵', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (552, 27, '细带组合', '细带组合', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (553, 28, 'T型带(脚背)', 'T型带(脚背)', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (554, 29, '珍珠方扣', '珍珠方扣', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (555, 30, '水钻', '水钻', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (556, 31, '皮带扣', '皮带扣', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (557, 32, '亮片', '亮片', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (558, 33, '刺绣/绣花', '刺绣/绣花', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (559, 34, '链子', '链子', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (560, 35, '镂空', '镂空', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (561, 36, '豹纹', '豹纹', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (562, 37, '松糕跟', '松糕跟', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (563, 38, '刺绣', '刺绣', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (564, 39, '格纹', '格纹', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (565, 40, '防水台', '防水台', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (566, 41, '编织', '编织', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (567, 42, '网状', '网状', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (568, 43, 'T型绑带', 'T型绑带', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');
INSERT INTO `sys_dict_data` VALUES (569, 44, '流苏', '流苏', 'fashion_elements', '0', 'admin', '2025-04-15 22:59:20', '', '2025-04-15 22:59:20', '流行元素子项', 0, '0');


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