package com.yuyan.frown;

import droid.app.Controller;

import java.util.Arrays;

public class MainController extends Controller {
    public void printHello(String[] who) {
        System.out.println(Arrays.toString(who) + " print hello!");
    }
}
