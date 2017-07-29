CREATE TABLE `sku_inventory` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sku_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `time` varchar(30) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sku_id_date_time` (`sku_id`,`date`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;