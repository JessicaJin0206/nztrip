CREATE TABLE `price_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(30) NOT NULL,
  `category` varchar(30) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `price` DECIMAL NOT NULL,
  `url` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;