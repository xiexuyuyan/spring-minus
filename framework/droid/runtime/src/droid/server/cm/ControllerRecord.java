package droid.server.cm;

import droid.app.Controller;
import droid.content.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * maintain Controllers */
public class ControllerRecord {
    Map<ComponentName, Controller> controllerMap = new ConcurrentHashMap<>();

    public ControllerRecord(Context context) {
        appContext = context;
    }

    Context appContext;

    public Controller getController(ComponentName target) {
        for (ComponentName key : controllerMap.keySet()) {
            if (key.formatFullClassName().equals(target.formatFullClassName())) {
                target = key;
            }
        }

        Controller controller = controllerMap.get(target);
        if (controller != null) {
            controller.onStart();
            return controller;
        }
        controller = makeController(target);

        ComponentName s = new ComponentName(target.getPackageName(), target.getClassName());
        controllerMap.put(s, controller);

        assert controller != null;
        controller.onCreate();
        controller.onStart();
        return controller;
    }

    private Controller makeController(ComponentName componentName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> clz_Controller = classLoader.loadClass(componentName.formatFullClassName());

            Controller controller = (Controller) clz_Controller.newInstance();

            Class<?> clz_ControllerParent = Controller.class;
            Method method_attach = clz_ControllerParent.getDeclaredMethod("attachBaseContext", Context.class);
            method_attach.setAccessible(true);
            method_attach.invoke(controller, appContext);

            return controller;
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static class Lifecycle {
        public static void onCreate(Controller controller) {
            try {
                Class<?> clz_Controller = Controller.class;
                Method method_onCreate = clz_Controller.getDeclaredMethod("onCreate");
                method_onCreate.invoke(controller);
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        public static void onStart(Controller controller) {
            try {
                Class<?> clz_Controller = Controller.class;
                Method method_onStart = clz_Controller.getDeclaredMethod("onStart");
                method_onStart.invoke(controller);
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        public static void exec(Context context, Controller controller, Intent intent) {
            try {
                Class<?> clz_Controller = controller.getClass();
                Action action = intent.getAction();
                String methodName = action.getName();
                Parameter[] parameters = action.getParameters();
                Argument[] arguments = action.getArguments();

                Class<?> clz_ControllerParent = Controller.class;
                Method method_attach = clz_ControllerParent.getDeclaredMethod("attach", Context.class, Intent.class);
                method_attach.setAccessible(true);
                method_attach.invoke(controller, context, intent);

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

            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
