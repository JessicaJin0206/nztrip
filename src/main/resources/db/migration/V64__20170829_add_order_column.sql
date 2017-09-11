ALTER TABLE `order` ADD COLUMN refund DECIMAL(11,2) default 0.00;
UPDATE `order` SET refund = 0.00;