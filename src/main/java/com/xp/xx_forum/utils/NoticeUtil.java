package com.xp.xx_forum.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ph
 * @version 1.0
 * @date 2020/4/3 10:58
 */
@Component
public class NoticeUtil {
    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public Boolean isLogin(String accountId){
        if(redisTemplate.hasKey("loginUser:"+accountId)){
            return true;
        }else{
            return false;
        }
    }

    public void sendNotice(String accountId){
        simpMessagingTemplate.convertAndSendToUser(accountId,"/message",1);
    }

    public void keepNotice(String accountId){

        if (redisTemplate.hasKey("user:"+accountId)) {
            redisTemplate.opsForValue().increment("user:"+accountId,1);
        }else{
            redisTemplate.opsForValue().set("user:"+accountId,1);
        }
    }


}
