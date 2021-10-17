package droid.server.cat;

import cat.handler.ServletHandler;
import droid.content.Context;
import droid.content.PackageInfo;
import droid.server.ContextThread;

import java.util.List;

public abstract class CatManager {
    Context context;
    public CatManager(Context context) {
        this.context = context;
    }

    public abstract List<ServletHandler> getHandlers();
    public abstract void registerServletHandler(PackageInfo packageInfo);

    // TODO: 2021/10/16 创建一个专属的管理线程的manager
    public abstract ContextThread getThread(String pkgName);
}
