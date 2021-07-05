package com.yuyan.room;

import org.yuyan.room.entity.ColumnInfo;
import org.yuyan.room.entity.Entity;
import org.yuyan.room.entity.PrimaryKey;

@Entity(tableName = "experience")
public class Experience {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    int uid;

    @ColumnInfo(name = "experience")
    String experience;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "uid: " + uid
                + ", experience: " + experience;
    }
}
