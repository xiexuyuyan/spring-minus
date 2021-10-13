package droid.server.cm;

import droid.content.Intent;
import droid.server.ContextThread;

public abstract class ControllerManager {
    public abstract void executeController(ContextThread thread, Intent intent);
}
