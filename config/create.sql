CREATE TABLE `tbl_db_field` (
  `id` bigint(20) NOT NULL COMMENT '字段ID',
  `originName` varchar(22) NOT NULL COMMENT '字段名',
  `sqlType` int(11) NOT NULL COMMENT '字段sql类型',
  `length` int(11) DEFAULT '0' COMMENT '字段长度',
  `notNull` bit(1) DEFAULT b'0' COMMENT '是否非空',
  `primaryKey` bit(1) DEFAULT b'0' COMMENT '是否主键',
  `key` bit(1) DEFAULT b'0' COMMENT '是否创建索引',
  `comment` varchar(255) DEFAULT NULL COMMENT '字段的注释',
  `tableId` bigint(20) NOT NULL COMMENT '所属数据库表的ID',
  `dataSourceId` bigint(20) DEFAULT NULL COMMENT '在做数据迁移时，数据的',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库表模型字段信息';


CREATE TABLE `tbl_db_table` (
  `id` bigint(20) NOT NULL COMMENT '唯一标识ID',
  `tableName` varchar(50) NOT NULL COMMENT '数据库表模型信息表名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `keyTableName` (`tableName`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库表模型信息表';


CREATE TABLE `tbl_website_account_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `website` varchar(255) NOT NULL,
  `account` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;