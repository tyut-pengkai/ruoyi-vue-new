drop table if exists `agent_info`;
CREATE TABLE `agent_info` (
    `agent_id`      bigint(20)   not null auto_increment  COMMENT '智能体id'
  , `agent_name`    varchar(200) not null  COMMENT '智能体名称'
  , `agent_desc`    text                      COMMENT '智能体描述'
  , `modle_name`    varchar(200) DEFAULT NULL COMMENT '模型名称'
  , `system_prompt` text                      COMMENT '系统提示词'
  , `voice`         varchar(100) DEFAULT NULL COMMENT '音色'
  , `voice_speed`   varchar(32)  DEFAULT NULL COMMENT '音速'
  , `mem`           varchar(32) DEFAULT NULL COMMENT '记忆模型'
  , `intent`        varchar(32) DEFAULT NULL COMMENT '意图模型'
  , `lang_code`     varchar(10) DEFAULT NULL COMMENT '交互语种编码'
  , `lang_name`     varchar(10) DEFAULT NULL COMMENT '交互语种名称'
  , `sort`          int(10) unsigned DEFAULT '0' COMMENT '排序权重'
  , `belong_user_id`     bigint(20)   not null COMMENT '所属用户ID'
  , `share_type`         varchar(50)  not null COMMENT '分享属性(0分享 1私有)'
  , `status`        char(1)     DEFAULT '0'              comment '状态（0正常 1停用）'
  , `del_flag`      char(1)     DEFAULT '0'              comment '删除标志（0代表存在 2代表删除）'
  , `created_at`    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP                             COMMENT '创建时间'
  , `updated_at`    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
  , PRIMARY KEY (`agent_id`)

) engine=innodb auto_increment=1001 comment = '智能体信息';


 


-- 智能体管理
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) 
VALUES (2010, '智能体管理', 0, 6, 'agent', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'tool', 'admin', sysdate(), '', NULL, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('智能体信息', '2010', '1', 'info', 'agent/info/index', 1, 0, 'C', '0', '0', 'agent:info:list', '#', 'admin', sysdate(), '', null, '智能体信息菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('智能体信息查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'agent:info:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('智能体信息新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'agent:info:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('智能体信息修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'agent:info:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('智能体信息删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'agent:info:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('智能体信息导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'agent:info:export',       '#', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 智能体设备关联表 20250620
-- ----------------------------
drop table if exists agent_device;
create table agent_device (
   agent_id        bigint(20)     not null   comment '智能体id'
  ,device_id      bigint(20)     not null   comment '设备id'
  ,create_time 	  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP                                        comment '创建时间'
  ,update_time    TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP            comment '更新时间'
  ,primary key (device_id)
) engine=innodb auto_increment=200 comment = '智能体设备关联表';










