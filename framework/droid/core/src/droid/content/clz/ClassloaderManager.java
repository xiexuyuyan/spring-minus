package droid.content.clz;

public abstract class ClassloaderManager {
    public abstract void addClassloader(ClassLoader classLoader);
    public abstract ClassLoader getDefaultClassloader();
}
