package com.xp.xx_forum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xp.xx_forum.mapper")
public class XxForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxForumApplication.class, args);
    }

}
