-- ----------------------------
-- 学生信息表
-- ----------------------------
drop table if exists sys_student;
create table sys_student (
  student_id        bigint(20)      not null auto_increment    comment '学生ID',
  student_name      varchar(30)     not null                   comment '学生姓名',
  avatar            varchar(255)    default ''                 comment '头像地址',
  introduction      text                                       comment '个人介绍',
  gender            char(1)         default '0'                comment '性别（0男 1女）',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (student_id)
) engine=innodb auto_increment=1000 default charset=utf8mb4 comment = '学生信息表';

-- ----------------------------
-- 初始化-学生信息表数据
-- ----------------------------
insert into sys_student values(1, '张三', '/profile/avatar/2023/01/15/xxx_1.jpg', '计算机科学专业大三学生，热爱编程，擅长Java和Python开发。', '0', '0', '0', 'admin', sysdate(), '', null, '优秀学生');
insert into sys_student values(2, '李四', '/profile/avatar/2023/02/20/xxx_2.jpg', '软件工程专业大二学生，对前端开发有浓厚兴趣，熟悉Vue和React框架。', '1', '0', '0', 'admin', sysdate(), '', null, '前端爱好者');
insert into sys_student values(3, '王五', '/profile/avatar/2023/03/10/xxx_3.jpg', '信息安全专业大三学生，热衷于网络安全研究，曾参与多次CTF比赛。', '0', '0', '0', 'admin', sysdate(), '', null, '安全达人');
insert into sys_student values(4, '赵六', '/profile/avatar/2023/04/05/xxx_4.jpg', '数据科学专业大二学生，擅长数据分析和机器学习，熟悉Python和R语言。', '1', '0', '0', 'admin', sysdate(), '', null, '数据分析师');
insert into sys_student values(5, '钱七', '/profile/avatar/2023/05/12/xxx_5.jpg', '人工智能专业大三学生，研究方向为深度学习和自然语言处理。', '0', '0', '0', 'admin', sysdate(), '', null, 'AI研究者');

-- ----------------------------
-- 学生信息菜单
-- ----------------------------
-- 一级菜单
insert into sys_menu values('2100', '学生管理', '0', '5', 'student', null, '', '', 1, 0, 'M', '0', '0', '', 'education', 'admin', sysdate(), '', null, '学生管理目录');

-- 二级菜单
insert into sys_menu values('2101', '学生信息', '2100', '1', 'info', 'student/info/index', '', '', 1, 0, 'C', '0', '0', 'student:info:list', 'user', 'admin', sysdate(), '', null, '学生信息菜单');

-- 学生信息按钮
insert into sys_menu values('2102', '学生查询', '2101', '1',  '', '', '', '', 1, 0, 'F', '0', '0', 'student:info:query',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2103', '学生新增', '2101', '2',  '', '', '', '', 1, 0, 'F', '0', '0', 'student:info:add',            '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2104', '学生修改', '2101', '3',  '', '', '', '', 1, 0, 'F', '0', '0', 'student:info:edit',           '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2105', '学生删除', '2101', '4',  '', '', '', '', 1, 0, 'F', '0', '0', 'student:info:remove',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2106', '学生导出', '2101', '5',  '', '', '', '', 1, 0, 'F', '0', '0', 'student:info:export',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2107', '学生导入', '2101', '6',  '', '', '', '', 1, 0, 'F', '0', '0', 'student:info:import',         '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 学生信息权限分配给超级管理员角色
-- ----------------------------
insert into sys_role_menu values ('1', '2100');
insert into sys_role_menu values ('1', '2101');
insert into sys_role_menu values ('1', '2102');
insert into sys_role_menu values ('1', '2103');
insert into sys_role_menu values ('1', '2104');
insert into sys_role_menu values ('1', '2105');
insert into sys_role_menu values ('1', '2106');
insert into sys_role_menu values ('1', '2107');