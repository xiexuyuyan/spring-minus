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

    public Controller getController(ComponentName target) {
        for (ComponentName key : controllerMap.keySet()) {
            if (key.formatFullClassName().equals(target.formatFullClassName())) {
                target = key;
            }
        }

        Controller controller = controllerMap.get(target);
        if (controller != null) {
            return controller;
        }
        controller = makeController(target);

        ComponentName s = new ComponentName(target.getPackageName(), target.getClassName());
        controllerMap.put(s, controller);

        return controller;
    }

    private Controller makeController(ComponentName componentName) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> clz_Controller = classLoader.loadClass(componentName.formatFullClassName());
            return (Controller) clz_Controller.newInstance();
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static class Lifecycle {
        public static void onCreate(Controller controller) {
            try {
                Class<?> clz_Controller = controller.getClass();
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
                Class<?> clz_Controller = controller.getClass();
                Method method_onStart = clz_Controller.getDeclaredMethod("onStart");
                method_onStart.invoke(controller);
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        public static void exec(Intent intent) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
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
}
