package com.xp.xx_forum.service;

import com.xp.xx_forum.bean.*;
import com.xp.xx_forum.dto.CommentDTO;
import com.xp.xx_forum.enums.CommentType;
import com.xp.xx_forum.exception.CustomizeErrorCode;
import com.xp.xx_forum.exception.CustomizeException;
import com.xp.xx_forum.mapper.CommentExtMapper;
import com.xp.xx_forum.mapper.CommentMapper;
import com.xp.xx_forum.mapper.QuestionMapper;
import com.xp.xx_forum.mapper.UserMapper;
import com.xp.xx_forum.utils.NoticeUtil;
import com.xp.xx_forum.utils.RabbitMqUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/8 16:12
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpAdmin amqpAdmin;


    @Autowired
    private RabbitMqUtils rabbitMqUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private NoticeUtil noticeUtil;


    @Transactional
    public CommentDTO insertComment(Comment comment, User user){
        comment.setCreator(user.getAccountId());
        comment.setCommentCount(0);
        comment.setLikeCount(0);
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
//        判断是对问题进行评论还是对评论进行二级评论
        if(comment.getType() == CommentType.QUESTION.getType()){
//            对问题进行评论
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

//            问题的评论数加一
            questionService.incCommentCount(comment.getParentId());
//            增加评论,使用主键生成策略，将生成的主键返回
            commentExtMapper.insert(comment);
//发送通知
            amqpAdmin.declareExchange(new DirectExchange("question-"+question.getId(),true,false));
            amqpAdmin.declareBinding(new Binding("user-"+user.getAccountId(), Binding.DestinationType.QUEUE,"question-"+question.getId(),"user."+user.getAccountId(),null));

            Notice notice = new Notice();
            notice.setName(user.getName());
            notice.setQuestionId(question.getId());
            notice.setTitle(question.getTitle());
            notice.setUserId(user.getAccountId());
            notice.setGmtCreate(System.currentTimeMillis());
            rabbitMqUtils.sendMessage(notice,"question-"+question.getId(),"user."+user.getAccountId());

            redisTemplate.opsForList().leftPush("question:"+question.getId(),notice);

            if(noticeUtil.isLogin(String.valueOf(question.getCreator()))){
                //发送消息
//                noticeUtil.keepNotice(String.valueOf(question.getCreator()));
                noticeUtil.sendNotice(String.valueOf(question.getCreator()));
            }else{
                //未登录，将消息存起来
                noticeUtil.keepNotice(String.valueOf(question.getCreator()));
            }


            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);
            return commentDTO;
        }
        else{
//            对评论进行评论
            Comment parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(parentComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Long parentId = parentComment.getParentId();
            Question parentQuestion = questionMapper.selectByPrimaryKey(parentId);
            if(parentQuestion == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
//          增加评论的评论数
            commentExtMapper.incCommentCount(parentComment);
//            插入二级评论
            commentExtMapper.insert(comment);

            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(user);
            return commentDTO;
        }
    }

    public List<CommentDTO> findComments(Long parentId) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(parentId);
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
