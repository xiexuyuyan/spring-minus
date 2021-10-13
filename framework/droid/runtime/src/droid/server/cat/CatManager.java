package droid.server.cat;

import cat.handler.ServletHandler;
import droid.content.Context;
import droid.content.PackageInfo;

import java.util.List;

public abstract class CatManager {
    Context context;
    public CatManager(Context context) {
        this.context = context;
    }

    public abstract List<ServletHandler> getHandlers();
    public abstract void registerServletHandler(PackageInfo packageInfo);
}
