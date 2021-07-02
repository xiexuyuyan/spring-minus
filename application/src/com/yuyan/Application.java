package com.yuyan;

import com.yuyan.room.User;
import com.yuyan.room.UserDatabase;
import org.yuyan.room.base.Room;

public class Application {
    public static void main(String[] args) {
        System.out.println("hello world");
        // MiniApplication.run(Application.class, args);
        UserDatabase database = Room.Companion.databaseBuilder(UserDatabase.class, "test").build();
        database.userDao().insert(new User(3, "rose", "rose@ktc.com"));
        // System.out.println(database.configure.getUrl());
    }
}
