package com.yuyan.frown;

import droid.app.Controller;

import java.util.Arrays;
import java.util.Properties;

public class MainController extends Controller {
    String threadName = Thread.currentThread().getName();

    @Override
    public void onCreate() {
        System.out.println("Thread[" + threadName + "]:MainController onCreate() ");
    }

    public void printHello(String[] who) {
        System.out.println(Arrays.toString(who) + " print hello!");
    }
}
