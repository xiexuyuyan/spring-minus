package com.yuyan.controllers;

import org.yuyan.beans.AutoWired;
import com.yuyan.service.SalaryService;
import org.yuyan.web.mvc.Controller;
import org.yuyan.web.mvc.RequestMapping;
import org.yuyan.web.mvc.RequestParam;

@Controller
public class SalaryController {
    @AutoWired
    private SalaryService salaryService;

    @RequestMapping("get_salary.jsp")
    public Integer getSalary(@RequestParam("name") String name
            , @RequestParam("experience") String experience){
        if (experience == null) {
            experience = "0";
        }
        return salaryService.calcSalary(Integer.parseInt(experience));
    }
}
