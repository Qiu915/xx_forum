package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.dto.ResultDTO;
import com.xp.xx_forum.exception.CustomizeErrorCode;
import com.xp.xx_forum.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ph
 * @version 1.0
 * @date 2020/4/3 15:38
 */
@Controller
public class UserController {
    @ResponseBody
    @PostMapping(value = "/findCurrentUser")
    public ResultDTO findCurrentUser(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(null == user){
            return ResultDTO.error(CustomizeErrorCode.NO_LOGIN);
        }
        return ResultDTO.success(user);
    }
}
