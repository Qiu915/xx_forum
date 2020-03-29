package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.Liked;
import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.dto.ResultDTO;
import com.xp.xx_forum.enums.LikedType;
import com.xp.xx_forum.exception.CustomizeErrorCode;
import com.xp.xx_forum.service.LikedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/15 11:26
 *
 */
@Controller
public class IlikedController {

    @Autowired
    private LikedService likedService;

    @ResponseBody
    @PostMapping(value = "/liked")
     public Object handleLiked(@RequestBody Liked liked,HttpServletRequest request){

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(user == null){
            return ResultDTO.error(CustomizeErrorCode.NO_LOGIN);
        }
        if(liked.getType()  == LikedType.COMMENT.getType()){
//        对评论点赞进行处理
            ResultDTO resultDTO = likedService.handleCommentLiked(liked.getId(), liked.getType(), user);
            return resultDTO;
        }else{
//            对问题点赞进行处理

        }
         return null;
     }
}
