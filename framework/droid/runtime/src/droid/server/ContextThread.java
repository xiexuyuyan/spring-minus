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
            }


        }
    }

    final class H extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String threadName = currentThread().getName();
            switch (msg.what) {
                case ControllerManager.EXEC_CONTROLLER_CREATE:
                    String classNameCreate = (String) msg.obj;
                    System.out.println("Thread[" + threadName + "]: handleMessage(): EXEC_CONTROLLER_CREATE" +
                            ", class name = " + classNameCreate);
                    handleExecControllerCreate(classNameCreate);
                    break;
                case ControllerManager.EXEC_CONTROLLER_START:
                    String classNameStart = (String) msg.obj;
                    System.out.println("Thread[" + threadName + "]: handleMessage(): EXEC_CONTROLLER_START" +
                            ", class name = " + classNameStart);
                    handleExecControllerStart(classNameStart);
                    break;
                case ControllerManager.EXEC_CONTROLLER:
                    Intent intent = (Intent) msg.obj;
                    System.out.println("Thread[" + currentThread().getName() + "]: handleMessage(): EXEC_CONTROLLER");
                    handleExecController(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleExecControllerStart(String classNameStart) {
        try {
            ClassLoader classLoader = currentThread().getContextClassLoader();
            Class<?> clz_Controller = classLoader.loadClass(classNameStart);
            Controller controller = (Controller) clz_Controller.newInstance();
            Method method_onCreate = clz_Controller.getDeclaredMethod("onCreate");
            Method method_onStart = clz_Controller.getDeclaredMethod("onStart");
            method_onCreate.invoke(controller);
            method_onStart.invoke(controller);

        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void handleExecControllerCreate(String classNameCreate) {
        try {
            ClassLoader classLoader = currentThread().getContextClassLoader();
            Class<?> clz_Controller = classLoader.loadClass(classNameCreate);
            Controller controller = (Controller) clz_Controller.newInstance();
            Method method_onCreate = clz_Controller.getDeclaredMethod("onCreate");
            method_onCreate.invoke(controller);

        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Application makeApplication(Context appContext, String applicationClassName) {
        Application application = null;
        try {
            ClassLoader classLoader = currentThread().getContextClassLoader();
            Class<?> clz_Application = classLoader.loadClass(applicationClassName);
            application = (Application) clz_Application.newInstance();
            application.attachBaseContext(appContext);

            Method method = clz_Application.getDeclaredMethod("onCreate");
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

    private void handleExecController(Intent intent) {
        try {
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