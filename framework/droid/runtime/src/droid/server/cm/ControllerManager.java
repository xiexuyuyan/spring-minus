package droid.server.cm;

import droid.content.Intent;
import droid.server.ContextThread;

public abstract class ControllerManager {
    public static final int EXEC_CONTROLLER = 0;
    public static final int EXEC_CONTROLLER_CREATE = 1;
    public static final int EXEC_CONTROLLER_START = 2;


    public abstract void executeController(ContextThread thread, Intent intent);
    public abstract void executeOnCreate(ContextThread thread, Intent intent);
    public abstract void executeOnStart(ContextThread thread, Intent intent);
}
