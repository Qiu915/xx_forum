package com.xp.xx_forum.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xp.xx_forum.bean.*;
import com.xp.xx_forum.dto.CommentDTO;
import com.xp.xx_forum.dto.QuestionDTO;
import com.xp.xx_forum.exception.CustomizeErrorCode;
import com.xp.xx_forum.exception.CustomizeException;
import com.xp.xx_forum.mapper.CommentMapper;
import com.xp.xx_forum.mapper.QuestionExtMapper;
import com.xp.xx_forum.mapper.QuestionMapper;
import com.xp.xx_forum.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/1 16:21
 */
@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private CommentMapper commentMapper;



    public Map<String,Object> findAll(Integer startPage, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageHelper.startPage(startPage,pageSize);
        List<Question> questions = questionExtMapper.queryAll();
        PageInfo page = new PageInfo(questions,5);
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            UserExample example = new UserExample();
            example.createCriteria()
                    .andAccountIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(example);
            questionDTO.setUser(users.get(0));
            questionDTOS.add(questionDTO);
        }
        map.put("page",page);
        map.put("questions",questionDTOS);
        return map;
    }

    public void createOrUpdateQuestion(Question question) {
//        判断id是否为null，若为空，则是新增问题。否则为更改问题
        if(question.getId() ==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else{
//         根据传进来的问题id查询出数据库中的问题
            Question selectQuestion = questionMapper.selectByPrimaryKey(question.getId());
//            判断是否查出问题，没有查出则抛出异常
            if(selectQuestion == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
//            判断数据库中查出的问题的发布者和当前操作者是否相同，不同则抛出异常
//            Long类型是包装类，不能直接使用==比较，要使用equals方法
            if(!(selectQuestion.getCreator().equals(question.getCreator()))){
                throw new CustomizeException(CustomizeErrorCode.INVALID_OPERATION);
            }
//            执行更新操作
            Question updateQuestion = new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
//            判断是否更新成功
            if(update!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        UserExample example = new UserExample();
        example.createCriteria()
                .andAccountIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(example);
        if(users.size()<1){
            throw new CustomizeException(CustomizeErrorCode.USER_NOT_FOUND);
        }
        questionDTO.setUser(users.get(0));
        return questionDTO;
    }

    public void incViewCount(Long id) {
        Question question = new Question();
        question.setId(id);
        questionExtMapper.incViewCount(question);
    }
    public  void incCommentCount(Long questionId) {
        Question question = new Question();
        question.setId(questionId);
        questionExtMapper.incCommentCount(question);
    }


    public List<CommentDTO> findComment(Long id) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id);
        example.setOrderByClause("gmt_create asc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if(comments.size() == 0){
            return new ArrayList<>();
        }
//        获取所有评论该问题的用户,去除重复.避免多次查询数据库
        Set<Long> commentSet = new HashSet<>();
        for (Comment comment : comments) {
            commentSet.add(comment.getCreator());
        }
//        从数据库中查出所有评论的用户，并转换成Map
        List<Long> commentList = new ArrayList<>();
        commentList.addAll(commentSet);
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                    .andAccountIdIn(commentList);
        List<User> users = userMapper.selectByExample(userExample);

        Map<Long,User> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.getAccountId(),user);
        }
//        获取返回值commentDTO
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCreator()));
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }
}


