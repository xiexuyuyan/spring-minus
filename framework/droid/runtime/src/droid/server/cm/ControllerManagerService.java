package droid.server.cm;

import droid.content.Intent;
import droid.message.Handler;
import droid.message.Message;
import droid.server.ContextThread;

public class ControllerManagerService extends ControllerManager {
    @Override
    public void executeController(ContextThread thread, Intent intent) {
        Handler handler = thread.getHandler();
        Message message = new Message();
        message.what = EXEC_CONTROLLER;
        message.obj = intent;
        handler.sendMessage(message);
    }
}
