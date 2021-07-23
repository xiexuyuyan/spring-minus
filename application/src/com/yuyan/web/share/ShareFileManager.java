package com.yuyan.web.share;

import java.io.File;

public enum ShareFileManager {
    INSTANCE;

    private String ROOT_PATH = "D:\\phpstudy_pro\\WWW\\upload\\";

    private File rootFileDirectory;

    public static boolean init(){
        INSTANCE.rootFileDirectory = new File(INSTANCE.ROOT_PATH);

        return INSTANCE.rootFileDirectory.exists()
                && INSTANCE.rootFileDirectory.isDirectory();
    }
    public static boolean init(String path){
        INSTANCE.ROOT_PATH = path;
        return init();
    }

    public static File root() {
        return INSTANCE.rootFileDirectory;
    }
}