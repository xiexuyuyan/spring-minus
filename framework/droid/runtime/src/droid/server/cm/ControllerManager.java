package droid.server.cm;

import droid.content.Intent;
import droid.server.ContextThread;

public abstract class ControllerManager {
    public static final int EXEC_CONTROLLER = 0;

    public abstract void executeController(ContextThread thread, Intent intent);
}
