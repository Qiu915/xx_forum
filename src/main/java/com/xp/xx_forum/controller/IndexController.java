package com.xp.xx_forum.controller;

import com.xp.xx_forum.dto.QuestionDTO;
import com.xp.xx_forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author ph
 * @version 1.0
 * @date 2020/2/28 10:03
 */

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping("/")
    public String index(
            @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize",defaultValue = "7") Integer pageSize,
            Model model){
        Map<String, Object> map = questionService.findAll(pageNo, pageSize);
        model.addAttribute("map",map);
        return "index";
    }



}
