drop table if exists `webhook_events`;
CREATE TABLE `webhook_events` (
    `event_id`         varchar(128)   not null   COMMENT '事件Id'
  , `event_channel`    varchar(100)   not null   COMMENT '事件渠道(paypal等)'
  , `event_type`       varchar(100)   not null   COMMENT '事件类型'
  , `summary`          varchar(2000)                           COMMENT '事件概要' 
  , `create_time`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'
  , PRIMARY KEY (`event_id`)

) COMMENT = '记录回调事件';