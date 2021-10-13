package droid.server.pm;

import droid.content.Context;
import droid.content.PackageInfo;
import droid.server.ContextThread;

import java.util.List;
import java.util.Map;

public abstract class PackageManager {
    ContextThread contextThread;
    Context context;

    public Context getContext() {
        return context;
    }

    public ContextThread getContextThread() {
        return contextThread;
    }

    public abstract List<PackageInfo> getAllPackages();
    public abstract void updatePackages(Map<ClassLoader, List<PackageInfo>> packageLinkedPackagesList);
}
