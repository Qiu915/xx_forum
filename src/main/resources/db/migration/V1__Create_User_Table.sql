CREATE TABLE user(
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(36) NULL,
  `name` varchar(36) NULL,
  `avatar` varchar(255) NULL,
  `bio` varchar(255) NULL,
  `token` varchar(36) NULL,
  `gmt_create` bigint(36) NULL,
  `gmt_modified` bigint(36) NULL,
  PRIMARY KEY (`id`)
);