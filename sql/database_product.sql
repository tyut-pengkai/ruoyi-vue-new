-- 创建数据库产品表
CREATE TABLE `database_product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `database_name` varchar(100) NOT NULL COMMENT '数据库名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`product_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='数据库产品表';

-- 插入示例数据
INSERT INTO `database_product` VALUES (100, 'mysql', 'https://img.alicdn.com/imgextra/i2/O1CN01QH6qH01uV45a4y8sH_!!6000000006375-2-tps-200-200.png', '0', 'admin', '2024-01-20 10:00:00', 'admin', '2024-01-20 10:00:00', 'MySQL数据库');
INSERT INTO `database_product` VALUES (101, 'oracle', 'https://img.alicdn.com/imgextra/i4/O1CN01Jj1p9J1pU9d0XJ0eI_!!6000000005464-2-tps-200-200.png', '0', 'admin', '2024-01-20 10:00:00', 'admin', '2024-01-20 10:00:00', 'Oracle数据库');
INSERT INTO `database_product` VALUES (102, 'sqlserver', 'https://img.alicdn.com/imgextra/i1/O1CN012U2N5t1q0oE8kMl65_!!6000000005171-2-tps-200-200.png', '0', 'admin', '2024-01-20 10:00:00', 'admin', '2024-01-20 10:00:00', 'SQL Server数据库');