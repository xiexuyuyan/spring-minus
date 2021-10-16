package droid.server;

import droid.app.Controller;
import droid.content.*;
import droid.message.Handler;
import droid.message.Looper;
import droid.message.Message;
import cat.handler.ServletHandler;
import droid.server.cm.ControllerManager;
import droid.server.cm.ControllerManagerService;
import droid.server.pm.PackageManagerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ContextThread extends Thread{
    Looper mLooper;
    H mH;

    final boolean isSystem;

    ContextImpl mSystemContext;
    ContextImpl mContext;

    public ContextThread(boolean system, String name) {
        super(name);
        isSystem = system;
    }
    public ContextThread (boolean system) {
        isSystem = system;
    }

    public Handler getHandler() {
        return mH;
    }

    @Override
    public void run() {
        Looper.prepare();

        mLooper = Looper.myLooper();
        mH = new H();

        attach(isSystem);

        Looper.loop();

        try {
            throw new Exception("ContextThread looper stop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void attach(boolean system) {
        if (system) {
            System.out.println("Thread[" + Thread.currentThread().getName() + "]:, "
                    + "system = " + system);
            System.out.println("In the original design, " + "\n" +
                    "    we are supposed to launch some system app to run sth, " + "\n" +
                    "    but at this point[just debug] we don't need to handle something.");
        } else {
            System.out.println("Thread[" + Thread.currentThread().getName() + "]:, "
                    + "system = " + system);
        }
    }

    final class H extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ControllerManager.EXEC_CONTROLLER) {
                Intent intent = (Intent) msg.obj;
                System.out.println("Thread[" + currentThread().getName() + "]: handleMessage(): EXEC_CONTROLLER");
                handleExecController(intent);
            }
        }
    }

    private void handleExecController(Intent intent) {
        try {
            System.out.println("currentThread().getName() = " + currentThread().getName());
            ClassLoader classLoader = currentThread().getContextClassLoader();
            Class<?> clz_Controller = classLoader.loadClass(intent.getComponent().getClassName());
            Controller controller = (Controller) clz_Controller.newInstance();

            Action action = intent.getAction();
            String methodName = action.getName();
            Parameter[] parameters = action.getParameters();
            Argument[] arguments = action.getArguments();

            Class<?>[] paramsClzs = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                paramsClzs[i] = parameters[i].getTypeClz();
            }
            Method method = clz_Controller.getDeclaredMethod(methodName, paramsClzs);

            Object[] argv = new Object[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                argv[i] = arguments[i].getValue();
            }
            method.invoke(controller, argv);

        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}



























