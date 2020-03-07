CREATE TABLE `xx_forum`.`comment`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `parent_id` int(0) NOT NULL,
  `type` int(0) NULL,
  `creator` bigint(16) NULL,
  `comment_count` int(0) NULL DEFAULT 0,
  `content` varchar(1024) NULL,
  `gmt_create` bigint(32) NULL,
  `gmt_modified` bigint(32) NULL,
  `like_count` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);