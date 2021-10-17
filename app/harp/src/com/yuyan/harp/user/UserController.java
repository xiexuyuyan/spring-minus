package com.yuyan.harp.user;

import com.yuyan.harp.driver.ResultState;
import droid.app.Controller;

public class UserController extends Controller {
    public void loginByNameAndMail(String name, String mail) {
        System.out.println("loginByNameAndMail():name = " + name + ", mail = " + mail);
        System.out.println("ResultState.get(10000) = " + ResultState.get(10000 + ""));
    }
}
