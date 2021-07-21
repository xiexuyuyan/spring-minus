package com.yuyan.web.user.login;

import com.yuyan.room.*;
import com.yuyan.web.user.data.Result;
import org.yuyan.springmvc.beans.Bean;

@Bean
@SuppressWarnings("unchecked")
public class LoginService {
    private static final UserDatabase database = UserDatabaseHelper.get();

    public Result<WebSession> loginByUid(int uid) {
        WebSession webSession = new WebSession();
        webSession.setUid(uid);
        while (true) {
            int randomSessionUser = (int) (Math.random() * 10000) + 1;
            WebSession seed = database.webSessionDao().getSessionBySessionKey(randomSessionUser);
            if (seed.getUid() == 0) {
                webSession.setSessionKey(randomSessionUser);
                break;
            }
        }

        WebSession seed = database.webSessionDao().getSessionByUid(uid);
        if (seed.getUid() != 0) {
            database.webSessionDao().update(webSession);
        } else {
            User user = database.userDao().getUserByUid(uid);
            if (user.getUid() != 0) {
                database.webSessionDao().insert(webSession);
            } else {
                return new Result.Error(new Exception("null of this user: " + uid));
            }
        }

        Login login = Login.createCurrent(uid);
        database.webSessionDao().login(login);
        return new Result.Success<>(webSession);
    }

    public Result<WebSession> loginByNameAndMail(String name, String mail) {
        User user = database.userDao().getUserByNameAndMail(name, mail);
        if (user.getUid() == 0) {
            return new Result.Error(new Exception("null of this user: {name=" + name + ", mail=" + mail + "}"));
        }
        return loginByUid(user.getUid());
    }
}