DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` (
  `server_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Server ID',
  `host` varchar(255) DEFAULT NULL COMMENT 'host or ip',
  `status` int DEFAULT '0' COMMENT 'status 0 ok 1 disable',
  PRIMARY KEY (`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='server';

DROP TABLE IF EXISTS `server_group`;
CREATE TABLE `server_group` (
  `group_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Group ID',
  `name` varchar(255) NOT NULL COMMENT 'group name',
  `status` int DEFAULT '0' COMMENT 'status 0 ok 1 disable',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='server group';


DROP TABLE IF EXISTS `server_group_config`;
CREATE TABLE `server_group_config` (
  `group_id` bigint NOT NULL COMMENT 'group_id',
  `server_id` bigint NOT NULL COMMENT 'server_id',
  `status` int DEFAULT '0' COMMENT 'status 0 ok 1 disable',
  PRIMARY KEY (`group_id`,`server_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='server group config';
