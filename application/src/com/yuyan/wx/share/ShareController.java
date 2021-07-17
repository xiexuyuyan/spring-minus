package com.yuyan.wx.share;

import org.yuyan.springmvc.beans.TypeServletRequest;
import org.yuyan.springmvc.beans.TypeServletResponse;
import org.yuyan.springmvc.web.mvc.Controller;
import org.yuyan.springmvc.web.mvc.RequestMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.DataInputStream;
import java.io.IOException;

@Controller
public class ShareController {
    @TypeServletRequest
    private ServletRequest request;
    @TypeServletResponse
    private ServletResponse response;

    @RequestMapping("/wx/share/text.jsp")
    public String shareText(){
        System.out.println("header session_key= " + ((HttpServletRequest)request).getHeader("session_key"));
        byte[] originByteData = new byte[request.getContentLength()];
        try {
            new DataInputStream(request.getInputStream()).readFully(originByteData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content = new String(originByteData);
        System.out.println(content);
        return "{code:10000, msg:\"success\"}";
    }
}