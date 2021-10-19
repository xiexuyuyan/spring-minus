package droid.server.cm;

import droid.content.Intent;
import droid.message.Handler;
import droid.message.Message;
import droid.server.ContextThread;

public class ControllerManagerService extends ControllerManager {
    @Override
    public void executeOnCreate(ContextThread thread, Intent intent) {
        Handler handler = thread.getHandler();
        Message message = new Message();
        message.what = EXEC_CONTROLLER_CREATE;
        message.obj = intent;
        System.out.println("Thread[" + Thread.currentThread().getName() + "]:sendMessage() " + message.what);
        handler.sendMessage(message);
    }

    @Override
    public void executeOnStart(ContextThread thread, Intent intent) {
        Handler handler = thread.getHandler();
        Message message = new Message();
        message.what = EXEC_CONTROLLER_START;
        message.obj = intent;
        System.out.println("Thread[" + Thread.currentThread().getName() + "]:sendMessage() " + message.what);
        handler.sendMessage(message);
    }

    @Override
    public void executeController(ContextThread thread, Intent intent) {
        Handler handler = thread.getHandler();
        Message message = new Message();
        message.what = EXEC_CONTROLLER;
        message.obj = intent;
        System.out.println("Thread[" + Thread.currentThread().getName() + "]:sendMessage() " + message.what);
        handler.sendMessage(message);
    }
}
