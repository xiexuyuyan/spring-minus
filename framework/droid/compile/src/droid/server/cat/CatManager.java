package droid.server.cat;

import java.util.List;

public abstract class CatManager {
    public abstract List<ServletHandler> getHandlers();
    public abstract void registerServletHandler(ServletHandler handler);
}
