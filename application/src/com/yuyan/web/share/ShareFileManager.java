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

    public static File root(int uid) {
        String userPath = INSTANCE.ROOT_PATH + uid;
        File userRoot = new File(userPath);
        if (userRoot.exists() && userRoot.isDirectory()) {
            return userRoot;
        }
        if (userRoot.exists() && userRoot.isFile()) {
            return null;
        }
        if (!userRoot.exists() && userRoot.mkdir()) {
            return userRoot;
        }
        return null;
    }
}