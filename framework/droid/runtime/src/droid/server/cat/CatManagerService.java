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

    Argument[] wrapParams(Set<Map.Entry<String, String[]>> entries) {
        Argument[] arguments = new Argument[entries.size()];
        int i = 0;
        for (Map.Entry<String, String[]> entry : entries) {
            Argument argument = new Argument();
            argument.setName(entry.getKey());
            argument.setValue(entry.getValue()[0]);
            arguments[i++] = argument;
        }
        return arguments;
    }

    Intent formatIntent(PackageInfo packageInfo, String requestUrl, Argument[] arguments) {
        ControllerInfo tController = null;
        Action tAction = null;

        System.out.println("requestUrl = " + requestUrl);
        for (ControllerInfo controller : packageInfo.getControllers()) {
            for (Action action : controller.getActions()) {
                System.out.println("action.getUrl() = " + action.getUrl());
                if (action.getUrl().equals(requestUrl)) {
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
        tAction.setArguments(arguments);
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

            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                System.out.println("stringEntry.getKey() = " + stringEntry.getKey());
                System.out.println("stringEntry.getValue() = " + Arrays.toString(stringEntry.getValue()));
            }
            Argument[] arguments = wrapParams(parameterMap.entrySet());

            Intent intent = formatIntent(packageInfo, requestUrl, arguments);
            if (intent == null) {
                return false;
            }

            ControllerManagerService cms =
                    (ControllerManagerService) context.getSystemService(Context.CONTROLLER_SERVICE);
            cms.executeController(threadMap.get(pkgName), intent);

            return true;
        }
    }
}
