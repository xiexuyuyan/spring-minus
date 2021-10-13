package droid.server.clzm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassloaderManagerService extends ClassloaderManager {
    Map<String, ClassLoader> classLoaderList = new ConcurrentHashMap<>();

    @Override
    public void addClassloader(String pkgName, ClassLoader classLoader) {
        classLoaderList.put(pkgName, classLoader);
    }

    @Override
    public ClassLoader getClassloader(String pkgName) {
        return classLoaderList.get(pkgName);
    }
}
