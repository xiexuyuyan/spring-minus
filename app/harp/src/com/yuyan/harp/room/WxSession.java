package com.yuyan.harp.room;

import org.yuyan.room.entity.ColumnInfo;
import org.yuyan.room.entity.Entity;
import org.yuyan.room.entity.PrimaryKey;

@Entity(tableName = "wx_session")
public class WxSession {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    int uid;

    @ColumnInfo(name = "session_key")
    String sessionKey;

    @ColumnInfo(name = "session_user")
    String sessionUser;

    public WxSession() {
    }

    public WxSession(int uid, String sessionKey, String sessionUser) {
        this.uid = uid;
        this.sessionKey = sessionKey;
        this.sessionUser = sessionUser;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(String sessionUser) {
        this.sessionUser = sessionUser;
    }

    @Override
    public String toString() {
        return "uid = " + uid
                + ", sessionKey = " + sessionKey
                + ", sessionUser = " + sessionUser;
    }
}