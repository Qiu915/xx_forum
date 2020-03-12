package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.dto.CommentDTO;
import com.xp.xx_forum.dto.QuestionDTO;
import com.xp.xx_forum.service.QuestionService;
import com.xp.xx_forum.utils.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/6 14:45
 */
@Controller
public class QuestionController{

    @Autowired
    private QuestionService questionService;


    @GetMapping(value = "/question/{id}")
    public String findQuestion(@PathVariable(name = "id") Long id,
                               HttpServletRequest request,
                               Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> commentDTOS = questionService.findComment(id);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
//        判断查出的问题的发布者和当前用户是否是同一人，不是则将当前问题浏览数加一
//        if(questionDTO.getUser().getId() != user.getId()){
//            questionService.incViewCount(id);
//        }
        questionService.incViewCount(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOS);
        return "question";
    }

    @GetMapping(value = "/publish/{id}")
    public String modifyQuestion(@PathVariable(name="id") Long id,
                                 Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagUtil.getTags());
        return "publish";

    }

}
