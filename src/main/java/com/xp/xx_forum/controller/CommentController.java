package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.Comment;
import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.dto.CommentDTO;
import com.xp.xx_forum.dto.ResultDTO;
import com.xp.xx_forum.exception.CustomizeErrorCode;
import com.xp.xx_forum.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/8 14:12
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @PostMapping(value = "/comment")
    public Object insertComment(@RequestBody Comment comment, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
//        判断当前用户是否登录
        if(user == null){
            return ResultDTO.error(CustomizeErrorCode.NO_LOGIN);
        }
//        判断评论的问题是否存在
        if(comment.getParentId() == null){
            return ResultDTO.error(CustomizeErrorCode.TARGET_NOT_FOUND);
        }
//        判断评论的内容是否为空或者空字符串
        if(StringUtils.isBlank(comment.getContent())){
            return ResultDTO.error(CustomizeErrorCode.CONTENT_NOT_NULL);
        }

        CommentDTO commentDTO = commentService.insertComment(comment, user);

        return ResultDTO.success(commentDTO);
    }

//    以异步刷新的方式返回二级评论
    @ResponseBody
    @PostMapping(value = "/comments")
    public List<CommentDTO> findComments(@RequestBody Comment  comment){
        List<CommentDTO> comments = commentService.findComments(comment.getParentId());
        return comments;
    }
}
