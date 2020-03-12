package com.xp.xx_forum.handler;

import com.xp.xx_forum.exception.CustomizeErrorCode;
import com.xp.xx_forum.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * @author ph
 * @version 1.0
 * @date 2020/3/5 19:25
 * 定义处理自定义错误的方法
 */
@ControllerAdvice
public class MyExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public static ModelAndView errorHtml(Exception e, Model model){
        if (e instanceof CustomizeException) {
            model.addAttribute("message", e.getMessage());
        } else {
            model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
        }
//        转发到/error
        return new ModelAndView("error");
    }

    @ResponseBody
    public static Map<String,Object> errorJson(Exception e, HttpServletRequest request ){
        Map<String, Object> map = new HashMap<>();
        if (e instanceof CustomizeException) {
            map.put("message", e.getMessage());
        } else {
            map.put("message", CustomizeErrorCode.SYS_ERROR.getMessage());
        }
//        转发到/error
        return map;
    }

}
