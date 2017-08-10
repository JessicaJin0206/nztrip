DROP TABLE IF EXISTS `order_record`;
CREATE TABLE `order_record` (
  `id`  int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `order_id`  int(11) NOT NULL ,
  `operator`  varchar(255) NOT NULL ,
  `operate_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `content_change_from`  text  NULL ,
  `content_change_to`  text  NULL ,
  `status_change_from`  tinyint(2) NOT NULL ,
  `status_change_to`  tinyint(2) NOT NULL ,
  PRIMARY KEY (`id`),
  INDEX `order_id` (`order_id`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8;