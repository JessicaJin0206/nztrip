ALTER TABLE `vendor` ADD COLUMN order_contact_prefix VARCHAR(30) default "";
update `vendor` set order_contact_prefix = "EYO " where id = 11;