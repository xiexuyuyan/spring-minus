package com.yuyan.test;

import com.yuyan.room.User;
import com.yuyan.room.UserDatabase;
import com.yuyan.room.UserDatabaseHelper;
import com.yuyan.wx.LoginService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.yuyan.springmvc.beans.AutoWired;
import org.yuyan.springmvc.beans.TypeServletRequest;
import org.yuyan.springmvc.beans.TypeServletResponse;
import org.yuyan.springmvc.web.mvc.Controller;
import org.yuyan.springmvc.web.mvc.RequestMapping;
import org.yuyan.springmvc.web.mvc.RequestParam;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Controller
public class SalaryController {
    @AutoWired
    private SalaryService salaryService;
    @TypeServletRequest
    private ServletRequest request;
    @TypeServletResponse
    private ServletResponse response;

    @RequestMapping("/test/get_salary.json")
    public Integer getSalary(@RequestParam("uid") String uid){
        System.out.println("uri = " + ((HttpServletRequest)request).getRequestURI() + ", uid = " + uid);
        if (uid == null) {
            uid = "1";
        }
        UserDatabase database = UserDatabaseHelper.get();
        User u = database.userDao().getUserNameByUid(Integer.parseInt(uid));
        System.out.println(u.toString());
        return salaryService.calcSalary(Integer.parseInt(uid));
    }
}
