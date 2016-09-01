ALTER TABLE sku ADD COLUMN duration_id INT NOT NULL default 0;
ALTER TABLE `vendor` MODIFY COLUMN email varchar(100);
ALTER TABLE `vendor` MODIFY COLUMN `name` varchar(50);