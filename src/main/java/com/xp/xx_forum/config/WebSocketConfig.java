package com.xp.xx_forum.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * @author ph
 * @version 1.0
 * @date 2020/4/1 10:13
 */
@Configuration
@EnableWebSocketMessageBroker//启用STOMP消息, 表明这个配置类不仅配置了WebSocket， 还配置了基于代理的STOMP消息
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        注册两个simpleBroker /topic用来实现广播。/user实现点对点通信
        registry.enableSimpleBroker("/topic","/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        采用sockjs，防止浏览器对原生的websocket不支持
        registry.addEndpoint("/topicServer").withSockJS();//实现广播
        registry.addEndpoint("/noticeServer").withSockJS();//实现点对点
    }

}
