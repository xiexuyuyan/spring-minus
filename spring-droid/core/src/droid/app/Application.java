package droid.app;

import droid.content.Context;
import droid.content.ContextWrapper;

public class Application extends ContextWrapper {
    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }
}
