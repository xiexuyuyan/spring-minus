package com.yuyan;

import com.yuyan.room.UserDatabaseHelper;
import com.yuyan.web.share.ShareFileManager;
import org.yuyan.springmvc.starter.MiniApplication;
import org.yuyan.springmvc.web.state.ResultState;

public class Application {

    public static void main(String[] args) {
        System.out.println("hello world");
        MiniApplication.run(Application.class, args);
        UserDatabaseHelper.init();
        ShareFileManager.init();
        ResultState.init();
    }
}
