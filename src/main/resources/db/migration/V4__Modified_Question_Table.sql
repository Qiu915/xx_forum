ALTER TABLE `xx_forum`.`question`
MODIFY COLUMN `comment_count` int(10) NULL DEFAULT 0 ,
MODIFY COLUMN `view_count` int(10) NULL DEFAULT 0 ,
MODIFY COLUMN `like_count` int(10) NULL DEFAULT 0;