package com.yuyan.harp.user;

import com.yuyan.harp.data.driver.ResultState;
import com.yuyan.harp.data.model.Result;
import com.yuyan.harp.data.model.ResultInWeb;
import com.yuyan.harp.room.WebSession;
import droid.app.Controller;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class UserController extends Controller {

    LoginService loginService;
    private ServletRequest request;
    private ServletResponse response;

    @Override
    public void onCreate() {
        loginService = new LoginService();
    }

    public void login(String name, String mail) {
        System.out.println("loginByNameAndMail():name = " + name + ", mail = " + mail);
        System.out.println("ResultState.get(10000) = " + ResultState.get(10000 + ""));
    }
    public void login(String uid) {
        System.out.println("uid = " + uid);
        System.out.println("ResultState.get(10000) = " + ResultState.get(10000 + ""));
    }

    public String _login() {
        String uid = request.getParameter("uid");
        String name = request.getParameter("name");
        String mail = request.getParameter("mail");

        System.out.println("uid = " + uid);
        System.out.println("name = " + name);
        System.out.println("mail = " + mail);

        Result<?> result;
        if (uid != null) {
            result = loginService.loginByUid(Integer.parseInt(uid));
        } else if (name != null && mail != null) {
            result = loginService.loginByNameAndMail(name, mail);
        } else {
            return ResultInWeb.create(10002)
                    .with("details", "params posted into [/web/login.jsp] all get tobe null")
                    .jsonString();
        }

        if (result instanceof Result.Error) {
            Exception e = ((Result.Error) result).getError();
            ResultInWeb error = ResultInWeb.error();
            error.setMsg(e.getMessage());
            return error.jsonString();
        }

        WebSession webSession = ((Result.Success<WebSession>)result).getData();
        System.out.println("webSession.getSessionKey() = " + webSession.getSessionKey());
        ((HttpServletResponse)response).addHeader("session_key", webSession.getSessionKey()+"");

        return ResultInWeb.success().jsonString();
    }

}
