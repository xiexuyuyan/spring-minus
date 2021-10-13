package droid.server.clzm;

public abstract class ClassloaderManager {
    public abstract void addClassloader(String pkgName, ClassLoader classLoader);
    public abstract ClassLoader getClassloader(String pkgName);
}
