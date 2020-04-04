package com.xp.xx_forum.listener;

import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.utils.SpringBeanUtil;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * @author ph
 * @version 1.0
 * @date 2020/4/3 9:41
 */
public class MySessionAttributeListener implements HttpSessionAttributeListener {

//    不能使用@autowired
//    @Autowired
//    private RedisTemplate redisTemplate;


    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        HttpSession session = se.getSession();
        User user = (User) session.getAttribute("user");
        RedisTemplate redisTemplate = (RedisTemplate) SpringBeanUtil.getBean("redisTemplate");
        redisTemplate.opsForValue().set("loginUser:"+user.getAccountId(),user);

//        用户登录，将离线消息推送给用户
//新开子线程。待用户登录成功将离线消息通过websocket推送给当前用户
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    RedisTemplate redisTemplate = (RedisTemplate) SpringBeanUtil.getBean("redisTemplate");
                    Object data = redisTemplate.opsForValue().get("user:" + user.getAccountId());
                    SimpMessagingTemplate simpMessagingTemplate = (SimpMessagingTemplate) SpringBeanUtil.getBean(SimpMessagingTemplate.class);
                    simpMessagingTemplate.convertAndSendToUser(user.getAccountId()+"","/message",data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {

    }
}
