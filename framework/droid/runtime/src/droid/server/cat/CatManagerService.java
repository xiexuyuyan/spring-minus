package droid.server.cat;

import cat.handler.ServletHandler;
import cat.server.TomcatServer;
import droid.content.*;
import droid.server.ContextThread;
import droid.server.cm.ControllerManagerService;
import droid.server.pm.PackageManagerService;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CatManagerService extends CatManager{
    private TomcatServer mTomcatServer;
    private final Map<String, ContextThread> threadMap = new ConcurrentHashMap<>();

    CatManagerService(Context context) {
        super(context);
    }

    @Override
    public List<ServletHandler> getHandlers() {
        return null;
    }

    public void attachTomcat(TomcatServer tomcatServer) {
        mTomcatServer = tomcatServer;
    }

    @Override
    public void registerServletHandler(PackageInfo packageInfo) {
        String pkgName = packageInfo.getPkgName();
        ClassLoader classLoader = packageInfo.getJarPackage().getClassLoader();
        ServletHandler sh = new SH(pkgName);
        ContextThread contextThread = new ContextThread(false, pkgName);
        contextThread.setContextClassLoader(classLoader);
        contextThread.start();
        threadMap.put(pkgName, contextThread);
        mTomcatServer.registerServletHandler(sh);
    }

    boolean checkServletHandler(String it, String target) {
        System.out.println("it = " + it);
        System.out.println("target = " + target);
        return it.equals(target);
    }


    Intent formatIntent(PackageInfo packageInfo, String requestUrl, String[] argumentNames) {
        ControllerInfo tController = null;
        Action tAction = null;

        System.out.println("requestUrl = " + requestUrl);
        for (ControllerInfo controller : packageInfo.getControllers()) {
            for (Action action : controller.getActions()) {
                System.out.println("action.getUrl() = " + action.getUrl());
                if (action.getUrl().equals(requestUrl)
                        && ParameterUtil.match(argumentNames, action.getParameters())) {
                    tAction = action;
                    tController = controller;
                    break;
                }
            }
            if (tController != null) {
                break;
            }
        }

        if (tController == null) {
            return null;
        }

        String tPkgName = tController.getPackageName();
        String tClassName = tController.getClassName();

        ComponentName tComponentName = new ComponentName(tPkgName, tClassName);
        Intent tIntent = new Intent();
        tIntent.setComponent(tComponentName);
        tIntent.setAction(tAction);

        return tIntent;
    }



    /** this class is run in system thread which dispatch servlet handler */
    class SH extends ServletHandler {
        public SH(String _pkgName) {
            super(_pkgName);
        }

        @Override
        public boolean handle(ServletRequest req, ServletResponse res) {
            // 0. check package name & get package info
            HttpServletRequest request = (HttpServletRequest) req;
            String requestUrl = request.getRequestURI();
            System.out.println("requestUrl = " + requestUrl);
            String requestPackageName = parsePackageName(requestUrl);
            if (!checkServletHandler(pkgName, requestPackageName)) {
                return false;
            }
            requestUrl = parseUrl(request.getRequestURI());

            PackageManagerService packageManagerService =
                    (PackageManagerService) context.getSystemService(Context.PACKAGE_SERVICE);
            PackageInfo packageInfo = packageManagerService.getPackageInfo(pkgName);
            System.out.println("packageInfo = " + packageInfo);

            // 1. get controller & action with parameter only
            Map<String, String[]> argumentMap = request.getParameterMap();
            String[] argumentNames = new String[argumentMap.size()];
            int i = 0;
            for (String s : argumentMap.keySet()) {
                argumentNames[i++] = s;
            }
            Intent intent = formatIntent(packageInfo, requestUrl, argumentNames);
            if (intent == null) {
                return false;
            }

            // 2. make arguments with parameter in intent.action
            Parameter[] parameters = intent.getAction().getParameters();
            Argument[] arguments = ParameterUtil.formatValues(argumentMap, parameters);
            Action action = intent.getAction();
            action.setArguments(arguments);
            intent.setAction(action);

            ControllerManagerService cms =
                    (ControllerManagerService) context.getSystemService(Context.CONTROLLER_SERVICE);
            cms.executeController(threadMap.get(pkgName), intent);
            return true;
        }
    }
}