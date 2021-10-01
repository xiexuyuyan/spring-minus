package droid.content;

public class WebIntent extends Intent{
    private String pkgName;

    public WebIntent(String pkgName) {
        this.pkgName = pkgName;
    }
    public String getPkgName() {
        return pkgName;
    }

    public WebIntent setPkgName(String pkgName) {
        this.pkgName = pkgName;
        return this;
    }
}
