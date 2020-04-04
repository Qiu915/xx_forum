package com.xp.xx_forum.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ph
 * @version 1.0
 * @date 2020/4/3 10:43
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            SpringBeanUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T>  name) throws BeansException {
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(name);
    }

    public static Object getBean(String  name) throws BeansException {
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(name);
    }
}
