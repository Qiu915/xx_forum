package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.Notice;
import com.xp.xx_forum.bean.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "/notice")
    public String notice(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Notice notice;
        Boolean flag = true;
        while(flag){
            notice = (Notice) rabbitTemplate.receiveAndConvert("user-"+user.getAccountId());
            if(notice !=null ){
                System.out.println(notice);
            }else{
                flag = false;
            }

        }
        return "notice";
    }

}
