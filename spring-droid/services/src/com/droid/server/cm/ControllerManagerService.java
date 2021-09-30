package com.droid.server.cm;

import com.droid.server.clz.ClassloaderManagerService;
import com.droid.server.pm.PackageManagerService;
import droid.app.ContextThread;
import droid.app.Controller;
import droid.content.*;
import droid.content.cm.ControllerManager;
import droid.content.pm.ControllerInfo;
import droid.content.pm.PackageInfo;
import droid.content.pm.PackageManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ControllerManagerService extends ControllerManager {
    private Context mContext;

    public ControllerManagerService() {
        System.out.println("ControllerManagerService constructor init.");
    }

    @Override
    public void executeController(Intent intent) {
        PackageManagerService pms =
                (PackageManagerService) mContext.getSystemService(Context.PACKAGE_SERVICE);
        ClassloaderManagerService clzms =
                (ClassloaderManagerService) mContext.getSystemService(Context.CLASSLOADER_SERVICE);

        ControllerInfo controllerInfo = null;

        ComponentName componentName = intent.getComponent();
        if (componentName != null) {
            controllerInfo = pms.resolveComponentName(componentName);
        }

        Action action = intent.getAction();
        if (controllerInfo == null) {
            if (action != null) {
                controllerInfo = pms.resolveAction(action);
            }
        }

        if (controllerInfo == null) {
            System.out.println("controllerInfo = null");
            return;
        }

        ClassLoader classLoader = clzms.chooseBestClassloader(new PackageInfo(
                controllerInfo.getPackageName()
        ));
        if (classLoader == null) {
            System.out.println("classLoader = null");
            return;
        }

        Intent i = new Intent();

        componentName = new ComponentName(controllerInfo.getPackageName()
                , controllerInfo.getClassName());
        i.setComponent(componentName);


        if (action != null) {
            Parameter[] parameters = controllerInfo.completeAction(action.getArguments());
            action.setParameters(parameters);
        }
        i.setAction(action);

        executeController(classLoader, i);
    }

    private void executeController(ClassLoader classLoader, Intent intent) {
        String pkgName = intent.getComponent().getPackageName();
        String clsName = intent.getComponent().getClassName();
        if (!clsName.startsWith(pkgName)) {
            if (!clsName.startsWith(".")) {
                clsName = "." + clsName;
            }
            clsName = pkgName + clsName;
        }
        try {
            Class<?> controllerClz = classLoader.loadClass(clsName);
            Controller controller = (Controller) controllerClz.newInstance();

            Class<?> baseControllerClz = Controller.class;
            Method attachMethod = baseControllerClz.getDeclaredMethod("attach", Context.class, Intent.class);
            attachMethod.setAccessible(true);
            attachMethod.invoke(controller, mContext, intent);

            Action action = intent.getAction();
            Method method = null;
            if (action == null) {
                method = baseControllerClz.getMethod("onCreate");
                method.invoke(controller);
            } else {
                String methodName = action.getName();
                Parameter[] parameters = action.getParameters();
                Argument[] arguments = action.getArguments();

                Class<?>[] paramsClzs = new Class[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    paramsClzs[i] = parameters[i].getTypeClz();
                }
                method = controllerClz.getDeclaredMethod(methodName, paramsClzs);

                Object[] argv = new Object[arguments.length];
                for (int i = 0; i < arguments.length; i++) {
                    argv[i] = arguments[i].getValue();
                }
                method.invoke(controller, argv);
            }
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void systemReady(ContextThread systemContextThread) {
        mContext = systemContextThread.getContext();
    }
}
