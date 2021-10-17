package droid.app;

import droid.content.Context;

public class Application extends Context {
    Context base;


    public void onCreate(){
        System.out.println("Application super");
    }

    public void attachBaseContext(Context context) {
        base = context;
    }

    @Override
    public Object getSystemService(String name) {
        return base.getSystemService(name);
    }
}
