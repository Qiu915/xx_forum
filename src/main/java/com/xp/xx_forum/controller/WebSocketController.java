package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.ReceiveMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author ph
 * @version 1.0
 * @date 2020/4/2 15:41
 */
@Controller
public class WebSocketController {

    @Autowired
    private  SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping(value = "/webSocket")
    public String webSocket(){

        return "webSocket";
    }

    @MessageMapping("/subscribe")
    public void subscribe(ReceiveMessage rm) {
        System.out.println(rm.getName());
        for(int i =1;i<=20;i++) {
            /*广播使用convertAndSendToUser方法，第一个参数为用户id，此时js中的订阅地址为
            "/user/" + 用户Id + "/message",其中"/user"是固定的*/
            simpMessagingTemplate.convertAndSendToUser("zhangsan","/message",rm.getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
