package com.xp.xx_forum.controller;



import com.xp.xx_forum.bean.Notice;
import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.dto.ResultDTO;
import com.xp.xx_forum.exception.CustomizeErrorCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * @author ph
 * @version 1.0
 * @date 2020/3/22 16:14
 */
@Controller
public class NoticeController {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/profile/{section}")
    public String toProfile(@PathVariable(name = "section") String section,
                            HttpServletResponse response){
        if("question".equals(section)){
            Cookie cookie = new Cookie("section", section);
            cookie.setMaxAge(10);
            response.addCookie(cookie);
            return "notice";
        }else{
            Cookie cookie = new Cookie("section", section);
            cookie.setMaxAge(10);
            response.addCookie(cookie);
            return "notice";
        }
    }

    @PostMapping(value = "notice")
    @ResponseBody
    public ResultDTO findNotice(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(null == user){
            return ResultDTO.error(CustomizeErrorCode.NO_LOGIN);
        }
        List<Notice> notices = new ArrayList<>();
        boolean flag = true;
        while (flag){
            Notice notice = (Notice) rabbitTemplate.receiveAndConvert("user-" + user.getAccountId());
            if(null == notice){
                flag = false;
            }else{
                notices.add(notice);
            }
        }
        return ResultDTO.success(notices);
    }

}
