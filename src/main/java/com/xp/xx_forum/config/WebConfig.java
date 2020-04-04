package com.xp.xx_forum.config;

import com.xp.xx_forum.intercepter.LoginIntercepter;
import com.xp.xx_forum.listener.MySessionAttributeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.EventListener;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/1 15:44
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginIntercepter loginIntercepter;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginIntercepter).addPathPatterns("/**");
    }
//注册listener

    @Bean
    public ServletListenerRegistrationBean mySessionAttributeListener(){
        ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new MySessionAttributeListener());
        return registrationBean;
    }

}
