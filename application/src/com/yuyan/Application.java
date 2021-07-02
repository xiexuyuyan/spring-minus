package com.yuyan;

import com.yuyan.room.User;
import com.yuyan.room.UserDatabase;
import org.yuyan.room.base.Room;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    // MySQL 8.0 above
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String USER = "yuyan";
    static final String PASS = "123456";


    public static void main(String[] args) {
        System.out.println("hello world");
        // MiniApplication.run(Application.class, args);
        UserDatabase database = Room.Companion.databaseBuilder(UserDatabase.class, "database name").build();
        try {
            Class.forName(JDBC_DRIVER);
            database.connection = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (database.connection == null) {
            System.out.println("connection is null!");
        } else {
            database.userDao().insert(new User(2, "jack", "jack@ktc.com"));
        }
    }
}
