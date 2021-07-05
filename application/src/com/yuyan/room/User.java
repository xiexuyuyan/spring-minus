package com.yuyan.room;

import org.yuyan.room.entity.ColumnInfo;
import org.yuyan.room.entity.Entity;
import org.yuyan.room.entity.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    public int uid;

    @ColumnInfo(name = "user_name")
    public String name;

    @ColumnInfo(name = "user_mail")
    public String mail;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "uid: " + uid
                + ", name: " + name
                + ", mail: " + mail;
    }
}