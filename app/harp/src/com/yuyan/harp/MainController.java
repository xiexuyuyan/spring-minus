package com.yuyan.harp;

import com.yuyan.harp.data.driver.ResultState;
import droid.app.Controller;

import java.util.Arrays;

public class MainController extends Controller {
    String threadName = Thread.currentThread().getName();

    int test = 0;

    @Override
    public void onCreate() {
        System.out.println("Thread[" + threadName + "]: MainController onCreate().");
        System.out.println("MainController onCreate() ResultState.get(10000) = " + ResultState.get(10000 + ""));
        System.out.println("MainController onCreate() test = " + test);
        System.out.println("MainController onCreate() " + this);
        test++;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Thread[" + threadName + "]:MainController onStart().");
        System.out.println("MainController onStart() test = " + test);
        test++;
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
