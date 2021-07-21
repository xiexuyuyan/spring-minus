package com.yuyan.web.user.login;

import com.yuyan.room.UserDatabase;
import com.yuyan.room.UserDatabaseHelper;
import com.yuyan.room.WebSession;
import com.yuyan.web.user.data.Result;
import com.yuyan.web.user.data.ResultInWeb;
import com.yuyan.web.user.login.LoginService;
import org.yuyan.springmvc.beans.AutoWired;
import org.yuyan.springmvc.beans.TypeServletRequest;
import org.yuyan.springmvc.beans.TypeServletResponse;
import org.yuyan.springmvc.web.mvc.Controller;
import org.yuyan.springmvc.web.mvc.RequestMapping;
import org.yuyan.springmvc.web.mvc.RequestParam;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("/web/login.jsp")
    public String login() {
        String uid = request.getParameter("uid");
        String name = request.getParameter("name");
        String mail = request.getParameter("mail");

        System.out.println("uid = " + uid);
        System.out.println("name = " + name);
        System.out.println("mail = " + mail);

        Result<WebSession> result;
        if (uid != null) {
            result = loginService.loginByUid(Integer.parseInt(uid));
        } else if (name != null && mail != null) {
            result = loginService.loginByNameAndMail(name, mail);
        } else {
            return ResultInWeb.error().jsonString();
        }

        if (result instanceof Result.Error) {
            Exception e = ((Result.Error) result).getException();
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