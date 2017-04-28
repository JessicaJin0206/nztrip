CREATE TABLE `hot_item` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sku_uuid` varchar(45) NOT NULL DEFAULT '',
  `lookup_url` varchar(200) DEFAULT NULL COMMENT '官网查询url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;