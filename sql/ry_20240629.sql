-- ----------------------------
-- Table structure for advert
-- ----------------------------
DROP TABLE IF EXISTS `advert`;
CREATE TABLE `advert`
(
    `id`                  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '推广营销主键',
    `show_type`           int UNSIGNED NOT NULL COMMENT '展示类型：1. 时间范围  2. 位置枚举',
    `basic_symbol`        varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '每一个广告位基础锁符号（10位大小写字母数字）',
    `store_buy_limit`     int UNSIGNED NOT NULL COMMENT '每个档口可以购买该推广位数量限制',
    `platform_id`         int UNSIGNED NOT NULL COMMENT '推广平台ID PC/APP',
    `type_id`             int UNSIGNED NOT NULL COMMENT '推广类型ID',
    `tab_id`              int UNSIGNED NOT NULL COMMENT '所属tabID',
    `online_status`       int UNSIGNED NOT NULL COMMENT '上线/下线ID',
    `display_type`        int UNSIGNED NOT NULL COMMENT '展示类型：推广图、商品、图和商品',
    `start_price`         decimal(10, 2) UNSIGNED NOT NULL COMMENT '起始竞价价格',
    `play_interval`       int UNSIGNED NOT NULL COMMENT '播放间隔（单位天）',
    `play_round`          int UNSIGNED NOT NULL COMMENT '播放轮次（第一轮、第二轮、第三轮、第四轮）',
    `deadline`            varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '推广购买截止时间',
    `play_num`            int                                                          NOT NULL COMMENT '播放的数量（A/B/C/D/E等）',
    `example_pic_id`      bigint NULL DEFAULT NULL COMMENT '范例文件ID',
    `pic_pixel`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '范例图片尺寸',
    `pic_size`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '范例图片大小',
    `prod_max_num`        int UNSIGNED NULL DEFAULT NULL COMMENT '如果是播放商品或图及商品，最多可选择的商品数量',
    `discount_type`       int UNSIGNED NULL DEFAULT NULL COMMENT '推广折扣类型',
    `discount_amount`     decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '推广折扣金额',
    `discount_start_time` datetime NULL DEFAULT NULL COMMENT '推广折扣开始时间',
    `discount_end_time`   datetime NULL DEFAULT NULL COMMENT '推广折扣结束时间',
    `version`             bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '推广营销' ROW_FORMAT = Dynamic;

INSERT INTO `advert` VALUES (39, 1, 'oCYTRg9Wpk', 1, 1, 1, 1, 1, 1, 300.00, 3, 4, '22:00:00', 5, 121, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:51:52', '', '2025-08-13 22:51:52');
INSERT INTO `advert` VALUES (40, 2, 'tyuG0P6QEl', 4, 1, 2, 1, 1, 2, 300.00, 3, 4, '22:00:00', 4, NULL, '', '', 4, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:52:52', '', '2025-08-13 22:52:52');
INSERT INTO `advert` VALUES (41, 2, 'cWsRqCVIC7', 2, 1, 3, 1, 1, 2, 300.00, 3, 4, '22:00:00', 2, NULL, '', '', 2, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:53:32', '', '2025-08-13 22:53:32');
INSERT INTO `advert` VALUES (42, 2, 'WG7oSnTbTn', 2, 1, 4, 1, 1, 2, 300.00, 3, 4, '22:00:00', 2, NULL, '', '', 2, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:54:06', '', '2025-08-13 22:54:06');
INSERT INTO `advert` VALUES (43, 2, 'CnZcnndkoz', 2, 1, 5, 1, 1, 2, 300.00, 3, 4, '22:00:00', 2, NULL, '', '', 2, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:54:33', '', '2025-08-13 22:54:33');
INSERT INTO `advert` VALUES (44, 2, 'fNn28SYfJT', 2, 1, 6, 1, 1, 2, 300.00, 3, 4, '22:00:00', 2, NULL, '', '', 2, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:54:54', '', '2025-08-13 22:54:54');
INSERT INTO `advert` VALUES (45, 1, 'ENYmwRr7T1', 1, 1, 7, 1, 1, 3, 300.00, 3, 4, '22:00:00', 4, 122, '840*470', '不超过5.00M', 4, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:55:39', '', '2025-08-13 22:55:39');
INSERT INTO `advert` VALUES (46, 1, 'KIMSCC0h48', 1, 1, 8, 1, 1, 1, 300.00, 3, 4, '22:00:00', 5, 123, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:56:27', '', '2025-08-13 22:56:27');
INSERT INTO `advert` VALUES (47, 1, 'iAHKrJlQSX', 1, 1, 9, 1, 1, 1, 300.00, 3, 4, '22:00:00', 2, 124, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:57:13', '', '2025-08-13 22:57:13');
INSERT INTO `advert` VALUES (48, 2, '3I8CDuP3mZ', 2, 1, 10, 1, 1, 2, 300.00, 3, 4, '22:00:00', 2, NULL, '', '', 2, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 22:57:41', '', '2025-08-13 22:57:41');
INSERT INTO `advert` VALUES (50, 1, 'cjHdgozRUw', 5, 1, 12, 1, 1, 2, 450.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 1, '0', '', '2025-08-13 23:00:55', '', '2025-11-13 22:36:27');
INSERT INTO `advert` VALUES (51, 1, 'RNsmF98eoy', 1, 1, 13, 1, 1, 1, 300.00, 3, 4, '22:00:00', 1, 126, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:01:31', '', '2025-08-13 23:01:31');
INSERT INTO `advert` VALUES (52, 1, '1egXOizbf8', 1, 1, 14, 1, 1, 4, 750.00, 15, 4, '22:00:00', 10, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:04:07', '', '2025-08-13 23:04:07');
INSERT INTO `advert` VALUES (53, 2, 'Pi7b4vnguS', 5, 1, 15, 1, 1, 2, 150.00, 3, 4, '22:00:00', 5, NULL, '', '', 5, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:04:47', '', '2025-08-13 23:04:47');
INSERT INTO `advert` VALUES (54, 1, '5g4asEnvfn', 1, 1, 101, 1, 1, 1, 300.00, 3, 4, '22:00:00', 5, 127, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:05:31', '', '2025-08-13 23:05:31');
INSERT INTO `advert` VALUES (55, 1, 'ZfROmE3ddo', 1, 1, 102, 1, 1, 1, 300.00, 3, 4, '22:00:00', 1, 128, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:06:05', '', '2025-08-13 23:06:05');
INSERT INTO `advert` VALUES (56, 1, 'k4QlUkCnvw', 1, 1, 103, 2, 1, 1, 300.00, 3, 4, '22:00:00', 10, 129, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:07:05', '', '2025-08-13 23:07:05');
INSERT INTO `advert` VALUES (57, 1, 'dM98LYYe6Z', 1, 1, 104, 2, 1, 1, 300.00, 3, 4, '22:00:00', 5, 130, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:08:43', '', '2025-08-13 23:08:43');
INSERT INTO `advert` VALUES (58, 2, 'g2MFw171qs', 8, 1, 105, 2, 1, 2, 150.00, 3, 4, '22:00:00', 8, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:09:18', '', '2025-08-13 23:09:18');
INSERT INTO `advert` VALUES (59, 1, '3aEm9h1ho3', 1, 1, 106, 2, 1, 1, 300.00, 3, 4, '22:00:00', 1, 131, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:09:52', '', '2025-08-13 23:09:52');
INSERT INTO `advert` VALUES (60, 2, 'rsb1B3Nt3C', 5, 1, 107, 2, 1, 2, 450.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 1, '0', '', '2025-08-13 23:10:35', '', '2025-11-13 22:36:52');
INSERT INTO `advert` VALUES (61, 1, '61qRD0PX7D', 1, 1, 201, 1, 1, 3, 300.00, 3, 4, '22:00:00', 5, 132, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:11:18', '', '2025-08-13 23:11:18');
INSERT INTO `advert` VALUES (62, 1, 'Fpn9UiUgWa', 1, 1, 202, 5, 1, 1, 300.00, 3, 4, '22:00:00', 1, 133, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:12:10', '', '2025-08-13 23:12:10');
INSERT INTO `advert` VALUES (63, 1, 'Z6P15nhcMt', 1, 1, 203, 5, 1, 4, 300.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 1, '0', '', '2025-08-13 23:13:11', '', '2025-11-11 19:56:35');
INSERT INTO `advert` VALUES (64, 2, 'qlikk4dbHN', 10, 1, 300, 3, 1, 2, 300.00, 3, 4, '22:00:00', 10, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:15:06', '', '2025-08-13 23:15:06');
INSERT INTO `advert` VALUES (65, 2, 'hahWvpAsj4', 5, 1, 401, 4, 1, 2, 150.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 1, '0', '', '2025-08-13 23:15:50', '', '2025-11-13 22:37:10');
INSERT INTO `advert` VALUES (66, 2, 'CzeHJMNe5x', 10, 1, 402, 4, 1, 2, 150.00, 3, 4, '22:00:00', 10, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 1, '0', '', '2025-08-13 23:16:29', '', '2025-11-13 22:37:26');
INSERT INTO `advert` VALUES (67, 2, 'lalX5WN3VK', 10, 1, 403, 4, 1, 2, 50.00, 1, 4, '22:00:00', 10, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:17:00', '', '2025-08-13 23:17:00');
INSERT INTO `advert` VALUES (68, 1, 'NeKk8Ni9f4', 1, 2, 501, 7, 1, 1, 300.00, 3, 4, '22:00:00', 5, 134, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:18:08', '', '2025-08-13 23:18:08');
INSERT INTO `advert` VALUES (69, 2, 'sNivhtSNKp', 5, 2, 502, 7, 1, 2, 150.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:18:29', '', '2025-08-13 23:18:29');
INSERT INTO `advert` VALUES (70, 2, 'jbyM6b4aBT', 5, 2, 503, 7, 1, 2, 150.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:18:50', '', '2025-08-13 23:18:50');
INSERT INTO `advert` VALUES (71, 2, 'oKZpPXqIkI', 5, 2, 504, 7, 1, 2, 150.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:19:13', '', '2025-08-13 23:19:13');
INSERT INTO `advert` VALUES (72, 2, 'pP4a9ycW9Q', 5, 2, 505, 7, 1, 2, 150.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:19:32', '', '2025-08-13 23:19:32');
INSERT INTO `advert` VALUES (73, 2, '6dpebpvCSg', 5, 2, 506, 7, 1, 2, 150.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:19:54', '', '2025-08-13 23:19:54');
INSERT INTO `advert` VALUES (74, 2, 'AsMPO2AR87', 5, 2, 507, 7, 1, 2, 150.00, 3, 4, '22:00:00', 5, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:20:21', '', '2025-08-13 23:20:21');
INSERT INTO `advert` VALUES (75, 1, 'mDbxt8mf4U', 1, 2, 601, 8, 1, 1, 150.00, 3, 4, '22:00:00', 5, 135, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-13 23:20:54', '', '2025-08-13 23:20:54');
INSERT INTO `advert` VALUES (76, 2, 'AIBZ2bw1GG', 10, 2, 602, 10, 1, 2, 150.00, 3, 4, '22:00:00', 10, NULL, '', '', NULL, NULL, NULL, NULL, NULL, 1, '0', '', '2025-08-13 23:21:33', '', '2025-11-13 22:38:02');
INSERT INTO `advert` VALUES (77, 1, 'nLSxTWcCCk', 1, 1, 16, 1, 1, 1, 300.00, 3, 4, '22:00:00', 1, 711, '840*470', '不超过5.00M', NULL, NULL, NULL, NULL, NULL, 0, '0', '', '2025-08-26 14:47:32', '', '2025-08-26 14:47:32');
INSERT INTO `advert` VALUES (78, 2, 'kPsS29GTJI', 5, 1, 404, 4, 1, 2, 300.00, 3, 4, '22:00:00', 5, NULL, '', '', 10, NULL, NULL, NULL, NULL, 1, '0', '', '2025-09-10 18:39:36', '', '2025-11-13 22:37:39');


-- ----------------------------
-- Table structure for advert_round
-- ----------------------------
DROP TABLE IF EXISTS `advert_round`;
CREATE TABLE `advert_round`
(
    `id`                  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '推广营销主键',
    `voucher_date`        date NULL DEFAULT NULL COMMENT '凭证日期',
    `advert_id`           bigint UNSIGNED NOT NULL COMMENT '广告ID',
    `display_type`        int UNSIGNED NOT NULL COMMENT '展示类型：推广图、商品、图和商品',
    `show_type`           int UNSIGNED NOT NULL COMMENT '展示类型：1. 时间范围  2. 位置枚举',
    `type_id`             int UNSIGNED NOT NULL COMMENT '推广类型ID',
    `round_id`            int UNSIGNED NOT NULL COMMENT '轮次ID',
    `symbol`              varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象锁符号（12位或者13位对象锁符号）',
    `launch_status`       int UNSIGNED NOT NULL COMMENT '投放状态（待投放、投放中、已过期、已退订）',
    `start_time`          date                                                         NOT NULL COMMENT '投放开始时间',
    `end_time`            date                                                         NOT NULL COMMENT '投放结束时间',
    `deadline`            varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '推广购买截止时间',
    `position`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '投放位置（A B C D E）',
    `store_id`            bigint UNSIGNED NULL DEFAULT NULL COMMENT '档口ID',
    `start_price`         decimal(10, 2) UNSIGNED NOT NULL COMMENT '起始竞价价格',
    `pay_price`           decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '档口出价',
    `bidding_status`      int UNSIGNED NULL DEFAULT NULL COMMENT '竞价状态 最初是：已出价 之后会变成 竞价成功',
    `bidding_temp_status` int UNSIGNED NULL DEFAULT NULL COMMENT '竞价成功',
    `reject_reason`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核驳回理由',
    `pic_audit_status`    int UNSIGNED NULL DEFAULT NULL COMMENT '图片审核状态（待审核、审核通过、审核驳回）',
    `pic_set_type`        int UNSIGNED NULL DEFAULT NULL COMMENT '图片是否已设置',
    `pic_id`              bigint UNSIGNED NULL DEFAULT NULL COMMENT '图片ID',
    `prod_id_str`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品ID字符串',
    `pic_design_type`     int UNSIGNED NULL DEFAULT NULL COMMENT '图片设计（自主设计、平台设计）',
    `sys_intercept`       int UNSIGNED NULL DEFAULT NULL COMMENT '系统拦截（0 未拦截 1已拦截）',
    `style_type`          int UNSIGNED NULL DEFAULT NULL COMMENT '风格榜风格ID',
    `version`             bigint UNSIGNED NULL DEFAULT NULL COMMENT '版本号',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX                 `idx_advert_round_pay_pos`(`advert_id`, `round_id`, `pay_price`, `position`) USING BTREE,
    INDEX                 `idx_del_flag_launch_status_type_id`(`del_flag`, `launch_status`, `type_id`) USING BTREE,
    INDEX                 `idx_del_flag_launch_status_round_id`(`del_flag`, `launch_status`, `round_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '推广营销轮次位置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for advert_round_record
-- ----------------------------
DROP TABLE IF EXISTS `advert_round_record`;
CREATE TABLE `advert_round_record`
(
    `id`                  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '推广营销主键',
    `show_type`           int UNSIGNED NOT NULL COMMENT '展示类型：1. 时间范围  2. 位置枚举',
    `voucher_date`        date NULL DEFAULT NULL COMMENT '凭证日期',
    `advert_round_id`     bigint UNSIGNED NOT NULL COMMENT '推广营销轮次位置ID',
    `type_id`             int UNSIGNED NOT NULL COMMENT '推广类型ID',
    `advert_id`           bigint UNSIGNED NOT NULL COMMENT '广告ID',
    `round_id`            int UNSIGNED NOT NULL COMMENT '轮次ID',
    `symbol`              varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象锁符号（12位或者13位对象锁符号）',
    `display_type`        int UNSIGNED NOT NULL COMMENT '展示类型：推广图、商品、图和商品',
    `launch_status`       int UNSIGNED NOT NULL COMMENT '投放状态（待投放、投放中、已过期、已退订）',
    `start_time`          date                                                         NOT NULL COMMENT '投放开始时间',
    `end_time`            date                                                         NOT NULL COMMENT '投放结束时间',
    `position`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '投放位置（A B C D E）',
    `store_id`            bigint UNSIGNED NULL DEFAULT NULL COMMENT '档口ID',
    `start_price`         decimal(10, 2) UNSIGNED NOT NULL COMMENT '起始竞价价格',
    `pay_price`           decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '档口出价',
    `pic_set_type`        int UNSIGNED NULL DEFAULT NULL COMMENT '图片是否已设置',
    `bidding_status`      int UNSIGNED NULL DEFAULT NULL COMMENT '竞价状态',
    `bidding_temp_status` int UNSIGNED NULL DEFAULT NULL COMMENT '竞价成功',
    `pic_audit_status`    int UNSIGNED NULL DEFAULT NULL COMMENT '图片审核状态（待审核、审核通过、审核驳回）',
    `reject_reason`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核驳回理由',
    `pic_id`              bigint UNSIGNED NULL DEFAULT NULL COMMENT '图片ID',
    `prod_id_str`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品ID字符串',
    `pic_design_type`     int UNSIGNED NULL DEFAULT NULL COMMENT '图片设计（自主设计、平台设计）',
    `sys_intercept`       int UNSIGNED NULL DEFAULT NULL COMMENT '系统拦截（0 未拦截 1已拦截）',
    `version`             bigint UNSIGNED NULL DEFAULT NULL COMMENT '版本号',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX                 `idx_del_flag_store_id_advert_round_id_voucher_date_type_id`(`del_flag`, `store_id`, `advert_round_id`, `voucher_date`, `type_id`) USING BTREE,
    INDEX                 `idx_del_flag_type_id_round_id_voucher_date_position`(`del_flag`, `type_id`, `round_id`, `voucher_date`, `position`) USING BTREE,
    INDEX                 `idx_del_flag_store_id_voucher_date`(`del_flag`, `store_id`, `voucher_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '推广营销轮次位置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for advert_store_file
-- ----------------------------
DROP TABLE IF EXISTS `advert_store_file`;
CREATE TABLE `advert_store_file`
(
    `id`              bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '推广营销主键',
    `advert_round_id` bigint UNSIGNED NOT NULL COMMENT '推广营销轮次位置ID',
    `pic_own_type`    int UNSIGNED NOT NULL COMMENT '图片归属人  档口 系统',
    `position`        varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '投放位置（A B C D E）',
    `voucher_date`    date                                                         NOT NULL COMMENT '凭证日期',
    `display_type`    int UNSIGNED NOT NULL COMMENT '展示类型：推广图、商品、图和商品',
    `store_id`        bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `pic_id`          bigint UNSIGNED NOT NULL COMMENT '文件ID',
    `type_id`         int UNSIGNED NOT NULL COMMENT '类型ID',
    `version`         bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口上传的推广营销文件' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for daily_prod_tag
-- ----------------------------
DROP TABLE IF EXISTS `daily_prod_tag`;
CREATE TABLE `daily_prod_tag`
(
    `id`            bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '每天商品打标',
    `store_id`      bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_prod_id` bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `type`          tinyint UNSIGNED NOT NULL COMMENT '标签类型',
    `tag`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
    `voucher_date`  date                                                         NOT NULL COMMENT '单据日期',
    `version`       bigint UNSIGNED NULL DEFAULT NULL COMMENT '版本号',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品标签统计' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for daily_sale
-- ----------------------------
DROP TABLE IF EXISTS `daily_sale`;
CREATE TABLE `daily_sale`
(
    `id`            bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '每日销售数据ID',
    `store_id`      bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `sale_amount`   decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '销售金额',
    `refund_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退货金额',
    `sale_num`      int UNSIGNED NULL DEFAULT NULL COMMENT '销售数量',
    `refund_num`    int NULL DEFAULT NULL COMMENT '退货数量',
    `customer_num`  int UNSIGNED NULL DEFAULT NULL COMMENT '拿货人次',
    `storage_num`   int UNSIGNED NULL DEFAULT NULL COMMENT '入库商品数量',
    `voucher_date`  date NOT NULL COMMENT '单据日期',
    `version`       bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '每天档口销量统计' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_daily_sale_store_del_date ON daily_sale(store_id, del_flag, voucher_date);

-- ----------------------------
-- Table structure for daily_sale_customer
-- ----------------------------
DROP TABLE IF EXISTS `daily_sale_customer`;
CREATE TABLE `daily_sale_customer`
(
    `id`            bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '每天档口客户销售ID',
    `store_id`      bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_cus_id`  bigint UNSIGNED NOT NULL COMMENT '档口客户ID',
    `sale_amount`   decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '销售金额',
    `refund_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退货金额',
    `sale_num`      int UNSIGNED NULL DEFAULT NULL COMMENT '销售数量',
    `refund_num`    int NULL DEFAULT NULL COMMENT '退货数量',
    `voucher_date`  date NOT NULL COMMENT '单据日期',
    `version`       bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '每天档口客户销量统计' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_dsc_store_del_date_cus_sale ON daily_sale_customer ( store_id, del_flag, voucher_date, store_cus_id, sale_amount );


-- ----------------------------
-- Table structure for daily_sale_product
-- ----------------------------
DROP TABLE IF EXISTS `daily_sale_product`;
CREATE TABLE `daily_sale_product`
(
    `id`            bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '每天档口商品销售ID',
    `store_id`      bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_prod_id` bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `prod_art_num`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品货号',
    `sale_amount`   decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '销售金额',
    `refund_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退货金额',
    `sale_num`      int UNSIGNED NULL DEFAULT NULL COMMENT '销售数量',
    `refund_num`    int NULL DEFAULT NULL COMMENT '退货数量',
    `voucher_date`  date NOT NULL COMMENT '单据日期',
    `version`       bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品销量统计' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_dsp_store_del_date ON daily_sale_product(store_id, del_flag, voucher_date, store_prod_id);


-- ----------------------------
-- Table structure for daily_store_tag
-- ----------------------------
DROP TABLE IF EXISTS `daily_store_tag`;
CREATE TABLE `daily_store_tag`
(
    `id`           bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口标签ID',
    `store_id`     bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `type`         tinyint UNSIGNED NOT NULL COMMENT '标签类型',
    `tag`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
    `voucher_date` date                                                         NOT NULL COMMENT '单据日期',
    `version`      bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '每天档口标签' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `content`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '反馈内容',
    `contact`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '联系方式',
    `version`     bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user
(
    user_id     bigint(20) not null auto_increment comment '用户ID',
    user_name   varchar(30) not null comment '用户账号',
    nick_name   varchar(30) not null comment '用户昵称',
    user_type   varchar(2)   default '00' comment '用户类型（00系统用户）',
    email       varchar(50)  default '' comment '用户邮箱',
    phonenumber varchar(11)  default '' comment '手机号码',
    sex         char(1)      default '0' comment '用户性别（0男 1女 2未知）',
    avatar      varchar(256) default '' comment '头像地址',
    password    varchar(100) default '' comment '密码',
    status      char(1)      default '0' comment '帐号状态（0正常 1停用）',
    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    login_ip    varchar(128) default '' comment '最后登录IP',
    login_date  datetime comment '最后登录时间',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    version     bigint(20) default 0 comment '版本号',
    primary key (user_id),
    key         `idx_user_name` (`user_name`) USING BTREE,
    key         `idx_phonenumber` (`phonenumber`) USING BTREE,
    key         `idx_email` (`email`) USING BTREE
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user
values (1, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '',
        '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', sysdate(), 'admin',
        sysdate(), '', null, '管理员', 0);


-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role
(
    role_id     bigint(20) not null auto_increment comment '角色ID',
    role_name   varchar(30)  not null comment '角色名称',
    role_key    varchar(100) not null comment '角色权限字符串',
    role_sort   int(4) not null comment '显示顺序',
    store_id    bigint(20) default null comment '档口ID',
    status      char(1)      not null comment '角色状态（0正常 1停用）',
    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    version     bigint(20) default 0 comment '版本号',
    primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role
values (1, '超级管理员', 'admin', 1, null, '0', '0', 'admin', sysdate(), '', null, '超级管理员', 0);
insert into sys_role
values (2, '管理员', 'general_admin', 2, null, '0', '0', 'admin', sysdate(), '', null, '管理员', 0);
insert into sys_role
values (3, '档口供应商', 'store', 3, null, '0', '0', 'admin', sysdate(), '', null, '档口供应商', 0);
insert into sys_role
values (4, '电商卖家', 'seller', 4, null, '0', '0', 'admin', sysdate(), '', null, '电商卖家', 0);
insert into sys_role
values (5, '代发', 'agent', 5, null, '0', '0', 'admin', sysdate(), '', null, '代发', 0);


-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu
(
    menu_id     bigint(20) not null auto_increment comment '菜单ID',
    menu_name   varchar(50) not null comment '菜单名称',
    parent_id   bigint(20) default 0 comment '父菜单ID',
    order_num   int(4) default 0 comment '显示顺序',
    path        varchar(200) default '' comment '路由地址',
    component   varchar(255) default null comment '组件路径',
    query       varchar(255) default null comment '路由参数',
    route_name  varchar(50)  default '' comment '路由名称',
    is_frame    int(1) default 1 comment '是否为外链（0是 1否）',
    is_cache    int(1) default 0 comment '是否缓存（0缓存 1不缓存）',
    menu_type   char(1)      default '' comment '菜单类型（M目录 C菜单 F按钮）',
    visible     char(1)      default 0 comment '菜单状态（0显示 1隐藏）',
    status      char(1)      default 0 comment '菜单状态（0正常 1停用）',
    perms       varchar(100) default null comment '权限标识',
    icon        varchar(100) default '#' comment '菜单图标',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default '' comment '备注',
    primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- 删除标识&版本号
ALTER TABLE `sys_menu`
    ADD COLUMN `del_flag` char(1) default '0' comment '删除标志（0代表存在 2代表删除）';
ALTER TABLE `sys_menu`
    ADD COLUMN `version` bigint(20) default 0 comment '版本号';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '步橘网菜单', 0, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-14 22:54:40', 'admin', '2025-07-14 22:54:40', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1001, '商品管理', 1, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:01:20', 'admin', '2025-07-15 16:01:20', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1002, '商品列表', 1001, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:01:38', 'admin', '2025-07-15 16:01:38', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1003, '发布商品', 1001, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:01:50', 'admin', '2025-07-15 16:01:50', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1004, '打印条码', 1001, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:02:02', 'admin', '2025-07-15 16:02:02', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1006, '条码一键迁移', 1001, 5, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:02:31', 'admin', '2025-07-15 16:02:31', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1007, '进货车列表', 1001, 6, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:02:46', 'admin', '2025-07-15 16:02:46', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1008, '我的收藏', 1001, 7, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:02:55', 'admin', '2025-07-15 16:02:55', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1009, '推广营销', 1, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:03:11', 'admin', '2025-07-15 16:03:11', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1010, '推广订购', 1009, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:03:26', 'admin', '2025-07-15 16:03:26', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1011, '已购推广', 1009, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:03:35', 'admin', '2025-07-15 16:03:35', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1012, '代发管理', 1, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:03:52', 'admin', '2025-07-15 16:03:52', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1013, '档口直发', 1012, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:04:04', 'admin', '2025-07-15 16:04:04', '', '0', 0);
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
INSERT INTO `sys_menu` VALUES (1045, '投放管理', 1009, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:12:30', 'admin', '2025-07-15 16:12:30', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1046, '推广管理', 1009, 2, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:12:44', 'admin', '2025-07-15 16:12:44', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1047, '推广图管理', 1009, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:12:54', 'admin', '2025-07-15 16:12:54', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1048, '档口管理', 1, 12, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:13:06', 'admin', '2025-07-15 16:13:06', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1049, '档口商品列表', 1048, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:13:28', 'admin', '2025-07-15 16:13:28', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1051, '档口列表', 1048, 3, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:13:55', 'admin', '2025-07-15 16:13:55', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1052, '待介入代发单', 1048, 4, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:14:16', 'admin', '2025-07-15 16:14:16', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1053, '档口会员列表', 1048, 5, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:14:31', 'admin', '2025-07-15 16:14:31', '', '0', 0);
INSERT INTO `sys_menu` VALUES (1054, '代发人员', 1, 13, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-07-15 16:14:54', 'admin', '2025-07-15 16:14:54', '', '0', 0);
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
INSERT INTO `sys_menu` VALUES (2000, '卖家首页', 1, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-08-07 21:06:14', 'admin', '2025-08-07 21:06:14', '', '0', 0);
INSERT INTO `sys_menu` VALUES (2001, '档口首页', 1, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-08-07 21:06:28', 'admin', '2025-08-07 21:06:28', '', '0', 0);
INSERT INTO `sys_menu` VALUES (2002, '管理员首页', 1, 1, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-08-07 21:06:36', 'admin', '2025-08-07 21:06:36', '', '0', 0);
INSERT INTO `sys_menu` VALUES (2003, '投诉反馈', 1057, 4, '', '', '', '', 1, 1, 'C', '0', '0', '', '', 'admin', '2025-11-07 11:22:04', 'admin', '2025-11-07 11:22:04', '', '0', 0);


-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role
(
    user_id  bigint(20) not null comment '用户ID',
    role_id  bigint(20) not null comment '角色ID',
    store_id bigint(20) default null comment '档口ID',
    primary key (user_id, role_id),
    key      `idx_role_id` (`role_id`) USING BTREE
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role
values (1, 1, null);


-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu
(
    role_id bigint(20) not null comment '角色ID',
    menu_id bigint(20) not null comment '菜单ID',
    primary key (role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
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
INSERT INTO `sys_role_menu` VALUES (2, 2002);
INSERT INTO `sys_role_menu` VALUES (2, 2003);
INSERT INTO `sys_role_menu` VALUES (3, 1002);
INSERT INTO `sys_role_menu` VALUES (3, 1003);
INSERT INTO `sys_role_menu` VALUES (3, 1004);
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
INSERT INTO `sys_role_menu` VALUES (3, 2001);
INSERT INTO `sys_role_menu` VALUES (4, 1007);
INSERT INTO `sys_role_menu` VALUES (4, 1008);
INSERT INTO `sys_role_menu` VALUES (4, 1014);
INSERT INTO `sys_role_menu` VALUES (4, 1038);
INSERT INTO `sys_role_menu` VALUES (4, 1043);
INSERT INTO `sys_role_menu` VALUES (4, 1058);
INSERT INTO `sys_role_menu` VALUES (4, 1059);
INSERT INTO `sys_role_menu` VALUES (4, 2000);
INSERT INTO `sys_role_menu` VALUES (5, 1007);
INSERT INTO `sys_role_menu` VALUES (5, 1008);
INSERT INTO `sys_role_menu` VALUES (5, 1014);
INSERT INTO `sys_role_menu` VALUES (5, 1038);
INSERT INTO `sys_role_menu` VALUES (5, 1043);
INSERT INTO `sys_role_menu` VALUES (5, 1058);
INSERT INTO `sys_role_menu` VALUES (5, 1059);
INSERT INTO `sys_role_menu` VALUES (5, 1060);
INSERT INTO `sys_role_menu` VALUES (5, 2000);


-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log
(
    oper_id        bigint(20) not null auto_increment comment '日志主键',
    title          varchar(50)   default '' comment '模块标题',
    business_type  int(2) default 0 comment '业务类型（0其它 1新增 2修改 3删除）',
    method         varchar(200)  default '' comment '方法名称',
    request_method varchar(10)   default '' comment '请求方式',
    operator_type  int(1) default 0 comment '操作类别（0其它 1后台用户 2手机端用户）',
    oper_name      varchar(50)   default '' comment '操作人员',
    oper_url       varchar(255)  default '' comment '请求URL',
    oper_ip        varchar(128)  default '' comment '主机地址',
    oper_location  varchar(255)  default '' comment '操作地点',
    oper_param     varchar(2000) default '' comment '请求参数',
    json_result    varchar(2000) default '' comment '返回参数',
    status         int(1) default 0 comment '操作状态（0正常 1异常）',
    error_msg      varchar(2000) default '' comment '错误消息',
    oper_time      datetime comment '操作时间',
    cost_time      bigint(20) default 0 comment '消耗时间',
    primary key (oper_id),
    key            idx_sys_oper_log_bt (business_type),
    key            idx_sys_oper_log_s (status),
    key            idx_sys_oper_log_ot (oper_time)
) engine=innodb auto_increment=100 comment = '操作日志记录';


-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
    dict_id     bigint(20) not null auto_increment comment '字典主键',
    dict_name   varchar(100) default '' comment '字典名称',
    dict_type   varchar(100) default '' comment '字典类型',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    remark      varchar(500) default null comment '备注',
    `version`   bigint UNSIGNED NULL DEFAULT NULL COMMENT '版本号',
    `del_flag`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (dict_id),
    unique (dict_type)
) engine=innodb auto_increment=200 comment = '字典类型表';

INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', '用户性别列表', 0, '0', 'admin', '2025-03-26 21:35:36', 'admin', '2025-06-28 11:05:55');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', '菜单状态列表', 0, '0', 'admin', '2025-03-26 21:35:36', '', NULL);
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', '系统开关列表', 0, '0', 'admin', '2025-03-26 21:35:36', '', NULL);
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', '任务状态列表', 0, '0', 'admin', '2025-03-26 21:35:36', '', NULL);
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', '任务分组列表', 0, '0', 'admin', '2025-03-26 21:35:36', '', NULL);
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', '系统是否列表', 0, '0', 'admin', '2025-03-26 21:35:36', '', NULL);
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', '通知类型列表', 0, '0', 'admin', '2025-03-26 21:35:36', '', NULL);
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', '通知状态列表', 0, '0', 'admin', '2025-03-26 21:35:36', '', NULL);
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', '操作类型列表', 0, '0', 'admin', '2025-03-26 21:35:36', '', NULL);
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', '登录状态列表', 0, '0', 'admin', '2025-03-26 21:35:36', '', NULL);
INSERT INTO `sys_dict_type` VALUES (203, '帮面材质', 'upper_material', '0', '', 0, '0', 'admin', '2025-07-03 19:42:28', '', '2025-07-03 19:42:28');
INSERT INTO `sys_dict_type` VALUES (204, '内里材质', 'lining_material', '0', '', 0, '0', 'admin', '2025-07-03 19:45:31', '', '2025-07-03 19:45:31');
INSERT INTO `sys_dict_type` VALUES (205, '鞋垫材质', 'insole_material', '0', '', 0, '0', 'admin', '2025-07-03 19:45:41', '', '2025-07-03 19:45:41');
INSERT INTO `sys_dict_type` VALUES (206, '后跟高', 'heel_height', '0', '', 0, '0', 'admin', '2025-07-03 19:45:52', '', '2025-07-03 19:45:52');
INSERT INTO `sys_dict_type` VALUES (207, '跟底款式', 'heel_type', '0', '', 0, '0', 'admin', '2025-07-03 19:46:03', '', '2025-07-03 19:46:03');
INSERT INTO `sys_dict_type` VALUES (208, '鞋头款式', 'toe_style', '0', '', 0, '0', 'admin', '2025-07-03 19:46:20', '', '2025-07-03 19:46:20');
INSERT INTO `sys_dict_type` VALUES (209, '适合季节', 'suitable_season', '0', '', 0, '0', 'admin', '2025-07-03 19:46:29', '', '2025-07-03 19:46:29');
INSERT INTO `sys_dict_type` VALUES (210, '开口深度', 'collar_depth', '0', '', 0, '0', 'admin', '2025-07-03 19:46:39', '', '2025-07-03 19:46:39');
INSERT INTO `sys_dict_type` VALUES (211, '鞋底材质', 'outsole_material', '0', '', 0, '0', 'admin', '2025-07-03 19:46:48', '', '2025-07-03 19:46:48');
INSERT INTO `sys_dict_type` VALUES (212, '风格', 'style', '0', '', 0, '0', 'admin', '2025-07-03 19:46:58', '', '2025-07-03 19:46:58');
INSERT INTO `sys_dict_type` VALUES (213, '款式', 'design', '0', '', 0, '0', 'admin', '2025-07-03 19:47:11', '', '2025-07-03 19:47:11');
INSERT INTO `sys_dict_type` VALUES (214, '皮质特征', 'leather_features', '0', '', 0, '0', 'admin', '2025-07-03 19:47:30', '', '2025-07-03 19:47:30');
INSERT INTO `sys_dict_type` VALUES (215, '制作工艺', 'manufacturing_process', '0', '', 0, '0', 'admin', '2025-07-03 19:47:38', '', '2025-07-03 19:47:38');
INSERT INTO `sys_dict_type` VALUES (216, '图案', 'pattern', '0', '', 0, '0', 'admin', '2025-07-03 19:47:45', '', '2025-07-03 19:47:45');
INSERT INTO `sys_dict_type` VALUES (217, '闭合方式', 'closure_type', '0', '', 0, '0', 'admin', '2025-07-03 19:47:53', '', '2025-07-03 19:47:53');
INSERT INTO `sys_dict_type` VALUES (218, '适用场景', 'occasion', '0', '', 0, '0', 'admin', '2025-07-03 19:48:04', '', '2025-07-03 19:48:04');
INSERT INTO `sys_dict_type` VALUES (219, '适用年龄', 'suitable_age', '0', '', 0, '0', 'admin', '2025-07-03 19:48:11', '', '2025-07-03 19:48:11');
INSERT INTO `sys_dict_type` VALUES (220, '厚薄', 'thickness', '0', '', 0, '0', 'admin', '2025-07-03 19:48:20', '', '2025-07-03 19:48:20');
INSERT INTO `sys_dict_type` VALUES (221, '流行元素', 'fashion_elements', '0', '', 0, '0', 'admin', '2025-07-03 19:48:29', '', '2025-07-03 19:48:29');
INSERT INTO `sys_dict_type` VALUES (222, '退货原因', 'refund_reason', '0', NULL, 0, '0', 'admin', '2025-07-18 22:56:58', '', '2025-07-18 22:57:00');
INSERT INTO `sys_dict_type` VALUES (223, '季节年份', 'release_year_season', '0', '', 0, '0', 'admin', '2025-08-03 20:51:28', 'admin', '2025-08-03 20:51:28');
INSERT INTO `sys_dict_type` VALUES (224, '鞋面材质', 'shaft_material', '0', '', 0, '0', 'admin', '2025-10-30 20:09:55', '', '2025-10-30 20:09:55');
INSERT INTO `sys_dict_type` VALUES (225, '鞋面内里材质', 'shoe_upper_lining_material', '0', '', 0, '0', 'admin', '2025-10-30 21:55:41', '', '2025-10-30 21:55:41');



-- ----------------------------
-- 12、字典数据表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`
(
    `id`          bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
    `dict_sort`   int NULL DEFAULT 0 COMMENT '字典排序',
    `dict_label`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
    `dict_value`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
    `dict_type`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `version`     bigint UNSIGNED NULL DEFAULT NULL COMMENT '版本号',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 600 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

INSERT INTO `sys_dict_data`
VALUES (1, 1, '男', '0', 'sys_user_sex', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '性别男', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (2, 2, '女', '1', 'sys_user_sex', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '性别女', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (3, 3, '未知', '2', 'sys_user_sex', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '性别未知', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (4, 1, '显示', '0', 'sys_show_hide', '0', 'admin', '2025-03-26 21:35:36', '', '2025-06-28 16:00:50', '显示菜单', 1,
        '2');
INSERT INTO `sys_dict_data`
VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '隐藏菜单', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (6, 1, '正常', '0', 'sys_normal_disable', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '正常状态', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (7, 2, '停用', '1', 'sys_normal_disable', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '停用状态', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (8, 1, '正常', '0', 'sys_job_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '正常状态', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (9, 2, '暂停', '1', 'sys_job_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '停用状态', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '默认分组', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '系统分组', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (12, 1, '是', 'Y', 'sys_yes_no', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '系统默认是', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (13, 2, '否', 'N', 'sys_yes_no', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '系统默认否', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (14, 1, '通知', '1', 'sys_notice_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '通知', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (15, 2, '公告', '2', 'sys_notice_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '公告', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (16, 1, '正常', '0', 'sys_notice_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '正常状态', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (17, 2, '关闭', '1', 'sys_notice_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '关闭状态', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (18, 99, '其他', '0', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '其他操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (19, 1, '新增', '1', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '新增操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (20, 2, '修改', '2', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '修改操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (21, 3, '删除', '3', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '删除操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (22, 4, '授权', '4', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '授权操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (23, 5, '导出', '5', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '导出操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (24, 6, '导入', '6', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '导入操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (25, 7, '强退', '7', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '强退操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '生成操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '清空操作', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (28, 1, '成功', '0', 'sys_common_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '正常状态', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (29, 2, '失败', '1', 'sys_common_status', '0', 'admin', '2025-03-26 21:35:36', '', NULL, '停用状态', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (601, 1, '羊皮（除羊反绒/羊猄）', '羊皮（除羊反绒/羊猄）', 'upper_material', '0', 'admin', '2025-07-03 20:09:15', '',
        '2025-07-03 20:09:15', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (602, 2, '头层牛皮', '头层牛皮', 'upper_material', '0', 'admin', '2025-07-03 20:09:28', '', '2025-07-03 20:09:28', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (603, 3, '橡胶发泡', '橡胶发泡', 'upper_material', '0', 'admin', '2025-07-03 20:09:37', '', '2025-07-03 20:09:37', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (604, 4, '泡沫', '泡沫', 'upper_material', '0', 'admin', '2025-07-03 20:09:45', '', '2025-07-03 20:09:45', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (605, 5, '聚氨酯', '聚氨酯', 'upper_material', '0', 'admin', '2025-07-03 20:09:54', '', '2025-07-03 20:09:54', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (606, 6, '蛇皮弹力布', '蛇皮弹力布', 'upper_material', '0', 'admin', '2025-07-03 20:10:02', '', '2025-07-03 20:10:02', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (607, 7, '反毛皮', '反毛皮', 'upper_material', '0', 'admin', '2025-07-03 20:10:12', '', '2025-07-03 20:10:12', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (608, 8, '蛇蜥皮', '蛇蜥皮', 'upper_material', '0', 'admin', '2025-07-03 20:10:20', '', '2025-07-03 20:10:20', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (609, 9, '驼鸟皮', '驼鸟皮', 'upper_material', '0', 'admin', '2025-07-03 20:10:27', '', '2025-07-03 20:10:27', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (610, 10, '麻', '麻', 'upper_material', '0', 'admin', '2025-07-03 20:10:35', '', '2025-07-03 20:10:35', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (611, 11, '闪粉面料', '闪粉面料', 'upper_material', '0', 'admin', '2025-07-03 20:10:44', '', '2025-07-03 20:10:44', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (612, 12, '羊反绒（羊猄）', '羊反绒（羊猄）', 'upper_material', '0', 'admin', '2025-07-03 20:10:54', '', '2025-07-03 20:10:54',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (613, 13, '树脂', '树脂', 'upper_material', '0', 'admin', '2025-07-03 20:11:09', '', '2025-07-03 20:11:09', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (614, 14, '牛皮', '牛皮', 'upper_material', '0', 'admin', '2025-07-03 20:11:18', '', '2025-07-03 20:11:18', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (615, 15, '鸵鸟皮', '鸵鸟皮', 'upper_material', '0', 'admin', '2025-07-03 20:11:42', '', '2025-07-03 20:11:42', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (616, 16, '裘皮', '裘皮', 'upper_material', '0', 'admin', '2025-07-03 20:11:56', '', '2025-07-03 20:11:56', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (617, 17, '袋鼠皮', '袋鼠皮', 'upper_material', '0', 'admin', '2025-07-03 20:12:03', '', '2025-07-03 20:12:03', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (618, 18, '蜥蜴皮', '蜥蜴皮', 'upper_material', '0', 'admin', '2025-07-03 20:12:13', '', '2025-07-03 20:12:13', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (619, 19, '聚亚胺酯', '聚亚胺酯', 'upper_material', '0', 'admin', '2025-07-03 20:12:22', '', '2025-07-03 20:12:22', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (620, 20, '羊驼皮', '羊驼皮', 'upper_material', '0', 'admin', '2025-07-03 20:12:33', '', '2025-07-03 20:12:33', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (621, 21, '锦纶', '锦纶', 'upper_material', '0', 'admin', '2025-07-03 20:12:42', '', '2025-07-03 20:12:42', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (622, 22, '海豹皮', '海豹皮', 'upper_material', '0', 'admin', '2025-07-03 20:12:50', '', '2025-07-03 20:12:50', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (623, 23, '毛线', '毛线', 'upper_material', '0', 'admin', '2025-07-03 20:12:59', '', '2025-07-03 20:12:59', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (624, 24, '橡塑', '橡塑', 'upper_material', '0', 'admin', '2025-07-03 20:13:09', '', '2025-07-03 20:13:09', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (625, 25, '牛剖层革+合成革', '牛剖层革+合成革', 'upper_material', '0', 'admin', '2025-07-03 20:13:17', '',
        '2025-07-03 20:13:17', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (626, 26, '绵羊皮毛', '绵羊皮毛', 'upper_material', '0', 'admin', '2025-07-03 20:16:51', '', '2025-07-03 20:16:51', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (627, 27, '牛皮剖层绒面革', '牛皮剖层绒面革', 'upper_material', '0', 'admin', '2025-07-03 20:17:00', '', '2025-07-03 20:17:00',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (628, 28, '漆皮', '漆皮', 'upper_material', '0', 'admin', '2025-07-03 20:17:09', '', '2025-07-03 20:17:09', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (629, 29, '牛二层皮覆膜', '牛二层皮覆膜', 'upper_material', '0', 'admin', '2025-07-03 20:17:20', '', '2025-07-03 20:17:20',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (630, 30, 'PVC', 'PVC', 'upper_material', '0', 'admin', '2025-07-03 20:17:38', '', '2025-07-03 20:17:38', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (631, 31, '合成革', '合成革', 'upper_material', '0', 'admin', '2025-07-03 20:17:48', '', '2025-07-03 20:17:48', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (632, 32, '牛皮/羊皮', '牛皮/羊皮', 'upper_material', '0', 'admin', '2025-07-03 20:17:58', '', '2025-07-03 20:17:58', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (633, 33, '绸缎', '绸缎', 'upper_material', '0', 'admin', '2025-07-03 20:18:06', '', '2025-07-03 20:18:06', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (634, 34, '牛反绒（磨砂皮）', '牛反绒（磨砂皮）', 'upper_material', '0', 'admin', '2025-07-03 20:18:15', '',
        '2025-07-03 20:18:15', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (635, 35, '二层羊皮', '二层羊皮', 'upper_material', '0', 'admin', '2025-07-03 20:18:24', '', '2025-07-03 20:18:24', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (636, 36, '马皮', '马皮', 'upper_material', '0', 'admin', '2025-07-03 20:18:33', '', '2025-07-03 20:18:33', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (637, 37, '马毛', '马毛', 'upper_material', '0', 'admin', '2025-07-03 20:18:41', '', '2025-07-03 20:18:41', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (638, 38, 'PU', 'PU', 'upper_material', '0', 'admin', '2025-07-03 20:18:51', '', '2025-07-03 20:18:51', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (639, 39, '绒面', '绒面', 'upper_material', '0', 'admin', '2025-07-03 20:19:01', '', '2025-07-03 20:19:01', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (640, 40, '多种材质拼接', '多种材质拼接', 'upper_material', '0', 'admin', '2025-07-03 20:19:08', '', '2025-07-03 20:19:08',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (641, 41, '网布', '网布', 'upper_material', '0', 'admin', '2025-07-03 20:19:15', '', '2025-07-03 20:19:15', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (642, 42, '亮片布', '亮片布', 'upper_material', '0', 'admin', '2025-07-03 20:19:24', '', '2025-07-03 20:19:24', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (643, 43, '二层牛皮', '二层牛皮', 'upper_material', '0', 'admin', '2025-07-03 20:19:31', '', '2025-07-03 20:19:31', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (644, 44, '布', '布', 'upper_material', '0', 'admin', '2025-07-03 20:19:38', '', '2025-07-03 20:19:38', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (645, 45, '人造革', '人造革', 'upper_material', '0', 'admin', '2025-07-03 20:19:49', '', '2025-07-03 20:19:49', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (646, 46, '太空革', '太空革', 'upper_material', '0', 'admin', '2025-07-03 20:19:58', '', '2025-07-03 20:19:58', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (647, 47, '鹿皮', '鹿皮', 'upper_material', '0', 'admin', '2025-07-03 20:20:08', '', '2025-07-03 20:20:08', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (648, 48, '皮革', '皮革', 'upper_material', '0', 'admin', '2025-07-03 20:20:17', '', '2025-07-03 20:20:17', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (649, 49, '蛇皮', '蛇皮', 'upper_material', '0', 'admin', '2025-07-03 20:20:27', '', '2025-07-03 20:20:27', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (650, 50, '貂毛', '貂毛', 'upper_material', '0', 'admin', '2025-07-03 20:20:37', '', '2025-07-03 20:20:37', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (651, 51, '涤纶', '涤纶', 'upper_material', '0', 'admin', '2025-07-03 20:20:47', '', '2025-07-03 20:20:47', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (652, 52, '羊皮毛一体', '羊皮毛一体', 'upper_material', '0', 'admin', '2025-07-03 20:20:53', '', '2025-07-03 20:20:53', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (653, 53, '超细纤维', '超细纤维', 'upper_material', '0', 'admin', '2025-07-03 20:20:59', '', '2025-07-03 20:20:59', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (654, 54, '织物', '织物', 'upper_material', '0', 'admin', '2025-07-03 20:21:12', '', '2025-07-03 20:21:12', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (655, 55, '西施绒', '西施绒', 'upper_material', '0', 'admin', '2025-07-03 20:21:18', '', '2025-07-03 20:21:18', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (656, 56, '猪皮', '猪皮', 'upper_material', '0', 'admin', '2025-07-03 20:21:25', '', '2025-07-03 20:21:25', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (657, 57, '牛皮漆膜革', '牛皮漆膜革', 'upper_material', '0', 'admin', '2025-07-03 20:21:32', '', '2025-07-03 20:21:32', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (658, 58, '缎面', '缎面', 'upper_material', '0', 'admin', '2025-07-03 20:21:39', '', '2025-07-03 20:21:39', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (659, 59, '超纤皮', '超纤皮', 'upper_material', '0', 'admin', '2025-07-03 20:21:46', '', '2025-07-03 20:21:46', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (660, 60, '格利特皮革', '格利特皮革', 'upper_material', '0', 'admin', '2025-07-03 20:21:56', '', '2025-07-03 20:21:56', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (661, 61, '绒面革拼漆皮', '绒面革拼漆皮', 'upper_material', '0', 'admin', '2025-07-03 20:22:05', '', '2025-07-03 20:22:05',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (662, 62, '牛皮网', '牛皮网', 'upper_material', '0', 'admin', '2025-07-03 20:22:14', '', '2025-07-03 20:22:14', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (663, 63, '织物/羊皮革', '织物/羊皮革', 'upper_material', '0', 'admin', '2025-07-03 20:22:23', '', '2025-07-03 20:22:23',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (664, 64, '二层牛皮（除牛反绒）', '二层牛皮（除牛反绒）', 'upper_material', '0', 'admin', '2025-07-03 20:22:30', '',
        '2025-07-03 20:22:30', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (665, 65, 'PVC化纤', 'PVC化纤', 'upper_material', '0', 'admin', '2025-07-03 20:22:40', '', '2025-07-03 20:22:40', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (666, 66, '皮质', '皮质', 'upper_material', '0', 'admin', '2025-07-03 20:22:50', '', '2025-07-03 20:22:50', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (667, 67, '牛皮革', '牛皮革', 'upper_material', '0', 'admin', '2025-07-03 20:23:00', '', '2025-07-03 20:23:00', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (668, 68, '绒面革', '绒面革', 'upper_material', '0', 'admin', '2025-07-03 20:23:10', '', '2025-07-03 20:23:10', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (669, 69, '汉麻', '汉麻', 'upper_material', '0', 'admin', '2025-07-03 20:23:18', '', '2025-07-03 20:23:18', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (670, 70, '织物+羊皮革', '织物+羊皮革', 'upper_material', '0', 'admin', '2025-07-03 20:23:25', '', '2025-07-03 20:23:25',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (671, 71, '合成材料', '合成材料', 'upper_material', '0', 'admin', '2025-07-03 20:23:36', '', '2025-07-03 20:23:36', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (672, 72, '牛皮剖层革', '牛皮剖层革', 'upper_material', '0', 'admin', '2025-07-03 20:23:45', '', '2025-07-03 20:23:45', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (673, 73, '牛皮绒面革', '牛皮绒面革', 'upper_material', '0', 'admin', '2025-07-03 20:23:54', '', '2025-07-03 20:23:54', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (674, 74, '麂皮', '麂皮', 'upper_material', '0', 'admin', '2025-07-03 20:24:02', '', '2025-07-03 20:24:02', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (675, 75, '棉麻', '棉麻', 'upper_material', '0', 'admin', '2025-07-03 20:24:08', '', '2025-07-03 20:24:08', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (676, 76, '牛皮革+织物', '牛皮革+织物', 'upper_material', '0', 'admin', '2025-07-03 20:24:16', '', '2025-07-03 20:24:16',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (677, 77, '牛皮剖层移膜革', '牛皮剖层移膜革', 'upper_material', '0', 'admin', '2025-07-03 20:24:22', '', '2025-07-03 20:24:22',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (678, 78, '灯芯绒', '灯芯绒', 'upper_material', '0', 'admin', '2025-07-03 20:24:32', '', '2025-07-03 20:24:32', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (679, 79, '织锦', '织锦', 'upper_material', '0', 'admin', '2025-07-03 20:24:43', '', '2025-07-03 20:24:43', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (680, 80, '贴皮', '贴皮', 'upper_material', '0', 'admin', '2025-07-03 20:24:51', '', '2025-07-03 20:24:51', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (681, 81, '塑胶', '塑胶', 'upper_material', '0', 'admin', '2025-07-03 20:24:58', '', '2025-07-03 20:24:58', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (682, 82, '羊皮革', '羊皮革', 'upper_material', '0', 'admin', '2025-07-03 20:25:05', '', '2025-07-03 20:25:05', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (683, 83, '拉菲草', '拉菲草', 'upper_material', '0', 'admin', '2025-07-03 20:25:15', '', '2025-07-03 20:25:15', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (684, 84, '鱼皮', '鱼皮', 'upper_material', '0', 'admin', '2025-07-03 20:25:21', '', '2025-07-03 20:25:21', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (685, 85, '羊皮革+牛皮革', '羊皮革+牛皮革', 'upper_material', '0', 'admin', '2025-07-03 20:25:28', '', '2025-07-03 20:25:28',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (686, 86, '兔毛', '兔毛', 'upper_material', '0', 'admin', '2025-07-03 20:25:34', '', '2025-07-03 20:25:34', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (687, 87, '羊羔皮', '羊羔皮', 'upper_material', '0', 'admin', '2025-07-03 20:25:41', '', '2025-07-03 20:25:41', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (688, 88, '粘胶纤维', '粘胶纤维', 'upper_material', '0', 'admin', '2025-07-03 20:25:48', '', '2025-07-03 20:25:48', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (689, 89, '牛皮革+牛皮剖层漆膜革', '牛皮革+牛皮剖层漆膜革', 'upper_material', '0', 'admin', '2025-07-03 20:25:55', '',
        '2025-07-03 20:25:55', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (690, 90, '多种材质拼接羊皮革', '多种材质拼接羊皮革', 'upper_material', '0', 'admin', '2025-07-03 20:26:02', '',
        '2025-07-03 20:26:02', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (691, 91, '天鹅绒', '天鹅绒', 'upper_material', '0', 'admin', '2025-07-03 20:26:10', '', '2025-07-03 20:26:10', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (692, 92, '孔雀皮', '孔雀皮', 'upper_material', '0', 'admin', '2025-07-03 20:26:18', '', '2025-07-03 20:26:18', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (693, 93, '橡塑材料', '橡塑材料', 'upper_material', '0', 'admin', '2025-07-03 20:26:27', '', '2025-07-03 20:26:27', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (694, 94, '羊毛', '羊毛', 'upper_material', '0', 'admin', '2025-07-03 20:26:35', '', '2025-07-03 20:26:35', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (695, 95, '二层猪皮', '二层猪皮', 'upper_material', '0', 'admin', '2025-07-03 20:26:43', '', '2025-07-03 20:26:43', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (696, 96, '羊毛皮', '羊毛皮', 'upper_material', '0', 'admin', '2025-07-03 20:26:49', '', '2025-07-03 20:26:49', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (697, 97, '羊皮漆膜革', '羊皮漆膜革', 'upper_material', '0', 'admin', '2025-07-03 20:26:55', '', '2025-07-03 20:26:55', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (698, 98, '牛皮革/织物', '牛皮革/织物', 'upper_material', '0', 'admin', '2025-07-03 20:27:01', '', '2025-07-03 20:27:01',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (699, 99, '牛绒面革', '牛绒面革', 'upper_material', '0', 'admin', '2025-07-03 20:27:07', '', '2025-07-03 20:27:07', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (700, 100, '鳄鱼纹皮', '鳄鱼纹皮', 'upper_material', '0', 'admin', '2025-07-03 20:27:13', '', '2025-07-03 20:27:13', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (701, 101, '羊皮革/牛皮革', '羊皮革/牛皮革', 'upper_material', '0', 'admin', '2025-07-03 20:27:22', '',
        '2025-07-03 20:27:22', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (702, 102, '棉', '棉', 'upper_material', '0', 'admin', '2025-07-03 20:27:30', '', '2025-07-03 20:27:30', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (703, 103, '纳帕羊皮', '纳帕羊皮', 'upper_material', '0', 'admin', '2025-07-03 20:27:42', '', '2025-07-03 20:27:42', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (704, 104, '珍珠鱼皮', '珍珠鱼皮', 'upper_material', '0', 'admin', '2025-07-03 20:27:49', '', '2025-07-03 20:27:49', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (705, 105, '毛毡', '毛毡', 'upper_material', '0', 'admin', '2025-07-03 20:27:55', '', '2025-07-03 20:27:55', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (706, 106, '亚麻', '亚麻', 'upper_material', '0', 'admin', '2025-07-03 20:28:01', '', '2025-07-03 20:28:01', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (707, 107, '微纤维', '微纤维', 'upper_material', '0', 'admin', '2025-07-03 20:28:10', '', '2025-07-03 20:28:10', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (708, 108, '牛仔布', '牛仔布', 'upper_material', '0', 'admin', '2025-07-03 20:28:21', '', '2025-07-03 20:28:21', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (709, 109, '乙纶', '乙纶', 'upper_material', '0', 'admin', '2025-07-03 20:28:28', '', '2025-07-03 20:28:28', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (710, 110, '鳗鱼皮', '鳗鱼皮', 'upper_material', '0', 'admin', '2025-07-03 20:28:33', '', '2025-07-03 20:28:33', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (711, 111, '牛毛皮', '牛毛皮', 'upper_material', '0', 'admin', '2025-07-03 20:28:40', '', '2025-07-03 20:28:40', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (712, 112, '草鱼皮', '草鱼皮', 'upper_material', '0', 'admin', '2025-07-03 20:28:53', '', '2025-07-03 20:28:53', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (713, 113, '多种材质拼接牛皮革', '多种材质拼接牛皮革', 'upper_material', '0', 'admin', '2025-07-03 20:29:00', '',
        '2025-07-03 20:29:00', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (714, 114, '弹力布', '弹力布', 'upper_material', '0', 'admin', '2025-07-03 20:29:10', '', '2025-07-03 20:29:10', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (715, 115, '牛皮头层革', '牛皮头层革', 'upper_material', '0', 'admin', '2025-07-03 20:29:17', '', '2025-07-03 20:29:17',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (716, 115, '牛皮头层革', '牛皮头层革', 'upper_material', '0', 'admin', '2025-07-03 20:29:17', '', '2025-07-03 20:29:17',
        '', 1, '2');
INSERT INTO `sys_dict_data`
VALUES (717, 116, '牛毛', '牛毛', 'upper_material', '0', 'admin', '2025-07-03 20:29:25', '', '2025-07-03 20:29:25', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (718, 117, '人造毛绒', '人造毛绒', 'upper_material', '0', 'admin', '2025-07-03 20:29:44', '', '2025-07-03 20:29:44', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (719, 118, '呢子', '呢子', 'upper_material', '0', 'admin', '2025-07-03 20:29:50', '', '2025-07-03 20:29:50', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (720, 119, '尼龙', '尼龙', 'upper_material', '0', 'admin', '2025-07-03 20:29:56', '', '2025-07-03 20:29:56', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (721, 120, '棉纺布', '棉纺布', 'upper_material', '0', 'admin', '2025-07-03 20:30:02', '', '2025-07-03 20:30:02', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (722, 121, '藤草', '藤草', 'upper_material', '0', 'admin', '2025-07-03 20:30:09', '', '2025-07-03 20:30:09', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (723, 122, '羊绒面革', '羊绒面革', 'upper_material', '0', 'admin', '2025-07-03 20:30:17', '', '2025-07-03 20:30:17', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (724, 123, '聚酯纤维', '聚酯纤维', 'upper_material', '0', 'admin', '2025-07-03 20:30:23', '', '2025-07-03 20:30:23', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (725, 124, '鳄鱼皮', '鳄鱼皮', 'upper_material', '0', 'admin', '2025-07-03 20:30:30', '', '2025-07-03 20:30:30', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (726, 1, '头层牛皮', '头层牛皮', 'lining_material', '0', 'admin', '2025-07-03 20:31:02', '', '2025-07-03 20:31:02', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (727, 2, '头层猪皮', '头层猪皮', 'lining_material', '0', 'admin', '2025-07-03 20:31:08', '', '2025-07-03 20:31:08', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (728, 3, '超细纤维', '超细纤维', 'lining_material', '0', 'admin', '2025-07-03 20:31:14', '', '2025-07-03 20:31:14', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (729, 4, '合成革', '合成革', 'lining_material', '0', 'admin', '2025-07-03 20:31:24', '', '2025-07-03 20:31:24', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (730, 5, 'PU', 'PU', 'lining_material', '0', 'admin', '2025-07-03 20:31:30', '', '2025-07-03 20:31:30', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (731, 6, '二层猪皮', '二层猪皮', 'lining_material', '0', 'admin', '2025-07-03 20:31:37', '', '2025-07-03 20:31:37', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (732, 7, 'PVC', 'PVC', 'lining_material', '0', 'admin', '2025-07-03 20:31:45', '', '2025-07-03 20:31:45', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (733, 8, '牛皮/猪皮内里', '牛皮/猪皮内里', 'lining_material', '0', 'admin', '2025-07-03 20:31:52', '', '2025-07-03 20:31:52',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (734, 9, '兔毛', '兔毛', 'lining_material', '0', 'admin', '2025-07-03 20:31:58', '', '2025-07-03 20:31:58', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (735, 10, '皮革', '皮革', 'lining_material', '0', 'admin', '2025-07-03 20:32:04', '', '2025-07-03 20:32:04', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (736, 11, '牛皮/羊皮', '牛皮/羊皮', 'lining_material', '0', 'admin', '2025-07-03 20:32:13', '', '2025-07-03 20:32:13',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (737, 12, '马皮', '马皮', 'lining_material', '0', 'admin', '2025-07-03 20:32:21', '', '2025-07-03 20:32:21', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (738, 13, '布', '布', 'lining_material', '0', 'admin', '2025-07-03 20:32:30', '', '2025-07-03 20:32:30', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (739, 14, '仿猪皮', '仿猪皮', 'lining_material', '0', 'admin', '2025-07-03 20:32:36', '', '2025-07-03 20:32:36', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (740, 15, '羊皮毛一体', '羊皮毛一体', 'lining_material', '0', 'admin', '2025-07-03 20:32:43', '', '2025-07-03 20:32:43',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (741, 16, '羊皮', '羊皮', 'lining_material', '0', 'admin', '2025-07-03 20:32:50', '', '2025-07-03 20:32:50', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (742, 17, '网纱', '网纱', 'lining_material', '0', 'admin', '2025-07-03 20:32:56', '', '2025-07-03 20:32:56', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (743, 18, '牛皮', '牛皮', 'lining_material', '0', 'admin', '2025-07-03 20:33:02', '', '2025-07-03 20:33:02', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (744, 19, '羊皮革', '羊皮革', 'lining_material', '0', 'admin', '2025-07-03 20:33:09', '', '2025-07-03 20:33:09', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (745, 20, '聚酯纤维', '聚酯纤维', 'lining_material', '0', 'admin', '2025-07-03 20:33:17', '', '2025-07-03 20:33:17', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (746, 21, '织物', '织物', 'lining_material', '0', 'admin', '2025-07-03 20:33:22', '', '2025-07-03 20:33:22', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (747, 22, '人造短毛绒', '人造短毛绒', 'lining_material', '0', 'admin', '2025-07-03 20:33:29', '', '2025-07-03 20:33:29',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (748, 23, '山羊皮革', '山羊皮革', 'lining_material', '0', 'admin', '2025-07-03 20:33:35', '', '2025-07-03 20:33:35', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (749, 24, '混合皮革', '混合皮革', 'lining_material', '0', 'admin', '2025-07-03 20:33:41', '', '2025-07-03 20:33:41', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (750, 25, '皮革/织物', '皮革/织物', 'lining_material', '0', 'admin', '2025-07-03 20:33:47', '', '2025-07-03 20:33:47',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (751, 26, '牛皮革', '牛皮革', 'lining_material', '0', 'admin', '2025-07-03 20:33:52', '', '2025-07-03 20:33:52', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (752, 27, '牛二层皮', '牛二层皮', 'lining_material', '0', 'admin', '2025-07-03 20:33:58', '', '2025-07-03 20:33:58', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (753, 28, '棉', '棉', 'lining_material', '0', 'admin', '2025-07-03 20:34:05', '', '2025-07-03 20:34:05', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (754, 29, '羊毛羊绒混纺', '羊毛羊绒混纺', 'lining_material', '0', 'admin', '2025-07-03 20:34:11', '', '2025-07-03 20:34:11',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (755, 30, '狐狸毛', '狐狸毛', 'lining_material', '0', 'admin', '2025-07-03 20:34:17', '', '2025-07-03 20:34:17', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (756, 31, '帆布', '帆布', 'lining_material', '0', 'admin', '2025-07-03 20:34:24', '', '2025-07-03 20:34:24', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (757, 32, '人造长毛绒', '人造长毛绒', 'lining_material', '0', 'admin', '2025-07-03 20:34:33', '', '2025-07-03 20:34:33',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (758, 33, '纯羊毛', '纯羊毛', 'lining_material', '0', 'admin', '2025-07-03 20:34:39', '', '2025-07-03 20:34:39', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (759, 34, '涤沦', '涤沦', 'lining_material', '0', 'admin', '2025-07-03 20:34:46', '', '2025-07-03 20:34:46', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (760, 35, '无内里', '无内里', 'lining_material', '0', 'admin', '2025-07-03 20:34:53', '', '2025-07-03 20:34:53', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (761, 1, '羊皮', '羊皮', 'insole_material', '0', 'admin', '2025-07-03 20:35:22', '', '2025-07-03 20:35:22', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (762, 2, '头层牛皮', '头层牛皮', 'insole_material', '0', 'admin', '2025-07-03 20:35:30', '', '2025-07-03 20:35:30', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (763, 3, '头层猪皮', '头层猪皮', 'insole_material', '0', 'admin', '2025-07-03 20:35:36', '', '2025-07-03 20:35:36', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (764, 4, 'PU', 'PU', 'insole_material', '0', 'admin', '2025-07-03 20:35:41', '', '2025-07-03 20:35:41', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (765, 5, '超纤皮', '超纤皮', 'insole_material', '0', 'admin', '2025-07-03 20:35:47', '', '2025-07-03 20:35:47', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (766, 6, '合成革', '合成革', 'insole_material', '0', 'admin', '2025-07-03 20:35:52', '', '2025-07-03 20:35:52', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (767, 7, '二层猪皮', '二层猪皮', 'insole_material', '0', 'admin', '2025-07-03 20:35:58', '', '2025-07-03 20:35:58', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (768, 8, '布', '布', 'insole_material', '0', 'admin', '2025-07-03 20:36:04', '', '2025-07-03 20:36:04', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (769, 9, '人造短毛绒', '人造短毛绒', 'insole_material', '0', 'admin', '2025-07-03 20:36:09', '', '2025-07-03 20:36:09', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (770, 10, '纯羊毛', '纯羊毛', 'insole_material', '0', 'admin', '2025-07-03 20:36:16', '', '2025-07-03 20:36:16', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (771, 11, '羊皮毛一体', '羊皮毛一体', 'insole_material', '0', 'admin', '2025-07-03 20:36:22', '', '2025-07-03 20:36:22',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (772, 12, '羊毛羊绒混纺', '羊毛羊绒混纺', 'insole_material', '0', 'admin', '2025-07-03 20:36:28', '', '2025-07-03 20:36:28',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (773, 13, '人造长毛绒', '人造长毛绒', 'insole_material', '0', 'admin', '2025-07-03 20:36:33', '', '2025-07-03 20:36:33',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (774, 14, '天然皮革', '天然皮革', 'insole_material', '0', 'admin', '2025-07-03 20:36:40', '', '2025-07-03 20:36:40', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (775, 15, '乳胶', '乳胶', 'insole_material', '0', 'admin', '2025-07-03 20:36:45', '', '2025-07-03 20:36:45', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (776, 1, '平跟(小于等于1cm)', '平跟(小于等于1cm)', 'heel_height', '0', 'admin', '2025-07-03 20:37:14', '',
        '2025-07-03 20:37:14', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (777, 2, '低跟(1-3cm)', '低跟(1-3cm)', 'heel_height', '0', 'admin', '2025-07-03 20:37:20', '', '2025-07-03 20:37:20',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (778, 3, '中跟(3-5cm)', '中跟(3-5cm)', 'heel_height', '0', 'admin', '2025-07-03 20:37:25', '', '2025-07-03 20:37:25',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (779, 4, '高跟(5-8cm)', '高跟(5-8cm)', 'heel_height', '0', 'admin', '2025-07-03 20:37:30', '', '2025-07-03 20:37:30',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (780, 5, '超高跟(大于8cm)', '超高跟(大于8cm)', 'heel_height', '0', 'admin', '2025-07-03 20:37:37', '',
        '2025-07-03 20:37:37', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (781, 6, '平跟', '平跟', 'heel_height', '0', 'admin', '2025-07-03 20:37:43', '', '2025-07-03 20:37:43', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (782, 7, '低跟', '低跟', 'heel_height', '0', 'admin', '2025-07-03 20:37:51', '', '2025-07-03 20:37:51', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (783, 8, '中跟', '中跟', 'heel_height', '0', 'admin', '2025-07-03 20:37:58', '', '2025-07-03 20:37:58', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (784, 9, '高跟', '高跟', 'heel_height', '0', 'admin', '2025-07-03 20:38:05', '', '2025-07-03 20:38:05', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (785, 10, '超高跟', '超高跟', 'heel_height', '0', 'admin', '2025-07-03 20:38:11', '', '2025-07-03 20:38:11', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (786, 1, '粗跟', '粗跟', 'heel_type', '0', 'admin', '2025-07-03 20:46:09', '', '2025-07-03 20:46:09', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (787, 2, '平跟', '平跟', 'heel_type', '0', 'admin', '2025-07-03 20:46:16', '', '2025-07-03 20:46:16', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (788, 3, '细跟', '细跟', 'heel_type', '0', 'admin', '2025-07-03 20:46:25', '', '2025-07-03 20:46:25', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (789, 4, '内增高', '内增高', 'heel_type', '0', 'admin', '2025-07-03 20:46:32', '', '2025-07-03 20:46:32', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (790, 5, '平底', '平底', 'heel_type', '0', 'admin', '2025-07-03 20:46:37', '', '2025-07-03 20:46:37', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (791, 6, '坡跟', '坡跟', 'heel_type', '0', 'admin', '2025-07-03 20:46:45', '', '2025-07-03 20:46:45', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (792, 7, '方跟', '方跟', 'heel_type', '0', 'admin', '2025-07-03 20:46:52', '', '2025-07-03 20:46:52', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (793, 8, '松糕底', '松糕底', 'heel_type', '0', 'admin', '2025-07-03 20:46:59', '', '2025-07-03 20:46:59', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (794, 9, '异型跟', '异型跟', 'heel_type', '0', 'admin', '2025-07-03 20:47:06', '', '2025-07-03 20:47:06', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (795, 10, '酒杯跟', '酒杯跟', 'heel_type', '0', 'admin', '2025-07-03 20:47:13', '', '2025-07-03 20:47:13', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (796, 11, '猫跟', '猫跟', 'heel_type', '0', 'admin', '2025-07-03 20:47:21', '', '2025-07-03 20:47:21', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (797, 12, '摇摇底', '摇摇底', 'heel_type', '0', 'admin', '2025-07-03 20:47:27', '', '2025-07-03 20:47:27', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (798, 13, '镂空跟', '镂空跟', 'heel_type', '0', 'admin', '2025-07-03 20:47:37', '', '2025-07-03 20:47:37', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (799, 14, '锥形跟', '锥形跟', 'heel_type', '0', 'admin', '2025-07-03 20:47:44', '', '2025-07-03 20:47:44', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (800, 15, '马蹄跟', '马蹄跟', 'heel_type', '0', 'admin', '2025-07-03 20:47:51', '', '2025-07-03 20:47:51', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (801, 1, '方头', '方头', 'toe_style', '0', 'admin', '2025-07-03 20:50:51', '', '2025-07-03 20:50:51', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (802, 2, '圆头', '圆头', 'toe_style', '0', 'admin', '2025-07-03 20:50:58', '', '2025-07-03 20:50:58', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (803, 3, '尖头', '尖头', 'toe_style', '0', 'admin', '2025-07-03 20:51:04', '', '2025-07-03 20:51:04', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (804, 4, '杏头', '杏头', 'toe_style', '0', 'admin', '2025-07-03 20:51:17', '', '2025-07-03 20:51:17', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (805, 5, '蝴蝶结头', '蝴蝶结头', 'toe_style', '0', 'admin', '2025-07-03 20:51:23', '', '2025-07-03 20:51:23', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (806, 6, '斜头', '斜头', 'toe_style', '0', 'admin', '2025-07-03 20:51:29', '', '2025-07-03 20:51:29', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (807, 7, '分趾', '分趾', 'toe_style', '0', 'admin', '2025-07-03 20:51:36', '', '2025-07-03 20:51:36', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (808, 8, '露趾', '露趾', 'toe_style', '0', 'admin', '2025-07-03 20:51:42', '', '2025-07-03 20:51:42', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (809, 9, '鱼嘴', '鱼嘴', 'toe_style', '0', 'admin', '2025-07-03 20:51:48', '', '2025-07-03 20:51:48', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (810, 10, '翘头', '翘头', 'toe_style', '0', 'admin', '2025-07-03 20:51:55', '', '2025-07-03 20:51:55', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (811, 11, '其他', '其他', 'toe_style', '0', 'admin', '2025-07-03 20:52:01', '', '2025-07-03 20:52:01', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (812, 1, '春秋', '春秋', 'suitable_season', '0', 'admin', '2025-07-03 20:52:28', '', '2025-07-03 20:52:28', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (813, 2, '夏季', '夏季', 'suitable_season', '0', 'admin', '2025-07-03 20:52:33', '', '2025-07-03 20:52:33', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (814, 3, '冬季', '冬季', 'suitable_season', '0', 'admin', '2025-07-03 20:52:38', '', '2025-07-03 20:52:38', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (815, 4, '四季通用', '四季通用', 'suitable_season', '0', 'admin', '2025-07-03 20:52:44', '', '2025-07-03 20:52:44', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (816, 1, '浅口', '浅口', 'collar_depth', '0', 'admin', '2025-07-03 20:52:59', '', '2025-07-03 20:52:59', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (817, 2, '中口', '中口', 'collar_depth', '0', 'admin', '2025-07-03 20:53:05', '', '2025-07-03 20:53:05', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (818, 3, '深口', '深口', 'collar_depth', '0', 'admin', '2025-07-03 20:53:10', '', '2025-07-03 20:53:10', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (819, 1, 'TPR(牛筋）', 'TPR(牛筋）', 'outsole_material', '0', 'admin', '2025-07-03 20:53:23', '',
        '2025-07-03 20:53:23', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (820, 2, '橡胶', '橡胶', 'outsole_material', '0', 'admin', '2025-07-03 20:53:28', '', '2025-07-03 20:53:28', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (821, 3, '聚氨酯', '聚氨酯', 'outsole_material', '0', 'admin', '2025-07-03 20:53:36', '', '2025-07-03 20:53:36', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (822, 4, '橡胶发泡', '橡胶发泡', 'outsole_material', '0', 'admin', '2025-07-03 20:53:43', '', '2025-07-03 20:53:43', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (823, 5, 'TPR', 'TPR', 'outsole_material', '0', 'admin', '2025-07-03 20:53:49', '', '2025-07-03 20:53:49', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (824, 6, 'tpu', 'tpu', 'outsole_material', '0', 'admin', '2025-07-03 20:53:55', '', '2025-07-03 20:53:55', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (825, 7, '真皮', '真皮', 'outsole_material', '0', 'admin', '2025-07-03 20:54:01', '', '2025-07-03 20:54:01', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (826, 8, '泡沫', '泡沫', 'outsole_material', '0', 'admin', '2025-07-03 20:54:08', '', '2025-07-03 20:54:08', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (827, 9, 'EVA发泡胶', 'EVA发泡胶', 'outsole_material', '0', 'admin', '2025-07-03 20:54:15', '', '2025-07-03 20:54:15',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (828, 10, '木', '木', 'outsole_material', '0', 'admin', '2025-07-03 20:54:21', '', '2025-07-03 20:54:21', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (829, 11, 'EVA', 'EVA', 'outsole_material', '0', 'admin', '2025-07-03 20:54:26', '', '2025-07-03 20:54:26', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (830, 12, '橡胶组合底', '橡胶组合底', 'outsole_material', '0', 'admin', '2025-07-03 20:54:32', '', '2025-07-03 20:54:32',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (831, 13, '皮革', '皮革', 'outsole_material', '0', 'admin', '2025-07-03 20:54:40', '', '2025-07-03 20:54:40', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (832, 14, '千层底', '千层底', 'outsole_material', '0', 'admin', '2025-07-03 20:54:46', '', '2025-07-03 20:54:46', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (833, 15, '复合底', '复合底', 'outsole_material', '0', 'admin', '2025-07-03 20:54:56', '', '2025-07-03 20:54:56', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (834, 16, 'PVC', 'PVC', 'outsole_material', '0', 'admin', '2025-07-03 20:55:03', '', '2025-07-03 20:55:03', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (835, 17, '麻', '麻', 'outsole_material', '0', 'admin', '2025-07-03 20:55:09', '', '2025-07-03 20:55:09', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (836, 18, '塑胶', '塑胶', 'outsole_material', '0', 'admin', '2025-07-03 20:55:17', '', '2025-07-03 20:55:17', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (837, 19, '塑料', '塑料', 'outsole_material', '0', 'admin', '2025-07-03 20:55:23', '', '2025-07-03 20:55:23', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (838, 20, 'MD', 'MD', 'outsole_material', '0', 'admin', '2025-07-03 20:55:29', '', '2025-07-03 20:55:29', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (839, 1, '甜美', '甜美', 'style', '0', 'admin', '2025-07-03 20:56:15', '', '2025-07-03 20:56:15', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (840, 2, '日系', '日系', 'style', '0', 'admin', '2025-07-03 20:56:20', '', '2025-07-03 20:56:20', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (841, 3, '学院', '学院', 'style', '0', 'admin', '2025-07-03 20:56:26', '', '2025-07-03 20:56:26', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (842, 4, '公主', '公主', 'style', '0', 'admin', '2025-07-03 20:56:31', '', '2025-07-03 20:56:31', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (843, 5, '森女', '森女', 'style', '0', 'admin', '2025-07-03 20:56:37', '', '2025-07-03 20:56:37', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (844, 6, '田园', '田园', 'style', '0', 'admin', '2025-07-03 20:56:43', '', '2025-07-03 20:56:43', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (845, 7, '洛丽塔', '洛丽塔', 'style', '0', 'admin', '2025-07-03 20:56:48', '', '2025-07-03 20:56:48', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (846, 8, '街头', '街头', 'style', '0', 'admin', '2025-07-03 20:56:53', '', '2025-07-03 20:56:53', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (847, 9, '欧美', '欧美', 'style', '0', 'admin', '2025-07-03 20:56:59', '', '2025-07-03 20:56:59', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (848, 10, '休闲', '休闲', 'style', '0', 'admin', '2025-07-03 20:57:04', '', '2025-07-03 20:57:04', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (849, 11, '朋克', '朋克', 'style', '0', 'admin', '2025-07-03 20:57:15', '', '2025-07-03 20:57:15', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (850, 12, '军装', '军装', 'style', '0', 'admin', '2025-07-03 20:57:23', '', '2025-07-03 20:57:23', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (851, 13, '嘻哈', '嘻哈', 'style', '0', 'admin', '2025-07-03 20:57:31', '', '2025-07-03 20:57:31', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (852, 14, '中性', '中性', 'style', '0', 'admin', '2025-07-03 20:57:37', '', '2025-07-03 20:57:37', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (853, 15, '波普', '波普', 'style', '0', 'admin', '2025-07-03 20:57:45', '', '2025-07-03 20:57:45', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (854, 16, '通勤', '通勤', 'style', '0', 'admin', '2025-07-03 20:57:53', '', '2025-07-03 20:57:53', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (855, 17, '韩版', '韩版', 'style', '0', 'admin', '2025-07-03 20:57:59', '', '2025-07-03 20:57:59', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (856, 18, '简约', '简约', 'style', '0', 'admin', '2025-07-03 20:58:08', '', '2025-07-03 20:58:08', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (857, 19, '英伦', '英伦', 'style', '0', 'admin', '2025-07-03 20:58:14', '', '2025-07-03 20:58:14', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (858, 20, '优雅', '优雅', 'style', '0', 'admin', '2025-07-03 20:58:21', '', '2025-07-03 20:58:21', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (859, 21, '民族风', '民族风', 'style', '0', 'admin', '2025-07-03 20:58:27', '', '2025-07-03 20:58:27', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (860, 22, '性感', '性感', 'style', '0', 'admin', '2025-07-03 20:58:32', '', '2025-07-03 20:58:32', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (861, 23, '舒适', '舒适', 'style', '0', 'admin', '2025-07-03 20:58:38', '', '2025-07-03 20:58:38', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (862, 24, '复古风', '复古风', 'style', '0', 'admin', '2025-07-03 20:58:45', '', '2025-07-03 20:58:45', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (863, 25, '复古', '复古', 'style', '0', 'admin', '2025-07-03 20:58:53', '', '2025-07-03 20:58:53', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (864, 26, '罗马风', '罗马风', 'style', '0', 'admin', '2025-07-03 20:59:00', '', '2025-07-03 20:59:00', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (865, 27, '轻熟', '轻熟', 'style', '0', 'admin', '2025-07-03 20:59:06', '', '2025-07-03 20:59:06', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (866, 28, '名媛风', '名媛风', 'style', '0', 'admin', '2025-07-03 20:59:13', '', '2025-07-03 20:59:13', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (867, 29, '波西米亚', '波西米亚', 'style', '0', 'admin', '2025-07-03 20:59:20', '', '2025-07-03 20:59:20', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (868, 30, '文艺', '文艺', 'style', '0', 'admin', '2025-07-03 20:59:30', '', '2025-07-03 20:59:30', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (869, 31, '少女', '少女', 'style', '0', 'admin', '2025-07-03 20:59:35', '', '2025-07-03 20:59:35', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (870, 32, 'ins风', 'ins风', 'style', '0', 'admin', '2025-07-03 20:59:40', '', '2025-07-03 20:59:40', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (871, 33, '赫本风', '赫本风', 'style', '0', 'admin', '2025-07-03 20:59:45', '', '2025-07-03 20:59:45', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (872, 34, '仙女风', '仙女风', 'style', '0', 'admin', '2025-07-03 20:59:50', '', '2025-07-03 20:59:50', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (873, 35, '时尚', '时尚', 'style', '0', 'admin', '2025-07-03 20:59:57', '', '2025-07-03 20:59:57', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (874, 36, '汉元素', '汉元素', 'style', '0', 'admin', '2025-07-03 21:00:03', '', '2025-07-03 21:00:03', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (875, 37, '潮酷', '潮酷', 'style', '0', 'admin', '2025-07-03 21:00:10', '', '2025-07-03 21:00:10', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (876, 38, '御姐', '御姐', 'style', '0', 'admin', '2025-07-03 21:00:17', '', '2025-07-03 21:00:17', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (877, 39, '原宿', '原宿', 'style', '0', 'admin', '2025-07-03 21:00:24', '', '2025-07-03 21:00:24', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (878, 40, '金属风', '金属风', 'style', '0', 'admin', '2025-07-03 21:00:32', '', '2025-07-03 21:00:32', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (879, 41, '泫雅风', '泫雅风', 'style', '0', 'admin', '2025-07-03 21:00:40', '', '2025-07-03 21:00:40', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (880, 42, '轻运动风', '轻运动风', 'style', '0', 'admin', '2025-07-03 21:00:46', '', '2025-07-03 21:00:46', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (881, 43, '可爱', '可爱', 'style', '0', 'admin', '2025-07-03 21:00:53', '', '2025-07-03 21:00:53', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (882, 1, '单鞋', '单鞋', 'design', '0', 'admin', '2025-07-03 21:01:12', '', '2025-07-03 21:01:12', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (883, 2, '玛丽珍鞋', '玛丽珍鞋', 'design', '0', 'admin', '2025-07-03 21:01:16', '', '2025-07-03 21:01:16', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (884, 3, '高跟鞋', '高跟鞋', 'design', '0', 'admin', '2025-07-03 21:01:20', '', '2025-07-03 21:01:20', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (885, 4, '芭蕾舞平底鞋', '芭蕾舞平底鞋', 'design', '0', 'admin', '2025-07-03 21:01:26', '', '2025-07-03 21:01:26', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (886, 5, '奶奶鞋', '奶奶鞋', 'design', '0', 'admin', '2025-07-03 21:01:32', '', '2025-07-03 21:01:32', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (887, 6, '懒人鞋', '懒人鞋', 'design', '0', 'admin', '2025-07-03 21:01:38', '', '2025-07-03 21:01:38', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (888, 7, '穆勒鞋', '穆勒鞋', 'design', '0', 'admin', '2025-07-03 21:01:44', '', '2025-07-03 21:01:44', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (889, 8, '网面鞋', '网面鞋', 'design', '0', 'admin', '2025-07-03 21:01:50', '', '2025-07-03 21:01:50', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (890, 9, '松糕鞋', '松糕鞋', 'design', '0', 'admin', '2025-07-03 21:01:56', '', '2025-07-03 21:01:56', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (891, 10, '休闲', '休闲', 'design', '0', 'admin', '2025-07-03 21:02:02', '', '2025-07-03 21:02:02', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (892, 11, '乐福鞋', '乐福鞋', 'design', '0', 'admin', '2025-07-03 21:02:06', '', '2025-07-03 21:02:06', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (893, 12, '丁字鞋', '丁字鞋', 'design', '0', 'admin', '2025-07-03 21:02:20', '', '2025-07-03 21:02:20', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (894, 13, '运动休闲鞋', '运动休闲鞋', 'design', '0', 'admin', '2025-07-03 21:02:28', '', '2025-07-03 21:02:28', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (895, 14, '豆豆鞋', '豆豆鞋', 'design', '0', 'admin', '2025-07-03 21:02:34', '', '2025-07-03 21:02:34', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (896, 15, '鱼嘴鞋', '鱼嘴鞋', 'design', '0', 'admin', '2025-07-03 21:02:40', '', '2025-07-03 21:02:40', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (897, 16, '布洛克鞋', '布洛克鞋', 'design', '0', 'admin', '2025-07-03 21:02:45', '', '2025-07-03 21:02:45', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (898, 17, '护士鞋', '护士鞋', 'design', '0', 'admin', '2025-07-03 21:02:54', '', '2025-07-03 21:02:54', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (899, 18, '专业鞋', '专业鞋', 'design', '0', 'admin', '2025-07-03 21:03:00', '', '2025-07-03 21:03:00', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (900, 19, '帆船鞋', '帆船鞋', 'design', '0', 'admin', '2025-07-03 21:03:06', '', '2025-07-03 21:03:06', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (901, 20, '布鞋', '布鞋', 'design', '0', 'admin', '2025-07-03 21:03:12', '', '2025-07-03 21:03:12', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (902, 21, '牛津鞋', '牛津鞋', 'design', '0', 'admin', '2025-07-03 21:03:19', '', '2025-07-03 21:03:19', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (903, 22, '板鞋', '板鞋', 'design', '0', 'admin', '2025-07-03 21:03:23', '', '2025-07-03 21:03:23', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (904, 23, '摇摇鞋', '摇摇鞋', 'design', '0', 'admin', '2025-07-03 21:03:31', '', '2025-07-03 21:03:31', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (905, 1, '水染皮', '水染皮', 'leather_features', '0', 'admin', '2025-07-03 21:03:56', '', '2025-07-03 21:03:56', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (906, 2, '修面皮', '修面皮', 'leather_features', '0', 'admin', '2025-07-03 21:04:00', '', '2025-07-03 21:04:00', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (907, 3, '漆皮', '漆皮', 'leather_features', '0', 'admin', '2025-07-03 21:04:07', '', '2025-07-03 21:04:07', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (908, 4, '磨砂', '磨砂', 'leather_features', '0', 'admin', '2025-07-03 21:04:13', '', '2025-07-03 21:04:13', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (909, 5, '覆膜', '覆膜', 'leather_features', '0', 'admin', '2025-07-03 21:04:19', '', '2025-07-03 21:04:19', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (910, 6, '软面皮', '软面皮', 'leather_features', '0', 'admin', '2025-07-03 21:04:25', '', '2025-07-03 21:04:25', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (911, 7, '植鞣', '植鞣', 'leather_features', '0', 'admin', '2025-07-03 21:04:30', '', '2025-07-03 21:04:30', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (912, 8, '打蜡', '打蜡', 'leather_features', '0', 'admin', '2025-07-03 21:04:36', '', '2025-07-03 21:04:36', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (913, 9, '压花皮', '压花皮', 'leather_features', '0', 'admin', '2025-07-03 21:04:42', '', '2025-07-03 21:04:42', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (914, 10, '纳帕纹', '纳帕纹', 'leather_features', '0', 'admin', '2025-07-03 21:04:50', '', '2025-07-03 21:04:50', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (915, 11, '油鞣', '油鞣', 'leather_features', '0', 'admin', '2025-07-03 21:04:55', '', '2025-07-03 21:04:55', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (916, 12, '印花', '印花', 'leather_features', '0', 'admin', '2025-07-03 21:04:59', '', '2025-07-03 21:04:59', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (917, 13, '摔纹皮', '摔纹皮', 'leather_features', '0', 'admin', '2025-07-03 21:05:04', '', '2025-07-03 21:05:04', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (918, 14, '擦色皮', '擦色皮', 'leather_features', '0', 'admin', '2025-07-03 21:05:11', '', '2025-07-03 21:05:11', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (919, 15, '反绒皮', '反绒皮', 'leather_features', '0', 'admin', '2025-07-03 21:05:17', '', '2025-07-03 21:05:17', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (920, 16, '裂紋', '裂紋', 'leather_features', '0', 'admin', '2025-07-03 21:05:23', '', '2025-07-03 21:05:23', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (921, 17, '醛鞣', '醛鞣', 'leather_features', '0', 'admin', '2025-07-03 21:05:29', '', '2025-07-03 21:05:29', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (922, 18, '开边珠', '开边珠', 'leather_features', '0', 'admin', '2025-07-03 21:05:35', '', '2025-07-03 21:05:35', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (923, 19, '疯马皮', '疯马皮', 'leather_features', '0', 'admin', '2025-07-03 21:05:41', '', '2025-07-03 21:05:41', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (924, 20, '铬鞣', '铬鞣', 'leather_features', '0', 'admin', '2025-07-03 21:05:48', '', '2025-07-03 21:05:48', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (925, 1, '胶粘工艺', '胶粘工艺', 'manufacturing_process', '0', 'admin', '2025-07-03 21:06:16', '', '2025-07-03 21:06:16',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (926, 2, '缝制鞋', '缝制鞋', 'manufacturing_process', '0', 'admin', '2025-07-03 21:06:20', '', '2025-07-03 21:06:20',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (927, 3, '硫化工艺', '硫化工艺', 'manufacturing_process', '0', 'admin', '2025-07-03 21:06:25', '', '2025-07-03 21:06:25',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (928, 4, '注压工艺', '注压工艺', 'manufacturing_process', '0', 'admin', '2025-07-03 21:06:30', '', '2025-07-03 21:06:30',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (929, 1, '纯色', '纯色', 'pattern', '0', 'admin', '2025-07-03 21:06:40', '', '2025-07-03 21:06:40', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (930, 2, '植物花卉', '植物花卉', 'pattern', '0', 'admin', '2025-07-03 21:06:45', '', '2025-07-03 21:06:45', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (931, 3, '条纹', '条纹', 'pattern', '0', 'admin', '2025-07-03 21:06:50', '', '2025-07-03 21:06:50', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (932, 4, '格子', '格子', 'pattern', '0', 'admin', '2025-07-03 21:06:55', '', '2025-07-03 21:06:55', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (933, 5, '拼色', '拼色', 'pattern', '0', 'admin', '2025-07-03 21:07:01', '', '2025-07-03 21:07:01', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (934, 6, '波点', '波点', 'pattern', '0', 'admin', '2025-07-03 21:07:06', '', '2025-07-03 21:07:06', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (935, 7, '卡通动漫', '卡通动漫', 'pattern', '0', 'admin', '2025-07-03 21:07:12', '', '2025-07-03 21:07:12', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (936, 8, '心形', '心形', 'pattern', '0', 'admin', '2025-07-03 21:07:19', '', '2025-07-03 21:07:19', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (937, 9, '涂鸦', '涂鸦', 'pattern', '0', 'admin', '2025-07-03 21:07:25', '', '2025-07-03 21:07:25', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (938, 10, '碎花', '碎花', 'pattern', '0', 'admin', '2025-07-03 21:07:30', '', '2025-07-03 21:07:30', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (939, 11, '手绘', '手绘', 'pattern', '0', 'admin', '2025-07-03 21:07:36', '', '2025-07-03 21:07:36', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (940, 12, '小熊', '小熊', 'pattern', '0', 'admin', '2025-07-03 21:07:47', '', '2025-07-03 21:07:47', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (941, 13, '渐变', '渐变', 'pattern', '0', 'admin', '2025-07-03 21:07:58', '', '2025-07-03 21:07:58', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (942, 14, '豹纹', '豹纹', 'pattern', '0', 'admin', '2025-07-03 21:08:05', '', '2025-07-03 21:08:05', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (943, 15, '蝴蝶', '蝴蝶', 'pattern', '0', 'admin', '2025-07-03 21:08:12', '', '2025-07-03 21:08:12', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (944, 16, '圆点', '圆点', 'pattern', '0', 'admin', '2025-07-03 21:08:18', '', '2025-07-03 21:08:18', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (945, 17, '蛇纹', '蛇纹', 'pattern', '0', 'admin', '2025-07-03 21:08:26', '', '2025-07-03 21:08:26', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (946, 18, '菱形', '菱形', 'pattern', '0', 'admin', '2025-07-03 21:08:56', '', '2025-07-03 21:08:56', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (947, 19, '小孩', '小孩', 'pattern', '0', 'admin', '2025-07-03 21:09:03', '', '2025-07-03 21:09:03', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (948, 20, '熊猫', '熊猫', 'pattern', '0', 'admin', '2025-07-03 21:09:09', '', '2025-07-03 21:09:09', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (949, 21, '鳄鱼纹', '鳄鱼纹', 'pattern', '0', 'admin', '2025-07-03 21:09:15', '', '2025-07-03 21:09:15', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (950, 22, '蜜蜂', '蜜蜂', 'pattern', '0', 'admin', '2025-07-03 21:09:23', '', '2025-07-03 21:09:23', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (951, 23, '小蜜蜂', '小蜜蜂', 'pattern', '0', 'admin', '2025-07-03 21:09:30', '', '2025-07-03 21:09:30', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (952, 24, '胖妹妹', '胖妹妹', 'pattern', '0', 'admin', '2025-07-03 21:09:38', '', '2025-07-03 21:09:38', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (953, 25, '人物', '人物', 'pattern', '0', 'admin', '2025-07-03 21:09:43', '', '2025-07-03 21:09:43', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (954, 26, '迷彩', '迷彩', 'pattern', '0', 'admin', '2025-07-03 21:09:50', '', '2025-07-03 21:09:50', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (955, 27, '石头纹', '石头纹', 'pattern', '0', 'admin', '2025-07-03 21:10:04', '', '2025-07-03 21:10:04', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (956, 28, '千鸟格', '千鸟格', 'pattern', '0', 'admin', '2025-07-03 21:10:10', '', '2025-07-03 21:10:10', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (957, 29, '小熊/小猪/小动物', '小熊/小猪/小动物', 'pattern', '0', 'admin', '2025-07-03 21:10:17', '', '2025-07-03 21:10:17',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (958, 30, '人物图案', '人物图案', 'pattern', '0', 'admin', '2025-07-03 21:10:23', '', '2025-07-03 21:10:23', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (959, 31, '彩虹', '彩虹', 'pattern', '0', 'admin', '2025-07-03 21:10:29', '', '2025-07-03 21:10:29', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (960, 32, '星星', '星星', 'pattern', '0', 'admin', '2025-07-03 21:10:35', '', '2025-07-03 21:10:35', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (961, 33, '米老鼠', '米老鼠', 'pattern', '0', 'admin', '2025-07-03 21:10:42', '', '2025-07-03 21:10:42', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (962, 34, '千格鸟', '千格鸟', 'pattern', '0', 'admin', '2025-07-03 21:10:49', '', '2025-07-03 21:10:49', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (963, 35, '骷髅', '骷髅', 'pattern', '0', 'admin', '2025-07-03 21:10:55', '', '2025-07-03 21:10:55', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (964, 36, '字母', '字母', 'pattern', '0', 'admin', '2025-07-03 21:11:01', '', '2025-07-03 21:11:01', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (965, 37, '字母/数字/文字', '字母/数字/文字', 'pattern', '0', 'admin', '2025-07-03 21:11:07', '', '2025-07-03 21:11:07', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (966, 1, '套脚', '套脚', 'closure_type', '0', 'admin', '2025-07-03 21:12:03', '', '2025-07-03 21:12:03', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (967, 2, '一字式扣带', '一字式扣带', 'closure_type', '0', 'admin', '2025-07-03 21:12:08', '', '2025-07-03 21:12:08', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (968, 3, '一脚蹬', '一脚蹬', 'closure_type', '0', 'admin', '2025-07-03 21:12:12', '', '2025-07-03 21:12:12', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (969, 4, '丁字式扣带', '丁字式扣带', 'closure_type', '0', 'admin', '2025-07-03 21:12:17', '', '2025-07-03 21:12:17', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (970, 5, '系带', '系带', 'closure_type', '0', 'admin', '2025-07-03 21:12:23', '', '2025-07-03 21:12:23', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (971, 6, '交叉扣带', '交叉扣带', 'closure_type', '0', 'admin', '2025-07-03 21:12:29', '', '2025-07-03 21:12:29', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (972, 7, '松紧带', '松紧带', 'closure_type', '0', 'admin', '2025-07-03 21:12:36', '', '2025-07-03 21:12:36', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (973, 8, '按扣式', '按扣式', 'closure_type', '0', 'admin', '2025-07-03 21:12:43', '', '2025-07-03 21:12:43', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (974, 9, '旋钮', '旋钮', 'closure_type', '0', 'admin', '2025-07-03 21:12:50', '', '2025-07-03 21:12:50', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (975, 9, '旋钮', '旋钮', 'closure_type', '0', 'admin', '2025-07-03 21:12:50', '', '2025-07-03 21:12:50', '', 1, '2');
INSERT INTO `sys_dict_data`
VALUES (976, 10, '脚腕扣带', '脚腕扣带', 'closure_type', '0', 'admin', '2025-07-03 21:13:04', '', '2025-07-03 21:13:04', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (977, 11, '魔术贴', '魔术贴', 'closure_type', '0', 'admin', '2025-07-03 21:13:11', '', '2025-07-03 21:13:11', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (978, 12, '拉链', '拉链', 'closure_type', '0', 'admin', '2025-07-03 21:13:16', '', '2025-07-03 21:13:16', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (979, 13, '滑扣调节', '滑扣调节', 'closure_type', '0', 'admin', '2025-07-03 21:13:24', '', '2025-07-03 21:13:24', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (980, 14, '翻扣式', '翻扣式', 'closure_type', '0', 'admin', '2025-07-03 21:13:31', '', '2025-07-03 21:13:31', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (981, 15, '侧拉链', '侧拉链', 'closure_type', '0', 'admin', '2025-07-03 21:13:37', '', '2025-07-03 21:13:37', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (982, 16, '前拉链', '前拉链', 'closure_type', '0', 'admin', '2025-07-03 21:13:45', '', '2025-07-03 21:13:45', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (983, 17, '前系带', '前系带', 'closure_type', '0', 'admin', '2025-07-03 21:13:50', '', '2025-07-03 21:13:50', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (984, 18, '后拉链', '后拉链', 'closure_type', '0', 'admin', '2025-07-03 21:13:56', '', '2025-07-03 21:13:56', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (985, 19, '后系带', '后系带', 'closure_type', '0', 'admin', '2025-07-03 21:14:02', '', '2025-07-03 21:14:02', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (986, 1, '日常', '日常', 'occasion', '0', 'admin', '2025-07-03 21:14:33', '', '2025-07-03 21:14:33', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (987, 2, '宴会', '宴会', 'occasion', '0', 'admin', '2025-07-03 21:14:40', '', '2025-07-03 21:14:40', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (988, 3, '戏剧表演', '戏剧表演', 'occasion', '0', 'admin', '2025-07-03 21:14:45', '', '2025-07-03 21:14:45', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (989, 4, '旅行', '旅行', 'occasion', '0', 'admin', '2025-07-03 21:14:49', '', '2025-07-03 21:14:49', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (990, 5, '跑步', '跑步', 'occasion', '0', 'admin', '2025-07-03 21:14:54', '', '2025-07-03 21:14:54', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (991, 6, '健身', '健身', 'occasion', '0', 'admin', '2025-07-03 21:15:00', '', '2025-07-03 21:15:00', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (992, 7, '面试', '面试', 'occasion', '0', 'admin', '2025-07-03 21:15:07', '', '2025-07-03 21:15:07', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (993, 8, '音乐会', '音乐会', 'occasion', '0', 'admin', '2025-07-03 21:15:13', '', '2025-07-03 21:15:13', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (994, 9, '约会', '约会', 'occasion', '0', 'admin', '2025-07-03 21:15:18', '', '2025-07-03 21:15:18', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (995, 10, '婚宴', '婚宴', 'occasion', '0', 'admin', '2025-07-03 21:15:25', '', '2025-07-03 21:15:25', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (996, 11, '跳舞', '跳舞', 'occasion', '0', 'admin', '2025-07-03 21:15:31', '', '2025-07-03 21:15:31', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (997, 12, '办公室', '办公室', 'occasion', '0', 'admin', '2025-07-03 21:15:37', '', '2025-07-03 21:15:37', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (998, 13, '婚礼', '婚礼', 'occasion', '0', 'admin', '2025-07-03 21:15:42', '', '2025-07-03 21:15:42', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (999, 14, 'JK制服搭配', 'JK制服搭配', 'occasion', '0', 'admin', '2025-07-03 21:15:48', '', '2025-07-03 21:15:48', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1000, 15, '古风搭配', '古风搭配', 'occasion', '0', 'admin', '2025-07-03 21:15:57', '', '2025-07-03 21:15:57', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1001, 16, '运动', '运动', 'occasion', '0', 'admin', '2025-07-03 21:16:04', '', '2025-07-03 21:16:04', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1002, 17, 'Lolita搭配', 'Lolita搭配', 'occasion', '0', 'admin', '2025-07-03 21:16:12', '', '2025-07-03 21:16:12',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1003, 18, '上班', '上班', 'occasion', '0', 'admin', '2025-07-03 21:16:16', '', '2025-07-03 21:16:16', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1004, 19, '沙滩', '沙滩', 'occasion', '0', 'admin', '2025-07-03 21:16:21', '', '2025-07-03 21:16:21', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1005, 20, '居家', '居家', 'occasion', '0', 'admin', '2025-07-03 21:16:26', '', '2025-07-03 21:16:26', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1006, 1, '18-40岁', '18-40岁', 'suitable_age', '0', 'admin', '2025-07-03 21:16:46', '', '2025-07-03 21:16:46', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (1007, 2, '40-60岁', '40-60岁', 'suitable_age', '0', 'admin', '2025-07-03 21:16:51', '', '2025-07-03 21:16:51', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (1008, 1, '加厚', '加厚', 'thickness', '0', 'admin', '2025-07-03 21:17:05', '', '2025-07-03 21:17:05', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1009, 2, '常规', '常规', 'thickness', '0', 'admin', '2025-07-03 21:17:08', '', '2025-07-03 21:17:08', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1010, 3, '薄款', '薄款', 'thickness', '0', 'admin', '2025-07-03 21:17:13', '', '2025-07-03 21:17:13', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1011, 1, '浅口', '浅口', 'fashion_elements', '0', 'admin', '2025-07-03 21:17:25', '', '2025-07-03 21:17:25', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1012, 2, '拼接', '拼接', 'fashion_elements', '0', 'admin', '2025-07-03 21:17:30', '', '2025-07-03 21:17:30', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1013, 3, '搭扣', '搭扣', 'fashion_elements', '0', 'admin', '2025-07-03 21:17:36', '', '2025-07-03 21:17:36', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1014, 4, '串珠', '串珠', 'fashion_elements', '0', 'admin', '2025-07-03 21:17:41', '', '2025-07-03 21:17:41', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1015, 5, '亮丝/亮片', '亮丝/亮片', 'fashion_elements', '0', 'admin', '2025-07-03 21:17:46', '', '2025-07-03 21:17:46',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1016, 6, '绣花', '绣花', 'fashion_elements', '0', 'admin', '2025-07-03 21:17:52', '', '2025-07-03 21:17:52', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1017, 7, '粗跟', '粗跟', 'fashion_elements', '0', 'admin', '2025-07-03 21:17:57', '', '2025-07-03 21:17:57', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1018, 8, '拼色', '拼色', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:03', '', '2025-07-03 21:18:03', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1019, 9, '坡跟', '坡跟', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:08', '', '2025-07-03 21:18:08', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1020, 10, '铆钉', '铆钉', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:13', '', '2025-07-03 21:18:13', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1021, 11, '格子', '格子', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:19', '', '2025-07-03 21:18:19', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1022, 12, '花卉', '花卉', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:24', '', '2025-07-03 21:18:24', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1023, 13, '金属装饰', '金属装饰', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:32', '', '2025-07-03 21:18:32',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1024, 14, '罗马风格', '罗马风格', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:42', '', '2025-07-03 21:18:42',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1025, 15, '珍珠', '珍珠', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:47', '', '2025-07-03 21:18:47', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1026, 16, '皮带装饰', '皮带装饰', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:53', '', '2025-07-03 21:18:53',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1027, 17, '丝带', '丝带', 'fashion_elements', '0', 'admin', '2025-07-03 21:18:58', '', '2025-07-03 21:18:58', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1028, 18, '亮丝', '亮丝', 'fashion_elements', '0', 'admin', '2025-07-03 21:19:04', '', '2025-07-03 21:19:04', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1029, 19, '一字扣', '一字扣', 'fashion_elements', '0', 'admin', '2025-07-03 21:19:09', '', '2025-07-03 21:19:09', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (1030, 20, '金属', '金属', 'fashion_elements', '0', 'admin', '2025-07-03 21:19:18', '', '2025-07-03 21:19:18', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1031, 21, '交叉绑带', '交叉绑带', 'fashion_elements', '0', 'admin', '2025-07-03 21:19:23', '', '2025-07-03 21:19:23',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1032, 22, '脚环绑带', '脚环绑带', 'fashion_elements', '0', 'admin', '2025-07-03 21:19:32', '', '2025-07-03 21:19:32',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1033, 23, '蕾丝', '蕾丝', 'fashion_elements', '0', 'admin', '2025-07-03 21:19:40', '', '2025-07-03 21:19:40', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1034, 24, '毛线扣', '毛线扣', 'fashion_elements', '0', 'admin', '2025-07-03 21:19:45', '', '2025-07-03 21:19:45', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (1035, 25, '蝴蝶结', '蝴蝶结', 'fashion_elements', '0', 'admin', '2025-07-03 21:19:51', '', '2025-07-03 21:19:51', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (1036, 26, '花朵', '花朵', 'fashion_elements', '0', 'admin', '2025-07-03 21:19:57', '', '2025-07-03 21:19:57', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1037, 27, '细带组合', '细带组合', 'fashion_elements', '0', 'admin', '2025-07-03 21:20:04', '', '2025-07-03 21:20:04',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1038, 28, 'T型带(脚背)', 'T型带(脚背)', 'fashion_elements', '0', 'admin', '2025-07-03 21:20:10', '',
        '2025-07-03 21:20:10', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1039, 29, '珍珠方扣', '珍珠方扣', 'fashion_elements', '0', 'admin', '2025-07-03 21:20:16', '', '2025-07-03 21:20:16',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1040, 30, '水钻', '水钻', 'fashion_elements', '0', 'admin', '2025-07-03 21:20:23', '', '2025-07-03 21:20:23', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1041, 31, '皮带扣', '皮带扣', 'fashion_elements', '0', 'admin', '2025-07-03 21:20:34', '', '2025-07-03 21:20:34', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (1042, 32, '亮片', '亮片', 'fashion_elements', '0', 'admin', '2025-07-03 21:20:39', '', '2025-07-03 21:20:39', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1043, 33, '刺绣/绣花', '刺绣/绣花', 'fashion_elements', '0', 'admin', '2025-07-03 21:20:44', '', '2025-07-03 21:20:44',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1044, 34, '链子', '链子', 'fashion_elements', '0', 'admin', '2025-07-03 21:20:49', '', '2025-07-03 21:20:49', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1045, 35, '镂空', '镂空', 'fashion_elements', '0', 'admin', '2025-07-03 21:20:58', '', '2025-07-03 21:20:58', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1046, 36, '豹纹', '豹纹', 'fashion_elements', '0', 'admin', '2025-07-03 21:21:06', '', '2025-07-03 21:21:06', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1047, 37, '松糕跟', '松糕跟', 'fashion_elements', '0', 'admin', '2025-07-03 21:21:12', '', '2025-07-03 21:21:12', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (1048, 38, '刺绣', '刺绣', 'fashion_elements', '0', 'admin', '2025-07-03 21:21:18', '', '2025-07-03 21:21:18', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1049, 39, '格纹', '格纹', 'fashion_elements', '0', 'admin', '2025-07-03 21:21:23', '', '2025-07-03 21:21:23', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1050, 40, '防水台', '防水台', 'fashion_elements', '0', 'admin', '2025-07-03 21:21:28', '', '2025-07-03 21:21:28', '',
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (1051, 41, '编织', '编织', 'fashion_elements', '0', 'admin', '2025-07-03 21:21:34', '', '2025-07-03 21:21:34', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1052, 42, '网状', '网状', 'fashion_elements', '0', 'admin', '2025-07-03 21:21:39', '', '2025-07-03 21:21:39', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1053, 43, 'T型绑带', 'T型绑带', 'fashion_elements', '0', 'admin', '2025-07-03 21:21:44', '', '2025-07-03 21:21:44',
        '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1054, 44, '流苏', '流苏', 'fashion_elements', '0', 'admin', '2025-07-03 21:21:50', '', '2025-07-03 21:21:50', '', 0,
        '0');
INSERT INTO `sys_dict_data`
VALUES (1055, 5, '不想要了', '不想要了', 'refund_reason', '0', 'admin', '2025-07-18 22:57:09', '', '2025-07-18 22:57:11', NULL,
        0, '0');
INSERT INTO `sys_dict_data`
VALUES (1072, 17, '2021年春季', '2021年春季', 'release_year_season', '0', 'admin', '2025-08-03 20:54:51', '',
        '2025-08-03 20:54:51', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1073, 18, '2021年夏季', '2021年夏季', 'release_year_season', '0', 'admin', '2025-08-03 20:54:59', '',
        '2025-08-03 20:54:59', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1074, 19, '2021年秋季', '2021年秋季', 'release_year_season', '0', 'admin', '2025-08-03 20:55:07', '',
        '2025-08-03 20:55:07', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1075, 20, '2021年冬季', '2021年冬季', 'release_year_season', '0', 'admin', '2025-08-03 20:55:14', '',
        '2025-08-03 20:55:14', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1076, 21, '2022年春季', '2022年春季', 'release_year_season', '0', 'admin', '2025-08-03 20:55:29', '',
        '2025-08-03 20:55:29', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1077, 22, '2022年夏季', '2022年夏季', 'release_year_season', '0', 'admin', '2025-08-03 20:55:37', '',
        '2025-08-03 20:55:37', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1078, 23, '2022年秋季', '2022年秋季', 'release_year_season', '0', 'admin', '2025-08-03 20:55:44', '',
        '2025-08-03 20:55:44', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1079, 24, '2022年冬季', '2022年冬季', 'release_year_season', '0', 'admin', '2025-08-03 20:55:53', '',
        '2025-08-03 20:55:53', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1080, 25, '2023年春季', '2023年春季', 'release_year_season', '0', 'admin', '2025-08-03 20:56:01', '',
        '2025-08-03 20:56:01', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1081, 26, '2023年夏季', '2023年夏季', 'release_year_season', '0', 'admin', '2025-08-03 20:56:09', '',
        '2025-08-03 20:56:09', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1082, 27, '2023年秋季', '2023年秋季', 'release_year_season', '0', 'admin', '2025-08-03 20:56:21', '',
        '2025-08-03 20:56:21', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1083, 28, '2023年冬季', '2023年冬季', 'release_year_season', '0', 'admin', '2025-08-03 20:56:30', '',
        '2025-08-03 20:56:30', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1084, 29, '2024年春季', '2024年春季', 'release_year_season', '0', 'admin', '2025-08-03 20:56:42', '',
        '2025-08-03 20:56:42', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1085, 30, '2024年夏季', '2024年夏季', 'release_year_season', '0', 'admin', '2025-08-03 20:56:51', '',
        '2025-08-03 20:56:51', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1086, 31, '2024年秋季', '2024年秋季', 'release_year_season', '0', 'admin', '2025-08-03 20:56:58', '',
        '2025-08-03 20:56:58', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1087, 32, '2024年冬季', '2024年冬季', 'release_year_season', '0', 'admin', '2025-08-03 20:57:06', '',
        '2025-08-03 20:57:06', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1088, 33, '2025年春季', '2025年春季', 'release_year_season', '0', 'admin', '2025-08-03 20:57:13', '',
        '2025-08-03 20:57:13', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1089, 34, '2025年夏季', '2025年夏季', 'release_year_season', '0', 'admin', '2025-08-03 20:57:21', '',
        '2025-08-03 20:57:21', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1090, 35, '2025年秋季', '2025年秋季', 'release_year_season', '0', 'admin', '2025-08-03 20:57:32', '',
        '2025-08-03 20:57:32', '', 0, '0');
INSERT INTO `sys_dict_data`
VALUES (1092, 37, '2025年冬季', '2025年冬季', 'release_year_season', '0', 'admin', '2025-09-01 03:00:00', '',
        '2025-09-01 03:00:00', NULL, 0, '0');
INSERT INTO `sys_dict_data` VALUES (1093, 1, '档口缺货', '档口缺货', 'refund_reason', '0', 'admin', '2025-09-21 13:26:39', '', '2025-09-21 13:26:39', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1094, 2, '协商一致退款', '协商一致退款', 'refund_reason', '0', 'admin', '2025-09-21 13:26:54', '', '2025-09-21 13:26:54', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1095, 3, '订单信息拍错', '订单信息拍错', 'refund_reason', '0', 'admin', '2025-09-21 13:27:13', '', '2025-09-21 13:27:13', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1096, 4, '商品破损，已拒收', '商品破损，已拒收', 'refund_reason', '0', 'admin', '2025-09-21 13:27:30', 'admin', '2025-09-21 13:27:44', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1097, 6, '物流状态异常', '物流状态异常', 'refund_reason', '0', 'admin', '2025-09-21 13:28:12', '', '2025-09-21 13:28:12', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1098, 7, '商品质量问题', '商品质量问题', 'refund_reason', '0', 'admin', '2025-09-21 13:28:28', '', '2025-09-21 13:28:28', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1099, 8, '其他', '其他', 'refund_reason', '0', 'admin', '2025-09-21 13:28:34', '', '2025-09-21 13:28:34', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1100, 1, '头层牛皮', '头层牛皮', 'shaft_material', '0', 'admin', '2025-10-30 21:41:37', '', '2025-10-30 21:41:37', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1101, 2, '牛二层皮覆膜', '牛二层皮覆膜', 'shaft_material', '0', 'admin', '2025-10-30 21:41:44', '', '2025-10-30 21:41:44', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1102, 3, '羊皮（除羊反绒/羊猄）', '羊皮（除羊反绒/羊猄）', 'shaft_material', '0', 'admin', '2025-10-30 21:41:50', '', '2025-10-30 21:41:50', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1103, 4, '超细纤维', '超细纤维', 'shaft_material', '0', 'admin', '2025-10-30 21:41:56', '', '2025-10-30 21:41:56', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1104, 5, '牛反绒（磨砂皮）', '牛反绒（磨砂皮）', 'shaft_material', '0', 'admin', '2025-10-30 21:42:03', '', '2025-10-30 21:42:03', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1105, 6, '绒面', '绒面', 'shaft_material', '0', 'admin', '2025-10-30 21:42:15', '', '2025-10-30 21:42:15', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1106, 7, '羊反绒（羊猄）', '羊反绒（羊猄）', 'shaft_material', '0', 'admin', '2025-10-30 21:42:23', '', '2025-10-30 21:42:23', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1107, 8, 'PU', 'PU', 'shaft_material', '0', 'admin', '2025-10-30 21:42:29', '', '2025-10-30 21:42:29', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1108, 9, '弹力布', '弹力布', 'shaft_material', '0', 'admin', '2025-10-30 21:42:36', '', '2025-10-30 21:42:36', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1109, 10, '牛皮革', '牛皮革', 'shaft_material', '0', 'admin', '2025-10-30 21:42:43', '', '2025-10-30 21:42:43', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1110, 11, '裘皮', '裘皮', 'shaft_material', '0', 'admin', '2025-10-30 21:42:49', '', '2025-10-30 21:42:49', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1111, 12, '混合材质', '混合材质', 'shaft_material', '0', 'admin', '2025-10-30 21:42:57', '', '2025-10-30 21:42:57', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1112, 13, '羊毛皮革', '羊毛皮革', 'shaft_material', '0', 'admin', '2025-10-30 21:43:05', '', '2025-10-30 21:43:05', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1113, 14, '珍珠鱼皮', '珍珠鱼皮', 'shaft_material', '0', 'admin', '2025-10-30 21:43:13', '', '2025-10-30 21:43:13', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1114, 15, '羊剖层革', '羊剖层革', 'shaft_material', '0', 'admin', '2025-10-30 21:43:20', '', '2025-10-30 21:43:20', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1115, 16, '织物', '织物', 'shaft_material', '0', 'admin', '2025-10-30 21:43:28', '', '2025-10-30 21:43:28', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1116, 17, '塑胶', '塑胶', 'shaft_material', '0', 'admin', '2025-10-30 21:43:39', '', '2025-10-30 21:43:39', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1117, 18, '乙纶', '乙纶', 'shaft_material', '0', 'admin', '2025-10-30 21:43:46', '', '2025-10-30 21:43:46', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1118, 19, '马皮', '马皮', 'shaft_material', '0', 'admin', '2025-10-30 21:43:54', '', '2025-10-30 21:43:54', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1119, 20, '毛线', '毛线', 'shaft_material', '0', 'admin', '2025-10-30 21:44:01', '', '2025-10-30 21:44:01', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1120, 21, '山羊绒', '山羊绒', 'shaft_material', '0', 'admin', '2025-10-30 21:44:08', '', '2025-10-30 21:44:08', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1121, 22, '漆皮', '漆皮', 'shaft_material', '0', 'admin', '2025-10-30 21:44:26', '', '2025-10-30 21:44:26', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1122, 23, '羊皮革', '羊皮革', 'shaft_material', '0', 'admin', '2025-10-30 21:44:33', '', '2025-10-30 21:44:33', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1123, 24, '布', '布', 'shaft_material', '0', 'admin', '2025-10-30 21:44:49', '', '2025-10-30 21:44:49', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1124, 25, '马毛', '马毛', 'shaft_material', '0', 'admin', '2025-10-30 21:44:56', '', '2025-10-30 21:44:56', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1125, 26, '牛仔布', '牛仔布', 'shaft_material', '0', 'admin', '2025-10-30 21:45:03', '', '2025-10-30 21:45:03', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1126, 27, '人造革', '人造革', 'shaft_material', '0', 'admin', '2025-10-30 21:45:11', '', '2025-10-30 21:45:11', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1127, 28, '聚酯纤维', '聚酯纤维', 'shaft_material', '0', 'admin', '2025-10-30 21:45:20', '', '2025-10-30 21:45:20', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1128, 29, '绸缎', '绸缎', 'shaft_material', '0', 'admin', '2025-10-30 21:45:27', '', '2025-10-30 21:45:27', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1129, 30, '猪皮', '猪皮', 'shaft_material', '0', 'admin', '2025-10-30 21:45:37', '', '2025-10-30 21:45:37', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1130, 31, '皮布拼接', '皮布拼接', 'shaft_material', '0', 'admin', '2025-10-30 21:45:47', '', '2025-10-30 21:45:47', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1131, 32, '羊皮革/羊剖层革', '羊皮革/羊剖层革', 'shaft_material', '0', 'admin', '2025-10-30 21:45:57', '', '2025-10-30 21:45:57', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1132, 33, '牛绒面革', '牛绒面革', 'shaft_material', '0', 'admin', '2025-10-30 21:46:05', '', '2025-10-30 21:46:05', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1133, 34, '牛剖层革', '牛剖层革', 'shaft_material', '0', 'admin', '2025-10-30 21:46:14', '', '2025-10-30 21:46:14', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1134, 35, '牛皮剖层反绒面革', '牛皮剖层反绒面革', 'shaft_material', '0', 'admin', '2025-10-30 21:46:21', '', '2025-10-30 21:46:21', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1135, 36, '格利特皮革', '格利特皮革', 'shaft_material', '0', 'admin', '2025-10-30 21:46:32', '', '2025-10-30 21:46:32', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1136, 37, '羊驼皮', '羊驼皮', 'shaft_material', '0', 'admin', '2025-10-30 21:46:43', '', '2025-10-30 21:46:43', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1137, 38, '鳄鱼皮', '鳄鱼皮', 'shaft_material', '0', 'admin', '2025-10-30 21:47:00', '', '2025-10-30 21:47:00', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1138, 39, '亮片布', '亮片布', 'shaft_material', '0', 'admin', '2025-10-30 21:47:09', '', '2025-10-30 21:47:09', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1139, 40, '氯纶', '氯纶', 'shaft_material', '0', 'admin', '2025-10-30 21:47:17', '', '2025-10-30 21:47:17', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1140, 41, '西施绒', '西施绒', 'shaft_material', '0', 'admin', '2025-10-30 21:47:26', '', '2025-10-30 21:47:26', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1141, 42, '牛毛皮', '牛毛皮', 'shaft_material', '0', 'admin', '2025-10-30 21:47:35', '', '2025-10-30 21:47:35', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1142, 43, '合成材料', '合成材料', 'shaft_material', '0', 'admin', '2025-10-30 21:47:43', '', '2025-10-30 21:47:43', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1143, 44, '袋鼠皮', '袋鼠皮', 'shaft_material', '0', 'admin', '2025-10-30 21:47:54', '', '2025-10-30 21:47:54', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1144, 45, '灯芯绒', '灯芯绒', 'shaft_material', '0', 'admin', '2025-10-30 21:48:03', '', '2025-10-30 21:48:03', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1145, 46, '蛇皮', '蛇皮', 'shaft_material', '0', 'admin', '2025-10-30 21:48:11', '', '2025-10-30 21:48:11', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1147, 48, '牛剖层移膜革', '牛剖层移膜革', 'shaft_material', '0', 'admin', '2025-10-30 21:48:25', '', '2025-10-30 21:48:25', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1148, 49, '树脂', '树脂', 'shaft_material', '0', 'admin', '2025-10-30 21:48:32', '', '2025-10-30 21:48:32', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1149, 50, '羊皮毛一体', '羊皮毛一体', 'shaft_material', '0', 'admin', '2025-10-30 21:48:39', '', '2025-10-30 21:48:39', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1150, 51, '鹿皮', '鹿皮', 'shaft_material', '0', 'admin', '2025-10-30 21:48:48', '', '2025-10-30 21:48:48', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1151, 52, '羊绒面革', '羊绒面革', 'shaft_material', '0', 'admin', '2025-10-30 21:48:55', '', '2025-10-30 21:48:55', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1152, 53, '网布', '网布', 'shaft_material', '0', 'admin', '2025-10-30 21:49:03', '', '2025-10-30 21:49:03', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1153, 54, '牛皮革/织物', '牛皮革/织物', 'shaft_material', '0', 'admin', '2025-10-30 21:49:11', '', '2025-10-30 21:49:11', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1154, 55, '橡胶', '橡胶', 'shaft_material', '0', 'admin', '2025-10-30 21:49:18', '', '2025-10-30 21:49:18', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1155, 56, '鳗鱼皮', '鳗鱼皮', 'shaft_material', '0', 'admin', '2025-10-30 21:49:26', '', '2025-10-30 21:49:26', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1156, 57, '牛皮剖层绒面革', '牛皮剖层绒面革', 'shaft_material', '0', 'admin', '2025-10-30 21:49:39', '', '2025-10-30 21:49:39', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1157, 58, '织物＋PU', '织物＋PU', 'shaft_material', '0', 'admin', '2025-10-30 21:49:46', '', '2025-10-30 21:49:46', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1158, 59, '呢子', '呢子', 'shaft_material', '0', 'admin', '2025-10-30 21:49:54', '', '2025-10-30 21:49:54', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1159, 60, '二层猪皮', '二层猪皮', 'shaft_material', '0', 'admin', '2025-10-30 21:50:01', '', '2025-10-30 21:50:01', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1160, 61, '棉', '棉', 'shaft_material', '0', 'admin', '2025-10-30 21:50:09', '', '2025-10-30 21:50:09', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1161, 62, '粒纹皮', '粒纹皮', 'shaft_material', '0', 'admin', '2025-10-30 21:50:17', '', '2025-10-30 21:50:17', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1162, 63, '羊皮剖层绒面革', '羊皮剖层绒面革', 'shaft_material', '0', 'admin', '2025-10-30 21:50:23', '', '2025-10-30 21:50:23', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1163, 64, 'EVA', 'EVA', 'shaft_material', '0', 'admin', '2025-10-30 21:50:32', '', '2025-10-30 21:50:32', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1164, 65, '蜥蜴皮', '蜥蜴皮', 'shaft_material', '0', 'admin', '2025-10-30 21:50:38', '', '2025-10-30 21:50:38', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1166, 67, '橡塑', '橡塑', 'shaft_material', '0', 'admin', '2025-10-30 21:50:52', '', '2025-10-30 21:50:52', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1167, 68, '藤草', '藤草', 'shaft_material', '0', 'admin', '2025-10-30 21:50:59', '', '2025-10-30 21:50:59', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1168, 69, '皮革', '皮革', 'shaft_material', '0', 'admin', '2025-10-30 21:51:06', '', '2025-10-30 21:51:06', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1169, 70, '麂皮', '麂皮', 'shaft_material', '0', 'admin', '2025-10-30 21:51:14', '', '2025-10-30 21:51:14', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1170, 71, '羊毛皮', '羊毛皮', 'shaft_material', '0', 'admin', '2025-10-30 21:51:23', '', '2025-10-30 21:51:23', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1171, 72, '鸵鸟皮', '鸵鸟皮', 'shaft_material', '0', 'admin', '2025-10-30 21:51:32', '', '2025-10-30 21:51:32', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1172, 73, '牛皮革/牛剖层革', '牛皮革/牛剖层革', 'shaft_material', '0', 'admin', '2025-10-30 21:51:42', '', '2025-10-30 21:51:42', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1173, 74, '孔雀皮', '孔雀皮', 'shaft_material', '0', 'admin', '2025-10-30 21:51:49', '', '2025-10-30 21:51:49', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1174, 1, '头层猪皮', '头层猪皮', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:55:54', 'admin', '2025-10-30 21:58:06', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1175, 2, '超细纤维', '超细纤维', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:00', 'admin', '2025-10-30 21:58:10', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1176, 3, '布', '布', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:05', 'admin', '2025-10-30 21:58:14', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1177, 4, '人造短毛绒', '人造短毛绒', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:11', 'admin', '2025-10-30 21:58:17', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1178, 5, '二层猪皮', '二层猪皮', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:16', 'admin', '2025-10-30 21:58:20', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1179, 6, '羊皮', '羊皮', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:21', 'admin', '2025-10-30 21:58:25', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1180, 7, '混合材质', '混合材质', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:26', 'admin', '2025-10-30 21:58:28', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1181, 8, '头层牛皮', '头层牛皮', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:34', 'admin', '2025-10-30 21:58:31', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1182, 9, '织物', '织物', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:41', 'admin', '2025-10-30 21:58:34', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1183, 10, '羊皮革', '羊皮革', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:47', 'admin', '2025-10-30 21:58:37', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1184, 11, '织物+PU合成革', '织物+PU合成革', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:56:55', 'admin', '2025-10-30 21:58:40', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1185, 12, 'PU', 'PU', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:57:02', 'admin', '2025-10-30 21:58:44', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1186, 13, '网纱', '网纱', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:57:09', 'admin', '2025-10-30 21:58:47', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1187, 14, '人造长毛绒', '人造长毛绒', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:57:16', 'admin', '2025-10-30 21:59:23', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1188, 15, '无内里', '无内里', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:57:24', 'admin', '2025-10-30 21:59:27', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1189, 16, '绒面', '绒面', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:57:31', 'admin', '2025-10-30 21:59:30', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1190, 17, '反毛皮', '反毛皮', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:57:41', 'admin', '2025-10-30 21:59:32', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1191, 18, '皮革', '皮革', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:57:48', 'admin', '2025-10-30 21:59:35', '', 1, '0');
INSERT INTO `sys_dict_data` VALUES (1192, 19, '纯羊毛', '纯羊毛', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:57:54', 'admin', '2025-10-30 21:59:37', '', 2, '0');
INSERT INTO `sys_dict_data` VALUES (1193, 20, '二层牛皮', '二层牛皮', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:59:48', '', '2025-10-30 21:59:48', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1194, 21, '牛皮革', '牛皮革', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 21:59:54', '', '2025-10-30 21:59:54', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1195, 22, '羊皮毛一体', '羊皮毛一体', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:00:01', '', '2025-10-30 22:00:01', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1196, 23, '棉', '棉', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:00:08', '', '2025-10-30 22:00:08', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1197, 24, '羊毛羊绒混纺', '羊毛羊绒混纺', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:01:09', '', '2025-10-30 22:01:09', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1198, 25, '马皮', '马皮', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:01:17', '', '2025-10-30 22:01:17', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1199, 26, '弹力绒布', '弹力绒布', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:01:24', '', '2025-10-30 22:01:24', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1200, 27, '羊毛混纺', '羊毛混纺', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:01:32', '', '2025-10-30 22:01:32', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1201, 28, '乙纶', '乙纶', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:01:38', '', '2025-10-30 22:01:38', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1202, 29, '胎牛皮', '胎牛皮', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:01:47', '', '2025-10-30 22:01:47', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1203, 30, '鹿纤绒', '鹿纤绒', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:01:53', '', '2025-10-30 22:01:53', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1204, 31, '涤沦', '涤沦', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:02:05', '', '2025-10-30 22:02:05', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1205, 32, '山羊绒', '山羊绒', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:02:12', '', '2025-10-30 22:02:12', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1206, 33, '聚酯纤维', '聚酯纤维', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:02:19', '', '2025-10-30 22:02:19', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1207, 34, '兔毛', '兔毛', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:02:26', '', '2025-10-30 22:02:26', '', 0, '0');
INSERT INTO `sys_dict_data` VALUES (1208, 35, '狐狸毛', '狐狸毛', 'shoe_upper_lining_material', '0', 'admin', '2025-10-30 22:02:34', '', '2025-10-30 22:02:34', '', 0, '0');


-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config
(
    config_id    int(5) not null auto_increment comment '参数主键',
    config_name  varchar(100) default '' comment '参数名称',
    config_key   varchar(100) default '' comment '参数键名',
    config_value varchar(500) default '' comment '参数键值',
    config_type  char(1)      default 'N' comment '系统内置（Y是 N否）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    remark       varchar(500) default null comment '备注',
    primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

insert into sys_config
values (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', sysdate(), '', null,
        '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
insert into sys_config
values (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', sysdate(), '', null, '初始化密码 123456');
insert into sys_config
values (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', sysdate(), '', null,
        '深色主题theme-dark，浅色主题theme-light');
insert into sys_config
values (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', sysdate(), '', null,
        '是否开启验证码功能（true开启，false关闭）');
insert into sys_config
values (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', sysdate(), '', null,
        '是否开启注册用户功能（true开启，false关闭）');
insert into sys_config
values (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', sysdate(), '', null,
        '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');


-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor
(
    info_id        bigint(20) not null auto_increment comment '访问ID',
    user_name      varchar(50)  default '' comment '用户账号',
    ipaddr         varchar(128) default '' comment '登录IP地址',
    login_location varchar(255) default '' comment '登录地点',
    browser        varchar(50)  default '' comment '浏览器类型',
    os             varchar(50)  default '' comment '操作系统',
    status         char(1)      default '0' comment '登录状态（0成功 1失败）',
    msg            varchar(255) default '' comment '提示消息',
    login_time     datetime comment '访问时间',
    primary key (info_id),
    key            idx_sys_logininfor_s (status),
    key            idx_sys_logininfor_lt (login_time)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 15、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job
(
    job_id          bigint(20) not null auto_increment comment '任务ID',
    job_name        varchar(64)  default '' comment '任务名称',
    job_group       varchar(64)  default 'DEFAULT' comment '任务组名',
    invoke_target   varchar(500) not null comment '调用目标字符串',
    cron_expression varchar(255) default '' comment 'cron执行表达式',
    misfire_policy  varchar(20)  default '3' comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
    concurrent      char(1)      default '1' comment '是否并发执行（0允许 1禁止）',
    status          char(1)      default '0' comment '状态（0正常 1暂停）',
    `version`       bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    create_by       varchar(64)  default '' comment '创建者',
    create_time     datetime comment '创建时间',
    update_by       varchar(64)  default '' comment '更新者',
    update_time     datetime comment '更新时间',
    remark          varchar(500) default '' comment '备注信息',
    primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

INSERT INTO `sys_job` VALUES (100, '每日3点05分生成季节年份', 'DEFAULT', 'xktTask.seasonTag', '0 5 3 * * ?', '1', '1', '0', 2, '0', 'admin', '2025-08-12 12:33:42', 'admin', '2025-11-08 23:30:29', '');
INSERT INTO `sys_job` VALUES (101, '凌晨01:00:01同步store到redis', 'DEFAULT', 'xktTask.saveStoreToRedis', '1 0 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:43:17', 'admin', '2025-11-08 23:25:11', '');
INSERT INTO `sys_job` VALUES (102, '凌晨01:01更新推广轮次', 'DEFAULT', 'xktTask.dailyAdvertRound', '0 1 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:44:43', 'admin', '2025-11-08 23:25:55', '');
INSERT INTO `sys_job` VALUES (103, '凌晨01:04更新symbol到redis', 'DEFAULT', 'xktTask.saveSymbolToRedis', '0 4 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:45:49', 'admin', '2025-11-08 23:26:12', '');
INSERT INTO `sys_job` VALUES (104, '凌晨01:08更新推广轮次结束时间', 'DEFAULT', 'xktTask.saveAdvertDeadlineToRedis', '0 8 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:46:46', 'admin', '2025-11-08 23:26:29', '');
INSERT INTO `sys_job` VALUES (105, '凌晨01:10同步档口销售数据', 'DEFAULT', 'xktTask.dailySale', '0 10 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:47:26', 'admin', '2025-11-08 23:26:45', '');
INSERT INTO `sys_job` VALUES (106, '凌晨01:15同步档口客户销售数据', 'DEFAULT', 'xktTask.dailySaleCustomer', '0 15 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:48:29', 'admin', '2025-11-08 23:26:56', '');
INSERT INTO `sys_job` VALUES (107, '凌晨01:20同步档口商品销售数据', 'DEFAULT', 'xktTask.dailySaleProduct', '0 20 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:49:07', 'admin', '2025-11-08 23:27:11', '');
INSERT INTO `sys_job` VALUES (108, '凌晨01:25同步商品最新分类排序', 'DEFAULT', 'xktTask.dailyCategorySort', '0 25 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:49:49', 'admin', '2025-11-08 23:27:22', '');
INSERT INTO `sys_job` VALUES (109, '凌晨01:30更新档口标签', 'DEFAULT', 'xktTask.dailyStoreTag', '0 30 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:50:20', 'admin', '2025-11-08 23:27:33', '');
INSERT INTO `sys_job` VALUES (110, '凌晨01:40更新商品标签', 'DEFAULT', 'xktTask.dailyProdTag', '0 40 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:50:52', 'admin', '2025-11-08 23:27:44', '');
INSERT INTO `sys_job` VALUES (111, '凌晨01:45将advert暂存到redis', 'DEFAULT', 'xktTask.saveAdvertToRedis', '0 45 1 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:54:16', 'admin', '2025-11-08 23:27:52', '');
INSERT INTO `sys_job` VALUES (112, '凌晨2点更新商品各项权重', 'DEFAULT', 'xktTask.dailyProdWeight', '0 0 2 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:59:17', 'admin', '2025-11-08 23:28:04', '');
INSERT INTO `sys_job` VALUES (113, '凌晨2:10更新档口权重', 'DEFAULT', 'xktTask.dailyStoreWeight', '0 10 2 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 12:59:57', 'admin', '2025-11-08 23:28:12', '');
INSERT INTO `sys_job` VALUES (114, '凌晨2:15更新用户搜索历史入库', 'DEFAULT', 'xktTask.dailyUpdateUserSearchHistory', '0 15 2 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 13:01:02', 'admin', '2025-11-08 23:28:25', '');
INSERT INTO `sys_job` VALUES (115, '凌晨2:20更新用户了浏览记录入库', 'DEFAULT', 'xktTask.dailyUpdateUserBrowsingHistory', '0 20 2 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 13:01:48', 'admin', '2025-11-08 23:28:36', '');
INSERT INTO `sys_job` VALUES (116, '凌晨2:25更新系统热搜到redis', 'DEFAULT', 'xktTask.dailyUpdateSearchHotToRedis', '0 25 2 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 13:02:42', 'admin', '2025-11-08 23:28:45', '');
INSERT INTO `sys_job` VALUES (117, '凌晨2:30更新图搜热款', 'DEFAULT', 'xktTask.imgSearchTopProductStatistics', '0 30 2 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 13:03:18', 'admin', '2025-11-08 23:28:56', '');
INSERT INTO `sys_job` VALUES (118, '凌晨2:45更新档口会员过期', 'DEFAULT', 'xktTask.autoCloseExpireStoreMember', '0 45 2 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 13:04:02', 'admin', '2025-11-08 23:29:09', '');
INSERT INTO `sys_job` VALUES (119, '凌晨2:50更新档口会员到redis', 'DEFAULT', 'xktTask.saveStoreMemberToRedis', '0 50 2 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 13:05:15', 'admin', '2025-11-08 23:29:18', '');
INSERT INTO `sys_job` VALUES (120, '凌晨2:55更新app商品销量榜、分类商品销量榜', 'DEFAULT', 'xktTask.dailyProdTopSale', '0 55 2 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 13:06:02', 'admin', '2025-11-08 23:29:33', '');
INSERT INTO `sys_job` VALUES (121, '凌晨3:00更新档口权重到redis', 'DEFAULT', 'xktTask.updateStoreWeightToES', '0 0 3 * * ?', '1', '1', '0', 1, '0', 'admin', '2025-08-12 13:08:09', 'admin', '2025-11-08 23:29:44', '');
INSERT INTO `sys_job` VALUES (122, '每晚22:00:10更新广告位竞价状态', 'DEFAULT', 'xktTask.updateAdvertRoundBiddingStatus', '10 0 22 * * ?', '1', '1', '0', 0, '0', 'admin', '2025-08-12 13:08:55', '', '2025-08-12 13:08:55', '');
INSERT INTO `sys_job` VALUES (123, '每小时定时发布商品', 'DEFAULT', 'xktTask.hourPublicStoreProduct', '0 0 * * * ?', '1', '1', '0', 0, '0', 'admin', '2025-08-12 13:10:05', '', '2025-08-12 13:10:05', '');
INSERT INTO `sys_job` VALUES (124, '自动关闭超时订单', 'DEFAULT', 'xktTask.autoCloseTimeoutStoreOrder', '0 0/10 * * * ? ', '3', '1', '0', 0, '0', 'admin', '2025-09-02 16:08:01', '', '2025-09-02 16:08:01', '');
INSERT INTO `sys_job` VALUES (125, '自动完成订单', 'DEFAULT', 'xktTask.autoCompleteStoreOrder', '0 0/10 * * * ? ', '3', '1', '0', 0, '0', 'admin', '2025-09-02 16:11:19', '', '2025-09-02 16:11:19', '');
INSERT INTO `sys_job` VALUES (126, '自动订单退款', 'DEFAULT', 'xktTask.autoRefundStoreOrder', '0 0/10 * * * ? ', '3', '1', '0', 0, '0', 'admin', '2025-09-02 16:12:10', '', '2025-09-02 16:12:10', '');
INSERT INTO `sys_job` VALUES (127, '继续处理退款（异常中断补偿，非正常流程）', 'DEFAULT', 'xktTask.continueProcessRefund', '0 0/10 * * * ? ', '3', '1', '0', 0, '0', 'admin', '2025-09-02 16:13:08', '', '2025-09-02 16:13:08', '');
INSERT INTO `sys_job` VALUES (128, '继续处理档口提现（异常中断补偿，非正常流程）', 'DEFAULT', 'xktTask.continueProcessWithdraw', '0 0/10 * * * ? ', '3', '1', '0', 0, '0', 'admin', '2025-09-02 16:13:54', '', '2025-09-02 16:13:54', '');
INSERT INTO `sys_job` VALUES (129, '继续处理支付宝支付回调信息（异常中断补偿，非正常流程）', 'DEFAULT', 'xktTask.continueProcessAliCallback', '0 0/10 * * * ? ', '3', '1', '0', 0, '0', 'admin', '2025-09-02 16:14:30', '', '2025-09-02 16:14:30', '');
INSERT INTO `sys_job` VALUES (130, '商品当日浏览量、下载量、图搜次数统计', 'DEFAULT', 'xktTask.dailyProductStatistics', '0 0 23 * * ? ', '3', '1', '0', 0, '0', 'admin', '2025-09-02 16:15:54', 'admin', '2025-09-02 16:15:54', '');
INSERT INTO `sys_job` VALUES (131, '从中通同步行政区划', 'DEFAULT', 'xktTask.syncRegionFromZto', '0 0 0 1 * ? ', '3', '1', '0', 0, '0', 'admin', '2025-09-02 16:17:37', 'admin', '2025-09-02 16:17:37', '');
INSERT INTO `sys_job` VALUES (132, '每月第一天凌晨5:00重置单据编号初始值', 'DEFAULT', 'xktTask.resetVoucherSequence', '0 0 5 1 * ?', '1', '1', '0', 0, '0', 'admin', '2025-11-06 22:26:37', '', '2025-11-06 22:26:37', '');


-- ----------------------------
-- 16、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log
(
    job_log_id     bigint(20) not null auto_increment comment '任务日志ID',
    job_name       varchar(64)  not null comment '任务名称',
    job_group      varchar(64)  not null comment '任务组名',
    invoke_target  varchar(500) not null comment '调用目标字符串',
    job_message    varchar(500) comment '日志信息',
    status         char(1)       default '0' comment '执行状态（0正常 1失败）',
    exception_info varchar(2000) default '' comment '异常信息',
    create_time    datetime comment '创建时间',
    primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';



-- ----------------------------
-- Table structure for picture_search
-- ----------------------------
DROP TABLE IF EXISTS `picture_search`;
CREATE TABLE `picture_search`
(
    `id`             bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文件搜索ID',
    `search_file_id` bigint UNSIGNED NOT NULL COMMENT '搜索的文件ID',
    `user_id`        bigint UNSIGNED NOT NULL COMMENT '使用以图搜款用户ID',
    `voucher_date`   date NOT NULL COMMENT '搜索日期',
    `version`        bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '以图搜款' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for quick_function
-- ----------------------------
DROP TABLE IF EXISTS `quick_function`;
CREATE TABLE `quick_function`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口快捷功能ID',
    `user_id`     bigint UNSIGNED NOT NULL COMMENT 'user.id',
    `menu_id`     bigint UNSIGNED NOT NULL COMMENT '菜单id',
    `order_num`   int UNSIGNED NOT NULL COMMENT '排序',
    `version`     bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口快捷功能' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`
(
    `id`            bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户进货车ID',
    `user_id`       bigint UNSIGNED NOT NULL COMMENT 'sys_user.id',
    `store_id`      bigint UNSIGNED NOT NULL COMMENT 'store.id',
    `store_prod_id` bigint UNSIGNED NOT NULL COMMENT 'store_prod.id',
    `prod_art_num`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品货号',
    `version`       bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户进货车' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for shopping_cart_detail
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart_detail`;
CREATE TABLE `shopping_cart_detail`
(
    `id`                  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户进货车ID',
    `shopping_cart_id`    bigint UNSIGNED NOT NULL COMMENT 'shopping_cart.id',
    `store_prod_color_id` bigint UNSIGNED NOT NULL COMMENT 'store_prod_color.id',
    `size`                int UNSIGNED NOT NULL COMMENT '尺码',
    `quantity`            int UNSIGNED NULL DEFAULT NULL COMMENT '商品数量',
    `store_color_id`      bigint UNSIGNED NOT NULL COMMENT '档口颜色ID',
    `color_name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '颜色名称',
    `version`             bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 220 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户进货车' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- 17、通知公告表
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`
(
    `id`             int                                                          NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `notice_title`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
    `notice_type`    int                                                          NOT NULL COMMENT '公告类型（1通知 2公告）',
    `owner_type`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '公告拥有者（1系统 2档口）',
    `notice_content` longblob NULL COMMENT '公告内容',
    `store_id`       bigint UNSIGNED NULL DEFAULT NULL COMMENT '档口ID',
    `user_id`        bigint UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
    `effect_start`   datetime NULL DEFAULT NULL COMMENT '公告生效时间(yyyy-MM-dd HH:mm)',
    `effect_end`     datetime NULL DEFAULT NULL COMMENT '公告失效时间(yyyy-MM-dd HH:mm)',
    `perpetuity`     tinyint(1) NULL DEFAULT NULL COMMENT '是否永久生效',
    `version`                  int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    `del_flag`                 char(1)        DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------


-- ----------------------------
-- 18、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table
(
    table_id          bigint(20) not null auto_increment comment '编号',
    table_name        varchar(200) default '' comment '表名称',
    table_comment     varchar(500) default '' comment '表描述',
    sub_table_name    varchar(64)  default null comment '关联子表的表名',
    sub_table_fk_name varchar(64)  default null comment '子表关联的外键名',
    class_name        varchar(100) default '' comment '实体类名称',
    tpl_category      varchar(200) default 'crud' comment '使用的模板（crud单表操作 tree树表操作）',
    tpl_web_type      varchar(30)  default '' comment '前端模板类型（element-ui模版 element-plus模版）',
    package_name      varchar(100) comment '生成包路径',
    module_name       varchar(30) comment '生成模块名',
    business_name     varchar(30) comment '生成业务名',
    function_name     varchar(50) comment '生成功能名',
    function_author   varchar(50) comment '生成功能作者',
    gen_type          char(1)      default '0' comment '生成代码方式（0zip压缩包 1自定义路径）',
    gen_path          varchar(200) default '/' comment '生成路径（不填默认项目路径）',
    options           varchar(1000) comment '其它生成选项',
    create_by         varchar(64)  default '' comment '创建者',
    create_time       datetime comment '创建时间',
    update_by         varchar(64)  default '' comment '更新者',
    update_time       datetime comment '更新时间',
    remark            varchar(500) default null comment '备注',
    primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 19、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column
(
    column_id      bigint(20) not null auto_increment comment '编号',
    table_id       bigint(20) comment '归属表编号',
    column_name    varchar(200) comment '列名称',
    column_comment varchar(500) comment '列描述',
    column_type    varchar(100) comment '列类型',
    java_type      varchar(500) comment 'JAVA类型',
    java_field     varchar(200) comment 'JAVA字段名',
    is_pk          char(1) comment '是否主键（1是）',
    is_increment   char(1) comment '是否自增（1是）',
    is_required    char(1) comment '是否必填（1是）',
    is_insert      char(1) comment '是否为插入字段（1是）',
    is_edit        char(1) comment '是否编辑字段（1是）',
    is_list        char(1) comment '是否列表字段（1是）',
    is_query       char(1) comment '是否查询字段（1是）',
    query_type     varchar(200) default 'EQ' comment '查询方式（等于、不等于、大于、小于、范围）',
    html_type      varchar(200) comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
    dict_type      varchar(200) default '' comment '字典类型',
    sort           int comment '排序',
    create_by      varchar(64)  default '' comment '创建者',
    create_time    datetime comment '创建时间',
    update_by      varchar(64)  default '' comment '更新者',
    update_time    datetime comment '更新时间',
    primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';

DROP TABLE IF EXISTS `store_order`;
CREATE TABLE `store_order`
(
    `id`                               bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `store_id`                         bigint(20) NOT NULL COMMENT '档口ID',
    `order_user_id`                    bigint(20) NOT NULL COMMENT '下单用户ID',
    `order_no`                         varchar(32)    NOT NULL COMMENT '订单号',
    `order_type`                       tinyint(4) NOT NULL COMMENT '订单类型[1:销售订单 2:退货订单]',
    `order_status`                     tinyint(4) NOT NULL COMMENT '订单状态（1开头为销售订单状态，2开头为退货订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]',
    `pay_status`                       tinyint(4) NOT NULL COMMENT '支付状态[1:初始 2:支付中 3:已支付]',
    `pay_channel`                      tinyint(4) NOT NULL COMMENT '支付渠道[1:支付宝]',
    `pay_trade_no`                     varchar(255)   DEFAULT NULL COMMENT '支付交易号',
    `order_remark`                     varchar(255)   DEFAULT NULL COMMENT '订单备注',
    `goods_quantity`                   int(11) NOT NULL COMMENT '商品数量',
    `goods_amount`                     decimal(18, 2) NOT NULL COMMENT '商品金额',
    `express_fee`                      decimal(18, 2) NOT NULL COMMENT '快递费',
    `total_amount`                     decimal(18, 2) NOT NULL COMMENT '总金额（商品金额+快递费）',
    `real_total_amount`                decimal(18, 2) DEFAULT NULL COMMENT '实际总金额（总金额-支付渠道服务费）',
    `origin_order_id`                  bigint(20) DEFAULT NULL COMMENT '退货原订单ID',
    `refund_reason_code`               varchar(255)   DEFAULT NULL COMMENT '退货原因',
    `refund_reject_reason`             varchar(255)   DEFAULT NULL COMMENT '退货拒绝原因',
    `express_id`                       bigint(20) DEFAULT NULL COMMENT '物流ID',
    `origin_contact_name`              varchar(32)    DEFAULT NULL COMMENT '发货人-名称',
    `origin_contact_phone_number`      varchar(32)    DEFAULT NULL COMMENT '发货人-电话',
    `origin_province_code`             varchar(16)     DEFAULT NULL COMMENT '发货人-省编码',
    `origin_city_code`                 varchar(16)     DEFAULT NULL COMMENT '发货人-市编码',
    `origin_county_code`               varchar(16)     DEFAULT NULL COMMENT '发货人-区县编码',
    `origin_detail_address`            varchar(255)   DEFAULT NULL COMMENT '发货人-详细地址',
    `destination_contact_name`         varchar(32)    DEFAULT NULL COMMENT '收货人-名称',
    `destination_contact_phone_number` varchar(32)    DEFAULT NULL COMMENT '收货人-电话',
    `destination_province_code`        varchar(16)     DEFAULT NULL COMMENT '收货人-省编码',
    `destination_city_code`            varchar(16)     DEFAULT NULL COMMENT '收货人-市编码',
    `destination_county_code`          varchar(16)     DEFAULT NULL COMMENT '收货人-区县编码',
    `destination_detail_address`       varchar(255)   DEFAULT NULL COMMENT '收货人-详细地址',
    `delivery_type`                    tinyint(4) DEFAULT NULL COMMENT '发货方式[1:货其再发 2:有货先发]',
    `delivery_end_time`                datetime       DEFAULT NULL COMMENT '最晚发货时间',
    `auto_end_time`                    datetime       DEFAULT NULL COMMENT '自动完成时间',
    `voucher_date`                     date           DEFAULT NULL COMMENT '凭证日期',
    `del_flag`                         char(1)        DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                        varchar(64)    DEFAULT '' COMMENT '创建者',
    `create_time`                      datetime       DEFAULT NULL COMMENT '创建时间',
    `update_by`                        varchar(64)    DEFAULT '' COMMENT '更新者',
    `update_time`                      datetime       DEFAULT NULL COMMENT '更新时间',
    `version`                          int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`) USING BTREE,
    KEY                                `idx_sid_ot` (`store_id`,`order_type`) USING BTREE,
    KEY                                `idx_origin_order_id` (`origin_order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代发订单';

ALTER TABLE `store_order`
    MODIFY COLUMN `refund_reject_reason` varchar (512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退货拒绝原因' AFTER `refund_reason_code`,
    ADD COLUMN `platform_involve_reason` varchar (512) NULL COMMENT '平台介入原因' AFTER `refund_reject_reason`,
    ADD COLUMN `platform_involve_result` varchar (512) NULL COMMENT '平台介入结果' AFTER `platform_involve_reason`;

ALTER TABLE `store_order`
ADD COLUMN `pay_over_time` datetime NULL COMMENT '支付完成时间' AFTER `voucher_date`,
ADD COLUMN `delivery_over_time` datetime NULL COMMENT '发货完成时间' AFTER `pay_over_time`;

ALTER TABLE `store_order`
ADD INDEX `idx_delivery_end_time`(`delivery_end_time`) USING BTREE,
ADD INDEX `idx_delivery_over_time`(`delivery_over_time`) USING BTREE;

ALTER TABLE `store_order`
ADD INDEX `idx_pay_over_time`(`pay_over_time`) USING BTREE;

DROP TABLE IF EXISTS `store_order_detail`;
CREATE TABLE `store_order_detail`
(
    `id`                       bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单明细ID',
    `store_order_id`           bigint(20) NOT NULL COMMENT '订单ID',
    `store_prod_color_size_id` bigint(20) NOT NULL COMMENT '商品颜色尺码ID',
    `store_prod_id`            bigint(20) NOT NULL COMMENT '商品ID',
    `prod_name`                varchar(64)    DEFAULT NULL COMMENT '商品名称',
    `prod_art_num`             varchar(64)    DEFAULT NULL COMMENT '商品货号',
    `prod_title`               varchar(64)    DEFAULT NULL COMMENT '商品标题',
    `store_color_id`           bigint(20) DEFAULT NULL COMMENT '档口颜色ID',
    `color_name`               varchar(64)    DEFAULT NULL COMMENT '颜色名称',
    `size`                     tinyint(4) DEFAULT NULL COMMENT '商品尺码',
    `detail_status`            tinyint(4) NOT NULL COMMENT '订单明细状态（同订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]',
    `pay_status`               tinyint(4) NOT NULL COMMENT '支付状态[1:初始 2:支付中 3:已支付]',
    `express_id`               bigint(20) DEFAULT NULL COMMENT '物流ID',
    `express_type`             tinyint(4) DEFAULT NULL COMMENT '物流类型[1:平台物流 2:档口物流]',
    `express_status`           tinyint(4) NOT NULL COMMENT '物流状态[1:初始 2:下单中 3:已下单 4:取消中 5:已揽件 6:拦截中 99:已结束]',
    `express_req_no`           varchar(32)    DEFAULT NULL COMMENT '物流请求单号',
    `express_waybill_no`       varchar(512)   DEFAULT NULL COMMENT '物流运单号（快递单号），档口/用户自己填写时可能存在多个，使用“,”分割',
    `goods_price`              decimal(18, 2) NOT NULL COMMENT '商品单价',
    `goods_quantity`           int(11) NOT NULL COMMENT '商品数量',
    `goods_amount`             decimal(18, 2) NOT NULL COMMENT '商品金额（商品单价*商品数量）',
    `express_fee`              decimal(18, 2) NOT NULL COMMENT '快递费',
    `total_amount`             decimal(18, 2) NOT NULL COMMENT '总金额（商品金额+快递费）',
    `real_total_amount`        decimal(18, 2) DEFAULT NULL COMMENT '实际总金额（总金额-支付渠道服务费）',
    `origin_order_detail_id`   bigint(20) DEFAULT NULL COMMENT '退货原订单明细ID',
    `refund_reason_code`       varchar(255)   DEFAULT NULL COMMENT '退货原因',
    `refund_reject_reason`     varchar(512)   DEFAULT NULL COMMENT '退货拒绝原因',
    `del_flag`                 char(1)        DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                varchar(64)    DEFAULT '' COMMENT '创建者',
    `create_time`              datetime       DEFAULT NULL COMMENT '创建时间',
    `update_by`                varchar(64)    DEFAULT '' COMMENT '更新者',
    `update_time`              datetime       DEFAULT NULL COMMENT '更新时间',
    `version`                  int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    KEY                        `idx_soid_spcsid` (`store_order_id`,`store_prod_color_size_id`) USING BTREE,
    KEY                        `idx_origin_order_detail_id` (`origin_order_detail_id`) USING BTREE,
    KEY                        `idx_eid_ewno` (`express_id`,`express_waybill_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代发订单明细';

ALTER TABLE `store_order_detail`
ADD COLUMN `delivery_over_time` datetime NULL COMMENT '发货完成时间' AFTER `refund_reject_reason`;

DROP TABLE IF EXISTS `store_order_operation_record`;
CREATE TABLE `store_order_operation_record`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '代发订单操作记录ID',
    `target_id`      bigint(20) NOT NULL COMMENT '订单ID/订单明细ID，根据类型确定',
    `target_type`    tinyint(4) NOT NULL COMMENT '类型[1:订单 2:订单明细]',
    `action`         tinyint(4) NOT NULL COMMENT '节点事件[1:下单 2:支付 3:取消 4:发货 5:完成 6:申请售后 7:寄回 8:售后拒绝 9:平台介入 10:售后完成]',
    `operator_id`    bigint(20) DEFAULT NULL COMMENT '操作人ID',
    `operation_time` datetime     DEFAULT NULL COMMENT '操作时间',
    `remark`         varchar(255) DEFAULT NULL COMMENT '备注',
    `del_flag`       char(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`      varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time`    datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time`    datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY              `idx_tid_ttype` (`target_id`,`target_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代发订单操作记录';

DROP TABLE IF EXISTS `express_track_record`;
CREATE TABLE `express_track_record`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物流轨迹记录ID',
    `express_waybill_no` varchar(255) NOT NULL COMMENT '物流运单号',
    `express_id`         bigint(20) DEFAULT NULL COMMENT '物流ID',
    `sort`               int(11) NOT NULL DEFAULT 0 COMMENT '排序',
    `action`             varchar(128)  DEFAULT NULL COMMENT '节点事件',
    `description`        varchar(5000) DEFAULT NULL COMMENT '描述',
    `remark`             varchar(5000) DEFAULT NULL COMMENT '备注',
    `del_flag`           char(1)       DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`          varchar(64)   DEFAULT '' COMMENT '创建者',
    `create_time`        datetime      DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(64)   DEFAULT '' COMMENT '更新者',
    `update_time`        datetime      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY                  `idx_express_waybill_no` (`express_waybill_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流轨迹记录';

DROP TABLE IF EXISTS `express`;
CREATE TABLE `express`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物流ID',
    `express_code`          varchar(16) NOT NULL COMMENT '物流编码',
    `express_name`          varchar(32) NOT NULL COMMENT '物流名称',
    `system_deliver_access` bit(1)      NOT NULL DEFAULT 0 COMMENT '系统发货可选',
    `store_deliver_access`  bit(1)      NOT NULL DEFAULT 0 COMMENT '档口发货可选',
    `user_refund_access`    bit(1)      NOT NULL DEFAULT 0 COMMENT '用户退货可选',
    `system_config`         varchar(5000)        DEFAULT NULL COMMENT '系统配置',
    `del_flag`              char(1)              DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`             varchar(64)          DEFAULT '' COMMENT '创建者',
    `create_time`           datetime             DEFAULT NULL COMMENT '创建时间',
    `update_by`             varchar(64)          DEFAULT '' COMMENT '更新者',
    `update_time`           datetime             DEFAULT NULL COMMENT '更新时间',
    `version`               int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_express_code` (`express_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流信息';

DROP TABLE IF EXISTS `express_region`;
CREATE TABLE `express_region`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `region_code`        varchar(16)  NOT NULL COMMENT '地区编码，基于行政区划代码做扩展，唯一约束',
    `region_name`        varchar(32) NOT NULL COMMENT '地区名称',
    `region_level`       tinyint     NOT NULL COMMENT '地区级别[1:省 2:市 3:区县]',
    `parent_region_code` varchar(16)  DEFAULT NULL COMMENT '上级地区编码，没有上级的默认空',
    `parent_region_name` varchar(32) DEFAULT NULL COMMENT '上级地区名称，冗余',
    `del_flag`           char(1)     DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`          varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time`        datetime    DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time`        datetime    DEFAULT NULL COMMENT '更新时间',
    `version`            int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_region_code` (`region_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流行政区划';

DROP TABLE IF EXISTS `express_fee_config`;
CREATE TABLE `express_fee_config`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `express_id`         bigint(20) NOT NULL COMMENT '物流ID',
    `region_code`        varchar(16)     NOT NULL COMMENT '地区编码，基于行政区划代码做扩展，唯一约束',
    `parent_region_code` varchar(16)  DEFAULT NULL COMMENT '上级地区编码，没有上级的默认空',
    `first_item_amount`  decimal(18, 2) NOT NULL COMMENT '首件运费',
    `next_item_amount`   decimal(18, 2) NOT NULL COMMENT '续费',
    `del_flag`           char(1)     DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`          varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time`        datetime    DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time`        datetime    DEFAULT NULL COMMENT '更新时间',
    `version`            int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    KEY                  `idx_express_id` (`express_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流费用配置';

DROP TABLE IF EXISTS `express_shipping_label`;
CREATE TABLE `express_shipping_label`
(
    `id`                               bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `express_waybill_no`               varchar(64) NOT NULL COMMENT '运单号',
    `express_id`                       bigint(20) DEFAULT NULL COMMENT '物流ID',
    `vas_type`                         varchar(32)  DEFAULT NULL COMMENT '服务类型',
    `mark`                             varchar(32)  DEFAULT NULL COMMENT '转运代码',
    `short_mark`                       varchar(32)  DEFAULT NULL COMMENT '短转运代码',
    `bag_addr`                         varchar(32)  DEFAULT NULL COMMENT '集包地',
    `last_print_time`                  datetime     DEFAULT NULL COMMENT '最后打印时间',
    `print_count`                      int(11) DEFAULT NULL COMMENT '打印次数',
    `goods_info`                       varchar(512) DEFAULT NULL COMMENT '商品信息',
    `remark`                           varchar(512) DEFAULT NULL COMMENT '备注',
    `origin_contact_name`              varchar(32)  DEFAULT NULL COMMENT '发货人-名称',
    `origin_contact_phone_number`      varchar(32)  DEFAULT NULL COMMENT '发货人-电话',
    `origin_province_name`             varchar(8)   DEFAULT NULL COMMENT '发货人-省',
    `origin_city_name`                 varchar(8)   DEFAULT NULL COMMENT '发货人-市',
    `origin_county_name`               varchar(8)   DEFAULT NULL COMMENT '发货人-区县',
    `origin_detail_address`            varchar(255) DEFAULT NULL COMMENT '发货人-详细地址',
    `destination_contact_name`         varchar(32)  DEFAULT NULL COMMENT '收货人-名称',
    `destination_contact_phone_number` varchar(32)  DEFAULT NULL COMMENT '收货人-电话',
    `destination_province_name`        varchar(8)   DEFAULT NULL COMMENT '收货人-省',
    `destination_city_name`            varchar(8)   DEFAULT NULL COMMENT '收货人-市',
    `destination_county_name`          varchar(8)   DEFAULT NULL COMMENT '收货人-区县',
    `destination_detail_address`       varchar(255) DEFAULT NULL COMMENT '收货人-详细地址',
    `del_flag`                         char(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                        varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time`                      datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`                        varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time`                      datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_express_waybill_no` (`express_waybill_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='快递面单';

DROP TABLE IF EXISTS `internal_account`;
CREATE TABLE `internal_account`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT COMMENT '内部账户ID',
    `owner_type`           tinyint(4) NOT NULL COMMENT '归属[1:平台 2:档口 3:用户]',
    `owner_id`             bigint(20) NOT NULL COMMENT '归属ID（平台=-1，档口=store_id）',
    `account_status`       tinyint(4) NOT NULL COMMENT '账户状态[1:正常 2:冻结]',
    `transaction_password` varchar(128)            DEFAULT NULL COMMENT '交易密码',
    `phone_number`         varchar(32)             DEFAULT NULL COMMENT '归属人手机号',
    `balance`              decimal(18, 2) NOT NULL DEFAULT '0.00' COMMENT '余额',
    `usable_balance`       decimal(18, 2) NOT NULL DEFAULT '0.00' COMMENT '可用余额',
    `payment_amount`       decimal(18, 2) NOT NULL DEFAULT '0.00' COMMENT '支付中金额',
    `remark`               varchar(255)            DEFAULT NULL COMMENT '备注',
    `del_flag`             char(1)                 DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`            varchar(64)             DEFAULT '' COMMENT '创建者',
    `create_time`          datetime                DEFAULT NULL COMMENT '创建时间',
    `update_by`            varchar(64)             DEFAULT '' COMMENT '更新者',
    `update_time`          datetime                DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_oid_otype` (`owner_id`,`owner_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COMMENT='内部账户';

DROP TABLE IF EXISTS `internal_account_trans_detail`;
CREATE TABLE `internal_account_trans_detail`
(
    `id`                  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '内部账户交易明细ID',
    `internal_account_id` bigint(20) NOT NULL COMMENT '内部账户ID',
    `src_bill_id`         bigint(20) DEFAULT NULL COMMENT '来源单据ID',
    `src_bill_type`       tinyint(4) DEFAULT NULL COMMENT '来源单据类型[1:收款 2:付款 3:转移]',
    `loan_direction`      tinyint(4) NOT NULL COMMENT '借贷方向[1:借(D) 2:贷(C)]',
    `trans_amount`        decimal(18, 2) NOT NULL COMMENT '交易金额',
    `trans_time`          datetime       NOT NULL COMMENT '交易时间',
    `handler_id`          bigint(20) DEFAULT NULL COMMENT '经办人ID',
    `entry_status`        tinyint(4) NOT NULL COMMENT '入账状态 [1:已入账 2:未入账]',
    `entry_executed`      tinyint(4) NOT NULL COMMENT '入账执行标识[1:已执行 2:未执行]',
    `remark`              varchar(255) DEFAULT NULL COMMENT '备注',
    `del_flag`            char(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time`         datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time`         datetime     DEFAULT NULL COMMENT '更新时间',
    `version`             int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                   `idx_internal_account_id` (`internal_account_id`) USING BTREE,
    KEY                   `idx_sbid_sbtype` (`src_bill_id`,`src_bill_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内部账户交易明细';

DROP TABLE IF EXISTS `external_account`;
CREATE TABLE `external_account`
(
    `id`                         bigint(20) NOT NULL AUTO_INCREMENT COMMENT '外部账户ID',
    `owner_type`                 tinyint(4) NOT NULL COMMENT '归属[1:平台 2:档口 3:用户]',
    `owner_id`                   bigint(20) NOT NULL COMMENT '归属ID（平台=-1，档口=store_id，用户=user_id）',
    `account_status`             tinyint(4) NOT NULL COMMENT '账户状态[1:正常 2:冻结]',
    `account_type`               tinyint(4) NOT NULL COMMENT '账户类型[1:支付宝账户]',
    `account_owner_number`       varchar(32)     DEFAULT NULL COMMENT '归属人实际账户',
    `account_owner_name`         varchar(32)     DEFAULT NULL COMMENT '归属人真实姓名',
    `account_owner_phone_number` varchar(32)     DEFAULT NULL COMMENT '归属人手机号',
    `account_auth_access`        bit(1) NOT NULL DEFAULT 0 COMMENT '归属人认证通过',
    `remark`                     varchar(255)    DEFAULT NULL COMMENT '备注',
    `del_flag`                   char(1)         DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                  varchar(64)     DEFAULT '' COMMENT '创建者',
    `create_time`                datetime        DEFAULT NULL COMMENT '创建时间',
    `update_by`                  varchar(64)     DEFAULT '' COMMENT '更新者',
    `update_time`                datetime        DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_oid_otype_atype` (`owner_id`,`owner_type`,`account_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COMMENT='外部账户';

DROP TABLE IF EXISTS `external_account_trans_detail`;
CREATE TABLE `external_account_trans_detail`
(
    `id`                  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '外部账户交易明细ID',
    `external_account_id` bigint(20) NOT NULL COMMENT '外部账户ID',
    `src_bill_id`         bigint(20) DEFAULT NULL COMMENT '来源单据ID',
    `src_bill_type`       tinyint(4) DEFAULT NULL COMMENT '来源单据类型[2:付款]',
    `loan_direction`      tinyint(4) NOT NULL COMMENT '借贷方向[1:借(D) 2:贷(C)]',
    `trans_amount`        decimal(18, 2) NOT NULL COMMENT '交易金额',
    `trans_time`          datetime       NOT NULL COMMENT '交易时间',
    `handler_id`          bigint(20) DEFAULT NULL COMMENT '经办人ID',
    `entry_status`        tinyint(4) NOT NULL COMMENT '入账状态 [1:已入账 2:未入账]',
    `remark`              varchar(255) DEFAULT NULL COMMENT '备注',
    `del_flag`            char(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time`         datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time`         datetime     DEFAULT NULL COMMENT '更新时间',
    `version`             int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                   `idx_external_account_id` (`external_account_id`) USING BTREE,
    KEY                   `idx_sbid_sbtype` (`src_bill_id`,`src_bill_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外部账户交易明细';

DROP TABLE IF EXISTS `finance_bill`;
CREATE TABLE `finance_bill`
(
    `id`                         bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付单据ID',
    `bill_no`                    varchar(64)    NOT NULL COMMENT '单号',
    `bill_type`                  tinyint(4) NOT NULL COMMENT '单据类型[1:收款 2:付款 3:转移]',
    `bill_status`                tinyint(4) NOT NULL COMMENT '单据状态[1:初始 2:执行中 3:执行成功 4:执行失败]',
    `src_type`                   tinyint(4) DEFAULT NULL COMMENT '来源类型[1:代发订单支付 2:代发订单完成 3:提现]',
    `src_id`                     bigint(20) DEFAULT NULL COMMENT '来源ID',
    `rel_type`                   tinyint(4) DEFAULT NULL COMMENT '关联类型[1:代发订单]',
    `rel_id`                     bigint(20) DEFAULT NULL COMMENT '关联ID',
    `business_unique_key`        varchar(64)  DEFAULT NULL COMMENT '业务唯一键',
    `input_internal_account_id`  bigint(20) DEFAULT NULL COMMENT '收入内部账户ID',
    `output_internal_account_id` bigint(20) DEFAULT NULL COMMENT '支出内部账户ID',
    `input_external_account_id`  bigint(20) DEFAULT NULL COMMENT '收入外部账户ID',
    `output_external_account_id` bigint(20) DEFAULT NULL COMMENT '支出外部账户ID',
    `business_amount`            decimal(18, 2) NOT NULL COMMENT '业务金额',
    `trans_amount`               decimal(18, 2) NOT NULL COMMENT '交易金额',
    `remark`                     varchar(255) DEFAULT NULL COMMENT '备注',
    `ext_info`                   varchar(2048) DEFAULT NULL COMMENT '扩展信息',
    `del_flag`                   char(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                  varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time`                datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`                  varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time`                datetime     DEFAULT NULL COMMENT '更新时间',
    `version`                    int(11) NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_bill_no` (`bill_no`) USING BTREE,
    UNIQUE KEY `uk_business_unique_key` (`business_unique_key`) USING BTREE,
    KEY                          `idx_sid_stype` (`src_id`,`src_type`) USING BTREE,
    KEY                          `idx_rid_rtype` (`rel_id`,`rel_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务单据';

DROP TABLE IF EXISTS `finance_bill_detail`;
CREATE TABLE `finance_bill_detail`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付单据明细ID',
    `finance_bill_id` bigint(20) DEFAULT NULL COMMENT '支付单据ID',
    `rel_type`        tinyint(4) DEFAULT NULL COMMENT '关联类型[1:代发订单明细]',
    `rel_id`          bigint(20) DEFAULT NULL COMMENT '关联ID',
    `business_amount` decimal(18, 2) NOT NULL COMMENT '业务金额',
    `trans_amount`    decimal(18, 2) NOT NULL COMMENT '交易金额',
    `remark`          varchar(255) DEFAULT NULL COMMENT '备注',
    `del_flag`        char(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`       varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time`     datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time`     datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY               `idx_finance_bill_id` (`finance_bill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务单据明细';

DROP TABLE IF EXISTS `alipay_callback`;
CREATE TABLE `alipay_callback`
(
    `id`                  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `biz_type`            tinyint(4) DEFAULT NULL COMMENT '业务类型',
    `notify_type`         varchar(64)    DEFAULT NULL COMMENT '通知的类型',
    `notify_id`           varchar(64)    DEFAULT NULL COMMENT '通知校验 ID',
    `app_id`              varchar(64)    DEFAULT NULL COMMENT '支付宝分配给开发者的应用 ID',
    `charset`             varchar(128)   DEFAULT NULL COMMENT '编码格式，如 utf-8、gbk、gb2312 等',
    `version`             varchar(32)    DEFAULT NULL COMMENT '调用的接口版本，固定为：1.0',
    `sign_type`           varchar(10)    DEFAULT NULL COMMENT '商家生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2',
    `sign`                varchar(3)     DEFAULT NULL COMMENT '详情可查看 异步返回结果的验签',
    `trade_no`            varchar(64)    DEFAULT NULL COMMENT '支付宝交易凭证号',
    `out_trade_no`        varchar(512)   DEFAULT NULL COMMENT '原支付请求的商户订单号',
    `out_biz_no`          varchar(64)    DEFAULT NULL COMMENT '商户业务 ID',
    `buyer_id`            varchar(128)   DEFAULT NULL COMMENT '买家支付宝用户号',
    `buyer_logon_id`      varchar(100)   DEFAULT NULL COMMENT '买家支付宝账号',
    `seller_id`           varchar(30)    DEFAULT NULL COMMENT '卖家支付宝用户号',
    `seller_email`        varchar(100)   DEFAULT NULL COMMENT '卖家支付宝账号',
    `trade_status`        varchar(32)    DEFAULT NULL COMMENT '交易目前所处的状态[WAIT_BUYER_PAY	交易创建，等待买家付款 TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款 TRADE_SUCCESS	交易支付成功 TRADE_FINISHED	交易结束，不可退款]',
    `total_amount`        decimal(18, 2) DEFAULT NULL COMMENT '本次交易支付的订单金额，单位为人民币（元）',
    `receipt_amount`      decimal(18, 2) DEFAULT NULL COMMENT '商家在收益中实际收到的款项，单位人民币（元）',
    `invoice_amount`      decimal(18, 2) DEFAULT NULL COMMENT '用户在交易中支付的可开发票的金额',
    `buyer_pay_amount`    decimal(18, 2) DEFAULT NULL COMMENT '用户在交易中支付的金额',
    `point_amount`        decimal(18, 2) DEFAULT NULL COMMENT '使用集分宝支付的金额',
    `refund_fee`          decimal(18, 2) DEFAULT NULL COMMENT '退款通知中，返回总退款金额，单位为人民币（元），支持两位小数',
    `subject`             varchar(256)   DEFAULT NULL COMMENT '商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来',
    `body`                varchar(400)   DEFAULT NULL COMMENT '订单的备注、描述、明细等。对应请求时的 body 参数，原样通知回来',
    `gmt_create`          varchar(32)    DEFAULT NULL COMMENT '该笔交易创建的时间',
    `gmt_payment`         varchar(32)    DEFAULT NULL COMMENT '该笔交易 的买家付款时间',
    `gmt_refund`          varchar(32)    DEFAULT NULL COMMENT '该笔交易的退款时间',
    `gmt_close`           varchar(32)    DEFAULT NULL COMMENT '该笔交易结束时间',
    `fund_bill_list`      varchar(512)   DEFAULT NULL COMMENT '支付成功的各个渠道金额信息',
    `passback_params`     varchar(512)   DEFAULT NULL COMMENT '公共回传参数，如果请求时传递了该参数，则返回给商家时会在异步通知时将该参数原样返回',
    `voucher_detail_list` varchar(512)   DEFAULT NULL COMMENT '本交易支付时所有优惠券信息',
    `process_status`      tinyint(4) NOT NULL COMMENT '回调处理状态[1:初始 2:处理中 3:处理成功 4:处理失败]',
    `del_flag`            char(1)        DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64)    DEFAULT '' COMMENT '创建者',
    `create_time`         datetime       DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64)    DEFAULT '' COMMENT '更新者',
    `update_time`         datetime       DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_notify_id` (`notify_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='阿里支付回调信息';

-- 上线前补全
INSERT INTO internal_account (`id`, `owner_type`, `owner_id`, `account_status`, `transaction_password`, `balance`,
                              `usable_balance`, `payment_amount`, `remark`, `del_flag`, `create_by`, `create_time`,
                              `update_by`, `update_time`)
VALUES (1, 1, -1, 1, NULL, 0.00, 0.00, 0.00, '平台账户', '0', 'SYSTEM', NOW(), 'SYSTEM', NOW());

INSERT INTO external_account (`id`, `owner_type`, `owner_id`, `account_status`, `account_type`, `account_owner_number`,
                              `account_owner_name`, `account_owner_phone_number`, `account_auth_access`, `remark`,
                              `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1, 1, -1, 1, 1, '', '', '', 1, '平台支付宝账户', '0', 'SYSTEM', NOW(), 'SYSTEM', NOW());

-- 快递?
INSERT INTO `express`(`id`, `express_code`, `express_name`, `system_deliver_access`, `store_deliver_access`,
                      `user_refund_access`, `system_config`, `del_flag`, `create_by`, `create_time`, `update_by`,
                      `update_time`, `version`)
VALUES (1, 'ZTO', '中通', b'1', b'1', b'1', NULL, '0', '', NULL, '', NULL, 0);
INSERT INTO `express`(`id`, `express_code`, `express_name`, `system_deliver_access`, `store_deliver_access`,
                      `user_refund_access`, `system_config`, `del_flag`, `create_by`, `create_time`, `update_by`,
                      `update_time`, `version`)
VALUES (2, 'YTO', '圆通', b'1', b'1', b'1', NULL, '0', '', NULL, '', NULL, 0);


DROP TABLE IF EXISTS `voucher_sequence`;
CREATE TABLE `voucher_sequence`
(
    `id`              bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '单据编号ID',
    `store_id`        bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `type`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据类型',
    `date_format`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据格式化类型',
    `next_sequence`   int                                                          NOT NULL COMMENT '下一个单据编号',
    `prefix`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据编号前缀',
    `sequence_format` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '格式化类型',
    `version`         bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统code生成规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of voucher_sequence
-- ----------------------------
INSERT INTO `voucher_sequence`
VALUES (1, 1, 'STORE_SALE', 'yyyy-MM-dd', 1, 'SD', '%04d', 0, '0', '', '2025-03-30 16:09:23', '',
        '2025-03-30 16:09:26');
INSERT INTO `voucher_sequence`
VALUES (2, 1, 'STORAGE', 'yyyy-MM-dd', 1, 'RK', '%04d', 0, '0', '', '2025-03-30 16:09:23', '', '2025-03-30 16:09:26');
INSERT INTO `voucher_sequence`
VALUES (3, 1, 'DEMAND', 'yyyy-MM-dd', 1, 'XQ', '%04d', 0, '0', '', '2025-03-30 16:09:23', '', '2025-03-30 16:09:26');
INSERT INTO `voucher_sequence`
VALUES (4, 1, 'STORE_ORDER', 'yyyy-MM-dd', 1, 'DF', '%04d', 0, '0', '', '2025-03-30 16:09:23', '',
        '2025-05-15 01:54:15');

SET
FOREIGN_KEY_CHECKS = 1;


DROP TABLE IF EXISTS `sys_product_category`;
CREATE TABLE `sys_product_category`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品分类ID',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
    `parent_id`   bigint UNSIGNED NULL DEFAULT 0 COMMENT '父菜单ID',
    `order_num`   int UNSIGNED NULL DEFAULT 0 COMMENT '显示顺序',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    `icon_url`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类ICON路径',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
    `version`     bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_product_category
-- ----------------------------
INSERT INTO `sys_product_category`
VALUES (1, '鞋库通商品分类', 0, 3, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '最顶层商品分类', 2, '0', 'admin', '2025-04-15 19:42:19', '', '2025-04-15 19:42:23');
INSERT INTO `sys_product_category`
VALUES (2, '靴子', 1, 2, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:46:49', '', '2025-04-15 19:46:49');
INSERT INTO `sys_product_category`
VALUES (3, '单鞋', 1, 1, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:47:06', '', '2025-04-15 19:47:06');
INSERT INTO `sys_product_category`
VALUES (4, '凉鞋', 1, 3, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:47:23', '', '2025-04-15 19:47:23');
INSERT INTO `sys_product_category`
VALUES (5, '休闲鞋', 1, 4, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:47:33', '', '2025-04-15 19:47:33');
INSERT INTO `sys_product_category`
VALUES (6, '雪地靴', 1, 11, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:47:44', '', '2025-04-15 19:47:44');
INSERT INTO `sys_product_category`
VALUES (7, '帆布鞋', 1, 11, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:48:20', '', '2025-04-15 19:48:20');
INSERT INTO `sys_product_category`
VALUES (8, '拖鞋', 1, 5, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:48:26', '', '2025-04-15 19:48:26');
INSERT INTO `sys_product_category`
VALUES (9, '高帮鞋', 1, 11, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:48:32', '', '2025-04-15 19:48:32');
INSERT INTO `sys_product_category`
VALUES (10, '汉服鞋', 1, 11, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:48:39', '', '2025-04-15 19:48:39');
INSERT INTO `sys_product_category`
VALUES (11, '绣花鞋', 1, 11, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:48:46', '', '2025-04-15 19:48:46');
INSERT INTO `sys_product_category`
VALUES (12, '传统布鞋', 1, 11, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 2, '0', '', '2025-04-15 19:48:52', '', '2025-04-15 19:48:52');
INSERT INTO `sys_product_category`
VALUES (13, '弹力靴/袜靴', 2, 1, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:51:18', '', '2025-04-15 19:51:18');
INSERT INTO `sys_product_category`
VALUES (14, '马丁靴', 2, 2, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:51:31', '', '2025-04-15 19:51:31');
INSERT INTO `sys_product_category`
VALUES (15, '骑士靴', 2, 3, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:51:44', '', '2025-04-15 19:51:44');
INSERT INTO `sys_product_category`
VALUES (16, '切尔西靴', 2, 4, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:51:51', '', '2025-04-15 19:51:51');
INSERT INTO `sys_product_category`
VALUES (17, '时装靴', 2, 5, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:51:57', '', '2025-04-15 19:51:57');
INSERT INTO `sys_product_category`
VALUES (18, '西部靴', 2, 6, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:52:03', '', '2025-04-15 19:52:03');
INSERT INTO `sys_product_category`
VALUES (19, '烟筒靴', 2, 7, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:52:09', '', '2025-04-15 19:52:09');
INSERT INTO `sys_product_category`
VALUES (20, '勃肯鞋/软木鞋', 3, 1, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:52:36', '', '2025-04-15 19:52:36');
INSERT INTO `sys_product_category`
VALUES (21, '乐福鞋（豆豆鞋）', 3, 2, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:52:56', '', '2025-04-15 19:52:56');
INSERT INTO `sys_product_category`
VALUES (22, '玛丽珍鞋', 3, 3, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:53:07', '', '2025-04-15 19:53:07');
INSERT INTO `sys_product_category`
VALUES (23, '穆勒鞋', 3, 4, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:53:13', '', '2025-04-15 19:53:13');
INSERT INTO `sys_product_category`
VALUES (24, '婚鞋', 3, 5, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:53:21', '', '2025-04-15 19:53:21');
INSERT INTO `sys_product_category`
VALUES (25, '浅口单鞋', 3, 6, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:53:37', '', '2025-04-15 19:53:37');
INSERT INTO `sys_product_category`
VALUES (26, '深口单鞋', 3, 7, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:53:44', '', '2025-04-15 19:53:44');
INSERT INTO `sys_product_category`
VALUES (27, '时尚芭蕾鞋', 3, 8, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:53:51', '', '2025-04-15 19:53:51');
INSERT INTO `sys_product_category`
VALUES (28, '牛津鞋/布洛克鞋/德比鞋', 3, 9, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:54:07', '', '2025-04-15 19:54:07');
INSERT INTO `sys_product_category`
VALUES (29, '松糕（摇摇）鞋', 3, 10, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:54:20', '', '2025-04-15 19:54:20');
INSERT INTO `sys_product_category`
VALUES (30, '渔夫鞋', 3, 11, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:54:26', '', '2025-04-15 19:54:26');
INSERT INTO `sys_product_category`
VALUES (31, '洞洞鞋', 4, 1, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:54:48', '', '2025-04-15 19:54:48');
INSERT INTO `sys_product_category`
VALUES (32, '罗马凉鞋', 4, 2, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:54:55', '', '2025-04-15 19:54:55');
INSERT INTO `sys_product_category`
VALUES (33, '沙滩鞋', 4, 3, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:55:00', '', '2025-04-15 19:55:00');
INSERT INTO `sys_product_category`
VALUES (34, '时装凉鞋', 4, 4, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:55:06', '', '2025-04-15 19:55:06');
INSERT INTO `sys_product_category`
VALUES (35, '休闲凉鞋', 4, 5, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:55:13', '', '2025-04-15 19:55:13');
INSERT INTO `sys_product_category`
VALUES (36, '一字带凉鞋', 4, 6, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:55:19', '', '2025-04-15 19:55:19');
INSERT INTO `sys_product_category`
VALUES (37, '德训鞋', 5, 1, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:55:37', '', '2025-04-15 19:55:37');
INSERT INTO `sys_product_category`
VALUES (38, '工装鞋', 5, 2, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:55:43', '', '2025-04-15 19:55:43');
INSERT INTO `sys_product_category`
VALUES (39, '健步鞋', 5, 3, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:55:50', '', '2025-04-15 19:55:50');
INSERT INTO `sys_product_category`
VALUES (40, '老爹鞋', 5, 4, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:55:56', '', '2025-04-15 19:55:56');
INSERT INTO `sys_product_category`
VALUES (41, '棉鞋', 5, 5, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:56:01', '', '2025-04-15 19:56:01');
INSERT INTO `sys_product_category`
VALUES (42, '时尚休闲鞋', 5, 6, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:56:07', '', '2025-04-15 19:56:07');
INSERT INTO `sys_product_category`
VALUES (43, '网面鞋', 5, 7, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:56:13', '', '2025-04-15 19:56:13');
INSERT INTO `sys_product_category`
VALUES (44, '休闲板鞋', 5, 8, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:56:19', '', '2025-04-15 19:56:19');
INSERT INTO `sys_product_category`
VALUES (45, '包头拖', 8, 1, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:56:44', '', '2025-04-15 19:56:44');
INSERT INTO `sys_product_category`
VALUES (46, '毛毛鞋', 8, 2, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:56:51', '', '2025-04-15 19:56:51');
INSERT INTO `sys_product_category`
VALUES (47, '人字拖', 8, 3, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:56:58', '', '2025-04-15 19:56:58');
INSERT INTO `sys_product_category`
VALUES (48, '一字拖', 8, 4, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:57:04', '', '2025-04-15 19:57:04');
INSERT INTO `sys_product_category`
VALUES (49, '其他拖鞋', 8, 5, '0',
        'https://pics1.baidu.com/feed/6609c93d70cf3bc758366c966adb10aecc112a85.png@f_auto?token=89fe04971fb0579147e9ccc52683a013',
        '', 0, '0', '', '2025-04-15 19:57:12', '', '2025-04-15 19:57:12');

SET
FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store`
(
    `id`                 bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口ID',
    `user_id`            bigint UNSIGNED NOT NULL COMMENT '档口负责人ID',
    `store_name`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档口名称',
    `store_weight`       int NULL DEFAULT NULL COMMENT '权重',
    `store_logo_id`      bigint UNSIGNED NULL DEFAULT NULL COMMENT '档口logo',
    `brand_name`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌名称',
    `contact_name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
    `contact_phone`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
    `contact_back_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备选联系电话',
    `wechat_account`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信账号',
    `qq_account`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'QQ账号',
    `operate_years`      int UNSIGNED NULL DEFAULT NULL COMMENT '经营年限',
    `store_address`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档口地址',
    `fac_address`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工厂地址',
    `prod_scale`         tinyint UNSIGNED NULL DEFAULT NULL COMMENT '生产规模',
    `integrity_gold`     decimal(10, 2) NULL DEFAULT NULL COMMENT '保证金',
    `remark`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `service_end_time`   date NULL DEFAULT NULL COMMENT '服务截止时间',
    `storage_usage`      decimal(10, 3) NULL DEFAULT NULL COMMENT '已使用文件大小',
    `template_num`       int UNSIGNED NULL DEFAULT NULL COMMENT '档口模板ID',
    `store_status`       tinyint UNSIGNED NULL DEFAULT NULL COMMENT '档口状态',
    `stock_sys`          int UNSIGNED NULL DEFAULT NULL COMMENT '库存系统',
    `service_amount`     decimal(10, 2) NULL DEFAULT NULL,
    `member_amount`      decimal(10, 2) NULL DEFAULT NULL,
    `view_count`         bigint UNSIGNED NULL DEFAULT NULL COMMENT '浏览量',
    `reject_reason`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拒绝理由',
    `version`            bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`           char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`        datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`        datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口' ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;


-- ----------------------------
-- Table structure for store_certificate
-- ----------------------------
DROP TABLE IF EXISTS `store_certificate`;
CREATE TABLE `store_certificate`
(
    `id`                       bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口认证ID',
    `store_id`                 bigint UNSIGNED NOT NULL COMMENT 'store.id',
    `real_name`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
    `phone`                    varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '联系电话',
    `id_card`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
    `id_card_face_file_id`     bigint UNSIGNED NULL DEFAULT NULL COMMENT '身份证人脸文件ID',
    `id_card_emblem_file_id`   bigint UNSIGNED NULL DEFAULT NULL COMMENT '身份证国徽文件ID',
    `license_file_id`          bigint UNSIGNED NULL DEFAULT NULL COMMENT '营业执照文件ID',
    `social_credit_code`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统一社会信用代码',
    `sole_proprietorship_type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '经营类型',
    `market_entry_type`        tinyint UNSIGNED NULL DEFAULT NULL COMMENT '市场主体类型',
    `license_name`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照名称',
    `register_org`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登记机关',
    `register_status`          tinyint UNSIGNED NULL DEFAULT NULL COMMENT '登记状态',
    `legal_name`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法定代表人/负责人名称',
    `real_business_address`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实际经营地址',
    `business_scope`           varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营范围',
    `register_capital`         int UNSIGNED NULL DEFAULT NULL COMMENT '注册资本(万)',
    `establish_date`           date NULL DEFAULT NULL COMMENT '成立日期',
    `business_term_start_date` date NULL DEFAULT NULL COMMENT '营业期限开始时间',
    `business_term_end_date`   date NULL DEFAULT NULL COMMENT '营业期限截止时间',
    `approval_date`            date NULL DEFAULT NULL COMMENT '核准日期',
    `version`                  bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`                 char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`              datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`              datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口认证' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for store_color
-- ----------------------------
DROP TABLE IF EXISTS `store_color`;
CREATE TABLE `store_color`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品颜色ID',
    `store_id`    bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `color_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '颜色名称',
    `order_num`   int UNSIGNED NOT NULL COMMENT '排序',
    `version`     bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口所有颜色' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for store_customer
-- ----------------------------
DROP TABLE IF EXISTS `store_customer`;
CREATE TABLE `store_customer`
(
    `id`                bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口客户ID',
    `store_id`          bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `cus_name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
    `all_prod_discount` decimal(10, 2) NULL DEFAULT NULL COMMENT '所有商品优惠金额',
    `phone`             varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户联系电话',
    `remark`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户备注',
    `version`           bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`       datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`       datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口客户' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_sc_id_cus_name ON store_customer(id, cus_name);

-- ----------------------------
-- Table structure for store_customer_product_discount
-- ----------------------------
DROP TABLE IF EXISTS `store_customer_product_discount`;
CREATE TABLE `store_customer_product_discount`
(
    `id`                  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口客户销售优惠ID',
    `store_id`            bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_prod_id`       bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `prod_art_num`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品货号',
    `store_cus_id`        bigint UNSIGNED NOT NULL COMMENT '档口客户ID',
    `store_cus_name`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档口客户名称',
    `store_prod_color_id` bigint UNSIGNED NOT NULL COMMENT '档口商品颜色ID',
    `discount`            decimal(10, 2) UNSIGNED NOT NULL COMMENT '优惠金额',
    `version`             bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_cus_id_prod_color_id` (`store_cus_id`, `store_prod_color_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口客户优惠' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for store_factory
-- ----------------------------
DROP TABLE IF EXISTS `store_factory`;
CREATE TABLE `store_factory`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口工厂ID',
    `store_id`    bigint UNSIGNED NOT NULL COMMENT 'store.id',
    `fac_name`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工厂名称',
    `fac_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工厂地址',
    `fac_contact` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
    `fac_phone`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工厂联系电话',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `version`     bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口合作工厂' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_homepage
-- ----------------------------
DROP TABLE IF EXISTS `store_homepage`;
CREATE TABLE `store_homepage`
(
    `id`           bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口首页ID',
    `store_id`     bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `file_type`    int UNSIGNED NULL DEFAULT NULL COMMENT '档口各位置类型',
    `display_type` int UNSIGNED NOT NULL COMMENT '跳转类型：1档口、2商品、10不跳转',
    `biz_id`       bigint UNSIGNED NULL DEFAULT NULL COMMENT '不跳转是null，跳转店铺为storeId，跳转商品是storeProdId',
    `file_id`      bigint UNSIGNED NULL DEFAULT NULL COMMENT '档口各位置文件ID',
    `order_num`    int UNSIGNED NULL DEFAULT NULL COMMENT '排序',
    `version`      bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口首页' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_member
-- ----------------------------
DROP TABLE IF EXISTS `store_member`;
CREATE TABLE `store_member`
(
    `id`           bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口会员主键',
    `store_id`     bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `level`        int UNSIGNED NOT NULL COMMENT '会员等级',
    `start_time`   date NOT NULL COMMENT '开始时间',
    `end_time`     date NOT NULL COMMENT '结束时间',
    `voucher_date` date NOT NULL COMMENT '单据日期',
    `version`      bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for store_product
-- ----------------------------
DROP TABLE IF EXISTS `store_product`;
CREATE TABLE `store_product`
(
    `id`                   bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品ID',
    `store_id`             bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `prod_cate_id`         bigint UNSIGNED NULL DEFAULT NULL COMMENT '商品分类ID',
    `prod_cate_name`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
    `factory_art_num`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工厂货号',
    `prod_art_num`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品货号',
    `prod_title`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品标题',
    `private_item`         tinyint UNSIGNED NOT NULL COMMENT '是否私款 0否 1是',
    `prod_weight`          decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '商品重量',
    `produce_price`        decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '生产价格',
    `delivery_time`        int UNSIGNED NULL DEFAULT NULL COMMENT '发货时效',
    `listing_way_schedule` datetime NULL DEFAULT NULL COMMENT '定时发货时间(精确到小时)',
    `voucher_date`         date NOT NULL COMMENT '单据日期',
    `prod_status`          tinyint UNSIGNED NULL DEFAULT NULL COMMENT '档口商品状态',
    `version`              bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`             char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`          datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`          datetime NULL DEFAULT NULL COMMENT '更新时间',
    `listing_way`          tinyint(1) NULL DEFAULT NULL COMMENT '上架方式（立即上架1、定时上架2）',
    `recommend_weight`     bigint UNSIGNED NULL DEFAULT NULL COMMENT '推荐数值',
    `sale_weight`          bigint UNSIGNED NULL DEFAULT NULL COMMENT '销量数值',
    `popularity_weight`    bigint UNSIGNED NULL DEFAULT NULL COMMENT '人气数值',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX                  `idx_storeid`(`store_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_store_product_store_status_del ON store_product(store_id, prod_status, del_flag);


-- ----------------------------
-- Table structure for store_product_barcode_match
-- ----------------------------
DROP TABLE IF EXISTS `store_product_barcode_match`;
CREATE TABLE `store_product_barcode_match`
(
    `id`                       bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口条形码匹配ID',
    `store_prod_id`            bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_prod_color_size_id` bigint UNSIGNED NOT NULL COMMENT '档口商品颜色尺码ID',
    `store_id`                 bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `other_sys_barcode_prefix` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它系统条形码前缀',
    `other_sys_barcode`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它系统条形码',
    `version`                  bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`                 char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`              datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`              datetime NULL DEFAULT NULL COMMENT '更新时间',
    `other_sys_type`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '其它库存系统条码类型',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口条形码和第三方系统条形码匹配结果' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for store_product_barcode_record
-- ----------------------------
DROP TABLE IF EXISTS `store_product_barcode_record`;
CREATE TABLE `store_product_barcode_record`
(
    `id`                       bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品条码打印记录ID',
    `store_prod_id`            bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_prod_color_size_id` bigint UNSIGNED NOT NULL COMMENT '档口商品颜色尺码ID',
    `barcode`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打印的条形码',
    `version`                  bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`                 char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`              datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`              datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口打印条形码记录' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for store_product_category_attribute
-- ----------------------------
DROP TABLE IF EXISTS `store_product_category_attribute`;
CREATE TABLE `store_product_category_attribute`
(
    `id`                         bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品类目属性ID',
    `store_id`                   bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_prod_id`              bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `upper_material`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帮面材质',
    `shaft_lining_material`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '靴筒内里材质',
    `shaft_material`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '靴筒面材质',
    `shoe_upper_lining_material` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鞋面内里材质',
    `shoe_style_name`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '靴款品名',
    `shaft_height`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '筒高',
    `insole_material`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鞋垫材质',
    `release_year_season`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上市季节年份',
    `heel_height`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后跟高',
    `heel_type`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟底款式',
    `toe_style`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鞋头款式',
    `suitable_season`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '适合季节',
    `collar_depth`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开口深度',
    `outsole_material`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鞋底材质',
    `style`                      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '风格',
    `design`                     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '款式',
    `leather_features`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '皮质特征',
    `manufacturing_process`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '制作工艺',
    `pattern`                    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图案',
    `closure_type`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '闭合方式',
    `occasion`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '适用场景',
    `thickness`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厚薄',
    `fashion_elements`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流行元素',
    `suitable_person`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '适用对象',
    `version`                    bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`                   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`                datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`                datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品类目信息' ROW_FORMAT = DYNAMIC;

SET
FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for store_product_color
-- ----------------------------
DROP TABLE IF EXISTS `store_product_color`;
CREATE TABLE `store_product_color`
(
    `id`                         bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品颜色ID',
    `store_color_id`             bigint UNSIGNED NOT NULL COMMENT '档口颜色ID',
    `store_prod_id`              bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_id`                   bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `color_name`                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '颜色名称',
    `shoe_upper_lining_material` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内里材质',
    `order_num`                  int UNSIGNED NOT NULL COMMENT '排序',
    `prod_status`                tinyint UNSIGNED NULL DEFAULT NULL COMMENT '档口商品状态',
    `version`                    bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`                   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`                datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`                datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX                        `idx_spc_prodid_colorid_del`(`store_prod_id`, `store_color_id`, `del_flag`) USING BTREE,
    INDEX                        `idx_spc_store_prod_del`(`store_prod_id`, `del_flag`, `store_id`) USING BTREE,
    INDEX                        `idx_spc_prod_status_del`(`prod_status`, `del_flag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2783 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口当前商品颜色' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for store_product_color_size
-- ----------------------------
DROP TABLE IF EXISTS `store_product_color_size`;
CREATE TABLE `store_product_color_size`
(
    `id`              bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品颜色尺码ID',
    `store_prod_id`   bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_color_id`  bigint UNSIGNED NOT NULL COMMENT '档口商品颜色ID',
    `size`            int UNSIGNED NOT NULL COMMENT '商品尺码',
    `price`           decimal(10, 2) UNSIGNED NOT NULL COMMENT '档口商品定价',
    `sn_prefix`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档口商品颜色尺码的前缀',
    `other_sn_prefix` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它系统条码前缀',
    `next_sn`         int UNSIGNED NULL DEFAULT NULL COMMENT '下一个条码起始值',
    `standard`        tinyint UNSIGNED NOT NULL COMMENT '是否是标准尺码（2不是 1是）',
    `version`         bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX             `idx_spcs_del_snprefix`(`sn_prefix`,`del_flag`) USING BTREE,
    INDEX             `idx_spcs_del_othersnprefix`(`other_sn_prefix`,`del_flag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品颜色的尺码' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_spcs_store_prod_color_del ON store_product_color_size(store_prod_id, store_color_id, del_flag);

-- ----------------------------
-- Table structure for store_product_demand
-- ----------------------------
DROP TABLE IF EXISTS `store_product_demand`;
CREATE TABLE `store_product_demand`
(
    `id`               bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品需求ID',
    `code`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档口商品需求code',
    `store_id`         bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_factory_id` bigint UNSIGNED NOT NULL COMMENT '档口工厂ID',
    `version`          bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`      datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`      datetime NULL DEFAULT NULL COMMENT '更新时间',
    `demand_status`    tinyint UNSIGNED NULL DEFAULT NULL COMMENT '生产状态 1待生产 2 生产中  3 生产完成',
    `remark`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品需求单' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for store_product_demand_detail
-- ----------------------------
DROP TABLE IF EXISTS `store_product_demand_detail`;
CREATE TABLE `store_product_demand_detail`
(
    `id`                   bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品需求明细ID',
    `store_prod_demand_id` bigint UNSIGNED NOT NULL COMMENT '档口商品需求ID',
    `store_factory_id`     bigint UNSIGNED NOT NULL COMMENT '档口工厂ID',
    `store_prod_color_id`  bigint UNSIGNED NOT NULL COMMENT '档口商品颜色ID',
    `store_color_id`       bigint UNSIGNED NOT NULL COMMENT '档口颜色ID',
    `store_prod_id`        bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_id`             bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `prod_art_num`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品货号',
    `color_name`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '颜色名称',
    `detail_status`        tinyint(1) NULL DEFAULT NULL COMMENT '需求明细状态',
    `size_30`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码30',
    `size_31`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码31',
    `size_32`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码32',
    `size_33`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码33',
    `size_34`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码34',
    `size_35`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码35',
    `size_36`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码36',
    `size_37`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码37',
    `size_38`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码38',
    `size_39`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码39',
    `size_40`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码40',
    `size_41`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码41',
    `size_42`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码42',
    `size_43`              int UNSIGNED NULL DEFAULT NULL COMMENT '尺码43',
    `quantity`             int                                                           NOT NULL COMMENT '总的数量',
    `emergency`            tinyint UNSIGNED NOT NULL COMMENT '是否紧急单（0正常 1紧急）',
    `version`              bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`             char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`          datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`          datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品需求单明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_product_detail
-- ----------------------------
DROP TABLE IF EXISTS `store_product_detail`;
CREATE TABLE `store_product_detail`
(
    `id`            bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品详情ID',
    `store_prod_id` bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `detail`        mediumblob NOT NULL COMMENT '详情内容',
    `version`       bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品详情内容' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_product_file
-- ----------------------------
DROP TABLE IF EXISTS `store_product_file`;
CREATE TABLE `store_product_file`
(
    `id`             bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品文件ID',
    `store_prod_id`  bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_id`       bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `file_id`        bigint UNSIGNED NOT NULL COMMENT '系统文件ID',
    `file_size`      decimal(10, 3) UNSIGNED NOT NULL COMMENT '文件大小（M）',
    `file_type`      tinyint UNSIGNED NULL DEFAULT NULL COMMENT '商品文件类型（1主图、2主图视频、3下载图包）',
    `pic_zip_status` tinyint NULL DEFAULT 1 COMMENT '图包状态[1:非图包/不处理 2:待处理 3:处理中 4:已处理 5:处理异常]',
    `order_num`      int UNSIGNED NULL DEFAULT NULL COMMENT '排序',
    `version`        bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品文件' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_spf_store_prod_file_type ON store_product_file(store_prod_id, file_type);

-- ----------------------------
-- Table structure for store_product_process
-- ----------------------------
DROP TABLE IF EXISTS `store_product_process`;
CREATE TABLE `store_product_process`
(
    `id`                    bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品工艺信息ID',
    `store_prod_id`         bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `partner_name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户',
    `trademark`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商标',
    `shoe_type`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鞋型',
    `shoe_size`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '楦号',
    `main_skin`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主皮',
    `main_skin_usage`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主皮用量',
    `match_skin`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配皮',
    `match_skin_usage`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配皮用量',
    `neckline`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '领口',
    `insole`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '膛底',
    `fastener`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扣件/拉头',
    `shoe_accessories`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '辅料',
    `toe_cap`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '包头',
    `edge_binding`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '包边',
    `mid_outsole`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中大底',
    `platform_sole`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '防水台',
    `midsole_factory_code`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中底厂家编码',
    `outsole_factory_code`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外底厂家编码',
    `heel_factory_code`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跟厂编码',
    `components`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配料',
    `second_sole_material`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第二底料',
    `second_upper_material` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第二配料',
    `custom_attr`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义',
    `version`               bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`              char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`           datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`           datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品工艺信息' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for store_product_service
-- ----------------------------
DROP TABLE IF EXISTS `store_product_service`;
CREATE TABLE `store_product_service`
(
    `id`                      bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品服务ID',
    `store_prod_id`           bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `custom_refund`           char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '大小码及定制款可退',
    `thirty_day_refund`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '30天包退（大小码及定制款不可退）',
    `one_batch_sale`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '一件起批',
    `refund_within_three_day` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款72小时到账',
    `version`                 bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`                char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`             datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`             datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品服务' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for store_product_statistics
-- ----------------------------
DROP TABLE IF EXISTS `store_product_statistics`;
CREATE TABLE `store_product_statistics`
(
    `id`               bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品统计ID',
    `store_prod_id`    bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_id`         bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `voucher_date`     date NOT NULL COMMENT '单据日期',
    `view_count`       bigint UNSIGNED NULL DEFAULT NULL COMMENT '浏览量',
    `img_search_count` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索次数',
    `download_count`   bigint UNSIGNED NULL DEFAULT NULL COMMENT '下载量',
    `version`          bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`      datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`      datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for store_product_stock
-- ----------------------------
DROP TABLE IF EXISTS `store_product_stock`;
CREATE TABLE `store_product_stock`
(
    `id`                  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品库存ID',
    `store_id`            bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_prod_id`       bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `prod_art_num`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品货号',
    `color_name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '颜色名称',
    `store_prod_color_id` bigint UNSIGNED NOT NULL COMMENT '档口商品颜色ID',
    `store_color_id`      bigint UNSIGNED NOT NULL COMMENT '档口颜色ID',
    `size_30`             int NULL DEFAULT NULL COMMENT '尺码30',
    `size_31`             int NULL DEFAULT NULL COMMENT '尺码31',
    `size_32`             int NULL DEFAULT NULL COMMENT '尺码32',
    `size_33`             int NULL DEFAULT NULL COMMENT '尺码33',
    `size_34`             int NULL DEFAULT NULL COMMENT '尺码34',
    `size_35`             int NULL DEFAULT NULL COMMENT '尺码35',
    `size_36`             int NULL DEFAULT NULL COMMENT '尺码36',
    `size_37`             int NULL DEFAULT NULL COMMENT '尺码37',
    `size_38`             int NULL DEFAULT NULL COMMENT '尺码38',
    `size_39`             int NULL DEFAULT NULL COMMENT '尺码39',
    `size_40`             int NULL DEFAULT NULL COMMENT '尺码40',
    `size_41`             int NULL DEFAULT NULL COMMENT '尺码41',
    `size_42`             int NULL DEFAULT NULL COMMENT '尺码42',
    `size_43`             int NULL DEFAULT NULL COMMENT '尺码43',
    `version`             bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品库存' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_sps_store_prod_color_del ON store_product_stock(store_prod_id, store_prod_color_id, del_flag);



-- ----------------------------
-- Table structure for store_product_storage
-- ----------------------------
DROP TABLE IF EXISTS `store_product_storage`;
CREATE TABLE `store_product_storage`
(
    `id`               bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品入库ID',
    `code`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '入库CODE',
    `store_id`         bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_factory_id` bigint UNSIGNED NOT NULL COMMENT '档口工厂ID',
    `storage_type`     tinyint UNSIGNED NOT NULL COMMENT '入库类型（生产入库1、其它入库2、维修入库3）',
    `quantity`         int UNSIGNED NOT NULL COMMENT '数量',
    `produce_amount`   decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '生产成本金额',
    `operator_id`      bigint UNSIGNED NOT NULL COMMENT '当前操作人ID sys_user.id',
    `operator_name`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作人名称',
    `version`          bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`      datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`      datetime NULL DEFAULT NULL COMMENT '更新时间',
    `voucher_date`     date                                                          NOT NULL COMMENT '单据日期',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品入库' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_sps_code_store_del ON store_product_storage(code, store_id, del_flag);


-- ----------------------------
-- Table structure for store_product_storage_demand_deduct
-- ----------------------------
DROP TABLE IF EXISTS `store_product_storage_demand_deduct`;
CREATE TABLE `store_product_storage_demand_deduct`
(
    `id`                           bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品入库抵扣需求ID',
    `store_prod_storage_detail_id` bigint UNSIGNED NOT NULL COMMENT '档口商品入库明细ID',
    `store_prod_demand_detail_id`  bigint UNSIGNED NOT NULL COMMENT '档口商品需求明细ID',
    `store_prod_color_id`          bigint UNSIGNED NOT NULL COMMENT '档口商品颜色ID',
    `storage_code`                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库code',
    `demand_code`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '需求code',
    `quantity`                     int UNSIGNED NOT NULL COMMENT '总的数量',
    `remark`                       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `size`                         int UNSIGNED NULL DEFAULT NULL COMMENT '尺码',
    `version`                      bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`                     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`                  datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`                    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`                  datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品入库抵扣需求' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_spsdd_del_storage_detail_id ON store_product_storage_demand_deduct(del_flag, store_prod_storage_detail_id);


-- ----------------------------
-- Table structure for store_product_storage_detail
-- ----------------------------
DROP TABLE IF EXISTS `store_product_storage_detail`;
CREATE TABLE `store_product_storage_detail`
(
    `id`                  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品入库明细ID',
    `store_prod_stor_id`  bigint UNSIGNED NOT NULL COMMENT '档口商品入库ID',
    `store_prod_color_id` bigint UNSIGNED NOT NULL COMMENT '档口商品颜色ID',
    `store_prod_id`       bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_color_id`      bigint UNSIGNED NOT NULL COMMENT '档口颜色ID',
    `prod_art_num`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品货号',
    `color_name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '颜色名称',
    `size_30`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码30',
    `size_31`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码31',
    `size_32`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码32',
    `size_33`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码33',
    `size_34`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码34',
    `size_35`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码35',
    `size_36`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码36',
    `size_37`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码37',
    `size_38`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码38',
    `size_39`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码39',
    `size_40`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码40',
    `size_41`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码41',
    `size_42`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码42',
    `size_43`             int UNSIGNED NULL DEFAULT NULL COMMENT '尺码43',
    `quantity`            int NULL DEFAULT NULL COMMENT '总数量',
    `produce_price`       decimal(10, 2) NULL DEFAULT NULL COMMENT '生产价格',
    `produce_amount`      decimal(10, 2) NULL DEFAULT NULL COMMENT '总的生产价格',
    `version`             bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口商品入库明细' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for store_sale
-- ----------------------------
DROP TABLE IF EXISTS `store_sale`;
CREATE TABLE `store_sale`
(
    `id`              bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口销售出库ID',
    `store_id`        bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_cus_id`    bigint UNSIGNED NOT NULL COMMENT '档口客户ID',
    `store_cus_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
    `sale_type`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '销售类型（普通销售、销售退货、普通销售/销售退货）',
    `code`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据编号',
    `round_off`       decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '抹零金额',
    `voucher_date`    date                                                          NOT NULL COMMENT '单据日期',
    `quantity`        int           NOT NULL COMMENT '数量',
    `sale_quantity`   int UNSIGNED NULL DEFAULT NULL COMMENT '销售数量',
    `refund_quantity` int NULL DEFAULT NULL COMMENT '退货数量',
    `amount`          decimal(10, 2) NOT NULL COMMENT '总金额',
    `sale_amount`     decimal(10, 2) NULL DEFAULT NULL COMMENT '销售金额',
    `refund_amount`   decimal(10, 2) NULL DEFAULT NULL COMMENT '退货金额',
    `pay_way`         tinyint UNSIGNED NOT NULL COMMENT '支付方式（1支付宝、2微信、3现金、4欠款）',
    `payment_status`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结款状态（已结清、欠款）',
    `operator_id`     bigint UNSIGNED NOT NULL COMMENT '当前操作人ID sys_user.id',
    `operator_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作人名称',
    `remark`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `version`         bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口销售出库' ROW_FORMAT = DYNAMIC;
CREATE INDEX idx_ss_store_del_cus ON store_sale(store_id, del_flag, store_cus_id);


-- ----------------------------
-- Table structure for store_sale_detail
-- ----------------------------
DROP TABLE IF EXISTS `store_sale_detail`;
CREATE TABLE `store_sale_detail`
(
    `id`                  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口商品销售出库明细ID',
    `store_sale_id`       bigint UNSIGNED NOT NULL COMMENT '档口商品销售ID',
    `store_prod_id`       bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_prod_color_id` bigint UNSIGNED NOT NULL COMMENT '档口商品颜色ID',
    `store_id`            bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_cus_id`        bigint UNSIGNED NOT NULL COMMENT '档口客户ID',
    `store_cus_name`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
    `voucher_date`        date                                                          NOT NULL COMMENT '单据日期',
    `prod_art_num`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品货号',
    `sale_type`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '销售类型（普通销售、销售退货、销售/退货）',
    `price`               decimal(10, 2) UNSIGNED NOT NULL COMMENT '销售单价',
    `discounted_price`    decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '给客户优惠后单价',
    `quantity`            int                                                           NOT NULL COMMENT '数量',
    `amount`              decimal(10, 2)                                                NOT NULL COMMENT '总金额',
    `color_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色',
    `size`                int UNSIGNED NULL DEFAULT NULL COMMENT '尺码',
    `sn`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条码',
    `version`             bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口销售明细' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for store_sale_refund_record
-- ----------------------------
DROP TABLE IF EXISTS `store_sale_refund_record`;
CREATE TABLE `store_sale_refund_record`
(
    `id`              bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口销售出库返单记录ID',
    `store_sale_id`   bigint UNSIGNED NOT NULL COMMENT '档口销售出库ID',
    `store_id`        bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_cus_id`    bigint UNSIGNED NOT NULL COMMENT '档口销售客户ID',
    `store_cus_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档口客户名称',
    `sale_type`       int UNSIGNED NOT NULL COMMENT '销售类型（销售、退货、销售/退货）',
    `code`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据编号',
    `voucher_date`    date                                                          NOT NULL COMMENT '单据日期',
    `quantity`        int                                                           NOT NULL COMMENT '数量',
    `sale_quantity`   int UNSIGNED NULL DEFAULT NULL COMMENT '销售数量',
    `refund_quantity` int NULL DEFAULT NULL COMMENT '退货数量',
    `amount`          decimal(10, 2)                                                NOT NULL COMMENT '总金额',
    `payment_status`  int UNSIGNED NULL DEFAULT NULL COMMENT '结款状态（已结清、欠款）',
    `operator_id`     bigint UNSIGNED NOT NULL COMMENT '当前操作人ID sys_user.id',
    `operator_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作人名称',
    `pay_way`         int UNSIGNED NOT NULL COMMENT '支付方式',
    `remark`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `version`         bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档口销售返单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for store_sale_refund_record_detail
-- ----------------------------
DROP TABLE IF EXISTS `store_sale_refund_record_detail`;
CREATE TABLE `store_sale_refund_record_detail`
(
    `id`                          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '档口销售返单明细ID',
    `store_sale_refund_record_id` bigint UNSIGNED NOT NULL COMMENT '档口销售返单ID',
    `store_prod_id`               bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `store_prod_color_id`         bigint UNSIGNED NOT NULL COMMENT '档口商品颜色尺码ID',
    `store_id`                    bigint UNSIGNED NOT NULL COMMENT '档口ID',
    `store_cus_id`                bigint UNSIGNED NOT NULL COMMENT '档口客户ID',
    `store_cus_name`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
    `prod_art_num`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品货号',
    `sale_type`                   int UNSIGNED NOT NULL COMMENT '销售类型（普通销售、销售退货、销售/退货）',
    `price`                       decimal(10, 2) UNSIGNED NOT NULL COMMENT '销售单价',
    `discounted_price`            decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '给客户优惠后单价',
    `quantity`                    int            NOT NULL COMMENT '数量',
    `amount`                      decimal(10, 2) NOT NULL COMMENT '总金额',
    `sn`                         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条码',
    `color_name`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '颜色',
    `size`                        int UNSIGNED NULL DEFAULT NULL COMMENT '尺码',
    `version`                     bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`                    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`                 datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`                 datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '系统文件ID',
    `file_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
    `file_url`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件路径',
    `file_size`   decimal(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '文件大小(M)',
    `version`     bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`
(
    `id`             bigint NOT NULL AUTO_INCREMENT COMMENT '用户地址ID',
    `user_id`        bigint NOT NULL COMMENT 'sys_user.id',
    `receive_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人名称',
    `receive_phone`  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收件人电话',
    `province_code`  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省code',
    `city_code`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市code',
    `district_code`  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区(县)code',
    `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
    `version`        bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户收货地址' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for user_authentication
-- ----------------------------
DROP TABLE IF EXISTS `user_authentication`;
CREATE TABLE `user_authentication`
(
    `id`                     bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '代发认证ID',
    `user_id`                bigint UNSIGNED NOT NULL COMMENT 'sys_user.id',
    `real_name`              varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实名称',
    `id_card`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
    `id_card_face_file_id`   bigint UNSIGNED NULL DEFAULT NULL COMMENT '身份证头像面文件ID',
    `id_card_emblem_file_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '身份证国徽面文件ID',
    `auth_status`            int UNSIGNED NULL DEFAULT NULL COMMENT '代发认证状态',
    `reject_reason`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拒绝理由',
    `version`                bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`               char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`            datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`            datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`, `real_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户代发认证' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for user_browsing_history
-- ----------------------------
DROP TABLE IF EXISTS `user_browsing_history`;
CREATE TABLE `user_browsing_history`
(
    `id`            bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户浏览足迹ID',
    `user_id`       bigint UNSIGNED NOT NULL COMMENT 'sys_user.id',
    `store_id`      bigint UNSIGNED NULL DEFAULT NULL COMMENT 'store.id',
    `store_name`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档口名称',
    `store_prod_id` bigint UNSIGNED NOT NULL COMMENT '档口商品ID',
    `prod_art_num`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品货号',
    `main_pic_url`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第一张主图路径',
    `price`         decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
    `prod_title`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品标题',
    `browsing_time` datetime NULL DEFAULT NULL COMMENT '凭证日期',
    `version`       bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户浏览历史' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for user_favorites
-- ----------------------------
DROP TABLE IF EXISTS `user_favorites`;
CREATE TABLE `user_favorites`
(
    `id`            bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户收藏ID',
    `user_id`       bigint UNSIGNED NOT NULL COMMENT 'sys_user.id',
    `store_id`      bigint UNSIGNED NOT NULL COMMENT 'store.id',
    `store_prod_id` bigint UNSIGNED NOT NULL COMMENT 'store_prod.id',
    `version`       bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_del_user_prod` (`del_flag`, `user_id`, `store_prod_id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户收藏商品' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for user_notice
-- ----------------------------
DROP TABLE IF EXISTS `user_notice`;
CREATE TABLE `user_notice`
(
    `id`                 bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户通知ID',
    `notice_id`          bigint UNSIGNED NOT NULL COMMENT 'sys_notice.id',
    `user_id`            bigint UNSIGNED NOT NULL COMMENT 'sys_user.id',
    `target_notice_type` int UNSIGNED NULL DEFAULT NULL COMMENT '用户接收消息类型',
    `read_status`        int UNSIGNED NOT NULL COMMENT '（0未读 1已读）',
    `voucher_date`       date NOT NULL COMMENT '凭证日期',
    `version`            bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`           char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`        datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`        datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户所有通知' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for user_notice_setting
-- ----------------------------
DROP TABLE IF EXISTS `user_notice_setting`;
CREATE TABLE `user_notice_setting`
(
    `id`                  bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户设置是否接收通知ID',
    `user_id`             bigint UNSIGNED NOT NULL COMMENT 'sys_user.id',
    `version`             bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime NULL DEFAULT NULL COMMENT '更新时间',
    `sys_msg_notice`      int UNSIGNED NULL DEFAULT NULL COMMENT '允许接收系统消息',
    `order_notice`        int UNSIGNED NULL DEFAULT NULL COMMENT '代发订单通知',
    `focus_notice`        int UNSIGNED NULL DEFAULT NULL COMMENT '我的关注通知',
    `favorite_notice`     int UNSIGNED NULL DEFAULT NULL COMMENT '收藏商品通知',
    `advert_notice`       int UNSIGNED NULL DEFAULT NULL COMMENT '推广营销通知',
    `prod_dynamic_notice` int UNSIGNED NULL DEFAULT NULL COMMENT '商品动态通知',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户通知接收设置' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for user_quick_function
-- ----------------------------
DROP TABLE IF EXISTS `user_quick_function`;
CREATE TABLE `user_quick_function`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户快捷功能ID',
    `user_id`     bigint UNSIGNED NOT NULL COMMENT 'sys_user.id',
    `func_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快捷菜单名称',
    `func_icon`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快捷功能图标',
    `func_url`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快捷功能路径',
    `order_num`   int UNSIGNED NULL DEFAULT NULL COMMENT '排序',
    `version`     bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户快捷功能' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for user_search_history
-- ----------------------------
DROP TABLE IF EXISTS `user_search_history`;
CREATE TABLE `user_search_history`
(
    `id`             bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户搜索历史主键',
    `user_id`        bigint UNSIGNED NOT NULL COMMENT '用户ID',
    `user_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
    `search_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索内容',
    `platform_id`    int NULL DEFAULT NULL COMMENT '搜索平台（电脑端、APP）',
    `search_time`    datetime NOT NULL COMMENT '搜索时间',
    `version`        bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for user_subscriptions
-- ----------------------------
DROP TABLE IF EXISTS `user_subscriptions`;
CREATE TABLE `user_subscriptions`
(
    `id`          bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户关注ID',
    `user_id`     bigint UNSIGNED NOT NULL COMMENT 'sys_user.id',
    `store_id`    bigint UNSIGNED NOT NULL COMMENT 'store.id',
    `version`     bigint UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户关注u档口' ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `sys_html`;
CREATE TABLE `sys_html` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(256) NOT NULL COMMENT '标题',
  `content` mediumblob DEFAULT NULL COMMENT '内容',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_title` (`title`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='网页内容';

-- 快递费用初始化（测试，上线前根据实际费用调整）
INSERT INTO `express_fee_config` VALUES (2, 1, '460000', '', 15.00, 15.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (3, 1, '330000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (4, 1, '530000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (6, 1, '140000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (7, 1, '340000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (8, 1, '210000', '', 7.00, 7.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (9, 1, '540000', '', 28.00, 28.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (10, 1, '410000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (11, 1, '150000', '', 15.00, 15.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (12, 1, '610000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (13, 1, '350000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (14, 1, '220000', '', 7.00, 7.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (16, 1, '420000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (17, 1, '620000', '', 7.00, 7.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (18, 1, '360000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (19, 1, '230000', '', 7.00, 7.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (21, 1, '430000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (22, 1, '630000', '', 7.00, 7.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (23, 1, '500000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (24, 1, '370000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (25, 1, '110000', '', 8.00, 8.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (26, 1, '440000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (27, 1, '310000', '', 8.00, 8.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (28, 1, '640000', '', 7.00, 7.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (29, 1, '510000', '', 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (30, 1, '120000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (32, 1, '450000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (33, 1, '320000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (34, 1, '650000', '', 28.00, 28.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (35, 1, '520000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (36, 1, '130000', NULL, 5.00, 5.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (65, 2, '460000', '', 10.00, 10.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (66, 2, '330000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (67, 2, '530000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (69, 2, '140000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (70, 2, '340000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (71, 2, '210000', '', 4.00, 4.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (72, 2, '540000', '', 16.00, 16.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (73, 2, '410000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (74, 2, '150000', '', 10.00, 10.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (75, 2, '610000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (76, 2, '350000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (77, 2, '220000', '', 4.00, 4.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (79, 2, '420000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (80, 2, '620000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (81, 2, '360000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (82, 2, '230000', '', 4.00, 4.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (84, 2, '430000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (85, 2, '630000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (86, 2, '500000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (87, 2, '370000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (88, 2, '110000', '', 4.20, 4.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (89, 2, '440000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (90, 2, '310000', '', 4.20, 4.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (91, 2, '640000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (92, 2, '510000', '', 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (93, 2, '120000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (95, 2, '450000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (96, 2, '320000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (97, 2, '650000', '', 16.00, 16.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (98, 2, '520000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (99, 2, '130000', NULL, 3.20, 3.20, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (128, 1, '513300', NULL, 8.00, 8.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (129, 1, '513200', NULL, 8.00, 8.00, '0', '', NULL, '', NULL, 0);
INSERT INTO `express_fee_config` VALUES (130, 2, '440300', NULL, 4.20, 4.20, '0', '', NULL, '', NULL, 0);


-- ----------------------------
-- Table structure for store_product_demand_template
-- ----------------------------
DROP TABLE IF EXISTS `store_product_demand_template`;
CREATE TABLE `store_product_demand_template`
(
    `id`                                bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `store_id`                          bigint(20) UNSIGNED NOT NULL COMMENT '档口ID',
    `select_size30`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码30',
    `select_size31`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码31',
    `select_size32`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码32',
    `select_size33`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码33',
    `select_size34`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码34',
    `select_size35`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码35',
    `select_size36`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码36',
    `select_size37`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码37',
    `select_size38`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码38',
    `select_size39`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码39',
    `select_size40`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码40',
    `select_size41`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码41',
    `select_size42`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码42',
    `select_size43`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '尺码43',
    `select_fac_name`                   tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '工厂名称',
    `select_demand_code`                tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '需求单号',
    `select_make_time`                  tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '提单时间',
    `select_factory_art_num`            tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '工厂货号',
    `select_prod_art_num`               tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '商品货号',
    `select_color_name`                 tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '颜色',
    `select_shoe_upper_lining_material` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '内里材质',
    `select_shaft_material`             tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '鞋面材质',
    `select_demand_status`              tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '生产状态',
    `select_emergency`                  tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '紧急程度',
    `select_quantity`                   tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '总数量',
    `select_partner_name`               tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '客户名称',
    `select_trademark`                  tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '商标',
    `select_shoe_type`                  tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '鞋型',
    `select_shoe_size`                  tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '楦号',
    `select_main_skin`                  tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '主皮',
    `select_main_skin_usage`            tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '主皮用量',
    `select_match_skin`                 tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '配皮',
    `select_match_skin_usage`           tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '配皮用量',
    `select_neckline`                   tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '领口',
    `select_insole`                     tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '膛底',
    `select_fastener`                   tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '扣件/拉头',
    `select_shoe_accessories`           tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '辅料',
    `select_toe_cap`                    tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '包头',
    `select_edge_binding`               tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '包边',
    `select_mid_outsole`                tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '中大底',
    `select_platform_sole`              tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '防水台',
    `select_midsole_factory_code`       tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '中底厂家编码',
    `select_outsole_factory_code`       tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '外底厂家编码',
    `select_heel_factory_code`          tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '跟厂编码',
    `select_components`                 tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '配料',
    `select_second_sole_material`       tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '第二底料',
    `select_second_upper_material`      tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '第二配料',
    `version`                           bigint(20) UNSIGNED NOT NULL COMMENT '版本号',
    `del_flag`                          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`                         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`                       datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`                         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time`                       datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;



