CREATE TABLE `duration` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `vendor` ADD COLUMN phone VARCHAR(30);

ALTER TABLE `order` ADD COLUMN vendor_phone VARCHAR(30);

ALTER TABLE `admin` ADD COLUMN discount INT default 50;

ALTER TABLE `order_ticket` ADD COLUMN price INT default 0;