package com.yuyan.service;

import com.yuyan.room.Experience;
import com.yuyan.room.User;
import com.yuyan.room.UserDatabase;
import org.yuyan.room.base.Room;
import org.yuyan.room.base.RoomDatabase;
import org.yuyan.room.database.Database;
import org.yuyan.springmvc.beans.Bean;

@Bean
public class SalaryService {
    public Integer calcSalary(Integer uid) {
        UserDatabase database = Room.Companion.databaseBuilder(UserDatabase.class, "test").build();
        User u = database.userDao().getUserNameByUid(uid);
        System.out.println(u.toString());
        Experience exp = database.userDao().getExperienceByUid(uid);
        System.out.println(exp.toString());
        return Integer.parseInt(exp.getExperience()) * 500;
    }
}
