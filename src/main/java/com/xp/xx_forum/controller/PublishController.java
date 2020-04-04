package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.Question;
import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.service.QuestionService;
import com.xp.xx_forum.utils.TagUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/5 10:03
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;



    @RequestMapping(value = "/publish")
    public  String toPublish(Model model)  {
        model.addAttribute("tags", TagUtil.getTags());
        return "publish";
    }
//    对提交的问题进行处理
    @PostMapping(value = "/publish")
    public String doPublish(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "tag", required = false) String tag,
                            @RequestParam(value = "id", required = false) Long id,
                            HttpServletRequest request,
                            Model model){

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags",TagUtil.getTags());

//       首先判断用户是否登录，未登录则返回提醒用户登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(null == user){
            model.addAttribute("error","当前未登录，请先进行登录");
            return "publish";
        }

//        判断标题是否为空，为空则将数据返回，并返回错误信息
        if(StringUtils.isBlank(title)){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }

        //    判断问题描述是否为空，为空则将数据返回，并返回错误信息
        if(StringUtils.isBlank(description)){
            model.addAttribute("error","问题描述不能为空");
            return "publish";
        }

        //   判断标签是否为空，为空则将数据返回，并返回错误信息
        if(StringUtils.isBlank(tag)){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getAccountId());
        question.setId(id);
        questionService.createOrUpdateQuestion(question);

        return "redirect:/";
    }

}
