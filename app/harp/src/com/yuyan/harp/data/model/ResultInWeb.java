package com.yuyan.harp.data.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yuyan.harp.data.driver.ResultState;

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
        this.data.replace("code", this.code);
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
        this.data.replace("msg", this.msg);
    }

    private final Map<String, Object> data = new HashMap<>();
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private ResultInWeb(){}

    public ResultInWeb(String code, String msg) {
        this.code = code;
        this.msg = msg;

        data.put("code", this.code);
        data.put("msg", this.msg);
    }

    public static ResultInWeb create(int code){
        return new ResultInWeb(code + "", ResultState.get(code + ""));
    }

    public static ResultInWeb success(){
        return create(10000);
    }
    public static ResultInWeb success(String code, String msg){
        return new ResultInWeb(code, msg);
    }

    public static ResultInWeb error(){
        return create(10001);
    }
    public static ResultInWeb error(String code, String msg){
        return new ResultInWeb(code, msg);
    }


    public String jsonString() {
        return gson.toJson(data);
    }

    public <V> void put(String key, V value) {
        data.put(key, value);
    }

    public <V> ResultInWeb with(String key, V value) {
        put(key, value);
        return this;
    }

}