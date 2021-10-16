package droid.server;

import droid.content.Context;

import java.util.Arrays;

public class CommandParser {
    public static void run(Context context, String cmd) {
        /*String am = "am start com.yuyan.harp/.MainController";
        String pm = "pm install /user/download/some.jar";*/
        String[] commands = cmd.split(" ");
        System.out.println("commands = " + Arrays.toString(commands));

        if (commands.length < 2) {
            System.out.println("Illegal commands!");
            return;
        }

        int cmdNum = parseCommand(commands);
        System.out.println("cmd = " + cmdNum);
        if (cmdNum == PM_INSTALL) {
            handleInstall(context, commands);
        }

    }

    private static void handleInstall(Context context, String[] commands) {
        String[] jarPkgPaths = new String[commands.length - 2];
        System.arraycopy(commands, 2, jarPkgPaths, 0, commands.length - 2);
        String threadName = Thread.currentThread().getName();
        System.out.println("Thread[" + threadName + "]: handleInstall():jarPkgPaths = " + Arrays.toString(jarPkgPaths));
        SystemServer.Installer.run(context, jarPkgPaths);
    }

    private static final int ERROR = 0;
    private static final int PM_INSTALL = 20001;
    private static final String CM_STRING = "am";
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
        return 0;
    }

}
