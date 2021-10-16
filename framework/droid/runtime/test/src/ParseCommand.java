import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ParseCommand {






    @Test
    public void fun() {
        String am = "am start com.yuyan.harp/.MainController";
        String pm = "pm install /user/download/some.jar";

        String[] commands = pm.split(" ");
        System.out.println("commands = " + Arrays.toString(commands));

        int cmd = parseCommand(commands);
        System.out.println("cmd = " + cmd);
        if (cmd == PM_INSTALL) {
            handleInstall(commands);
        }

    }

    void handleInstall(String[] commands) {
        String[] jarPkgPaths = new String[commands.length - 2];
        for (int i = 2; i < commands.length; i++) {
            jarPkgPaths[i-2] = commands[i];
        }
        String threadName = Thread.currentThread().getName();
        System.out.println("threadName = " + threadName + "， jarPkgPaths = " + Arrays.toString(jarPkgPaths));
    }

    public static final int ERROR = 0;
    public static final int PM_INSTALL = 20001;
    public static final String CM_STRING = "am";
    public static final String PM_STRING = "pm";
    int parseCommand(String[] cmd) {
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

    int parsePackageManagerCmd(String[] cmd) {
        String second = cmd[1];
        if (second.equals("install")) {
            return PM_INSTALL;
        }
        return 0;
    }

    int parseControllerManagerCmd(String[] cmd) {
        return 0;
    }


}
