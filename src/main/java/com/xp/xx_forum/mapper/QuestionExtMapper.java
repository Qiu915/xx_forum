package com.xp.xx_forum.mapper;

import com.xp.xx_forum.bean.Question;
import com.xp.xx_forum.bean.User;

import java.util.List;

public interface QuestionExtMapper {

    List<Question> queryAll();

    void incViewCount(Question question);

    void incCommentCount(Question question);

    List<Question> findQuestions(User user);
}