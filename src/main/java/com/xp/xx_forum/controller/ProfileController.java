package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "/profile")
    public String profile(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
                          @RequestParam(value = "pageSize",defaultValue = "7") Integer pageSize,
                          Model model,
                          HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(null == user){
            return "/";
        }
        Map<String,Object> map = questionService.findQuestions(user,pageNo,pageSize);
        model.addAttribute("map",map);
        return "profile";
    }
}
