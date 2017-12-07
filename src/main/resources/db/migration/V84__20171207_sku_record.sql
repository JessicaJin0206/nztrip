DROP TABLE IF EXISTS `sku_record`;
CREATE TABLE `sku_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sku_id` int NOT NULL,
  `operator_id` int NOT NULL,
  `operator_type` varchar(10) NOT NULL,
  `operate_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `operate_type` varchar(255) NOT NULL,
  `content_change_from` text NOT NULL,
  `content_change_to` text NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `sku_id_index` USING BTREE (`sku_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;