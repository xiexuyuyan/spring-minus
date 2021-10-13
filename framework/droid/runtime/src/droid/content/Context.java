package droid.content;

public abstract class Context {
    public static final String WINDOW_SERVICE = "window";
    public static final String PACKAGE_SERVICE = "package";
    public static final String CONTROLLER_SERVICE = "controller";
    public static final String INSTALL_SERVICE = "install";
    public static final String CLASSLOADER_SERVICE = "classloader";
    public static final String TOMCAT_SERVICE = "tomcat";

    public abstract Object getSystemService(String name);
}
