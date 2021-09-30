package droid.app;

import droid.content.Context;

public class ContextThread {
    Context context;

    public ContextThread() {
        context = new ContextImpl();
    }

    public Context getContext() {
        return context;
    }
}
