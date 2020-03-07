package com.xp.xx_forum.handler;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/6 18:16
 * 自定义处理错误的方法
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class MyErrorController implements ErrorController {

//ErrorController中定义的方法，ErrorController的子类AbstractErrorController
//    AbstractErrorController的子类BasicErrorController
    @Override
    public String getErrorPath() {
        return null;
    }
//BasicErrorController中定义对于响应页面错误的处理方法
    @RequestMapping(
            produces = {"text/html"}
    )
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response,Model model) {
        HttpStatus status = getStatus(request);
        if (status.is4xxClientError()) {
            model.addAttribute("message", "您的请求出现错误了，请重新试试！！！");
        }
        if (status.is5xxServerError()) {
            model.addAttribute("message", "服务器出现错误了，请稍后再试试！！！");
        }
        return new ModelAndView("error");
    }
    //BasicErrorController中定义对于响应Json数据错误的处理方法
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = this.getStatus(request);
        Map<Object, Object> errorMap = new HashMap<>();
        if(status.is4xxClientError()){
            errorMap.put("messgae","您的请求出现错误了，请重新试试！！！");
        }
        if(status.is5xxServerError()){
            errorMap.put("messgae","服务器出现错误了，请稍后再试试！！！");
        }
        return new ResponseEntity(errorMap, status);
    }

//AbstractErrorController中获取HttpStatus的方法
    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }


}

