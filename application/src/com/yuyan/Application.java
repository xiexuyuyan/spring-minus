package com.yuyan;

import com.yuyan.room.UserDatabase;
import org.yuyan.room.base.Room;

public class Application {
    public static void main(String[] args) {
        System.out.println("hello world");
        // MiniApplication.run(Application.class, args);
        UserDatabase database = Room.Companion.databaseBuilder(UserDatabase.class, "database name").build();
        database.userDao();
    }
}
