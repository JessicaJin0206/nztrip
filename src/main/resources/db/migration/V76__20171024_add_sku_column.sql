ALTER TABLE `sku` ADD COLUMN `api` tinyint(1) DEFAULT 0 AFTER `check_availability_website`;
UPDATE `sku` SET `api` = 0;
ALTER TABLE `agent` ADD COLUMN `has_api` tinyint(1) DEFAULT 0 AFTER `vendor_id`;
UPDATE `agent` SET `has_api` = 0;