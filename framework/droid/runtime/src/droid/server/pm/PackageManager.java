package droid.server.pm;

import droid.content.Context;
import droid.content.PackageInfo;
import droid.server.ContextThread;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public abstract class PackageManager {
    final Context context;

    PackageManager(Context context) {
        this.context = context;
    }

    public abstract ClassLoader install(PackageInfo packageInfo) throws MalformedURLException, ClassNotFoundException;
}
