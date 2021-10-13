package droid.content;

import droid.server.ContextThread;

public class AppBindData {
    ContextThread contextThread;
    PackageInfo packageInfo;

    public ContextThread getContextThread() {
        return contextThread;
    }

    public void setContextThread(ContextThread contextThread) {
        this.contextThread = contextThread;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }
}
