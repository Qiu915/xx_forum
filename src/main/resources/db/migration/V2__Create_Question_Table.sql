CREATE TABLE `question`  (
  `id` int(0) NOT NULL,
  `title` varchar(50) NULL,
  `description` varchar(255) NULL,
  `tag` varchar(50) NULL,
  `creator` bigint(20) NULL,
  `comment_count` int(10) NULL,
  `gmt_create` bigint(20) NULL,
  `gmt_modified` bigint(20) NULL,
  `view_count` int(10) NULL,
  `like_count` int(10) NULL,
  PRIMARY KEY (`id`)
);