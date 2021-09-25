package org.frame.app;

import org.frame.content.Context;
import org.frame.view.WindowManager;
import org.frame.view.WindowManagerImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final public class SystemServiceRegister {
    private static int sServiceCacheSize;
    private static final Map<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS =
            new ConcurrentHashMap<String, ServiceFetcher<?>>();

    private SystemServiceRegister(){ }

    static {
        registerService(Context.WINDOW_SERVICE, new CacheServiceFetcher<WindowManager>() {
            @Override
            public WindowManager createService(ContextImpl context) {
                return new WindowManagerImpl(context);
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
            return service;
        }

        public abstract T createService(ContextImpl context);
    }


}
