package com.yuyan.harp.room;

import org.yuyan.room.entity.ColumnInfo;
import org.yuyan.room.entity.Entity;
import org.yuyan.room.entity.PrimaryKey;

@Entity(tableName = "wxuser")
public class WxUser {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    int uid;

    @ColumnInfo(name = "openid")
    String openid;

    public WxUser() {
    }

    public WxUser(int uid, String openid) {
        this.uid = uid;
        this.openid = openid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }


    @Override
    public String toString() {
        return "uid = " + uid + ", openid = " + openid;
    }
}