ALTER TABLE `sku` ADD COLUMN `check_availability_website` TEXT AFTER `available`;
UPDATE `sku` SET check_availability_website = '';