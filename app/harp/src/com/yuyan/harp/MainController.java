package com.yuyan.harp;

import com.yuyan.harp.room.User;
import com.yuyan.harp.room.UserDatabaseHelper;
import com.yuyan.harp.room.UserDatabase;
import droid.app.Controller;

public class MainController extends Controller {

    @Override
    public void onCreate() {
        super.onCreate();
        new Root().run();
        System.out.println("harp main controller execute!");
        System.out.println("MainController onCreate() Thread.currentThread().getName() = " + Thread.currentThread().getName());
    }

    public void getSalary(String uid, String name, String mail) {
        System.out.println("getSalary: uid = " + uid);
        System.out.println("getSalary: name = " + name);
        System.out.println("getSalary: mail = " + mail);

        UserDatabaseHelper.init();
        UserDatabase userDatabase = UserDatabaseHelper.get();
        User user = userDatabase.userDao().getLastUser();
        System.out.println("user = " + user);
    }

}
