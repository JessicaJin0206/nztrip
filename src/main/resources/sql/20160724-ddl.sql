DROP TABLE IF EXISTS `agent`;
CREATE TABLE `agent` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(15) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(200) NULL,
  `discount` INT NOT NULL DEFAULT 100,
  `email` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNIQUE_user_name` (`user_name` ASC))
  ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `parent_category_id` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sku_id` int(11) NOT NULL,
  `agent_id` int(11) NOT NULL,
  `remark` text NOT NULL COMMENT 'user remark',
  `status` tinyint(2) NOT NULL COMMENT '10 - pending\n20 - confirmed\n30 - cancelled\n40 - closed\n90 - deleted',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `price` int(11) NOT NULL,
  `primary_contact` varchar(30) NOT NULL DEFAULT '',
  `primary_contact_email` varchar(30) NOT NULL DEFAULT '',
  `primary_contact_phone` varchar(20) NOT NULL DEFAULT '',
  `primary_contact_wechat` varchar(30) DEFAULT NULL,
  `secondary_contact` varchar(30) DEFAULT NULL,
  `secondary_contact_email` varchar(30) DEFAULT NULL,
  `secondary_contact_phone` varchar(30) DEFAULT NULL,
  `secondary_contact_wechat` varchar(30) DEFAULT NULL,
  `gathering_info` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;;

DROP TABLE IF EXISTS `sku`;
CREATE TABLE `sku` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `city_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  `vendor_id` INT NOT NULL,
  `description` VARCHAR(1000) NULL,
  `gathering_place` VARCHAR(200) NULL,
  `pickup_service` TINYINT(2) DEFAULT 0,
  `reference_number` VARCHAR(30) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sku_ticket`;
CREATE TABLE `sku_ticket` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sku_id` INT NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  `count_constraint` VARCHAR(15) NOT NULL DEFAULT '1',
  `age_constraint` VARCHAR(15) NULL,
  `weight_constraint` VARCHAR(15) NULL,
  `description` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sku_ticket_price`;
CREATE TABLE `sku_ticket_price` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sku_id` INT NOT NULL,
  `sku_ticket_id` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `time` VARCHAR(20) NOT NULL,
  `sale_price` INT DEFAULT 0,
  `cost_price` INT DEFAULT 0,
  `description` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sku_remark`;
CREATE TABLE `sku_remark` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sku_id` INT NOT NULL,
  `name` VARCHAR(15) NOT NULL,
  `required` TINYINT(1) NOT NULL DEFAULT 0,
  `type` TINYINT(2) NOT NULL COMMENT '1 - int\n2 - String\n3 - Date',
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `vendor`;
CREATE TABLE `vendor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL DEFAULT '',
  `email` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;