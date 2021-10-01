package com.yuyan.room;

import org.jetbrains.annotations.NotNull;
import org.yuyan.room.base.DatabaseConfigure;
import org.yuyan.room.base.Room;

public enum UserDatabaseHelper {
    INSTANCE;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private UserDatabase database;

    public static void init() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static UserDatabase get(ClassLoader classloader){
        if (INSTANCE.database == null) {
            INSTANCE.database = Room.Companion.databaseBuilder(
                    UserDatabase.class, new DatabaseConfigure(
                            "yuyan", "123456") {
                @NotNull
                @Override
                public String getUrl() {
                    return "jdbc:mysql://localhost:3306/" +
                            "test" +
                            "?useSSL=false" +
                            "&allowPublicKeyRetrieval=true" +
                            "&serverTimezone=UTC" +
                            "&rewriteBatchedStatements=true";
                }
            }).build(classloader);
        }
        return INSTANCE.database;
    }
}
