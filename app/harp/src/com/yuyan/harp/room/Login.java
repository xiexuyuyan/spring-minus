package com.yuyan.harp.room;

import org.yuyan.room.entity.ColumnInfo;
import org.yuyan.room.entity.Entity;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "login")
public class Login {
    @ColumnInfo(name = "uid")
    int uid;

    @ColumnInfo(name = "login_date")
    String date;

    @ColumnInfo(name = "login_time")
    String time;

    public Login() {
    }

    public Login(int uid, String date, String time) {
        this.uid = uid;
        this.date = date;
        this.time = time;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static Login createCurrent(int uid){
        Date dateOrigin = new Date();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        String date = format.format(dateOrigin);
        format.applyPattern("HH:mm:ss");
        String time = format.format(dateOrigin);
        return new Login(uid, date, time);
    }

    @Override
    public String toString() {
        return "uid = " + uid
                + ", date = " + date
                + ", time = " + time;
    }
}