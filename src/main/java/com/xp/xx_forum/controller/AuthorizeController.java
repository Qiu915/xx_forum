package com.xp.xx_forum.controller;

import com.xp.xx_forum.bean.User;

import com.xp.xx_forum.dto.AccessTokenDTO;
import com.xp.xx_forum.dto.GithubUserDTO;

import com.xp.xx_forum.service.UserService;
import com.xp.xx_forum.utils.GithubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.UUID;

/**
 * @author ph
 * @version 1.0
 * @date 2020/2/29 11:36
 */
@Controller
public class AuthorizeController {

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;


    @RequestMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);

        String token = GithubUtil.getAccessToken(accessTokenDTO);
        GithubUserDTO githubUser = GithubUtil.getUser(token);
//        获取到用户数据如果不为空，则登录成功并返回数据，回到首页
        if(null  != githubUser && githubUser.getId() != null){
            User user = new User();
            user.setAvatar(githubUser.getAvatarUrl());
            user.setAccountId(githubUser.getId());
            user.setBio(githubUser.getBio());
            user.setName(githubUser.getName());
            String userToken = UUID.randomUUID().toString();
            user.setToken(userToken);
             userService.createOrUpdateUser(user);
            Cookie cookie = new Cookie("token", userToken);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            response.addCookie(cookie);
            return "redirect:/";
        }else{
//            用户不存在，返回首页重新登录
            return "redirect:/";
        }

    }

    @RequestMapping("/loginOut")
    public String loginOut(HttpServletResponse response, HttpServletRequest request){
//        退出登录将session中存的user信息删除，并使传向服务器的cookie失效
        HttpSession session = request.getSession();
        session.removeAttribute("user");
//       设置cookie立即失效，向服务器发送一个同名的立即失效的cookie覆盖原有的cookie
        Cookie token = new Cookie("token", null);
        token.setMaxAge(0);
        token.setPath("/");
        response.addCookie(token);
        return "redirect:/";
    }
}
