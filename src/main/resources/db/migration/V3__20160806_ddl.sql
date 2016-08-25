CREATE TABLE `order_ticket` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sku_id` int(11) NOT NULL,
  `sku_ticket_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `order_ticket_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_ticket_id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL DEFAULT '',
  `age` int(3) NOT NULL,
  `weight` int(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

