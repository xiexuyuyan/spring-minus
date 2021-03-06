package droid.content;

import droid.server.ContextThread;
import droid.server.SystemServiceRegister;

public class ContextImpl extends Context{
    public final Object[] mServiceCache;

    Context parent;
    ContextThread contextThread;
    PackageInfo packageInfo;

    @Override
    public Object getSystemService(String name) {
        return SystemServiceRegister.getSystemService(this, name);
    }

    public static ContextImpl createSystemContext() {
        return new ContextImpl(null, null, null);
    }

    public static ContextImpl createAppContext(Context parent
            , ContextThread contextThread
            , PackageInfo packageInfo) {
        return new ContextImpl(parent, contextThread, packageInfo);
    }

    public ContextImpl(Context parent
            , ContextThread contextThread
            , PackageInfo packageInfo) {
        this.parent = parent;
        this.contextThread = contextThread;
        this.packageInfo = packageInfo;

        if (parent != null) {
            ContextImpl parentImpl = (ContextImpl) parent;
            mServiceCache = parentImpl.mServiceCache;
        } else {
            mServiceCache = SystemServiceRegister.createServiceCache();
        }
    }

}
