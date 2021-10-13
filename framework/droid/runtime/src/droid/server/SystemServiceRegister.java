package droid.server;

import droid.content.Context;
import droid.content.ContextImpl;
import droid.server.cat.CatManager;
import droid.server.clzm.ClassloaderManager;
import droid.server.cm.ControllerManager;
import droid.server.pm.InstallManager;
import droid.server.pm.PackageManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class SystemServiceRegister {
    private static int sServiceCacheSize;
    private static final Map<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS =
            new ConcurrentHashMap<String, ServiceFetcher<?>>();

    private SystemServiceRegister(){ }

    static {
        System.out.println("static block in SystemServiceRegister.");
        registerService(Context.PACKAGE_SERVICE, new CacheServiceFetcher<PackageManager>() {
            @Override
            public PackageManager createService(ContextImpl context) {
                try {
                    Class<?> clz = Class.forName("droid.server.pm.PackageManagerService");
                    Constructor<?> constructor = clz.getDeclaredConstructor(Context.class);
                    constructor.setAccessible(true);
                    return (PackageManager) constructor.newInstance(context);
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
                    Class<?> clz = Class.forName("droid.server.cm.ControllerManagerService");
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
                    Class<?> clz = Class.forName("droid.server.pm.InstallManagerService");
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
                    Class<?> clz = Class.forName("droid.server.clzm.ClassloaderManagerService");
                    return (ClassloaderManager) clz.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        registerService(Context.TOMCAT_SERVICE, new CacheServiceFetcher<CatManager>() {
            @Override
            public CatManager createService(ContextImpl context) {
                try {
                    Class<?> clz = Class.forName("droid.server.cat.CatManagerService");
                    Constructor<?> constructor = clz.getDeclaredConstructor(Context.class);
                    constructor.setAccessible(true);
                    return (CatManager) constructor.newInstance(context);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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
