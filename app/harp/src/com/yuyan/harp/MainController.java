package com.yuyan.harp;

import com.yuyan.room.User;
import com.yuyan.room.UserDatabase;
import com.yuyan.room.UserDatabaseHelper;
import droid.app.Controller;
import droid.content.Context;
import droid.content.clz.ClassloaderManager;

public class MainController extends Controller {

    @Override
    public void onCreate() {
        System.out.println("harp main controller execute!");
    }

    public void getSalary(String uid, String name, String mail) {
        System.out.println("getSalary: uid = " + uid);
        System.out.println("getSalary: name = " + name);
        System.out.println("getSalary: mail = " + mail);

        ClassloaderManager clzms =
                (ClassloaderManager) getSystemService(Context.CLASSLOADER_SERVICE);

        UserDatabaseHelper.init();
        UserDatabase database = UserDatabaseHelper.get(clzms.getDefaultClassloader());
        User user = database.userDao().getLastUser();
        System.out.println("user = " + user);
    }

}
