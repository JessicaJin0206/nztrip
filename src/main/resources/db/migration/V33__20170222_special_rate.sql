CREATE TABLE `special_rate` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agent_id` int(11) NOT NULL,
  `sku_id` int(11) NOT NULL,
  `sku` VARCHAR(30) NOT NULL,
  `discount` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
