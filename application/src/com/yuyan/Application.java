package com.yuyan;

import com.yuyan.room.Experience;
import com.yuyan.room.User;
import com.yuyan.room.UserDatabase;
import org.yuyan.room.base.Room;
import org.yuyan.springmvc.starter.MiniApplication;

public class Application {
    public static void main(String[] args) {
        System.out.println("hello world");
        MiniApplication.run(Application.class, args);
    }
}
