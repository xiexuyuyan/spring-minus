package com.yuyan.vicar;

import cat.server.TomcatServer;
import droid.app.Application;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args)
            throws ClassNotFoundException
            , IllegalAccessException
            , InstantiationException
            , NoSuchMethodException
            , InvocationTargetException
            , NoSuchFieldException {
        System.out.println("hello world");
        Root.main(args);
        new Main().start();
    }

    void start() throws ClassNotFoundException
            , IllegalAccessException
            , InstantiationException
            , NoSuchMethodException
            , InvocationTargetException
            , NoSuchFieldException {
        Class<?> targetClass = Class.forName("com.droid.server.SystemServer");
        Object server = targetClass.newInstance();
        Method runMethod = targetClass.getDeclaredMethod("tempRunForOuter");
        runMethod.invoke(server);
        Field field = targetClass.getDeclaredField("tempApplicationForOuter");
        Application app = (Application) field.get(server);

        TomcatServer tomcatServer = new TomcatServer();
        tomcatServer.startServer(app);
    }

}
