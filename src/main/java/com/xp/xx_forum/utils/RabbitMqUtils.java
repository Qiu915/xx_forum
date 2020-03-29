package com.xp.xx_forum.utils;


import com.xp.xx_forum.bean.Notice;

import org.springframework.amqp.core.Message;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;



/**
 * @author ph
 * @version 1.0
 * @date 2020/3/22 15:37
 */
@Component
public class RabbitMqUtils {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback(){

        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            if(!ack){

            }
        }
    };

  final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback(){
      @Override
      public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

      }
  };

    public  void sendMessage(Notice notice,String exchange,String routingKey){
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.convertAndSend(exchange,routingKey,notice);
    }






}
