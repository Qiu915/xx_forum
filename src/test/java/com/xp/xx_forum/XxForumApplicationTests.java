package com.xp.xx_forum;

import com.xp.xx_forum.bean.Question;
import com.xp.xx_forum.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;



@SpringBootTest
class XxForumApplicationTests {



    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("k1","v1");
        System.out.println( redisTemplate.opsForValue().get("k1"));
    }

    @Test
    void tests(){

        User user = new User();
        user.setName("ph");


        redisTemplate.opsForZSet().incrementScore("QUESTION:1",user,1);


    }

}
