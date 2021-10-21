package droid.server;

import droid.app.Application;
import droid.app.Controller;
import droid.content.*;
import droid.message.Handler;
import droid.message.Looper;
import droid.message.Message;
import cat.handler.ServletHandler;
import droid.server.cm.ControllerManager;
import droid.server.cm.ControllerManagerService;
import droid.server.cm.ControllerRecord;
import droid.server.pm.PackageManager;
import droid.server.pm.PackageManagerService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ContextThread extends Thread{
    Looper mLooper;
    H mH;

    final boolean isSystem;

    Context mSystemContext;
    Context mContext;// app context

    ControllerRecord controllerRecord;

    Application mApplication;

    public ContextThread(boolean system, String name) {
        super(name);
        isSystem = system;
    }
    public ContextThread (boolean system) {
        isSystem = system;
    }

    public void attachBaseContext(Context systemContext) {
        mSystemContext = systemContext;
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
            if (mSystemContext != null) {
                PackageManager pm =
                        (PackageManager) mSystemContext.getSystemService(Context.PACKAGE_SERVICE);
                PackageInfo packageInfo = pm.getPackageInfo(getName());
                mContext = ContextImpl.createAppContext(mSystemContext, this, packageInfo);
                String applicationClassName = packageInfo.getApplicationClassName();
                if (applicationClassName != null) {
                    mApplication = makeApplication(mContext, applicationClassName);
                }
                controllerRecord = new ControllerRecord(mContext);
            }


        }
    }

    final class H extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String threadName = currentThread().getName();
            System.out.println("Thread[" + threadName + "]: handleMessage(): " + msg.what);
            Intent intent;
            switch (msg.what) {
                case ControllerManager.EXEC_CONTROLLER_CREATE:
                    intent = (Intent) msg.obj;
                    handleExecControllerCreate(intent);
                    break;
                case ControllerManager.EXEC_CONTROLLER_START:
                    intent = (Intent) msg.obj;
                    handleExecControllerStart(intent);
                    break;
                case ControllerManager.EXEC_CONTROLLER:
                    intent = (Intent) msg.obj;
                    System.out.println("Thread[" + currentThread().getName() + "]: handleMessage(): EXEC_CONTROLLER");
                    handleExecController(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleExecControllerStart(Intent intent) {
        Controller controller = controllerRecord.getController(intent.getComponent());
        ControllerRecord.Lifecycle.onStart(controller);
    }

    private void handleExecControllerCreate(Intent intent) {
        Controller controller = controllerRecord.getController(intent.getComponent());
        ControllerRecord.Lifecycle.onCreate(controller);
    }

    private void handleExecController(Intent intent) {
        Controller controller = controllerRecord.getController(intent.getComponent());
        ControllerRecord.Lifecycle.exec(mContext, controller, intent);
    }

    private Application makeApplication(Context appContext, String applicationClassName) {
        Application application = null;
        try {
            ClassLoader classLoader = currentThread().getContextClassLoader();
            Class<?> clz_Application = classLoader.loadClass(applicationClassName);
            application = (Application) clz_Application.newInstance();
            application.attachBaseContext(appContext);

            Class<?> clz_Application_P = Application.class;
            Method method = clz_Application_P.getDeclaredMethod("onCreate");
            method.invoke(application);

        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return application;
    }

}