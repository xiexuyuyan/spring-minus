package com.yuyan.harp.driver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum ResultState {
    INSTANCE;


    private final Properties properties;

    ResultState() {
        properties = new Properties();
    }

    public static void init(InputStream in) {
        try {
            INSTANCE.properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void init(){
        InputStream in = INSTANCE.getClass().getResourceAsStream("/properties/web.result.code.properties");
        init(in);
    }
    public static String get(String key){
        return INSTANCE.properties.getProperty(key);
    }
}