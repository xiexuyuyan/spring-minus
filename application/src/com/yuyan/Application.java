package com.yuyan;

import com.yuyan.controllers.SalaryController;
import com.yuyan.starter.MiniApplication;

public class Application {
    public static void main(String[] args) {
        System.out.println("hello world");
        MiniApplication.run(Application.class, args);
    }
}
