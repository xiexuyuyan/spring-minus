package droid.content;

public class ContextWrapper extends Context{
    Context mBase;

    public ContextWrapper() {
    }

    public ContextWrapper(Context context) {
        this.mBase = context;
    }

    public Context getBaseContext() {
        return mBase;
    }

    public void attachBaseContext(Context context) {
        mBase = context;
    }

    @Override
    public Object getSystemService(String name) {
        return mBase.getSystemService(name);
    }

    @Override
    public void executeController(Intent intent) {
        mBase.executeController(intent);
    }
}
