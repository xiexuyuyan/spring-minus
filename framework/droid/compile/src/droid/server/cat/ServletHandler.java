package droid.server.cat;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class ServletHandler {
    public String pkgName = null;

    public ServletHandler(String _pkgName) {
        this.pkgName = _pkgName;
    }

    public abstract boolean handle(ServletRequest req, ServletResponse res);

    public static String parsePackageName(){
        return null;
    }
}
