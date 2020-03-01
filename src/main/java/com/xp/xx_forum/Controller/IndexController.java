package com.xp.xx_forum.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ph
 * @version 1.0
 * @date 2020/2/28 10:03
 */

@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }



}
