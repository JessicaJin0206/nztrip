CREATE TABLE `email_queue` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `from` varchar(100) NOT NULL DEFAULT '',
  `to` varchar(100) NOT NULL DEFAULT '',
  `subject` varchar(100) NOT NULL DEFAULT '',
  `content` text NOT NULL,
  `retry` int(11) NOT NULL DEFAULT '0',
  `succeed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
