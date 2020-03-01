package com.xp.xx_forum.utils;

import com.alibaba.fastjson.JSON;
import com.xp.xx_forum.dto.AccessTokenDTO;
import com.xp.xx_forum.dto.GithubUserDTO;
import okhttp3.*;

import java.io.IOException;

/**
 * @author ph
 * @version 1.0
 * @date 2020/2/29 11:41
 */
public class GithubUtil {
    public static String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String access_token = response.body().string();
            String token = access_token.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static GithubUserDTO getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUserDTO githubUser = JSON.parseObject(string, GithubUserDTO.class);
            return githubUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
