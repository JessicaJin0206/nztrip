ALTER TABLE `order_record`
CHANGE COLUMN `operator` `operator_id`  int(11) NOT NULL AFTER `order_id`,
MODIFY COLUMN `content_change_from`  text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `operate_time`,
MODIFY COLUMN `content_change_to`  text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `content_change_from`,
ADD COLUMN `operator_type`  varchar(10) NOT NULL AFTER `order_id`,
ADD COLUMN `operate_type`  varchar(255) NOT NULL AFTER `operator_id`;