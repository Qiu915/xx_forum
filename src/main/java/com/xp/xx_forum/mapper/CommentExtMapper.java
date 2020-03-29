package com.xp.xx_forum.mapper;


import com.xp.xx_forum.bean.Comment;

public interface CommentExtMapper {

    Integer insert(Comment comment);

    void incCommentCount(Comment parentComment);

    void incLikeCount(Comment comment);

    void decLikeCount(Comment comment);
}