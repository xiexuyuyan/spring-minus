package droid.server;

import droid.content.ComponentName;
import droid.content.Context;
import droid.content.Intent;
import droid.content.PackageInfo;
import droid.server.cat.CatManager;
import droid.server.cm.ControllerManager;
import droid.server.pm.PackageManager;

import java.util.Arrays;

public class CommandParser {
    public static void run(Context context, String cmd) {
        /*
        String cm = "cm start com.yuyan.harp/.MainController";
        String cm = "cm create com.yuyan.harp/.MainController";
        String pm = "pm install /user/download/some.jar";*/
        String[] commands = cmd.split(" ");
        System.out.println("CommandParser run commands = " + Arrays.toString(commands));

        if (commands.length < 2) {
            System.out.println("Illegal commands!");
            return;
        }

        int cmdNum = parseCommand(commands);
        System.out.println("CommandParser run cmd = " + cmdNum);
        switch (cmdNum) {
            case PM_INSTALL:
                handlePmInstall(context, commands);
                break;
            case CM_CREATE:
                handleCmCreate(context, commands);
                break;
            case CM_START:
                handleCmStart(context, commands);
                break;
        }

    }



    private static void handleCmCreate(Context context, String[] commands) {
        String[] createClasses = new String[commands.length - 2];
        System.arraycopy(commands, 2, createClasses, 0, commands.length - 2);
        String threadName = Thread.currentThread().getName();
        System.out.println("Thread[" + threadName + "]: " +
                "handleCmCreate():createClasses = " + Arrays.toString(createClasses));
        ControllerManager cm =
                (ControllerManager) context.getSystemService(Context.CONTROLLER_SERVICE);
        CatManager catManager =
                (CatManager) context.getSystemService(Context.TOMCAT_SERVICE);
        for (String createClass : createClasses) {
            String[] classComponent = createClass.split("/");
            if (classComponent.length != 2) {
                break;
            }
            String packageName = classComponent[0];
            System.out.println("packageName = " + packageName);

            String fullClassName = packageName + classComponent[1];
            System.out.println("fullClassName = " + fullClassName);

            ContextThread targetThread = catManager.getThread(packageName);
            if (targetThread == null) {
                System.out.println("Thread[" + threadName + "]: " +
                        "there is not existing package " + packageName + " be loaded.");
                return;
            }
            ComponentName componentName = new ComponentName(packageName, fullClassName);
            Intent intent = new Intent(componentName);
            cm.executeOnCreate(targetThread, intent);
        }

    }

    private static void handleCmStart(Context context, String[] commands) {
        String[] startClasses = new String[commands.length - 2];
        System.arraycopy(commands, 2, startClasses, 0, commands.length - 2);
        String threadName = Thread.currentThread().getName();
        System.out.println("Thread[" + threadName + "]: " +
                "handleCmStart():startClasses = " + Arrays.toString(startClasses));
        ControllerManager cm =
                (ControllerManager) context.getSystemService(Context.CONTROLLER_SERVICE);
        CatManager catManager =
                (CatManager) context.getSystemService(Context.TOMCAT_SERVICE);
        for (String startClass : startClasses) {
            String[] classComponent = startClass.split("/");
            if (classComponent.length != 2) {
                break;
            }
            String packageName = classComponent[0];
            System.out.println("packageName = " + packageName);
            String fullClassName = packageName + classComponent[1];
            System.out.println("fullClassName = " + fullClassName);

            ContextThread targetThread = catManager.getThread(packageName);
            ComponentName componentName = new ComponentName(packageName, fullClassName);
            Intent intent = new Intent(componentName);
            cm.executeOnCreate(targetThread, intent);
            cm.executeOnStart(targetThread, intent);
        }
    }

    private static void handlePmInstall(Context context, String[] commands) {
        String[] jarPkgPaths = new String[commands.length - 2];
        System.arraycopy(commands, 2, jarPkgPaths, 0, commands.length - 2);
        String threadName = Thread.currentThread().getName();
        System.out.println("Thread[" + threadName + "]: handlePmInstall():jarPkgPaths = " + Arrays.toString(jarPkgPaths));
        SystemServer.Installer.run(context, jarPkgPaths);
    }

    private static final int ERROR = 0;
    private static final int PM_INSTALL = 20001;
    private static final int CM_CREATE = 10001;
    private static final int CM_START = 10002;
    private static final String CM_STRING = "cm";
    private static final String PM_STRING = "pm";
    private static int parseCommand(String[] cmd) {
        String first = cmd[0];
        switch (first) {
            case CM_STRING:
                return parseControllerManagerCmd(cmd);
            case PM_STRING:
                return parsePackageManagerCmd(cmd);
            default:
                return ERROR;
        }
    }

    private static int parsePackageManagerCmd(String[] cmd) {
        String second = cmd[1];
        if (second.equals("install")) {
            return PM_INSTALL;
        }
        return 0;
    }

    private static int parseControllerManagerCmd(String[] cmd) {
        String second = cmd[1];
        if (second.equals("create")) {
            return CM_CREATE;
        } else if (second.equals("start")) {
            return CM_START;
        }
        return 0;
    }

}
