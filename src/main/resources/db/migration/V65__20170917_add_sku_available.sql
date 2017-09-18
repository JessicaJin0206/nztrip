ALTER TABLE `sku` ADD COLUMN `available` tinyint(1) DEFAULT 1 AFTER `auto_generate_reference_number`;
UPDATE `sku` SET available = 1;