package com.yuyan.controllers;

import org.yuyan.springmvc.beans.AutoWired;
import com.yuyan.service.SalaryService;
import org.yuyan.springmvc.web.mvc.Controller;
import org.yuyan.springmvc.web.mvc.RequestMapping;
import org.yuyan.springmvc.web.mvc.RequestParam;

@Controller
public class SalaryController {
    @AutoWired
    private SalaryService salaryService;

    @RequestMapping("get_salary.json")
    public Integer getSalary(@RequestParam("uid") String uid){
        System.out.println("controller: uid = "+uid);
        if (uid == null) {
            uid = "1";
        }
        return salaryService.calcSalary(Integer.parseInt(uid));
    }
}
