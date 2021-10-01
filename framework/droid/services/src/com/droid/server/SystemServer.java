package com.droid.server;

import com.droid.server.clz.ClassloaderManagerService;
import com.droid.server.cm.ControllerManagerService;
import com.droid.server.pm.InstallManagerService;
import com.droid.server.pm.PackageManagerService;
import droid.app.Application;
import droid.app.ContextThread;
import droid.content.Context;
import droid.content.Intent;
import droid.content.pm.PackageInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SystemServer {
    private ContextThread systemContextThread;
    private Context systemContext;

    public SystemServer() {}

    public Application tempApplicationForOuter;

    public void tempRunForOuter(){
        run();
        tempApplicationForOuter = new Application();
        tempApplicationForOuter.attachBaseContext(systemContext);
    }

    public static void main(String[] args) {
        new SystemServer().run();
    }

    private void run() {
        System.out.println("system server is run!");
        systemContext = createSystemContext();

        startBootstrapServices();
        startCoreServices();
        startOtherServices();
    }

    private Context createSystemContext() {
        systemContextThread = new ContextThread();
        return systemContextThread.getContext();
    }


    private void startBootstrapServices() {
        try {
            Class.forName("droid.app.SystemServiceRegister");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startCoreServices() {

    }

    private void startOtherServices() {
        PackageManagerService pms =
                (PackageManagerService) systemContext.getSystemService(Context.PACKAGE_SERVICE);
        // todo("Load the APPs through configuration")
        String[] jarFilePaths = new String[]{
                "/out/app/com.yuyan.harp/harp-0.0.1-alpha.jar"
        };
        List<PackageInfo> systemJarPackages = new ArrayList<>();

        InstallManagerService ims =
                (InstallManagerService) systemContext.getSystemService(Context.INSTALL_SERVICE);
        try {
            systemJarPackages.addAll(ims.loadJarFile(jarFilePaths));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClassloaderManagerService clzMs =
                (ClassloaderManagerService) systemContext.getSystemService(Context.CLASSLOADER_SERVICE);
        ClassLoader systemAppClassloader = clzMs.createSystemAppClassloader(systemJarPackages);
        clzMs.addClassloader(systemAppClassloader);
        clzMs.linkPackageToClassloader(systemAppClassloader, systemJarPackages);
        try {
            ims.installJarFile(systemAppClassloader, jarFilePaths);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        pms.updatePackages(clzMs.getClassloaderLinkedPackagesList());

        ControllerManagerService cms =
                (ControllerManagerService) systemContext.getSystemService(Context.CONTROLLER_SERVICE);
        cms.systemReady(systemContextThread);
    }





}
