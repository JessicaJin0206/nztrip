ALTER TABLE sku ADD COLUMN duration_id INT NOT NULL;
ALTER TABLE `vendor` MODIFY COLUMN email varchar(100);
ALTER TABLE `vendor` MODIFY COLUMN `name` varchar(50);