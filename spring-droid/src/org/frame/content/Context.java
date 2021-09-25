package org.frame.content;


public abstract class Context {
    public static final String WINDOW_SERVICE = "window";

    public abstract Object getSystemService(String name);

}
