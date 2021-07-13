package com.yuyan.test;

import com.yuyan.room.Experience;
import com.yuyan.room.User;
import com.yuyan.room.UserDatabase;
import com.yuyan.room.UserDatabaseHelper;
import org.yuyan.room.base.Room;
import org.yuyan.room.base.RoomDatabase;
import org.yuyan.room.database.Database;
import org.yuyan.springmvc.beans.Bean;

@Bean
public class SalaryService {
    public Integer calcSalary(Integer uid) {
        UserDatabase database = UserDatabaseHelper.get();
        Experience exp = database.userDao().getExperienceByUid(uid);
        System.out.println(exp.toString());
        return Integer.parseInt(exp.getExperience()) * 500;
    }
}
