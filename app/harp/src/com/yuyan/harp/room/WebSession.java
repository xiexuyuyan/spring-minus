package com.yuyan.harp.room;

import org.yuyan.room.entity.ColumnInfo;
import org.yuyan.room.entity.Entity;
import org.yuyan.room.entity.PrimaryKey;

@Entity(tableName = "web_session")
public class WebSession {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    int uid;

    @ColumnInfo(name = "session_key")
    int sessionKey;

    public WebSession() {
    }

    public WebSession(int uid, int sessionKey) {
        this.uid = uid;
        this.sessionKey = sessionKey;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(int sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public String toString() {
        return "uid = " + uid
                + ", sessionKey = " + sessionKey;
    }
}