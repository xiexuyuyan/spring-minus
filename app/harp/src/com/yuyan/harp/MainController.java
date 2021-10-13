package com.yuyan.harp;

import droid.app.Controller;

import java.util.Arrays;

public class MainController extends Controller {

    @Override
    public void onCreate() {
        super.onCreate();
        new Root().run();
        System.out.println("harp main controller execute!");
        System.out.println("MainController onCreate() Thread.currentThread().getName() = " + Thread.currentThread().getName());
    }

    public void getSalary(int uid, String name, String mail) {
        System.out.println("getSalary: uid = " + uid);
        System.out.println("getSalary: name = " + name);
        System.out.println("getSalary: mail = " + mail);
    }
    public void getSalary(int uid, String name, String mail, boolean withTax) {
        System.out.println("getSalary: uid = " + uid);
        System.out.println("getSalary: name = " + name);
        System.out.println("getSalary: mail = " + mail);
        System.out.println("withTax = " + withTax);
    }
    public void getSalary(int uid, String name, String mail, boolean withTax, int[] days) {
        System.out.println("getSalary: uid = " + uid);
        System.out.println("getSalary: name = " + name);
        System.out.println("getSalary: mail = " + mail);
        System.out.println("withTax = " + withTax);
        System.out.println("days = " + Arrays.toString(days));
    }

}
