package com.yuyan.wx.login;

import com.google.gson.JsonObject;
import com.yuyan.room.*;
import com.yuyan.wx.login.data.Result;
import com.yuyan.wx.login.data.SessionKey;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.yuyan.springmvc.beans.AutoWired;
import org.yuyan.springmvc.beans.TypeServletRequest;
import org.yuyan.springmvc.beans.TypeServletResponse;
import org.yuyan.springmvc.web.mvc.Controller;
import org.yuyan.springmvc.web.mvc.RequestMapping;
import org.yuyan.springmvc.web.mvc.RequestParam;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @AutoWired
    private LoginService loginService;
    @TypeServletRequest
    private ServletRequest request;
    @TypeServletResponse
    private ServletResponse response;

    private static final UserDatabase database = UserDatabaseHelper.get();

    @RequestMapping("/wx/login.jsp")
    public String login(@RequestParam("code") String code){
        System.out.println("uri = " + ((HttpServletRequest)request).getRequestURI() + ", code = " + code);
        if (code == null) {
            return "failed";
        }

        JsonObject reDataJsonObject = new JsonObject();
        Result<SessionKey> syncResult = loginService.request(code);

        if (syncResult instanceof Result.Error) {
            reDataJsonObject.addProperty("code", "10001");
            reDataJsonObject.addProperty("msg", "error");

            Exception exception = ((Result.Error) syncResult).getException();
            exception.printStackTrace();
            return reDataJsonObject.toString();
        }

        SessionKey sessionKey = ((Result.Success<SessionKey>) syncResult).getData();

        WxUser wxUser = database.wxUserDao().getWxUserByOpenid(sessionKey.getOpenid());
        Session session;
        if (wxUser.getUid() == 0) {
            User lastUser = database.userDao().getLastUser();
            User newUser = new User();
            newUser.setUid(lastUser.getUid() + 1);
            database.userDao().insert(newUser);

            wxUser.setUid(newUser.getUid());
            wxUser.setOpenid(sessionKey.getOpenid());
            database.wxUserDao().insert(wxUser);

            session = new Session();
            session.setUid(wxUser.getUid());
            session.setSessionKey(sessionKey.getKey());
            while (true) {
                int randomSessionUser = (int) (Math.random() * 10000) + 1;
                Session seed = database.sessionDao().getSessionBySessionUser(randomSessionUser);
                if (seed.getUid() == 0) {
                    session.setSessionUser(randomSessionUser + "");
                    break;
                }
            }
            database.sessionDao().insert(session);
        } else {
            session = database.sessionDao().getSessionByUid(wxUser.getUid());
            session.setSessionKey(sessionKey.getKey());
            database.sessionDao().update(session);
        }

        Login login = Login.createCurrent(wxUser.getUid());
        database.sessionDao().login(login);

        reDataJsonObject.addProperty("code", "10000");
        reDataJsonObject.addProperty("msg", "success");

        System.out.println("reDataJsonObject.toString() = " + reDataJsonObject.toString());

        ((HttpServletResponse)response).setHeader("session_key", session.getSessionUser());

        return reDataJsonObject.toString();
    }
}
