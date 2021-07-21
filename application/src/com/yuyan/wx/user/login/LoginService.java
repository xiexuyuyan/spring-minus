package com.yuyan.wx.user.login;

import com.google.gson.Gson;
import com.yuyan.wx.user.login.data.Result;
import com.yuyan.wx.user.login.data.SessionKey;
import okhttp3.*;
import org.yuyan.springmvc.beans.Bean;

import java.io.IOException;
import java.util.Objects;

@Bean
@SuppressWarnings("unchecked")
public class LoginService {
    public Result<SessionKey> request(String code){
        String authUri = "https://api.weixin.qq.com/sns/jscode2session" +
                "?appid=" + "" +
                "&secret=" + "" +
                "&js_code=" + code +
                "&grant_type=authorization_code";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(authUri)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            Gson gson = new Gson();
            String resStr = Objects.requireNonNull(response.body()).string();
            SessionKey key = gson.fromJson(resStr, SessionKey.class);
            return new Result.Success<SessionKey>(key);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result.Error(e);
        }
    }
}
