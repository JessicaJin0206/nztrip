ALTER TABLE `sku_ticket_price` modify COLUMN sale_price DECIMAL(11,2) default 0.00;
ALTER TABLE `sku_ticket_price` modify COLUMN cost_price DECIMAL(11,2) default 0.00;
ALTER TABLE `order_ticket` modify COLUMN sale_price DECIMAL(11,2) default 0.00;
ALTER TABLE `order_ticket` modify COLUMN cost_price DECIMAL(11,2) default 0.00;
ALTER TABLE `order_ticket` modify COLUMN price DECIMAL(11,2) default 0.00;
