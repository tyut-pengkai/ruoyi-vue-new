/*
 Navicat Premium Data Transfer

 Source Server         : 123.206.55.70
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 123.206.55.70:3306
 Source Schema         : hywlyz

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 08/05/2022 12:29:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_app`;
CREATE TABLE `sys_app`
(
    `app_id`              bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '软件ID',
    `app_name`            varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '软件名称',
    `description`         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件描述',
    `api_url`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API接口地址',
    `status`              char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '软件状态（0正常 1停用）',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `bind_type`           char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '绑定模式',
    `is_charge`           char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT 'Y' COMMENT '是否开启计费',
    `idx_url`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件主页',
    `free_quota_reg`      bigint(30)                                                    NULL DEFAULT 0 COMMENT '首次登录赠送免费时间或点数，单位秒或点',
    `reduce_quota_unbind` bigint(30)                                                    NULL DEFAULT 0 COMMENT '换绑设备扣减时间或点数，单位秒或点',
    `auth_type`           char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '认证类型',
    `bill_type`           char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '计费类型',
    `data_in_enc`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '数据输入加密方式',
    `data_in_pwd`         varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '数据输入加密密码',
    `data_out_enc`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '数据输出加密方式',
    `data_out_pwd`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据输出加密密码',
    `data_expire_time`    bigint(30)                                                    NULL DEFAULT -1 COMMENT '数据包过期时间，单位秒，-1为不限制，默认为-1',
    `login_limit_u`       int(10)                                                       NULL DEFAULT -1 COMMENT '登录用户数量限制，整数，-1为不限制，默认为-1',
    `login_limit_m`       int(10)                                                       NULL DEFAULT -1 COMMENT '登录机器数量限制，整数，-1为不限制，默认为-1',
    `limit_oper`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '达到上限后的操作，默认为TIPS',
    `heart_beat_time`     int(10)                                                       NULL DEFAULT 300 COMMENT '心跳包时间，单位秒，客户端若在此时间范围内无任何操作将自动下线，默认为300秒',
    `app_key`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'APP KEY',
    `app_secret`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'APP SECRET',
    `api_pwd`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API匿名密码',
    `icon`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件图标地址',
    `create_by`           varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`              varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `welcome_notice`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启动公告',
    `off_notice`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '停机公告',
    PRIMARY KEY (`app_id`) USING BTREE,
    INDEX `app_key` (`app_key`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '软件管理表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_app
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_logininfor`;
CREATE TABLE `sys_app_logininfor`
(
    `info_id`        bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '访问ID',
    `user_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
    `app_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'APP名',
    `app_version`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'APP版本',
    `device_code`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备码',
    `app_user_id`    bigint(20)                                                    NULL DEFAULT NULL COMMENT 'APP用户ID',
    `ipaddr`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
    `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
    `browser`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '浏览器类型',
    `os`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '操作系统',
    `status`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
    `msg`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
    `login_time`     datetime                                                      NULL DEFAULT NULL COMMENT '访问时间',
    PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_app_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_user`;
CREATE TABLE `sys_app_user`
(
    `app_user_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '软件用户ID',
    `user_id`         bigint(20)                                                    NULL DEFAULT NULL COMMENT '用户ID',
    `app_id`          bigint(20)                                                    NULL DEFAULT NULL COMMENT '软件ID',
    `status`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '状态（0正常 1停用）',
    `login_limit_u`   int(11)                                                       NULL DEFAULT NULL COMMENT '登录用户数量限制，整数，-1为不限制，默认为-1',
    `login_limit_m`   int(11)                                                       NULL DEFAULT NULL COMMENT '登录机器数量限制，整数，-1为不限制，默认为-1',
    `free_balance`    decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '免费余额',
    `pay_balance`     decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '支付余额',
    `total_pay`       decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '总消费',
    `last_login_time` datetime                                                      NULL DEFAULT NULL COMMENT '最后登录时间',
    `login_times`     bigint(11)                                                    NULL DEFAULT NULL COMMENT '登录次数',
    `pwd_error_times` int(11)                                                       NULL DEFAULT NULL COMMENT '密码连续错误次数',
    `expire_time`     datetime                                                      NULL DEFAULT NULL COMMENT '过期时间',
    `point`           decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '剩余点数',
    `login_code`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录码',
    `create_by`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`app_user_id`) USING BTREE,
    INDEX `user_id` (`user_id`) USING BTREE,
    INDEX `app_id` (`app_id`) USING BTREE,
    INDEX `login_code` (`login_code`(191)) USING BTREE,
    CONSTRAINT `sys_app_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `sys_app_user_ibfk_2` FOREIGN KEY (`app_id`) REFERENCES `sys_app` (`app_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '软件用户表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_app_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app_user_device_code
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_user_device_code`;
CREATE TABLE `sys_app_user_device_code`
(
    `id`              bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_user_id`     bigint(20)                                                    NULL DEFAULT NULL COMMENT 'APP ID',
    `device_code_id`  bigint(20)                                                    NULL DEFAULT NULL COMMENT '设备码',
    `last_login_time` datetime                                                      NULL DEFAULT NULL COMMENT '最后登录时间',
    `login_times`     int(11)                                                       NULL DEFAULT 0 COMMENT '登录次数',
    `status`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `app_user_id` (`app_user_id`) USING BTREE,
    INDEX `device_code_id` (`device_code_id`) USING BTREE,
    CONSTRAINT `sys_app_user_device_code_ibfk_1` FOREIGN KEY (`app_user_id`) REFERENCES `sys_app_user` (`app_user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT `sys_app_user_device_code_ibfk_2` FOREIGN KEY (`device_code_id`) REFERENCES `sys_device_code` (`device_code_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '软件用户与设备码关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_app_user_device_code
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app_version
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_version`;
CREATE TABLE `sys_app_version`
(
    `app_version_id` bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '版本ID',
    `app_id`         bigint(20)                                                    NULL DEFAULT NULL COMMENT '软件ID',
    `version_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本名称',
    `version_no`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '版本号',
    `update_log`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '更新日志',
    `download_url`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下载地址',
    `status`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '版本状态（0正常 1停用）',
    `del_flag`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `md5`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件MD5',
    `create_by`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `force_update`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '强制更新到此版本',
    PRIMARY KEY (`app_version_id`) USING BTREE,
    INDEX `app_id` (`app_id`, `version_no`) USING BTREE,
    CONSTRAINT `sys_app_version_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `sys_app` (`app_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '软件版本信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_app_version
-- ----------------------------

-- ----------------------------
-- Table structure for sys_card
-- ----------------------------
DROP TABLE IF EXISTS `sys_card`;
CREATE TABLE `sys_card`
(
    `card_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '卡密ID',
    `app_id`      bigint(20)                                                    NULL DEFAULT NULL COMMENT '软件ID',
    `card_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡名称',
    `card_no`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡号',
    `card_pass`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
    `quota`       bigint(20)                                                    NULL DEFAULT NULL COMMENT '额度',
    `price`       decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '价格',
    `expire_time` datetime                                                      NULL DEFAULT NULL COMMENT '过期时间',
    `is_sold`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否售出',
    `on_sale`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否上架',
    `is_charged`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否被充值',
    `charge_time` datetime                                                      NULL DEFAULT NULL COMMENT '充值时间',
    `template_id` bigint(20)                                                    NULL DEFAULT NULL COMMENT '卡类ID',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '卡密状态',
    `charge_rule` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '充值规则',
    `create_by`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`card_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 31236
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '卡密表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_card
-- ----------------------------

-- ----------------------------
-- Table structure for sys_card_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_card_template`;
CREATE TABLE `sys_card_template`
(
    `template_id`        bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '卡类ID',
    `app_id`             bigint(20)                                                    NULL DEFAULT NULL COMMENT '软件ID',
    `card_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡名称',
    `card_no_prefix`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡号前缀',
    `card_no_suffix`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡号后缀',
    `card_description`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡描述',
    `quota`              bigint(20)                                                    NULL DEFAULT NULL COMMENT '额度',
    `price`              decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '价格',
    `card_no_len`        int(11)                                                       NULL DEFAULT NULL COMMENT '卡号长度',
    `card_no_gen_rule`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '卡号生成规则',
    `card_no_regex`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡号正则',
    `card_pass_len`      int(11)                                                       NULL DEFAULT NULL COMMENT '密码长度',
    `card_pass_gen_rule` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '密码生成规则',
    `card_pass_regex`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '密码正则',
    `charge_rule`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '充值规则',
    `on_sale`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否上架',
    `first_stock`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '优先库存',
    `effective_duration` bigint(20)                                                    NULL DEFAULT NULL COMMENT '有效时长',
    `status`             char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '卡类状态',
    `enable_auto_gen`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '允许自动生成',
    `create_by`          varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`        datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`        datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`             varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`template_id`) USING BTREE,
    INDEX `app_id` (`app_id`) USING BTREE,
    CONSTRAINT `sys_card_template_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `sys_app` (`app_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 12
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '卡密模板表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_card_template
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `config_id`    int(5)                                                         NOT NULL AUTO_INCREMENT COMMENT '参数主键',
    `config_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '参数名称',
    `config_key`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '参数键名',
    `config_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键值',
    `config_type`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime                                                       NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime                                                       NULL DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '参数配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config`
VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2021-10-28 11:33:48', 'sadmin',
        '2022-03-23 15:50:01', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config`
VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2021-10-28 11:33:48', '', NULL,
        '初始化密码 123456');
INSERT INTO `sys_config`
VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2021-10-28 11:33:48', 'sadmin',
        '2022-03-23 15:51:09', '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config`
VALUES (4, '账号自助-验证码开关', 'sys.account.captchaOnOff', 'true', 'Y', 'admin', '2021-10-28 11:33:48', 'sadmin',
        '2022-05-04 15:40:55', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config`
VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', '2021-10-28 11:33:48', 'admin',
        '2021-11-04 14:39:56', '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config`
VALUES (6, '是否开启开发模式', 'sys.api.devMode', 'false', 'Y', 'admin', '2021-10-28 11:33:48', 'sadmin', '2022-05-08 00:13:28',
        '是否开启开发模式，开启后可使用一些用于API接入的相关工具等（true开启，false关闭），运营环境请关闭');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`
(
    `dept_id`     bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '部门id',
    `parent_id`   bigint(20)                                                   NULL DEFAULT 0 COMMENT '父部门id',
    `ancestors`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
    `dept_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
    `order_num`   int(4)                                                       NULL DEFAULT 0 COMMENT '显示顺序',
    `leader`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
    `phone`       varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 113
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '部门表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept`
VALUES (100, 0, '0', '默认部门', 0, '', '', '', '0', '0', 'admin', '2021-10-28 11:33:46', 'admin', '2022-01-01 00:30:18');

-- ----------------------------
-- Table structure for sys_device_code
-- ----------------------------
DROP TABLE IF EXISTS `sys_device_code`;
CREATE TABLE `sys_device_code`
(
    `device_code_id`  bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `device_code`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备码',
    `last_login_time` datetime                                                      NULL DEFAULT NULL COMMENT '最后登录时间',
    `login_times`     int(11)                                                       NULL DEFAULT 0 COMMENT '登录次数',
    `status`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`device_code_id`) USING BTREE,
    INDEX `device_code` (`device_code`(191)) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '机器码管理表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_device_code
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`
(
    `dict_code`   bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '字典编码',
    `dict_sort`   int(4)                                                        NULL DEFAULT 0 COMMENT '字典排序',
    `dict_label`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
    `dict_value`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
    `dict_type`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
    `css_class`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
    `list_class`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
    `is_default`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 139
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典数据表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data`
VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '性别男');
INSERT INTO `sys_dict_data`
VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '性别女');
INSERT INTO `sys_dict_data`
VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '性别未知');
INSERT INTO `sys_dict_data`
VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data`
VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data`
VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2021-10-28 11:33:48', '', NULL,
        '正常状态');
INSERT INTO `sys_dict_data`
VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL,
        '停用状态');
INSERT INTO `sys_dict_data`
VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '正常状态');
INSERT INTO `sys_dict_data`
VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '停用状态');
INSERT INTO `sys_dict_data`
VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '默认分组');
INSERT INTO `sys_dict_data`
VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '系统分组');
INSERT INTO `sys_dict_data`
VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data`
VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data`
VALUES (14, 2, '后台公告', '1', 'sys_notice_type', '', 'success', 'Y', '0', 'admin', '2021-10-28 11:33:48', 'sadmin',
        '2022-03-16 20:44:25', '后台公告');
INSERT INTO `sys_dict_data`
VALUES (15, 1, '前台公告', '0', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2021-10-28 11:33:48', 'sadmin',
        '2022-03-16 20:44:21', '前台公告');
INSERT INTO `sys_dict_data`
VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2021-10-28 11:33:48', '', NULL,
        '正常状态');
INSERT INTO `sys_dict_data`
VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL,
        '关闭状态');
INSERT INTO `sys_dict_data`
VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '新增操作');
INSERT INTO `sys_dict_data`
VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '修改操作');
INSERT INTO `sys_dict_data`
VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '删除操作');
INSERT INTO `sys_dict_data`
VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '授权操作');
INSERT INTO `sys_dict_data`
VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '导出操作');
INSERT INTO `sys_dict_data`
VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '导入操作');
INSERT INTO `sys_dict_data`
VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '强退操作');
INSERT INTO `sys_dict_data`
VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '生成操作');
INSERT INTO `sys_dict_data`
VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '清空操作');
INSERT INTO `sys_dict_data`
VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL,
        '正常状态');
INSERT INTO `sys_dict_data`
VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2021-10-28 11:33:48', '', NULL,
        '停用状态');
INSERT INTO `sys_dict_data`
VALUES (100, 1, '账号登录', '0', 'sys_auth_type', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:20:24', 'admin',
        '2022-01-23 21:30:58', NULL);
INSERT INTO `sys_dict_data`
VALUES (101, 2, '单码登录', '1', 'sys_auth_type', NULL, 'success', 'N', '0', 'admin', '2021-11-05 11:20:42', 'admin',
        '2022-01-23 21:30:41', NULL);
INSERT INTO `sys_dict_data`
VALUES (102, 1, '计时模式', '0', 'sys_bill_type', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:21:20', 'admin',
        '2021-11-10 23:57:46', NULL);
INSERT INTO `sys_dict_data`
VALUES (103, 2, '计点模式', '1', 'sys_bill_type', NULL, 'success', 'N', '0', 'admin', '2021-11-05 11:21:36', 'admin',
        '2021-11-11 00:01:04', NULL);
INSERT INTO `sys_dict_data`
VALUES (104, 1, '不绑定/无限制', '0', 'sys_bind_type', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:22:00', 'admin',
        '2021-11-11 00:01:32', NULL);
INSERT INTO `sys_dict_data`
VALUES (105, 2, '用户与设备一对一绑定', '1', 'sys_bind_type', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:22:13', 'admin',
        '2021-11-11 00:01:54', NULL);
INSERT INTO `sys_dict_data`
VALUES (106, 3, '一用户可绑定多个设备', '2', 'sys_bind_type', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:22:25', 'admin',
        '2021-11-11 00:01:58', NULL);
INSERT INTO `sys_dict_data`
VALUES (107, 4, '多用户可绑定同一设备', '3', 'sys_bind_type', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:22:39', 'admin',
        '2021-11-11 00:02:02', NULL);
INSERT INTO `sys_dict_data`
VALUES (108, 1, '无限制', '0', 'sys_charge_rule', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:22:55', 'admin',
        '2021-11-11 00:08:13', NULL);
INSERT INTO `sys_dict_data`
VALUES (109, 2, '只能用于到期账号', '1', 'sys_charge_rule', NULL, 'success', 'N', '0', 'admin', '2021-11-05 11:23:06', 'admin',
        '2021-11-11 00:08:17', NULL);
INSERT INTO `sys_dict_data`
VALUES (110, 1, '明文传输', '0', 'sys_encryp_type', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:23:21', 'admin',
        '2021-11-11 00:08:30', NULL);
INSERT INTO `sys_dict_data`
VALUES (111, 2, 'BASE64', '1', 'sys_encryp_type', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:23:33', 'admin',
        '2021-11-11 00:08:34', NULL);
INSERT INTO `sys_dict_data`
VALUES (112, 3, 'AES_CBC_PKCS5Padding', '2', 'sys_encryp_type', NULL, 'primary', 'N', '0', 'admin',
        '2021-11-05 11:23:46', 'admin', '2021-11-11 00:08:38', NULL);
INSERT INTO `sys_dict_data`
VALUES (113, 4, 'AES_CBC_ZeroPadding', '3', 'sys_encryp_type', NULL, 'primary', 'N', '0', 'admin',
        '2021-11-05 11:24:08', 'admin', '2021-11-11 00:08:43', NULL);
INSERT INTO `sys_dict_data`
VALUES (114, 5, 'AES_CBC_NoPadding', '4', 'sys_encryp_type', NULL, 'primary', 'N', '1', 'admin', '2021-11-05 11:24:24',
        'sadmin', '2022-01-27 21:04:15', NULL);
INSERT INTO `sys_dict_data`
VALUES (115, 1, '数字+大写字母+小写字母', '0', 'sys_gen_rule', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:24:41', 'admin',
        '2021-11-11 00:08:59', NULL);
INSERT INTO `sys_dict_data`
VALUES (116, 2, '数字+大写字母', '1', 'sys_gen_rule', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:24:52', 'admin',
        '2021-11-11 00:09:02', NULL);
INSERT INTO `sys_dict_data`
VALUES (117, 3, '数字+小写字母', '2', 'sys_gen_rule', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:25:05', 'admin',
        '2021-11-11 00:09:06', NULL);
INSERT INTO `sys_dict_data`
VALUES (118, 4, '大写字母+小写字母', '3', 'sys_gen_rule', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:25:15', 'admin',
        '2021-11-11 00:09:09', NULL);
INSERT INTO `sys_dict_data`
VALUES (119, 5, '大写字母', '4', 'sys_gen_rule', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:25:30', 'admin',
        '2021-11-11 00:09:13', NULL);
INSERT INTO `sys_dict_data`
VALUES (120, 6, '小写字母', '5', 'sys_gen_rule', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:25:41', 'admin',
        '2021-11-11 00:09:17', NULL);
INSERT INTO `sys_dict_data`
VALUES (121, 7, '数字', '6', 'sys_gen_rule', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:26:04', 'admin',
        '2021-11-11 00:09:24', NULL);
INSERT INTO `sys_dict_data`
VALUES (122, 8, '自定义规则（正则）', '7', 'sys_gen_rule', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:26:23', 'admin',
        '2021-11-11 00:09:21', NULL);
INSERT INTO `sys_dict_data`
VALUES (123, 1, '提示用户', '0', 'sys_limit_oper', NULL, 'primary', 'N', '0', 'admin', '2021-11-05 11:26:41', 'admin',
        '2021-11-11 00:09:37', NULL);
INSERT INTO `sys_dict_data`
VALUES (124, 2, '注销最早登录的用户', '1', 'sys_limit_oper', NULL, 'success', 'N', '0', 'admin', '2021-11-05 11:26:51', 'admin',
        '2021-11-11 00:09:46', NULL);
INSERT INTO `sys_dict_data`
VALUES (125, 11, '快速接入', '11', 'sys_oper_type', '', 'primary', 'N', '0', 'sadmin', '2022-02-04 20:40:53', 'sadmin',
        '2022-02-04 20:42:23', '快速接入');
INSERT INTO `sys_dict_data`
VALUES (126, 10, '调用API', '10', 'sys_oper_type', NULL, 'info', 'N', '0', 'sadmin', '2022-02-04 20:41:49', 'sadmin',
        '2022-02-04 20:42:19', '调用API');
INSERT INTO `sys_dict_data`
VALUES (127, 1, '待付款', '0', 'sale_order_status', NULL, 'danger', 'N', '0', 'sadmin', '2022-02-21 23:37:27', 'sadmin',
        '2022-02-21 23:39:00', NULL);
INSERT INTO `sys_dict_data`
VALUES (128, 2, '已付款', '1', 'sale_order_status', NULL, 'primary', 'N', '0', 'sadmin', '2022-02-21 23:37:40', 'sadmin',
        '2022-02-21 23:40:49', NULL);
INSERT INTO `sys_dict_data`
VALUES (131, 3, '交易关闭', '2', 'sale_order_status', NULL, 'info', 'N', '0', 'sadmin', '2022-02-21 23:38:17', 'sadmin',
        '2022-03-02 00:47:28', NULL);
INSERT INTO `sys_dict_data`
VALUES (132, 5, '交易结束', '4', 'sale_order_status', NULL, 'info', 'N', '0', 'sadmin', '2022-02-21 23:38:28', 'sadmin',
        '2022-03-08 00:16:44', NULL);
INSERT INTO `sys_dict_data`
VALUES (133, 4, '交易成功', '3', 'sale_order_status', NULL, 'success', 'N', '0', 'sadmin', '2022-02-21 23:38:38', 'sadmin',
        '2022-03-08 00:16:16', NULL);
INSERT INTO `sys_dict_data`
VALUES (135, 0, '手动发货', 'manual', 'pay_mode', NULL, 'info', 'N', '0', 'sadmin', '2022-03-15 17:56:24', 'sadmin',
        '2022-03-15 17:56:30', '手动发货');
INSERT INTO `sys_dict_data`
VALUES (137, 1, '支付宝当面付', 'alipay_f2f', 'pay_mode', NULL, 'primary', 'N', '0', 'sadmin', '2022-03-25 13:48:02',
        'sadmin', '2022-03-25 13:48:09', '支付宝当面付');
INSERT INTO `sys_dict_data`
VALUES (138, 2, '支付宝当面付沙箱', 'alipay_f2f_sandbox', 'pay_mode', NULL, 'info', 'N', '0', 'sadmin', '2022-03-25 13:48:40',
        '', NULL, '支付宝当面付沙箱');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`
(
    `dict_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '字典主键',
    `dict_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
    `dict_type`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`dict_id`) USING BTREE,
    UNIQUE INDEX `dict_type` (`dict_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 109
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典类型表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type`
VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type`
VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type`
VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type`
VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type`
VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type`
VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type`
VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type`
VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type`
VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type`
VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2021-10-28 11:33:48', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type`
VALUES (100, '认证方式', 'sys_auth_type', '0', 'admin', '2021-11-05 11:12:33', 'admin', '2021-11-07 23:09:29', '软件登录验证方式');
INSERT INTO `sys_dict_type`
VALUES (101, '计费方式', 'sys_bill_type', '0', 'admin', '2021-11-05 11:13:39', '', NULL, '软件计费方式');
INSERT INTO `sys_dict_type`
VALUES (102, '绑定类型', 'sys_bind_type', '0', 'admin', '2021-11-05 11:14:34', '', NULL, '机器码绑定类型');
INSERT INTO `sys_dict_type`
VALUES (103, '充值规则', 'sys_charge_rule', '0', 'admin', '2021-11-05 11:15:43', '', NULL, '软件充值规则');
INSERT INTO `sys_dict_type`
VALUES (104, '加密方式', 'sys_encryp_type', '0', 'admin', '2021-11-05 11:16:25', '', NULL, 'API数据加密方式');
INSERT INTO `sys_dict_type`
VALUES (105, '生成规则', 'sys_gen_rule', '0', 'admin', '2021-11-05 11:17:26', '', NULL, '密钥生成规则');
INSERT INTO `sys_dict_type`
VALUES (106, '顶号操作', 'sys_limit_oper', '0', 'admin', '2021-11-05 11:18:12', '', NULL, '软件顶号操作');
INSERT INTO `sys_dict_type`
VALUES (107, '销售订单状态', 'sale_order_status', '0', 'sadmin', '2022-02-21 23:36:35', '', NULL, '销售订单状态');
INSERT INTO `sys_dict_type`
VALUES (108, '支付方式', 'pay_mode', '0', 'sadmin', '2022-03-13 00:54:41', '', NULL, '支付方式');

-- ----------------------------
-- Table structure for sys_login_code
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_code`;
CREATE TABLE `sys_login_code`
(
    `card_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '卡密ID',
    `app_id`      bigint(20)                                                    NULL DEFAULT NULL COMMENT '软件ID',
    `card_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡名称',
    `card_no`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡号',
    `quota`       bigint(20)                                                    NULL DEFAULT NULL COMMENT '额度',
    `price`       decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '价格',
    `expire_time` datetime                                                      NULL DEFAULT NULL COMMENT '过期时间',
    `is_sold`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否售出',
    `on_sale`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否上架',
    `is_charged`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否被充值',
    `charge_time` datetime                                                      NULL DEFAULT NULL COMMENT '充值时间',
    `template_id` bigint(20)                                                    NULL DEFAULT NULL COMMENT '卡类ID',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '卡密状态',
    `create_by`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`card_id`) USING BTREE,
    INDEX `app_id` (`app_id`) USING BTREE,
    INDEX `card_no` (`card_no`) USING BTREE,
    CONSTRAINT `sys_login_code_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `sys_app` (`app_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 124
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '登录码表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_code
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_code_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_code_template`;
CREATE TABLE `sys_login_code_template`
(
    `template_id`        bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '卡类ID',
    `app_id`             bigint(20)                                                    NULL DEFAULT NULL COMMENT '软件ID',
    `card_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡名称',
    `card_no_prefix`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡号前缀',
    `card_no_suffix`     varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡号后缀',
    `card_description`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡描述',
    `quota`              bigint(20)                                                    NULL DEFAULT NULL COMMENT '额度',
    `price`              decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '价格',
    `card_no_len`        int(11)                                                       NULL DEFAULT NULL COMMENT '卡号长度',
    `card_no_gen_rule`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '卡号生成规则',
    `card_no_regex`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '卡号正则',
    `on_sale`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否上架',
    `first_stock`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '优先库存',
    `effective_duration` bigint(20)                                                    NULL DEFAULT NULL COMMENT '有效时长',
    `status`             char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '卡类状态',
    `enable_auto_gen`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '允许自动生成',
    `create_by`          varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`        datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`        datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`             varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`template_id`) USING BTREE,
    INDEX `app_id` (`app_id`) USING BTREE,
    CONSTRAINT `sys_login_code_template_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `sys_app` (`app_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '登录码类别表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_code_template
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`
(
    `info_id`        bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '访问ID',
    `user_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '用户账号',
    `ipaddr`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
    `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
    `browser`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '浏览器类型',
    `os`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '操作系统',
    `status`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
    `msg`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
    `login_time`     datetime                                                      NULL DEFAULT NULL COMMENT '访问时间',
    PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 770
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor`
VALUES (767, 'sadmin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2022-05-08 11:40:27');
INSERT INTO `sys_logininfor`
VALUES (768, 'sadmin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '退出成功', '2022-05-08 12:29:08');
INSERT INTO `sys_logininfor`
VALUES (769, 'sadmin', '127.0.0.1', '内网IP', 'Chrome 8', 'Windows 10', '0', '登录成功', '2022-05-08 12:29:13');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `menu_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `menu_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '菜单名称',
    `parent_id`   bigint(20)                                                    NULL DEFAULT 0 COMMENT '父菜单ID',
    `order_num`   int(4)                                                        NULL DEFAULT 0 COMMENT '显示顺序',
    `path`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
    `component`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
    `query`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
    `is_frame`    int(1)                                                        NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
    `is_cache`    int(1)                                                        NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
    `menu_type`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `visible`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    `perms`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
    `icon`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2104
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`
VALUES (1, '系统管理', 0, 1, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2021-10-28 11:33:47', '',
        NULL, '系统管理目录');
INSERT INTO `sys_menu`
VALUES (2, '系统监控', 0, 5, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2021-10-28 11:33:47',
        'sadmin', '2022-03-22 23:42:51', '系统监控目录');
INSERT INTO `sys_menu`
VALUES (3, '系统工具', 0, 6, 'tool', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2021-10-28 11:33:47', 'sadmin',
        '2022-03-22 23:43:01', '系统工具目录');
INSERT INTO `sys_menu`
VALUES (100, '账号管理', 2006, 2, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin',
        '2021-10-28 11:33:47', 'sadmin', '2022-01-23 22:29:27', '用户管理菜单');
INSERT INTO `sys_menu`
VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin',
        '2021-10-28 11:33:47', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu`
VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table',
        'admin', '2021-10-28 11:33:47', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu`
VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin',
        '2021-10-28 11:33:47', 'sadmin', '2022-03-07 15:34:43', '部门管理菜单');
INSERT INTO `sys_menu`
VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '1', '0', 'system:post:list', 'post', 'admin',
        '2021-10-28 11:33:47', 'sadmin', '2022-02-07 18:59:02', '岗位管理菜单');
INSERT INTO `sys_menu`
VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin',
        '2021-10-28 11:33:47', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu`
VALUES (106, '参数设置', 2094, 10, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit',
        'admin', '2021-10-28 11:33:47', 'sadmin', '2022-03-25 01:27:01', '参数设置菜单');
INSERT INTO `sys_menu`
VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message',
        'admin', '2021-10-28 11:33:47', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu`
VALUES (108, '日志管理', 2006, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2021-10-28 11:33:47', 'sadmin',
        '2022-01-23 22:23:04', '日志管理菜单');
INSERT INTO `sys_menu`
VALUES (109, '在线用户', 2006, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list',
        'online', 'admin', '2021-10-28 11:33:47', 'sadmin', '2022-01-23 22:29:20', '在线用户菜单');
INSERT INTO `sys_menu`
VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin',
        '2021-10-28 11:33:47', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu`
VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid',
        'admin', '2021-10-28 11:33:47', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu`
VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server',
        'admin', '2021-10-28 11:33:47', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu`
VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis',
        'admin', '2021-10-28 11:33:47', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu`
VALUES (114, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin',
        '2021-10-28 11:33:47', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu`
VALUES (115, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin',
        '2021-10-28 11:33:47', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu`
VALUES (116, 'API接口', 2006, 12, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', '0', '0', 'tool:swagger:list',
        'swagger', 'admin', '2021-10-28 11:33:47', 'sadmin', '2022-03-16 23:06:52', '系统接口菜单');
INSERT INTO `sys_menu`
VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list',
        'form', 'admin', '2021-10-28 11:33:47', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu`
VALUES (501, '后台登录', 108, 2, 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0',
        'monitor:logininfor:list', 'logininfor', 'admin', '2021-10-28 11:33:47', 'sadmin', '2022-01-23 22:24:08',
        '登录日志菜单');
INSERT INTO `sys_menu`
VALUES (1001, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1002, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1003, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1004, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1005, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1006, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1007, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1008, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1009, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1010, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1011, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1012, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1013, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1014, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1015, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1016, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1017, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1018, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1019, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1020, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1021, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1022, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1023, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1024, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1025, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1026, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1027, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1028, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1029, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1030, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1031, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1032, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1033, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1034, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1035, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1036, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1037, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1038, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1039, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1040, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1041, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1042, '日志导出', 500, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1043, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1044, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1045, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1049, '任务查询', 110, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1050, '任务新增', 110, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1051, '任务修改', 110, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1052, '任务删除', 110, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1053, '状态修改', 110, 5, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1054, '任务导出', 110, 7, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin',
        '2021-10-28 11:33:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1055, '生成查询', 115, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1056, '生成修改', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1057, '生成删除', 115, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1058, '导入代码', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1059, '预览代码', 115, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1060, '生成代码', 115, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2021-10-28 11:33:47',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2006, '验证管理', 0, 2, 'verify', NULL, NULL, 1, 0, 'M', '0', '0', '', 'validCode', 'admin', '2021-11-04 18:41:19',
        'sadmin', '2022-03-12 12:29:24', '');
INSERT INTO `sys_menu`
VALUES (2008, '软件管理', 2006, 3, 'app', 'system/app/index', NULL, 1, 0, 'C', '0', '0', 'system:app:list', 'modular',
        'admin', '2021-11-05 10:46:41', 'sadmin', '2022-01-23 22:29:46', '软件菜单');
INSERT INTO `sys_menu`
VALUES (2009, '软件查询', 2008, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:app:query', '#', 'admin',
        '2021-11-05 10:46:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2010, '软件新增', 2008, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:app:add', '#', 'admin',
        '2021-11-05 10:46:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2011, '软件修改', 2008, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:app:edit', '#', 'admin',
        '2021-11-05 10:46:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2012, '软件删除', 2008, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:app:remove', '#', 'admin',
        '2021-11-05 10:46:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2013, '软件导出', 2008, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:app:export', '#', 'admin',
        '2021-11-05 10:46:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2014, '设备管理', 2006, 5, 'deviceCode', 'system/deviceCode/index', NULL, 1, 0, 'C', '0', '0',
        'system:deviceCode:list', 'monitor', 'admin', '2021-12-28 14:52:11', 'sadmin', '2022-01-23 22:30:21',
        '机器码管理菜单');
INSERT INTO `sys_menu`
VALUES (2015, '设备查询', 2014, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:deviceCode:query', '#', 'admin',
        '2021-12-28 14:52:11', 'admin', '2021-12-28 14:54:02', '');
INSERT INTO `sys_menu`
VALUES (2016, '设备新增', 2014, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:deviceCode:add', '#', 'admin',
        '2021-12-28 14:52:11', 'admin', '2021-12-28 14:54:09', '');
INSERT INTO `sys_menu`
VALUES (2017, '设备修改', 2014, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:deviceCode:edit', '#', 'admin',
        '2021-12-28 14:52:11', 'admin', '2021-12-28 14:54:15', '');
INSERT INTO `sys_menu`
VALUES (2018, '设备删除', 2014, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:deviceCode:remove', '#', 'admin',
        '2021-12-28 14:52:11', 'admin', '2021-12-28 14:54:23', '');
INSERT INTO `sys_menu`
VALUES (2019, '设备导出', 2014, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:deviceCode:export', '#', 'admin',
        '2021-12-28 14:52:11', 'admin', '2021-12-28 14:54:28', '');
INSERT INTO `sys_menu`
VALUES (2020, '软件登录', 108, 10, 'appLogininfor', 'system/appLogininfor/index', NULL, 1, 0, 'C', '0', '0',
        'system:appLogininfor:list', 'logininfor', 'admin', '2021-12-29 19:16:32', 'sadmin', '2022-01-23 22:24:13',
        '系统访问记录菜单');
INSERT INTO `sys_menu`
VALUES (2021, '登录日志查询', 2020, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appLogininfor:query', '#', 'admin',
        '2021-12-29 19:16:32', 'admin', '2021-12-31 12:41:32', '');
INSERT INTO `sys_menu`
VALUES (2022, '登录日志新增', 2020, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appLogininfor:add', '#', 'admin',
        '2021-12-29 19:16:32', 'admin', '2021-12-31 12:41:37', '');
INSERT INTO `sys_menu`
VALUES (2023, '登录日志修改', 2020, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appLogininfor:edit', '#', 'admin',
        '2021-12-29 19:16:32', 'admin', '2021-12-31 12:41:43', '');
INSERT INTO `sys_menu`
VALUES (2024, '登录日志删除', 2020, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appLogininfor:remove', '#', 'admin',
        '2021-12-29 19:16:32', 'admin', '2021-12-31 12:41:49', '');
INSERT INTO `sys_menu`
VALUES (2025, '登录日志导出', 2020, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appLogininfor:export', '#', 'admin',
        '2021-12-29 19:16:32', 'admin', '2021-12-31 12:41:54', '');
INSERT INTO `sys_menu`
VALUES (2026, '软件版本', 2006, 10, 'appVersion', 'system/appVersion/index', NULL, 1, 0, 'C', '1', '0',
        'system:appVersion:list', '#', 'admin', '2021-12-31 12:32:33', 'sadmin', '2022-02-01 19:55:42', '软件版本信息菜单');
INSERT INTO `sys_menu`
VALUES (2027, '软件版本查询', 2026, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appVersion:query', '#', 'admin',
        '2021-12-31 12:32:33', 'admin', '2021-12-31 12:40:52', '');
INSERT INTO `sys_menu`
VALUES (2028, '软件版本新增', 2026, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appVersion:add', '#', 'admin',
        '2021-12-31 12:32:33', 'admin', '2021-12-31 12:40:57', '');
INSERT INTO `sys_menu`
VALUES (2029, '软件版本修改', 2026, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appVersion:edit', '#', 'admin',
        '2021-12-31 12:32:33', 'admin', '2021-12-31 12:41:02', '');
INSERT INTO `sys_menu`
VALUES (2030, '软件版本删除', 2026, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appVersion:remove', '#', 'admin',
        '2021-12-31 12:32:33', 'admin', '2021-12-31 12:41:06', '');
INSERT INTO `sys_menu`
VALUES (2031, '软件版本导出', 2026, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appVersion:export', '#', 'admin',
        '2021-12-31 12:32:33', 'admin', '2021-12-31 12:41:11', '');
INSERT INTO `sys_menu`
VALUES (2032, '软件用户', 2006, 4, 'appUser', 'system/appUser/index', NULL, 1, 0, 'C', '0', '0', 'system:appUser:list',
        'peoples', 'admin', '2021-12-31 12:32:50', 'sadmin', '2022-01-23 22:30:01', '软件用户菜单');
INSERT INTO `sys_menu`
VALUES (2033, '软件用户查询', 2032, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUser:query', '#', 'admin',
        '2021-12-31 12:32:50', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2034, '软件用户新增', 2032, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUser:add', '#', 'admin',
        '2021-12-31 12:32:50', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2035, '软件用户修改', 2032, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUser:edit', '#', 'admin',
        '2021-12-31 12:32:50', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2036, '软件用户删除', 2032, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUser:remove', '#', 'admin',
        '2021-12-31 12:32:51', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2037, '软件用户导出', 2032, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUser:export', '#', 'admin',
        '2021-12-31 12:32:51', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2038, '卡类管理', 2068, 6, 'cardTemplate', 'system/cardTemplate/index', NULL, 1, 0, 'C', '0', '0',
        'system:cardTemplate:list', 'component', 'admin', '2021-12-31 12:33:05', 'sadmin', '2022-01-23 20:57:33',
        '卡密模板菜单');
INSERT INTO `sys_menu`
VALUES (2039, '卡密模板查询', 2038, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:cardTemplate:query', '#', 'admin',
        '2021-12-31 12:33:05', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2040, '卡密模板新增', 2038, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:cardTemplate:add', '#', 'admin',
        '2021-12-31 12:33:05', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2041, '卡密模板修改', 2038, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:cardTemplate:edit', '#', 'admin',
        '2021-12-31 12:33:05', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2042, '卡密模板删除', 2038, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:cardTemplate:remove', '#', 'admin',
        '2021-12-31 12:33:05', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2043, '卡密模板导出', 2038, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:cardTemplate:export', '#', 'admin',
        '2021-12-31 12:33:05', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2044, '卡密管理', 2068, 7, 'card', 'system/card/index', NULL, 1, 0, 'C', '0', '0', 'system:card:list', 'copy',
        'admin', '2021-12-31 12:33:17', 'sadmin', '2022-01-23 20:57:47', '卡密菜单');
INSERT INTO `sys_menu`
VALUES (2045, '卡密查询', 2044, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:card:query', '#', 'admin',
        '2021-12-31 12:33:17', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2046, '卡密新增', 2044, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:card:add', '#', 'admin',
        '2021-12-31 12:33:17', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2047, '卡密修改', 2044, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:card:edit', '#', 'admin',
        '2021-12-31 12:33:17', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2048, '卡密删除', 2044, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:card:remove', '#', 'admin',
        '2021-12-31 12:33:17', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2049, '卡密导出', 2044, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:card:export', '#', 'admin',
        '2021-12-31 12:33:17', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2050, '设备管理(用户)', 2006, 3, 'appUserDeviceCode', 'system/appUserDeviceCode/index', NULL, 1, 0, 'C', '1', '0',
        'system:appUserDeviceCode:list', '#', 'admin', '2021-12-31 12:33:36', 'admin', '2021-12-31 12:38:43',
        '软件用户与设备码关联菜单');
INSERT INTO `sys_menu`
VALUES (2051, '软件用户与设备码关联查询', 2050, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUserDeviceCode:query', '#',
        'admin', '2021-12-31 12:33:36', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2052, '软件用户与设备码关联新增', 2050, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUserDeviceCode:add', '#', 'admin',
        '2021-12-31 12:33:36', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2053, '软件用户与设备码关联修改', 2050, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUserDeviceCode:edit', '#',
        'admin', '2021-12-31 12:33:36', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2054, '软件用户与设备码关联删除', 2050, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUserDeviceCode:remove', '#',
        'admin', '2021-12-31 12:33:36', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2055, '软件用户与设备码关联导出', 2050, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:appUserDeviceCode:export', '#',
        'admin', '2021-12-31 12:33:36', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2056, '单码类别', 2069, 8, 'loginCodeTemplate', 'system/loginCodeTemplate/index', NULL, 1, 0, 'C', '0', '0',
        'system:loginCodeTemplate:list', 'file-common', 'admin', '2022-01-06 14:06:01', 'sadmin', '2022-01-23 21:20:57',
        '登录码类别菜单');
INSERT INTO `sys_menu`
VALUES (2057, '登录码类别查询', 2056, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCodeTemplate:query', '#', 'admin',
        '2022-01-06 14:06:01', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2058, '登录码类别新增', 2056, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCodeTemplate:add', '#', 'admin',
        '2022-01-06 14:06:01', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2059, '登录码类别修改', 2056, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCodeTemplate:edit', '#', 'admin',
        '2022-01-06 14:06:01', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2060, '登录码类别删除', 2056, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCodeTemplate:remove', '#', 'admin',
        '2022-01-06 14:06:01', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2061, '登录码类别导出', 2056, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCodeTemplate:export', '#', 'admin',
        '2022-01-06 14:06:01', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2062, '单码管理', 2069, 9, 'loginCode', 'system/loginCode/index', NULL, 1, 0, 'C', '0', '0',
        'system:loginCode:list', 'file', 'admin', '2022-01-06 21:17:54', 'sadmin', '2022-01-23 21:21:04', '登录码菜单');
INSERT INTO `sys_menu`
VALUES (2063, '登录码查询', 2062, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCode:query', '#', 'admin',
        '2022-01-06 21:17:54', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2064, '登录码新增', 2062, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCode:add', '#', 'admin',
        '2022-01-06 21:17:54', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2065, '登录码修改', 2062, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCode:edit', '#', 'admin',
        '2022-01-06 21:17:54', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2066, '登录码删除', 2062, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCode:remove', '#', 'admin',
        '2022-01-06 21:17:54', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2067, '登录码导出', 2062, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:loginCode:export', '#', 'admin',
        '2022-01-06 21:17:54', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2068, '账号模式', 2006, 6, 'accountMode', NULL, NULL, 1, 0, 'M', '0', '0', '', 'user-filling', 'admin',
        '2022-01-23 20:55:38', 'sadmin', '2022-02-01 19:51:35', '');
INSERT INTO `sys_menu`
VALUES (2069, '单码模式', 2006, 7, 'cardMode', NULL, NULL, 1, 0, 'M', '0', '0', '', 'input', 'sadmin',
        '2022-01-23 20:58:12', 'sadmin', '2022-01-23 22:30:38', '');
INSERT INTO `sys_menu`
VALUES (2070, '授权许可', 1, 10, 'license', 'system/license/index', NULL, 1, 0, 'C', '0', '0', '', 'import', 'sadmin',
        '2022-01-30 14:55:30', 'sadmin', '2022-05-08 12:29:28', '');
INSERT INTO `sys_menu`
VALUES (2071, '销售订单', 2077, 1, 'saleOrder', 'sale/saleOrder/index', NULL, 1, 0, 'C', '0', '0', 'sale:saleOrder:list',
        'file', 'admin', '2022-02-21 22:49:53', 'sadmin', '2022-02-26 17:49:48', '销售订单菜单');
INSERT INTO `sys_menu`
VALUES (2072, '销售订单查询', 2071, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleOrder:query', '#', 'admin',
        '2022-02-21 22:49:54', 'sadmin', '2022-02-21 22:51:48', '');
INSERT INTO `sys_menu`
VALUES (2073, '销售订单新增', 2071, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleOrder:add', '#', 'admin',
        '2022-02-21 22:49:54', 'sadmin', '2022-02-21 22:51:57', '');
INSERT INTO `sys_menu`
VALUES (2074, '销售订单修改', 2071, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleOrder:edit', '#', 'admin',
        '2022-02-21 22:49:54', 'sadmin', '2022-02-21 22:52:04', '');
INSERT INTO `sys_menu`
VALUES (2075, '销售订单删除', 2071, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleOrder:remove', '#', 'admin',
        '2022-02-21 22:49:54', 'sadmin', '2022-02-21 22:52:12', '');
INSERT INTO `sys_menu`
VALUES (2076, '销售订单导出', 2071, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleOrder:export', '#', 'admin',
        '2022-02-21 22:49:54', 'sadmin', '2022-02-21 22:52:19', '');
INSERT INTO `sys_menu`
VALUES (2077, '销售管理', 0, 3, 'sale', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'file', 'sadmin', '2022-02-26 17:49:26', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (2084, '订单商品', 2077, 2, 'saleGoods', 'sale/saleGoods/index', NULL, 1, 0, 'C', '1', '0', 'sale:saleGoods:list',
        'success-filling', 'admin', '2022-03-01 23:38:08', 'sadmin', '2022-03-15 00:48:43', '订单商品菜单');
INSERT INTO `sys_menu`
VALUES (2085, '订单商品查询', 2084, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleGoods:query', '#', 'admin',
        '2022-03-01 23:38:08', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2086, '订单商品新增', 2084, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleGoods:add', '#', 'admin',
        '2022-03-01 23:38:08', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2087, '订单商品修改', 2084, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleGoods:edit', '#', 'admin',
        '2022-03-01 23:38:08', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2088, '订单商品删除', 2084, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleGoods:remove', '#', 'admin',
        '2022-03-01 23:38:08', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2089, '订单商品导出', 2084, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'sale:saleGoods:export', '#', 'admin',
        '2022-03-01 23:38:08', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2093, '网站设置', 2094, 1, 'website', 'system/website/index', NULL, 1, 0, 'C', '0', '0', '', 'setting-filling',
        'sadmin', '2022-03-22 19:40:27', 'sadmin', '2022-03-25 01:26:45', '');
INSERT INTO `sys_menu`
VALUES (2094, '系统配置', 0, 4, 'sysConfig', NULL, NULL, 1, 0, 'M', '0', '0', '', 'setting', 'sadmin',
        '2022-03-22 23:42:34', 'sadmin', '2022-04-03 14:06:12', '');
INSERT INTO `sys_menu`
VALUES (2095, '支付配置', 2094, 2, 'payment', 'system/payment/index', NULL, 1, 0, 'C', '0', '0', 'system:payment:list',
        'money', 'admin', '2022-03-24 21:27:40', 'sadmin', '2022-03-25 01:26:53', '支付配置菜单');
INSERT INTO `sys_menu`
VALUES (2096, '支付配置查询', 2095, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:payment:query', '#', 'admin',
        '2022-03-24 21:27:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2097, '支付配置新增', 2095, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:payment:add', '#', 'admin',
        '2022-03-24 21:27:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2098, '支付配置修改', 2095, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:payment:edit', '#', 'admin',
        '2022-03-24 21:27:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2099, '支付配置删除', 2095, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:payment:remove', '#', 'admin',
        '2022-03-24 21:27:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2100, '支付配置导出', 2095, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'system:payment:export', '#', 'admin',
        '2022-03-24 21:27:40', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2101, '仪表盘', 0, 0, 'dashboard', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'dashboard', 'sadmin',
        '2022-04-01 22:24:09', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (2102, '销售数据', 2101, 1, 'saleView', 'system/dashboard/sale/index', NULL, 1, 0, 'C', '0', '0',
        'system:dashboard:sale', 'shopping', 'sadmin', '2022-04-01 22:27:34', 'sadmin', '2022-04-03 14:05:24', '');
INSERT INTO `sys_menu`
VALUES (2103, '软件数据', 2101, 2, 'appView', 'system/dashboard/app/index', NULL, 1, 0, 'C', '0', '0',
        'system:dashboard:app', 'modular', 'sadmin', '2022-04-03 14:03:56', 'sadmin', '2022-04-03 14:05:31', '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`
(
    `notice_id`      int(4)                                                        NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `notice_title`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '公告标题',
    `notice_type`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NOT NULL COMMENT '公告类型（1通知 2公告）',
    `notice_content` longblob                                                      NULL COMMENT '公告内容',
    `status`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '通知公告表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice`
VALUES (1, '商城公告', '0',
        0x3C7020636C6173733D22716C2D616C69676E2D63656E746572223E3C7374726F6E673EE681ADE5969CE682A8E7BD91E7AB99E690ADE5BBBAE68890E58A9FEFBC81E6849FE8B0A2E682A8E98089E68BA9E7BAA2E58FB6E7BD91E7BB9CE9AA8CE8AF81E38082E7BAA2E58FB6E7BD91E7BB9CE9AA8CE8AF81E887B4E58A9BE4BA8EE68993E980A0E8BDAFE4BBB6E68E88E69D83E7AEA1E79086E8AEA1E8B4B9E99480E594AEE4B880E7AB99E5BC8FE8A7A3E586B3E696B9E6A1883C2F7374726F6E673E3C2F703E3C7020636C6173733D22716C2D616C69676E2D63656E746572223E3C7374726F6E673EE5AE98E696B95151E7BEA4EFBC9A39343731343433393620E6889620E5BEAEE4BFA1EFBC9A636F6F7264736F66743C2F7374726F6E673E3C2F703E,
        '0', 'admin', '2022-03-21 00:11:49', 'sadmin', '2022-05-07 21:32:59', NULL);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`
(
    `oper_id`        bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '日志主键',
    `title`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '模块标题',
    `business_type`  int(2)                                                         NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
    `method`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '方法名称',
    `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '请求方式',
    `operator_type`  int(1)                                                         NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
    `oper_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '操作人员',
    `dept_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '部门名称',
    `oper_url`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '请求URL',
    `oper_ip`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '主机地址',
    `oper_location`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '操作地点',
    `oper_param`     varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
    `json_result`    varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
    `status`         int(1)                                                         NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
    `error_msg`      varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
    `oper_time`      datetime                                                       NULL DEFAULT NULL COMMENT '操作时间',
    PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4465
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_payment
-- ----------------------------
DROP TABLE IF EXISTS `sys_payment`;
CREATE TABLE `sys_payment`
(
    `pay_id`      bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '支付ID',
    `code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付编码',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付名称',
    `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `mobile`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '移动端',
    `pc`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '电脑端',
    `icon`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
    `config`      longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '配置',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '状态',
    `create_by`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`pay_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '支付配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_payment
-- ----------------------------
INSERT INTO `sys_payment`
VALUES (1, 'alipay_f2f', '支付宝当面付', '支付宝当面付', 'N', 'Y', 'pay-alipay',
        '{\"privateKey\":\"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDIXCCEYDTnYTxxxMffAew3RJR+zijQTxRhlj1npasjvVRW7rxQWGx8N5DX2e7do+WrDdEDAlwHazGljXAqfjALkRq/OLcFEqm7x/9ankJhr7xoNhY333pIx+81Zb9q7garEI2BvNjjboWAenP0c+dhoNizwpxg78BqDOSKucPYCVA2RivedGJHWnzgXq+YTRAEVZdkiPXZH+e3VyqOCv07NiT7DzrP3rFQufztdMddxte/efQAm5VIKPsr07xdm15e3JZes8l7XHrNb7gu0xvSDfD9xJWpJR9Sr5rCRmVR6Y5JBZ+QGtOVrN7Bgr9XgwvJio0ZjB4v6/ymdtzdYcERAgMBAAECggEAFqUK68svz4LW4QjbiiHef7SZj+dfB4QYipr/X6qCuCxazuR2liIYSMXC8hJog9ZVS8ro940Zt6Du4IYmyjau2W/R9RDE5qbgVh/ZhXVjjUTeZ2zNgA0a9gTazU8tnjk+ubDKPYKJhNLl9cphNpyu5wLV2yNAp1gRiCri3ab3MoBPnKJa3y5a+nGIabUG/PL8211A23HFHz+kDx3YIfACmZpp/+TzAePKoEBptkUBAa0QweLwuWngUKmLV7m8RMeKToHARKq4LKbUqZcxfmA/IwiWMGXHRwrMHbupm6dN2sVWMGC2Kxe0jyKWBAch5hSnjcRPOPGBl9Gb3q+V/tScEQKBgQD1LIvOFfKOra8ikz/T7CYUrID4bRhQHaoO6Cawv/Cs1CzLEwijTexn9hDOiP+5mS7wCL7w6QudkrJmS1ZEJY3TbKKSXBb0kkVrIZ3KlGi0QZNtLt+XRELHcB3umocgwKLqw0Yix8mgeC6r7rocE4sM+H3yL3bGAEsQRoYYUw52jQKBgQDRNQAp9Neai6dzw9wqBm3FcuxbekULleZV0/8ukkVtwkeKsnTh2xapkq8yPWCnGHj953kmilJaZiYEgKWzKex2y4PqskqLl4YZBdvYMI+/jJ4Ak8KdqVlD7KszsjSE0MJw3VTPfvJW25QSLPw05uiydG2WESXP3NiLUJRKhb4FlQKBgBLSK51Tc/5d+O7XjPPQ0g+OOoxXm6Ey1cY1LhstcOVjmFiyilw29Cn66slgHPl7d+33TekiisC67TULHYE3vM55LXW82gpGXEvgFcPiZrNHwXCFQ6bSF6pFwhZ6CFuMTjVlbjHnUmQeNb7/IYxcN7V0Mf7wg9apWRnTwCGH5rlVAoGAChmg9GWZsyBi6TfffTfqPMoblx8EDlciU6p0e28cYvwqMAwFkJHfOjiWtLo53FdWIAv40V+EMlEULMt5NHklrWaN69rHto2OL88Umg9eIUVMq4J2tt3iLWFTsp874d2iRYip+4qJcKAROf9p/bPYMCVm1QPm624iFjfBsQdb8TECgYEA2bmVpKjkGejydYELVhBXlrddPGteIqwSCycy2JqZ720B7ziiaOYeEwjTc2O3+J3Mt4F6ubLudb/BAfE6nDZatviLFfCU8KTrP7dmjd9Ou0g+M9BOpjIZMhu3LRH1j2mfQ8FxBhnl2jAtADiMw3drzE1KJDxFfd0o9rk7r+58vhw=\",\"notifyUrl\":\"\",\"appId\":\"2021003122648690\",\"alipayPublicKey\":\"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs3TETDWmn6Giiy4tMqGJ+UoQc7Nb9KSavLyeg2RTDOjRhV13lQ9UmcdElh4GQK38ZfwIPVw8plI3DHRuJCkvOvhD+WHbL3rzgYLLgUNDWj6EigJ+l3Tupub+802CGpgh4LbrQldruAFdpk+uR04tl3KjEtJ1Z4XiJZAKdrEVYyCkHxhy7Ji3YNQecrZ+kI5GQ+uhBJzFeo0IKE0Ep4yug4Dk9gGFIc1H+xO2scahOM0yYOju7IBASgr3Y2/g7g8n2BmY+hnudt/BX9ERrcOvN0MDU2T0LOdzHxl1WhgEPK2rJRB72CwlaThZv+OX/m5uLAB23B5c7XKN/p0P7wpm/QIDAQAB\"}',
        '0', '', NULL, 'sadmin', '2022-05-08 11:44:01', '支付宝官方接口');
INSERT INTO `sys_payment`
VALUES (2, 'alipay_f2f_sandbox', '支付宝当面付沙箱', '支付宝当面付沙箱', 'N', 'Y', 'pay-alipay',
        '{\"privateKey\":\"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0/phi1ABrXfKPbRqkt/w9KBOVh0RO6fCmRxbWxZoy23HsPJB4l9/J55Npaduz9jVdpJWhlIH76TQbL4Aw+vX/bap6mRCS7qy16XovELkqhBEq9kMVz93zuak4lBpJWY5NiMMXGyIdO+zCMv8r+bIS9VlFBPu/dm5YOMQ4FXiNWU/yVlMeit4YMgw6G3IwmiHF/794s3d2m2Kfr2mOPNyx0wSbXMpiCA65Jw5A1iX0LNtCCkkeuvAH99oDyQwirlMhpK/LJr6j8dns7JFGkAtPEyXWuOqdVXikjtPLecV/YOMEym1O0Lfs5aeYVRV2o4ecja/Pdaz/JJBJKhygpOTJAgMBAAECggEBAJbSoYGZUFAoFXzXWiBxAMylnMw5z/5Ci7rD+pA2UeyXWTOWtH0Jcf757qklAWPRg17pS5c9/aNCDZ2p05T9TAjyBeHrsxf9tAZS7PJTaTm4m+XFGNoQQdBbolv3boA5FJAfqxKSFbduvDiH7oNiq7WIpj8RjAdcVU9G3pwtqCuAG0XtWqeQskIr4j7O5e/DeOqsooDIqeVpBNVor47WoJ38fsY1Deixyockag00nSXaT5GBg7pD1ReXBE+l+l/WDah2XDLEr+eW8SyhOJbaP8N46hmc5M6nlk/irQU5Vb1RCVQVOlkNXAWbC7+0xiiSqv3C9c/vYeZIcotT2FsmhJECgYEA9oDafIUTq+eB9FP4sIWPDxripaeclWsbsbYZ0IQMuCwckc767vXVqhQOcfTbmnKPN94oQ3uO7jIBNNGJxFe3nnrJLJO0O2IFcQ7rJWdpaD3sfCCAKvoWJYRk2FE1EXOFimA/CC19R2WWJoAD/f3+t9GUj9ZRi2OZE8Xr3Pr08N8CgYEAu/eouWHHoIEi01Dgc49caMKs/IDNx/jY4GCRUXPpYYiOuzzKw/BZ+qAdDH3vqJ083Eeoi8xGZF9Fxc9Atvl7SHzDOkWjI43LEVeSgbcy+zRvq3szL+YXROpO3DiX3c9WCW+BmI1PqmqDpJhvqjmLbPUKPv3cU8HcHAYcMwOdF1cCgYAvLYQjdtjH+tv9ZiDfsAAsVOnx6H1of4JiZcbVCKDikta49VNDbtuA3KvTFZj+G1TbzXIJUFmPrxRaBoyGfn9PHpLoLDC/eMgv1jodA4jCAbAEJbhCAXFBpvAiEpDEkUaKsFb/+qzSgFfXcILTFsysY7k6OjuLIPnINgYpWgKNIwKBgGcevW/GlvAVKHfp3NlJAxduBd0ZBMv6V3DxSYf4IUci1bse5NaN269Fe+pIhNxqNuNaZLsdPFkAc5TL2OMJB3uDBs/HOHLe7VL8SiHj0ZJC+CiJlFFo18c1DEKAwcAsaTUP+Xcpv1Tszn/UKR6oJzeFTzOzrdY9enXdXEcYamxNAoGAQTS/WT0wzvebALsDcJptn7KeIbzUDEo4mO3nHd5lrMuEYEjK8HwSvqUWip7FgniClXmMiZ76SH8L4KyImwgkFsQrppKCz486vJI7nAtND07fGlOVyugTSxN4AktSU2hFZG5Y1kXMOwSlVL7mojk12jGOZt3cdxVBckcKBtHhHL8=\",\"notifyUrl\":\"\",\"appId\":\"2016102000727957\",\"alipayPublicKey\":\"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgvhIg0spf1cPGjqyNB6Ev2iL3RKG3XPeeH8a6r43I1yLs7OyDoR9eBhS0R2Uwl7yaK6g3mFBPfhLulAfz7Sxymf+H01k+cR2fkjg1oVw7I7dr2xunyZs8oKwKe0WMeEE10T7bJrubp3UAVrV1vKnWyeTMVt/J6+T9+Geo9fDg49VXuwOvTxe4eeZS+QAyhcWYMCk3Amy/ZpRRa4qJ50LFtKeF52CtCWhENW5uQUNadnyv091ssb0nZZAoM02EnfeeC6UxW+x7uPr4QR265+L3TC7c0+21U1OU5iXHI8caX4wprQDxaMnuqJMm0ki2Gri6os+GT051VZ7vJuZW4MJ5QIDAQAB\"}',
        '0', 'sadmin', '2022-03-25 01:14:50', 'sadmin', '2022-05-08 12:28:40', '支付宝官方沙箱接口');

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`
(
    `post_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
    `post_code`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '岗位编码',
    `post_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '岗位名称',
    `post_sort`   int(4)                                                        NOT NULL COMMENT '显示顺序',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NOT NULL COMMENT '状态（0正常 1停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post`
VALUES (1, 'manager', '经理', 1, '0', 'admin', '2021-10-28 11:33:46', 'admin', '2021-12-31 12:17:44', '');
INSERT INTO `sys_post`
VALUES (4, 'user', '普通员工', 2, '0', 'admin', '2021-10-28 11:33:46', 'admin', '2021-12-31 12:17:51', '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `role_id`             bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name`           varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '角色名称',
    `role_key`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
    `role_sort`           int(4)                                                        NOT NULL COMMENT '显示顺序',
    `data_scope`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    `menu_check_strictly` tinyint(1)                                                    NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
    `dept_check_strictly` tinyint(1)                                                    NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
    `status`              char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NOT NULL COMMENT '角色状态（0正常 1停用）',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`              varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2021-10-28 11:33:47', '', NULL, '超级管理员');
INSERT INTO `sys_role`
VALUES (2, '软件作者', 'app_author', 3, '5', 1, 1, '0', '0', 'admin', '2021-10-28 11:33:47', 'sadmin',
        '2022-02-08 15:15:02', '普通角色');
INSERT INTO `sys_role`
VALUES (3, '管理员', 'manager', 2, '1', 1, 1, '0', '0', 'admin', '2021-12-31 12:23:37', 'sadmin', '2022-04-22 22:37:35',
        NULL);
INSERT INTO `sys_role`
VALUES (4, '软件用户', 'app_user', 4, '5', 1, 1, '0', '0', 'sadmin', '2022-01-18 01:29:00', 'sadmin', '2022-02-07 17:57:43',
        NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`
(
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色和部门关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu`
VALUES (2, 100);
INSERT INTO `sys_role_menu`
VALUES (2, 109);
INSERT INTO `sys_role_menu`
VALUES (2, 116);
INSERT INTO `sys_role_menu`
VALUES (2, 1001);
INSERT INTO `sys_role_menu`
VALUES (2, 1002);
INSERT INTO `sys_role_menu`
VALUES (2, 1003);
INSERT INTO `sys_role_menu`
VALUES (2, 1004);
INSERT INTO `sys_role_menu`
VALUES (2, 1005);
INSERT INTO `sys_role_menu`
VALUES (2, 1006);
INSERT INTO `sys_role_menu`
VALUES (2, 1007);
INSERT INTO `sys_role_menu`
VALUES (2, 1046);
INSERT INTO `sys_role_menu`
VALUES (2, 1047);
INSERT INTO `sys_role_menu`
VALUES (2, 1048);
INSERT INTO `sys_role_menu`
VALUES (2, 2006);
INSERT INTO `sys_role_menu`
VALUES (2, 2008);
INSERT INTO `sys_role_menu`
VALUES (2, 2009);
INSERT INTO `sys_role_menu`
VALUES (2, 2010);
INSERT INTO `sys_role_menu`
VALUES (2, 2011);
INSERT INTO `sys_role_menu`
VALUES (2, 2012);
INSERT INTO `sys_role_menu`
VALUES (2, 2013);
INSERT INTO `sys_role_menu`
VALUES (2, 2014);
INSERT INTO `sys_role_menu`
VALUES (2, 2015);
INSERT INTO `sys_role_menu`
VALUES (2, 2016);
INSERT INTO `sys_role_menu`
VALUES (2, 2017);
INSERT INTO `sys_role_menu`
VALUES (2, 2018);
INSERT INTO `sys_role_menu`
VALUES (2, 2019);
INSERT INTO `sys_role_menu`
VALUES (2, 2026);
INSERT INTO `sys_role_menu`
VALUES (2, 2027);
INSERT INTO `sys_role_menu`
VALUES (2, 2028);
INSERT INTO `sys_role_menu`
VALUES (2, 2029);
INSERT INTO `sys_role_menu`
VALUES (2, 2030);
INSERT INTO `sys_role_menu`
VALUES (2, 2031);
INSERT INTO `sys_role_menu`
VALUES (2, 2032);
INSERT INTO `sys_role_menu`
VALUES (2, 2033);
INSERT INTO `sys_role_menu`
VALUES (2, 2034);
INSERT INTO `sys_role_menu`
VALUES (2, 2035);
INSERT INTO `sys_role_menu`
VALUES (2, 2036);
INSERT INTO `sys_role_menu`
VALUES (2, 2037);
INSERT INTO `sys_role_menu`
VALUES (2, 2038);
INSERT INTO `sys_role_menu`
VALUES (2, 2039);
INSERT INTO `sys_role_menu`
VALUES (2, 2040);
INSERT INTO `sys_role_menu`
VALUES (2, 2041);
INSERT INTO `sys_role_menu`
VALUES (2, 2042);
INSERT INTO `sys_role_menu`
VALUES (2, 2043);
INSERT INTO `sys_role_menu`
VALUES (2, 2044);
INSERT INTO `sys_role_menu`
VALUES (2, 2045);
INSERT INTO `sys_role_menu`
VALUES (2, 2046);
INSERT INTO `sys_role_menu`
VALUES (2, 2047);
INSERT INTO `sys_role_menu`
VALUES (2, 2048);
INSERT INTO `sys_role_menu`
VALUES (2, 2049);
INSERT INTO `sys_role_menu`
VALUES (2, 2050);
INSERT INTO `sys_role_menu`
VALUES (2, 2051);
INSERT INTO `sys_role_menu`
VALUES (2, 2052);
INSERT INTO `sys_role_menu`
VALUES (2, 2053);
INSERT INTO `sys_role_menu`
VALUES (2, 2054);
INSERT INTO `sys_role_menu`
VALUES (2, 2055);
INSERT INTO `sys_role_menu`
VALUES (2, 2056);
INSERT INTO `sys_role_menu`
VALUES (2, 2057);
INSERT INTO `sys_role_menu`
VALUES (2, 2058);
INSERT INTO `sys_role_menu`
VALUES (2, 2059);
INSERT INTO `sys_role_menu`
VALUES (2, 2060);
INSERT INTO `sys_role_menu`
VALUES (2, 2061);
INSERT INTO `sys_role_menu`
VALUES (2, 2062);
INSERT INTO `sys_role_menu`
VALUES (2, 2063);
INSERT INTO `sys_role_menu`
VALUES (2, 2064);
INSERT INTO `sys_role_menu`
VALUES (2, 2065);
INSERT INTO `sys_role_menu`
VALUES (2, 2066);
INSERT INTO `sys_role_menu`
VALUES (2, 2067);
INSERT INTO `sys_role_menu`
VALUES (2, 2068);
INSERT INTO `sys_role_menu`
VALUES (2, 2069);
INSERT INTO `sys_role_menu`
VALUES (3, 1);
INSERT INTO `sys_role_menu`
VALUES (3, 2);
INSERT INTO `sys_role_menu`
VALUES (3, 100);
INSERT INTO `sys_role_menu`
VALUES (3, 106);
INSERT INTO `sys_role_menu`
VALUES (3, 107);
INSERT INTO `sys_role_menu`
VALUES (3, 108);
INSERT INTO `sys_role_menu`
VALUES (3, 109);
INSERT INTO `sys_role_menu`
VALUES (3, 111);
INSERT INTO `sys_role_menu`
VALUES (3, 112);
INSERT INTO `sys_role_menu`
VALUES (3, 113);
INSERT INTO `sys_role_menu`
VALUES (3, 116);
INSERT INTO `sys_role_menu`
VALUES (3, 500);
INSERT INTO `sys_role_menu`
VALUES (3, 501);
INSERT INTO `sys_role_menu`
VALUES (3, 1001);
INSERT INTO `sys_role_menu`
VALUES (3, 1002);
INSERT INTO `sys_role_menu`
VALUES (3, 1003);
INSERT INTO `sys_role_menu`
VALUES (3, 1004);
INSERT INTO `sys_role_menu`
VALUES (3, 1005);
INSERT INTO `sys_role_menu`
VALUES (3, 1006);
INSERT INTO `sys_role_menu`
VALUES (3, 1007);
INSERT INTO `sys_role_menu`
VALUES (3, 1031);
INSERT INTO `sys_role_menu`
VALUES (3, 1032);
INSERT INTO `sys_role_menu`
VALUES (3, 1033);
INSERT INTO `sys_role_menu`
VALUES (3, 1034);
INSERT INTO `sys_role_menu`
VALUES (3, 1035);
INSERT INTO `sys_role_menu`
VALUES (3, 1036);
INSERT INTO `sys_role_menu`
VALUES (3, 1037);
INSERT INTO `sys_role_menu`
VALUES (3, 1038);
INSERT INTO `sys_role_menu`
VALUES (3, 1039);
INSERT INTO `sys_role_menu`
VALUES (3, 1040);
INSERT INTO `sys_role_menu`
VALUES (3, 1041);
INSERT INTO `sys_role_menu`
VALUES (3, 1042);
INSERT INTO `sys_role_menu`
VALUES (3, 1043);
INSERT INTO `sys_role_menu`
VALUES (3, 1044);
INSERT INTO `sys_role_menu`
VALUES (3, 1045);
INSERT INTO `sys_role_menu`
VALUES (3, 1046);
INSERT INTO `sys_role_menu`
VALUES (3, 1047);
INSERT INTO `sys_role_menu`
VALUES (3, 1048);
INSERT INTO `sys_role_menu`
VALUES (3, 2006);
INSERT INTO `sys_role_menu`
VALUES (3, 2008);
INSERT INTO `sys_role_menu`
VALUES (3, 2009);
INSERT INTO `sys_role_menu`
VALUES (3, 2010);
INSERT INTO `sys_role_menu`
VALUES (3, 2011);
INSERT INTO `sys_role_menu`
VALUES (3, 2012);
INSERT INTO `sys_role_menu`
VALUES (3, 2013);
INSERT INTO `sys_role_menu`
VALUES (3, 2014);
INSERT INTO `sys_role_menu`
VALUES (3, 2015);
INSERT INTO `sys_role_menu`
VALUES (3, 2016);
INSERT INTO `sys_role_menu`
VALUES (3, 2017);
INSERT INTO `sys_role_menu`
VALUES (3, 2018);
INSERT INTO `sys_role_menu`
VALUES (3, 2019);
INSERT INTO `sys_role_menu`
VALUES (3, 2020);
INSERT INTO `sys_role_menu`
VALUES (3, 2021);
INSERT INTO `sys_role_menu`
VALUES (3, 2022);
INSERT INTO `sys_role_menu`
VALUES (3, 2023);
INSERT INTO `sys_role_menu`
VALUES (3, 2024);
INSERT INTO `sys_role_menu`
VALUES (3, 2025);
INSERT INTO `sys_role_menu`
VALUES (3, 2026);
INSERT INTO `sys_role_menu`
VALUES (3, 2027);
INSERT INTO `sys_role_menu`
VALUES (3, 2028);
INSERT INTO `sys_role_menu`
VALUES (3, 2029);
INSERT INTO `sys_role_menu`
VALUES (3, 2030);
INSERT INTO `sys_role_menu`
VALUES (3, 2031);
INSERT INTO `sys_role_menu`
VALUES (3, 2032);
INSERT INTO `sys_role_menu`
VALUES (3, 2033);
INSERT INTO `sys_role_menu`
VALUES (3, 2034);
INSERT INTO `sys_role_menu`
VALUES (3, 2035);
INSERT INTO `sys_role_menu`
VALUES (3, 2036);
INSERT INTO `sys_role_menu`
VALUES (3, 2037);
INSERT INTO `sys_role_menu`
VALUES (3, 2038);
INSERT INTO `sys_role_menu`
VALUES (3, 2039);
INSERT INTO `sys_role_menu`
VALUES (3, 2040);
INSERT INTO `sys_role_menu`
VALUES (3, 2041);
INSERT INTO `sys_role_menu`
VALUES (3, 2042);
INSERT INTO `sys_role_menu`
VALUES (3, 2043);
INSERT INTO `sys_role_menu`
VALUES (3, 2044);
INSERT INTO `sys_role_menu`
VALUES (3, 2045);
INSERT INTO `sys_role_menu`
VALUES (3, 2046);
INSERT INTO `sys_role_menu`
VALUES (3, 2047);
INSERT INTO `sys_role_menu`
VALUES (3, 2048);
INSERT INTO `sys_role_menu`
VALUES (3, 2049);
INSERT INTO `sys_role_menu`
VALUES (3, 2050);
INSERT INTO `sys_role_menu`
VALUES (3, 2051);
INSERT INTO `sys_role_menu`
VALUES (3, 2052);
INSERT INTO `sys_role_menu`
VALUES (3, 2053);
INSERT INTO `sys_role_menu`
VALUES (3, 2054);
INSERT INTO `sys_role_menu`
VALUES (3, 2055);
INSERT INTO `sys_role_menu`
VALUES (3, 2056);
INSERT INTO `sys_role_menu`
VALUES (3, 2057);
INSERT INTO `sys_role_menu`
VALUES (3, 2058);
INSERT INTO `sys_role_menu`
VALUES (3, 2059);
INSERT INTO `sys_role_menu`
VALUES (3, 2060);
INSERT INTO `sys_role_menu`
VALUES (3, 2061);
INSERT INTO `sys_role_menu`
VALUES (3, 2062);
INSERT INTO `sys_role_menu`
VALUES (3, 2063);
INSERT INTO `sys_role_menu`
VALUES (3, 2064);
INSERT INTO `sys_role_menu`
VALUES (3, 2065);
INSERT INTO `sys_role_menu`
VALUES (3, 2066);
INSERT INTO `sys_role_menu`
VALUES (3, 2067);
INSERT INTO `sys_role_menu`
VALUES (3, 2068);
INSERT INTO `sys_role_menu`
VALUES (3, 2069);
INSERT INTO `sys_role_menu`
VALUES (3, 2070);
INSERT INTO `sys_role_menu`
VALUES (3, 2071);
INSERT INTO `sys_role_menu`
VALUES (3, 2072);
INSERT INTO `sys_role_menu`
VALUES (3, 2073);
INSERT INTO `sys_role_menu`
VALUES (3, 2074);
INSERT INTO `sys_role_menu`
VALUES (3, 2075);
INSERT INTO `sys_role_menu`
VALUES (3, 2076);
INSERT INTO `sys_role_menu`
VALUES (3, 2077);
INSERT INTO `sys_role_menu`
VALUES (3, 2093);
INSERT INTO `sys_role_menu`
VALUES (3, 2094);
INSERT INTO `sys_role_menu`
VALUES (3, 2095);
INSERT INTO `sys_role_menu`
VALUES (3, 2096);
INSERT INTO `sys_role_menu`
VALUES (3, 2097);
INSERT INTO `sys_role_menu`
VALUES (3, 2098);
INSERT INTO `sys_role_menu`
VALUES (3, 2099);
INSERT INTO `sys_role_menu`
VALUES (3, 2100);
INSERT INTO `sys_role_menu`
VALUES (3, 2101);
INSERT INTO `sys_role_menu`
VALUES (3, 2102);
INSERT INTO `sys_role_menu`
VALUES (3, 2103);

-- ----------------------------
-- Table structure for sys_sale_order
-- ----------------------------
DROP TABLE IF EXISTS `sys_sale_order`;
CREATE TABLE `sys_sale_order`
(
    `order_id`      bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
    `user_id`       bigint(20)                                                    NULL DEFAULT NULL COMMENT '用户ID',
    `actual_fee`    decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '应付金额',
    `total_fee`     decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '总价格（折扣前）',
    `discount_rule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '折扣规则',
    `discount_fee`  decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '折扣金额',
    `pay_mode`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式',
    `status`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '1未付款2已付款3未发货4已发货5交易成功6交易关闭',
    `contact`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
    `query_pass`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询密码',
    `create_by`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `payment_time`  datetime                                                      NULL DEFAULT NULL COMMENT '支付时间',
    `delivery_time` datetime                                                      NULL DEFAULT NULL COMMENT '发货时间',
    `finish_time`   datetime                                                      NULL DEFAULT NULL COMMENT '订单完成时间',
    `close_time`    datetime                                                      NULL DEFAULT NULL COMMENT '订单关闭时间',
    `update_by`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `expire_time`   datetime                                                      NULL DEFAULT NULL COMMENT '订单过期时间',
    `trade_no`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付平台交易号',
    PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 245
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '销售订单表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_sale_order
-- ----------------------------

-- ----------------------------
-- Table structure for sys_sale_order_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_sale_order_item`;
CREATE TABLE `sys_sale_order_item`
(
    `item_id`       bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id`      bigint(20)                                                    NULL DEFAULT NULL COMMENT '订单ID',
    `template_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '1卡类2登录码类',
    `template_id`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '模板ID',
    `num`           int(11)                                                       NULL DEFAULT NULL COMMENT '购买数量',
    `title`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品标题',
    `price`         decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '商品单价',
    `total_fee`     decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '总价格（折扣前）',
    `discount_rule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '折扣规则',
    `discount_fee`  decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '折扣金额',
    `actual_fee`    decimal(10, 2)                                                NULL DEFAULT NULL COMMENT '应付金额',
    PRIMARY KEY (`item_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 245
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '销售订单详情表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_sale_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for sys_sale_order_item_goods
-- ----------------------------
DROP TABLE IF EXISTS `sys_sale_order_item_goods`;
CREATE TABLE `sys_sale_order_item_goods`
(
    `id`      bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `item_id` bigint(20) NULL DEFAULT NULL COMMENT '订单详情ID',
    `card_id` bigint(20) NULL DEFAULT NULL COMMENT '卡密ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 305
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '订单卡密表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_sale_order_item_goods
-- ----------------------------

-- ----------------------------
-- Table structure for sys_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_student`;
CREATE TABLE `sys_student`
(
    `student_id`       int(11)                                                      NOT NULL AUTO_INCREMENT COMMENT '编号',
    `student_name`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '学生名称',
    `student_age`      int(3)                                                       NULL DEFAULT NULL COMMENT '年龄',
    `student_hobby`    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '爱好（0代码 1音乐 2电影）',
    `student_sex`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT '0' COMMENT '性别（0男 1女 2未知）',
    `student_status`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `student_birthday` datetime                                                     NULL DEFAULT NULL COMMENT '生日',
    PRIMARY KEY (`student_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '学生信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_student
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `user_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `dept_id`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '部门ID',
    `user_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户账号',
    `nick_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户昵称',
    `user_type`   varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '用户邮箱',
    `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '手机号码',
    `sex`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    `avatar`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
    `password`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `login_ip`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
    `login_date`  datetime                                                      NULL DEFAULT NULL COMMENT '最后登录时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`user_id`) USING BTREE,
    INDEX `user_name` (`user_name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (1, 100, 'sadmin', '超级管理员', '00', NULL, NULL, '1', '',
        '$2a$10$xzFG9TaI1Xqh01qktsFkpOYEeaTmtHnqsMsaXUorXnPt5Xt0a9Ir6', '0', '0', '127.0.0.1', '2022-05-08 12:29:12',
        'admin', '2021-10-28 11:33:46', '', '2022-05-08 12:29:13', '管理员');
INSERT INTO `sys_user`
VALUES (2, 100, 'admin', '管理员', '00', '', '', '0', NULL, '$2a$10$XcaedIxwIC4hts5waTN22O3R4BFUMEdzcHy.rSITJLkFUQ/BTRR1i',
        '0', '0', NULL, '2022-05-06 22:58:53', 'admin', '2021-12-31 12:21:59', 'admin', '2022-05-06 22:58:52', NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post`
VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES (1, 1);
INSERT INTO `sys_user_role`
VALUES (2, 3);

-- ----------------------------
-- Table structure for sys_website
-- ----------------------------
DROP TABLE IF EXISTS `sys_website`;
CREATE TABLE `sys_website`
(
    `id`          bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `favicon`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `logo`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `short_name`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `shop_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `domain`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `contact`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `keywords`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL,
    `create_by`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_website
-- ----------------------------
INSERT INTO `sys_website`
VALUES (1, NULL, NULL, '红叶网络验证与软件管理系统', '红叶网络验证', '红叶在线商城', 'http://hy.coordsoft.com', '1179403448@qq.com',
        '网络验证,软件管理,云后台', '软件授权管理计费销售一站式解决方案', NULL, '', NULL, 'admin', '2022-05-05 21:59:16', NULL);

SET FOREIGN_KEY_CHECKS = 1;
