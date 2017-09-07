delete from special_rate where agent_id = 28 and sku = 'RJ100' and discount = 12;
delete from special_rate where agent_id = 28 and sku = 'OH001' and discount = 10;
alter table special_rate add UNIQUE INDEX IX_agent_id_sku(agent_id, sku);