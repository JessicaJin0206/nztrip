ALTER TABLE `sku` ADD COLUMN `suggest_remark` text AFTER `api`;
update sku set suggest_remark = '';