package com.xp.xx_forum;

import com.alibaba.fastjson.JSON;
import com.xp.xx_forum.dto.AccessTokenDTO;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class XxForumApplicationTests {


    @Test
    void contextLoads() {
        OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                 response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    @Test
    void test01(){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("b70b037cc7b67840da8d");
        accessTokenDTO.setClient_secret("dd3e28ba65fbe1ca7d982f0e812fa56446bca017");

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
             String access_token = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
