package droid.app;

import droid.content.Context;
import droid.content.ContextWrapper;
import droid.content.Intent;

public abstract class Controller extends ContextWrapper {
    private Intent mIntent;

    public Controller() {}
    public void onCreate(){
        System.out.println("controller onCreate() super");
    }
    public void onStart(){
        System.out.println("controller onStart super");
    }

    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    public Intent getIntent() {
        return mIntent;
    }

    final void attach(Context context, Intent intent) {
        attachBaseContext(context);
        mIntent = intent;
    }
}
