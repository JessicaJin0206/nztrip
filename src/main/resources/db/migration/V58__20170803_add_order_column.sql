ALTER TABLE `order` ADD COLUMN modified_price DECIMAL(11,2) default 0.00;
UPDATE `order` SET modified_price = price;