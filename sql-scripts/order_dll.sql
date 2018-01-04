CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` varchar(255) NOT NULL DEFAULT '' COMMENT 'order ID',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT 'order描述',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'user ID',
  `retailer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'retailer ID',
  `is_valid` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1 有效 0 无效',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_order_id` (`order_id`),
  KEY `ix_created_at` (`created_at`),
  KEY `ix_updated_at` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='order';