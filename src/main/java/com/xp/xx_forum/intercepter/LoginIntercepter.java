package com.xp.xx_forum.intercepter;

import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.bean.UserExample;
import com.xp.xx_forum.mapper.UserMapper;
import com.xp.xx_forum.utils.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/1 15:47
 */
@Component
public class LoginIntercepter implements HandlerInterceptor {

//    @Autowired
//    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
//        判断是否得到cookie
        if (cookies != null && cookies.length != 0)
        for (Cookie cookie : cookies) {
           if(cookie.getName().equals("token")){
               String token = cookie.getValue();
               UserMapper userMapper = (UserMapper) SpringBeanUtil.getBean(UserMapper.class);
               UserExample userExample = new UserExample();
               userExample.createCriteria()
                       .andTokenEqualTo(token);
               List<User> users = userMapper.selectByExample(userExample);
               if(users.size()!=0){
                   HttpSession session = request.getSession();
                   session.setAttribute("user", users.get(0));
               }
               break;
           }
        }
        return true;
    }
}
