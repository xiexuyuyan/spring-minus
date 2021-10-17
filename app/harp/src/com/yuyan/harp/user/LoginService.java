package com.yuyan.harp.user;

import com.yuyan.harp.data.model.Result;
import com.yuyan.harp.room.*;

public class LoginService {
    private static final UserDatabase database = UserDatabaseHelper.get();

    private Result<?> login(int uid) {
        WebSession webSession = new WebSession();
        webSession.setUid(uid);
        while (true) {
            int randomSessionUser = (int) (Math.random() * 10000) + 1;
            if (randomSessionUser < 1000) {
                randomSessionUser+=1000;
            }
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
            database.webSessionDao().insert(webSession);
        }

        Login login = Login.createCurrent(uid);
        database.webSessionDao().login(login);
        return new Result.Success<>(webSession);
    }

    public Result<?> loginByUid(int uid) {
        User user = database.userDao().getUserByUid(uid);
        if (user.getUid() != 0) {
            return login(uid);
        } else {
            return new Result.Error(new Exception("null of this user: " + uid));
        }
    }

    public Result<?> loginByNameAndMail(String name, String mail) {
        User user = database.userDao().getUserByNameAndMail(name, mail);
        if (user.getUid() == 0) {
            return new Result.Error(new Exception("null of this user: {name=" + name + ", mail=" + mail + "}"));
        }
        return login(user.getUid());
    }
}
