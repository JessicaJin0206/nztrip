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
  UNIQUE INDEX `name_UNIQUE` (`user_name` ASC))
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
  `id` INT NOT NULL AUTO_INCREMENT,
  `sku_id` INT NOT NULL,
  `agent_id` INT NOT NULL,
  `remark` TEXT(1000) NOT NULL COMMENT 'store in json format according to sku_remark',
  `status` TINYINT(2) NOT NULL COMMENT '10 - pending\n20 - confirmed\n30 - cancelled\n40 - closed\n90 - deleted',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `adult_count` INT NOT NULL,
  `child_count` INT NULL DEFAULT 0,
  `baby_count` INT NULL DEFAULT 0,
  `elder_count` INT NULL DEFAULT 0,
  `family_count` INT NULL DEFAULT 0,
  `price` INT NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sku`;
CREATE TABLE `sku` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uuid` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `city_id` INT NOT NULL,
  `category_id` INT NOT NULL,
  `vendor_id` INT NOT NULL,
  `description` VARCHAR(1000) NULL,
  `adult_ticket` TINYINT(1) NOT NULL DEFAULT 1,
  `adult_ticket_remark` VARCHAR(45) NULL,
  `child_ticket` TINYINT(1) NOT NULL DEFAULT 0,
  `child_ticket_remark` VARCHAR(45) NULL,
  `baby_ticket` TINYINT(1) NOT NULL DEFAULT 0,
  `baby_ticket_remark` VARCHAR(45) NULL,
  `elder_ticket` TINYINT(1) NOT NULL DEFAULT 0,
  `elder_ticket_remark` VARCHAR(45) NULL,
  `family_ticket` TINYINT(1) NOT NULL DEFAULT 0,
  `family_ticket_remark` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sku_price`;
CREATE TABLE `sku_price` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sku_id` INT NOT NULL,
  `start_time` TIMESTAMP NOT NULL,
  `adult_cost_price` INT NOT NULL DEFAULT 0,
  `adult_sale_price` INT NOT NULL DEFAULT 0,
  `child_cost_price` INT NULL,
  `child_sale_price` INT NULL,
  `baby_cost_price` INT NULL,
  `baby_sale_price` INT NULL,
  `elder_cost_price` INT NULL,
  `elder_sale_price` INT NULL,
  `family_cost_price` INT NULL,
  `family_sale_price` INT NULL,
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