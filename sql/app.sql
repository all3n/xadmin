DROP TABLE IF EXISTS `xadmin_env`;
CREATE TABLE `xadmin_env` (
  `env_id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT 'name',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`env_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='env of app';

DROP TABLE IF EXISTS `xadmin_app`;
CREATE TABLE `xadmin_app` (
  `app_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT 'name',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='app';

DROP TABLE IF EXISTS `xadmin_app_config`;
CREATE TABLE `xadmin_deploy_config` (
  `cfg_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT 'name',
  `app_id` bigint NOT NULL COMMENT '0 is public,>0 is app private',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT 'parent id',
  `env_id` int NOT NULL DEFAULT 0 COMMENT 'env id',
  `content` text DEFAULT NULL COMMENT 'config',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`cfg_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='config';



DROP TABLE IF EXISTS `xadmin_deploy`;
CREATE TABLE `xadmin_deploy` (
  `deploy_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `app_id` bigint NOT NULL COMMENT 'app id',
  `cfg_id` bigint NOT NULL COMMENT 'cfg id',
  `env_id` int NOT NULL COMMENT 'env id',
  `revision` varchar(255) DEFAULT NULL COMMENT 'revision',
  `status` int(3) NOT NULL COMMENT 'status',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`deploy_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='deploy';


DROP TABLE IF EXISTS `xadmin_log`;
CREATE TABLE `xadmin_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `deploy_id` bigint NOT NULL COMMENT 'deploy id',
  `content` text NULL COMMENT 'deploy logs',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='deploy log';
