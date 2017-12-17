DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uuid` varchar(20) NOT NULL,
  `type` tinyint(2) NOT NULL DEFAULT 0,
  `agent_id` int NOT NULL DEFAULT 0,
  `primary_contact` varchar(100) NOT NULL,
  `primary_contact_email` varchar(255) NOT NULL,
  `primary_contact_phone` varchar(255) NOT NULL,
  `primary_contact_wechat` varchar(255) NOT NULL,
  `ticket_date_start` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ticket_date_end` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_cost_price` decimal(11,2) NOT NULL,
  `total_price` decimal(11,2) NOT NULL,
  `status` tinyint(2) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `group_member`;
CREATE TABLE `group_member` (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `age` int(3) NOT NULL,
  `weight` int(3) NOT NULL,
  `people_type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `group_id_index` USING BTREE (`group_id`) comment ''
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `group_order`;
CREATE TABLE `group_order` (
  `group_id` int NOT NULL,
  `order_id` int NOT NULL,
  PRIMARY KEY (`group_id`, `order_id`),
  INDEX `order_id_index` USING HASH (`order_id`) comment ''
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `order` ADD COLUMN `group_type` tinyint(2) DEFAULT 0 AFTER `from_vendor`;
UPDATE `order` SET `group_type` = 0;

ALTER TABLE `email_queue` ADD COLUMN `group_id` int(11) NOT NULL AFTER `succeed`;
UPDATE `email_queue` SET `group_id` = 0;