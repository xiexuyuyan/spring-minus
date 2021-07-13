package com.yuyan.wx;

import com.yuyan.wx.data.Result;
import com.yuyan.wx.data.SessionKey;
import okhttp3.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.yuyan.springmvc.beans.AutoWired;
import org.yuyan.springmvc.beans.TypeServletRequest;
import org.yuyan.springmvc.beans.TypeServletResponse;
import org.yuyan.springmvc.web.mvc.Controller;
import org.yuyan.springmvc.web.mvc.RequestMapping;
import org.yuyan.springmvc.web.mvc.RequestParam;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {
    @AutoWired
    private LoginService loginService;
    @TypeServletRequest
    private ServletRequest request;
    @TypeServletResponse
    private ServletResponse response;

    @RequestMapping("/wx/login.jsp")
    public String login(@RequestParam("code") String code){
        System.out.println("uri = " + ((HttpServletRequest)request).getRequestURI() + ", code = " + code);
        if (code == null) {
            return "failed";
        }

        String reData = "success";
        Result<SessionKey> syncResult = loginService.request(code);
        if (syncResult instanceof Result.Success) {
            SessionKey sessionKey = ((Result.Success<SessionKey>) syncResult).getData();
            reData = sessionKey.toString();
            System.out.println(sessionKey.toString());
            ((HttpServletResponse)response).setHeader("session_key", sessionKey.getKey());
        } else if (syncResult instanceof Result.Error) {
            Exception exception = ((Result.Error) syncResult).getException();
            try {
                reData = "exception";
                exception.printStackTrace();
                throw( exception);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return reData;
    }
}
