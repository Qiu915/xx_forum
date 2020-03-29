package com.xp.xx_forum.service;

import com.xp.xx_forum.bean.Comment;
import com.xp.xx_forum.bean.LikedUser;
import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.dto.ResultDTO;
import com.xp.xx_forum.exception.CustomizeErrorCode;
import com.xp.xx_forum.mapper.CommentExtMapper;
import com.xp.xx_forum.mapper.CommentMapper;
import com.xp.xx_forum.mapper.QuestionExtMapper;
import com.xp.xx_forum.mapper.QuestionMapper;
import com.xp.xx_forum.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/15 11:40
 */
@Service
public class LikedService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private QuestionMapper questionMapper;


    public ResultDTO handleCommentLiked(String id,Integer type, User user) {
        Comment comment = commentMapper.selectByPrimaryKey(Long.valueOf(id));
//        判断点赞的评论是否存在
        if(null == comment){
            return ResultDTO.error(CustomizeErrorCode.COMMENT_NOT_FOUND);
        }
//        判断当前点赞用户和评论用户是否相同，相同则不能给自己点赞
//        if(comment.getCreator() == user.getAccountId()){
//            return ResultDTO.error(CustomizeErrorCode.INVALID_OPERATION);
//        }

//        判断当前用户是点赞还是取消赞
        if(redisTemplate.opsForHash().hasKey(RedisUtil.getId(id,type),user.getId())){
//            当前是取消赞的操作
            redisTemplate.opsForHash().delete(RedisUtil.getId(id,type),user.getId());
//            设置mysql数据库中comment表中点赞数-1
            commentExtMapper.decLikeCount(comment);
            Long size = redisTemplate.opsForHash().size(RedisUtil.getId(id,type));
            return ResultDTO.success(size);

        }else{
//            点赞操作将要保存的数据存入redis数据库
            LikedUser lUser = new LikedUser();
            lUser.setId(user.getId());
            lUser.setName(user.getName());
            lUser.setAvatar(user.getAvatar());
//            用hash数据结构存储
            redisTemplate.opsForHash().put(RedisUtil.getId(id,type),lUser.getId(), lUser);
//            设置mysql数据库中comment表中点赞数+1
            commentExtMapper.incLikeCount(comment);
//            返回redis中存储的点赞个数
            Long size = redisTemplate.opsForHash().size(RedisUtil.getId(id,type));
            return ResultDTO.success(size);
        }
    }
}
