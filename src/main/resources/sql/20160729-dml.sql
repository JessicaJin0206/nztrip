insert into city(`name`, `name_en`) values('皇后镇', 'Queenstown'), ('瓦纳卡', 'Wanaka'), ('箭镇', 'Arrowtown'), ('格林诺奇', 'Glenorchy'), ('米尔福德峡湾', 'Milford Sound'), ('神奇峡湾', 'Doubtful Sound'), ('蒂阿瑙', 'Te Anau'), ('旦尼丁', 'Dunedin'), ('奥玛鲁', 'Oamuru'),('基督城', 'Christchurch'),('蒂卡波', 'Tekapo'),('库克山', 'Mount Cook'),('福克斯冰川', 'Fox Glacier'),('弗朗兹约瑟夫冰川', 'Franz Josef Glacier'),('霍基蒂卡', 'Hokitika'),('格雷茅斯', 'Greymouth'),('阿卡罗阿', 'Akaroa'),('汉默温泉', 'Hanmer Springs'),('凯库拉', 'Kaikoura'),('奥克兰', 'Auckland'),('罗托鲁阿', 'Rotorua'),('惠林顿', 'Wellington');

INSERT INTO `agent` (`user_name`, `password`, `name`, `description`, `discount`, `email`)
VALUES
	('JINIAO', '123456', '上海极鸟网络科技发展有限公司', '', 40, 'dongrui@jibird.com'),
	('7DOUFENG', '123456', '成都去兜风科技有限公司', '', 25, 'info@7doufeng.com'),
	('SHIJIEBANG', '123456', '世界邦（北京）信息技术有限公司', '', 25, 'guanguang@shijiebang.net'),
	('HUANTAOYOU', '123456', '北京环滔信息技术有限公司', '', 25, 'gloria@huantaoyou.com'),
	('ROADBOOK', '123456', '北京一旅阳光文化传播有限公司', '', 25, 'ceo@roadbooks.com'),
	('MIQU', '123456', 'Miqu Tour Co.,Ltd', '', 40, 'nzcntour@outlook.com'),
	('GrahamWu', '123456', '新西兰司导', '', 25, 'mycandyou@sina.com'),
	('RonZhang', '123456', '新西兰司导', '', 25, 'freshbluenz@gmail.com'),
	('SongWang', '123456', '新西兰司导', '', 25, 'songwang51@gmail.com'),
	('315MOTEL', '123456', '新西兰Motel', '', 40, 'georgezhou7551@gmail.com'),
	('LINKTOUR', '123456', '北京游酷棒国际旅行社有限公司上海分公司', '', 25, '');

INSERT INTO `vendor` (`name`, `email`)
VALUES
	('nzone Skydive', 'skudive@gmail.com'),
	('Real Journey', 'realjourney@gmail.com'),
	('Puzzling World', 'puzzlingworld@gmail.com');

INSERT INTO `sku` (`uuid`, `name`, `city_id`, `category_id`, `vendor_id`, `description`, `gathering_place`, `pickup_service`)
VALUES
	('NS15000', '皇后镇NZONE跳伞15000英尺', 1, 1, 1, '皇后镇不可错过的极限体验', 'NZONE Skydive Shop @ 35 Shotover St,Queenstown', 0),
	('NS12000', '皇后镇NZONE跳伞12000英尺', 1, 1, 1, '皇后镇不可错过的极限体验', 'NZONE Skydive Shop @ 35 Shotover St,Queenstown', 0),
	('NS9000', '皇后镇NZONE跳伞9000英尺', 1, 1, 1, '皇后镇不可错过的极限体验', 'NZONE Skydive Shop @ 35 Shotover St,Queenstown', 0),
	('RJ100', '米尔福德峡湾景观游（仅游船）', 5, 2, 2, '世界第八大奇迹', 'the Real Journeys counter at the Milford Visitor Terminal, situated to the left as you enter through the main entrance', 0),
	('PW001', '奇幻世界组合票', 2, 2, 3, '适合全家一起游玩的奇幻世界', '188 Wanaka-Luggate Hwy, Wanaka 9382', 0);
