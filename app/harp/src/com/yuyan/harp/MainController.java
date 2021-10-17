package com.yuyan.harp;

import com.yuyan.harp.driver.ResultState;
import droid.app.Controller;
import droid.server.pm.InstallManager;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Properties;

public class MainController extends Controller {
    String threadName = Thread.currentThread().getName();

    @Override
    public void onCreate() {
        System.out.println("Thread[" + threadName + "]: MainController onCreate().");
        System.out.println("ResultState.get(10000) = " + ResultState.get(10000 + ""));
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Thread[" + threadName + "]:MainController onStart().");
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
