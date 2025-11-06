-- 更新数据库产品表结构，添加新字段
ALTER TABLE database_product 
ADD COLUMN `type` VARCHAR(100) NULL COMMENT '数据库类型' AFTER `icon`,
ADD COLUMN `description` TEXT NULL COMMENT '数据库描述' AFTER `type`,
-- 删除不需要的字段
DROP COLUMN `relation_type`;

-- 更新表注释
ALTER TABLE database_product COMMENT='数据库产品管理表';

-- 如果表不存在，创建表的SQL
CREATE TABLE IF NOT EXISTS `database_product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `database_name` varchar(100) NOT NULL COMMENT '数据库名称',
  `icon` varchar(200) DEFAULT NULL COMMENT '图标',
  `type` varchar(100) DEFAULT NULL COMMENT '数据库类型',
  `description` text DEFAULT NULL COMMENT '数据库描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='数据库产品管理表';