package com.yuyan.web.user.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class ResultInWeb {
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    private final Map<String, Object> data = new HashMap<>();
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public ResultInWeb(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResultInWeb success(){
        return new ResultInWeb("10000", "success");
    }
    public static ResultInWeb success(String code, String msg){
        return new ResultInWeb(code, msg);
    }

    public static ResultInWeb error(){
        return new ResultInWeb("10001", "error");
    }
    public static ResultInWeb error(String code, String msg){
        return new ResultInWeb(code, msg);
    }


    public String jsonString() {
        data.put("code", this.code);
        data.put("msg", this.msg);
        return gson.toJson(data);
    }

    public <V> ResultInWeb put(String key, V value) {
        data.put(key, value);
        return this;
    }

}