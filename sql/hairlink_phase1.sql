-- ============================
-- HairLink Phase 1 数据库初始化脚本
-- 版本：1.0.0
-- 创建日期：2025-12-19
-- 说明：包含P1阶段核心业务表（顾客管理、订单管理、基础配置）
-- 表数量：6个核心业务表
-- 依赖：需先执行 ry_20250522.sql（若依框架基础表）
-- ============================

-- ----------------------------
-- 1、顾客档案表
-- ----------------------------
drop table if exists hl_customer;
create table hl_customer (
  customer_id       bigint(20)      not null auto_increment comment '顾客ID',
  tenant_id         bigint(20)      default null comment '租户ID',
  dept_id           bigint(20)      default null comment '归属门店',

  -- [P1] 基础信息
  customer_name     varchar(50)     default '散客' comment '姓名',
  phone             varchar(11)     default null comment '手机号(兼容散客为空)',
  gender            char(1)         default '2' comment '性别 0男 1女 2未知',

  -- [P2] 资产快照（预留）
  member_level      char(1)         default '0' comment '会员等级 0普通 1银卡 2金卡',
  balance           decimal(10,2)   default 0.00 comment '储值余额',

  -- [P3] C端连接（预留）
  wx_openid         varchar(64)     default null comment '小程序OpenID',
  points            int(11)         default 0 comment '积分',

  -- 标准字段
  create_by         varchar(64)     default '',
  create_time       datetime        default null,
  update_by         varchar(64)     default '',
  update_time       datetime        default null,
  remark            varchar(500)    default null,

  primary key (customer_id),
  index idx_tenant (tenant_id),
  index idx_phone (phone),
  index idx_openid (wx_openid)
) engine=innodb auto_increment=100 comment = '顾客档案表';


-- ----------------------------
-- 2、会员权益卡表
-- ----------------------------
drop table if exists hl_member_card;
create table hl_member_card (
  card_id           bigint(20)      not null auto_increment comment '卡ID',
  tenant_id         bigint(20)      default null comment '租户ID',
  customer_id       bigint(20)      not null comment '顾客ID',

  -- [P1] 计次卡逻辑
  card_name         varchar(100)    default '' comment '卡名称',
  total_count       int(11)         default 0 comment '总次数',
  remain_count      int(11)         default 0 comment '剩余次数',

  -- [P2] 储值卡/期限卡逻辑（预留）
  card_type         char(1)         default '0' comment '卡类型 0计次 1储值 2期限',
  total_amount      decimal(10,2)   default 0.00 comment '总充值金额',
  remain_amount     decimal(10,2)   default 0.00 comment '剩余金额',
  gift_amount       decimal(10,2)   default 0.00 comment '赠送金额',

  expire_time       datetime        default null comment '过期时间',
  status            char(1)         default '0' comment '状态 0正常 1停用 2耗尽',

  create_by         varchar(64)     default '',
  create_time       datetime        default null,
  update_by         varchar(64)     default '',
  update_time       datetime        default null,

  primary key (card_id),
  index idx_tenant (tenant_id),
  index idx_customer (customer_id)
) engine=innodb auto_increment=100 comment = '会员权益卡表';


-- ----------------------------
-- 3、服务项目表
-- ----------------------------
drop table if exists hl_service;
create table hl_service (
  service_id        bigint(20)      not null auto_increment comment '服务ID',
  tenant_id         bigint(20)      default null comment '租户ID',

  service_name      varchar(100)    not null comment '服务名称',
  service_code      varchar(50)     default null comment '服务编码',
  price             decimal(10,2)   default 0.00 comment '标准价格',
  duration          int(11)         default 30 comment '服务时长（分钟）',
  category          varchar(50)     default null comment '服务分类',
  status            char(1)         default '0' comment '状态 0正常 1停用',
  sort_order        int(11)         default 0 comment '排序',

  create_by         varchar(64)     default '',
  create_time       datetime        default null,
  update_by         varchar(64)     default '',
  update_time       datetime        default null,
  remark            varchar(500)    default null,

  primary key (service_id),
  index idx_tenant (tenant_id),
  unique key uk_code (service_code)
) engine=innodb auto_increment=100 comment = '服务项目表';


-- ----------------------------
-- 4、员工扩展表
-- ----------------------------
drop table if exists hl_staff;
create table hl_staff (
  staff_id          bigint(20)      not null auto_increment comment '员工ID',
  user_id           bigint(20)      not null comment '关联sys_user用户ID',
  tenant_id         bigint(20)      default null comment '租户ID',
  dept_id           bigint(20)      default null comment '所属门店',

  staff_name        varchar(50)     not null comment '员工姓名',
  phone             varchar(11)     default null comment '手机号',
  position          varchar(50)     default '理发师' comment '职位',
  level             char(1)         default '1' comment '级别 1初级 2中级 3高级',

  -- [P2] 业绩相关（预留）
  commission_rate   decimal(5,2)    default 30.00 comment '提成比例',
  base_salary       decimal(10,2)   default 0.00 comment '底薪',

  status            char(1)         default '0' comment '状态 0在职 1离职',
  entry_date        date            default null comment '入职日期',

  create_by         varchar(64)     default '',
  create_time       datetime        default null,
  update_by         varchar(64)     default '',
  update_time       datetime        default null,

  primary key (staff_id),
  index idx_user (user_id),
  index idx_dept (dept_id)
) engine=innodb auto_increment=100 comment = '员工扩展表';


-- ----------------------------
-- 5、业务订单表
-- ----------------------------
drop table if exists hl_order;
create table hl_order (
  order_id          bigint(20)      not null auto_increment comment '订单ID',
  order_no          varchar(32)     not null comment '订单编号',
  tenant_id         bigint(20)      default null comment '租户ID',
  dept_id           bigint(20)      default null comment '门店ID',
  customer_id       bigint(20)      not null comment '顾客ID',

  -- [P1] 基础交易
  pay_amount        decimal(10,2)   default 0.00 comment '实收/实扣金额',
  pay_method        char(1)         default '0' comment '支付方式 0现金 1微信 2卡扣次',
  pay_status        char(1)         default '1' comment '支付状态 1已付',

  -- [P2] 复杂交易（预留）
  order_type        char(1)         default '0' comment '订单类型 0服务 1办卡 2零售',
  total_amount      decimal(10,2)   default 0.00 comment '原价总额',
  discount_amount   decimal(10,2)   default 0.00 comment '优惠金额',

  -- [P3] 在线支付（预留）
  transaction_id    varchar(64)     default null comment '微信支付流水号',

  create_by         varchar(64)     default '',
  create_time       datetime        default null,

  primary key (order_id),
  unique key uk_order_no (order_no),
  index idx_tenant (tenant_id),
  index idx_customer (customer_id),
  index idx_create_time (create_time)
) engine=innodb auto_increment=1000 comment = '业务订单表';


-- ----------------------------
-- 6、订单明细表
-- ----------------------------
drop table if exists hl_order_detail;
create table hl_order_detail (
  detail_id         bigint(20)      not null auto_increment comment '明细ID',
  order_id          bigint(20)      not null comment '订单ID',

  -- [P1] 核心内容
  item_id           bigint(20)      not null comment '项目ID',
  item_name         varchar(100)    not null comment '项目名称',
  price             decimal(10,2)   default 0.00 comment '单价',
  quantity          int(11)         default 1 comment '数量',
  staff_id          bigint(20)      default null comment '主理发师ID',
  deduct_card_id    bigint(20)      default null comment '关联扣次卡ID',

  -- [P2] 类型扩展（预留）
  item_type         char(1)         default '0' comment '项目类型 0服务 1产品',
  is_stock_deducted char(1)         default '0' comment '是否已扣库 0否 1是',

  primary key (detail_id),
  index idx_order (order_id)
) engine=innodb auto_increment=1000 comment = '订单明细表';


-- ----------------------------
-- 外键约束
-- ----------------------------

-- 会员卡 -> 顾客
ALTER TABLE hl_member_card
ADD CONSTRAINT fk_card_customer
FOREIGN KEY (customer_id) REFERENCES hl_customer(customer_id);

-- 订单 -> 顾客
ALTER TABLE hl_order
ADD CONSTRAINT fk_order_customer
FOREIGN KEY (customer_id) REFERENCES hl_customer(customer_id);

-- 订单明细 -> 订单（级联删除）
ALTER TABLE hl_order_detail
ADD CONSTRAINT fk_detail_order
FOREIGN KEY (order_id) REFERENCES hl_order(order_id) ON DELETE CASCADE;

-- 订单明细 -> 会员卡（可选）
ALTER TABLE hl_order_detail
ADD CONSTRAINT fk_detail_card
FOREIGN KEY (deduct_card_id) REFERENCES hl_member_card(card_id);


-- ----------------------------
-- 初始化-服务项目表数据
-- ----------------------------
INSERT INTO hl_service (service_name, service_code, price, duration, category, status, sort_order, create_by, create_time) VALUES
('男士剪发', 'MAN_CUT', 38.00, 30, '基础服务', '0', 1, 'admin', sysdate()),
('女士剪发', 'WOMAN_CUT', 58.00, 45, '基础服务', '0', 2, 'admin', sysdate()),
('烫发', 'PERM', 288.00, 120, '造型服务', '0', 3, 'admin', sysdate()),
('染发', 'DYE', 268.00, 120, '造型服务', '0', 4, 'admin', sysdate()),
('洗剪吹', 'WASH_CUT_BLOW', 68.00, 60, '套餐服务', '0', 5, 'admin', sysdate());


-- ----------------------------
-- 测试数据-顾客档案
-- ----------------------------
INSERT INTO hl_customer (customer_name, phone, gender, member_level, balance, create_by, create_time, remark) VALUES
('张三', '13800138001', '0', '0', 0.00, 'admin', sysdate(), '测试会员顾客1'),
('李四', '13800138002', '1', '0', 0.00, 'admin', sysdate(), '测试会员顾客2'),
('散客', NULL, '2', '0', 0.00, 'admin', sysdate(), '测试散客');


-- ----------------------------
-- 测试数据-员工扩展表
-- ----------------------------
INSERT INTO hl_staff (user_id, staff_name, phone, position, level, commission_rate, status, entry_date, create_by, create_time) VALUES
(1, '王师傅', '13900139001', '高级理发师', '3', 30.00, '0', '2024-01-01', 'admin', sysdate()),
(2, '赵师傅', '13900139002', '理发师', '2', 30.00, '0', '2024-06-01', 'admin', sysdate());


-- ----------------------------
-- 测试数据-会员卡
-- ----------------------------
-- 为张三创建一张10次理发卡
INSERT INTO hl_member_card (customer_id, card_name, card_type, total_count, remain_count, status, create_by, create_time) VALUES
(100, '理发计次卡', '0', 10, 10, '0', 'admin', sysdate());

-- 为李四创建一张20次理发卡
INSERT INTO hl_member_card (customer_id, card_name, card_type, total_count, remain_count, status, create_by, create_time) VALUES
(101, '理发计次卡', '0', 20, 19, '0', 'admin', sysdate());


-- ----------------------------
-- 测试数据-订单及明细
-- ----------------------------
-- 订单1：散客现金支付剪发
INSERT INTO hl_order (order_no, customer_id, pay_amount, pay_method, pay_status, order_type, total_amount, create_by, create_time) VALUES
('20251219000001', 102, 38.00, '0', '1', '0', 38.00, 'admin', sysdate());

INSERT INTO hl_order_detail (order_id, item_id, item_name, price, quantity, staff_id, item_type) VALUES
(1000, 100, '男士剪发', 38.00, 1, 100, '0');


-- 订单2：李四会员卡扣次烫发
INSERT INTO hl_order (order_no, customer_id, pay_amount, pay_method, pay_status, order_type, total_amount, create_by, create_time) VALUES
('20251219000002', 101, 288.00, '2', '1', '0', 288.00, 'admin', sysdate());

INSERT INTO hl_order_detail (order_id, item_id, item_name, price, quantity, staff_id, deduct_card_id, item_type) VALUES
(1001, 102, '烫发', 288.00, 1, 100, 101, '0');


-- ----------------------------
-- 初始化完成提示
-- ----------------------------
-- ============================
-- HairLink Phase 1 数据库初始化完成！
--
-- 已创建表：
-- 1. hl_customer (顾客档案表)
-- 2. hl_member_card (会员权益卡表)
-- 3. hl_service (服务项目表)
-- 4. hl_staff (员工扩展表)
-- 5. hl_order (业务订单表)
-- 6. hl_order_detail (订单明细表)
--
-- 已添加外键约束：4个
--
-- 已初始化数据：
-- - 服务项目：5条
-- - 测试顾客：3条
-- - 测试员工：2条
-- - 测试会员卡：2条
-- - 测试订单：2条（含订单明细）
--
-- 下一步：
-- 1. 在MySQL中执行本脚本
-- 2. 验证表结构和数据
-- 3. 开始后端代码开发
-- ============================
