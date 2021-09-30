package droid.app;

import droid.content.Context;
import droid.content.clz.ClassloaderManager;
import droid.content.cm.ControllerManager;
import droid.content.pm.InstallManager;
import droid.content.pm.PackageManager;
import droid.view.WindowManager;
import droid.view.WindowManagerImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class SystemServiceRegister {
    private static int sServiceCacheSize;
    private static final Map<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS =
            new ConcurrentHashMap<String, ServiceFetcher<?>>();

    private SystemServiceRegister(){ }

    static {
        System.out.println("static block in SystemServiceRegister.");
        registerService(Context.WINDOW_SERVICE, new CacheServiceFetcher<WindowManager>() {
            @Override
            public WindowManager createService(ContextImpl context) {
                return new WindowManagerImpl(context);
            }
        });
        registerService(Context.PACKAGE_SERVICE, new CacheServiceFetcher<PackageManager>() {
            @Override
            public PackageManager createService(ContextImpl context) {
                try {
                    Class<?> clz = Class.forName("com.droid.server.pm.PackageManagerService");
                    Constructor<?> constructor = clz.getConstructor();
                    return (PackageManager) constructor.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        registerService(Context.CONTROLLER_SERVICE, new CacheServiceFetcher<ControllerManager>() {
            @Override
            public ControllerManager createService(ContextImpl context) {
                try {
                    Class<?> clz = Class.forName("com.droid.server.cm.ControllerManagerService");
                    return (ControllerManager) clz.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        registerService(Context.INSTALL_SERVICE, new CacheServiceFetcher<InstallManager>() {
            @Override
            public InstallManager createService(ContextImpl context) {
                try {
                    Class<?> clz = Class.forName("com.droid.server.pm.InstallManagerService");
                    return (InstallManager) clz.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        registerService(Context.CLASSLOADER_SERVICE, new CacheServiceFetcher<ClassloaderManager>() {
            @Override
            public ClassloaderManager createService(ContextImpl context) {
                try {
                    Class<?> clz = Class.forName("com.droid.server.clz.ClassloaderManagerService");
                    return (ClassloaderManager) clz.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

    }

    private static <T> void registerService(String serviceName
            , ServiceFetcher<T> fetcher) {
        SYSTEM_SERVICE_FETCHERS.put(serviceName, fetcher);
    }

    public static Object[] createServiceCache() {
        return new Object[sServiceCacheSize];
    }

    public static Object getSystemService(ContextImpl context, String serviceName) {
        ServiceFetcher<?> fetcher = SYSTEM_SERVICE_FETCHERS.get(serviceName);
        return fetcher != null ? fetcher.getService(context) : null;
    }

    interface ServiceFetcher<T> {
        T getService(ContextImpl context);
    }

    static abstract class CacheServiceFetcher<T> implements ServiceFetcher<T> {
        private final int mCacheIndex;
        CacheServiceFetcher(){
            mCacheIndex = sServiceCacheSize++;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T getService(ContextImpl context) {
            final Object[] cache = context.mServiceCache;
            T service = (T) cache[mCacheIndex];
            if (service != null) {
                return service;
            }
            service = createService(context);
            cache[mCacheIndex] = service;
            return service;
        }

        public abstract T createService(ContextImpl context);
    }


}
