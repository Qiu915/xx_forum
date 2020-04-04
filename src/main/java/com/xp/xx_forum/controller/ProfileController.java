package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.dto.PageDTO;
import com.xp.xx_forum.dto.ResultDTO;
import com.xp.xx_forum.exception.CustomizeErrorCode;
import com.xp.xx_forum.exception.CustomizeException;
import com.xp.xx_forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/21 13:38
 */
@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @ResponseBody
    @PostMapping(value = "/profile")
    public ResultDTO profile(
            @RequestBody PageDTO pageDTO,
            Model model,
            HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(null == user){
            return ResultDTO.error(CustomizeErrorCode.NO_LOGIN);
        }
        pageDTO.setPageSize(7);
        Map<String,Object> map = questionService.findQuestions(user,pageDTO.getPageNo(),pageDTO.getPageSize());
        model.addAttribute("map",map);
        return ResultDTO.success(map);
    }
}
