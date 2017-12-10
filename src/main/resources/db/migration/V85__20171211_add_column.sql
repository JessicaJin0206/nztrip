ALTER TABLE `order` ADD COLUMN currency VARCHAR(20) default "NZD";
ALTER TABLE `order` ADD COLUMN pay_status int(11) default 0;
CREATE TABLE `payment_transaction` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ref_id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `amount` int(11) NOT NULL DEFAULT '0',
  `currency` varchar(4) NOT NULL DEFAULT 'NZD',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;