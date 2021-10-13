package droid.content;

import java.util.Objects;

public class PackageInfo {
    public static final String JAR_URL_PREFIX = "jar:file:/";
    public static final String JAR_URL_SUFFIX = "!/";
    public static final String JAR_MANIFEST = "Manifest.xml";

    private String pkgName;
    private String pkgPath;

    private ControllerInfo[] controllers;

    private JarPackage jarPackage;

    public JarPackage getJarPackage() {
        return jarPackage;
    }

    public void setJarPackage(JarPackage jarPackage) {
        this.jarPackage = jarPackage;
    }

    public void setControllers(ControllerInfo[] controllers) {
        this.controllers = controllers;
    }

    public ControllerInfo[] getControllers() {
        return controllers;
    }

    public PackageInfo() {
    }

    public PackageInfo(String pkgName) {
        this.pkgName = pkgName;
    }

    public PackageInfo(String pkgName, String pkgPath) {
        this.pkgName = pkgName;
        this.pkgPath = pkgPath;
    }

    public String getPkgPath() {
        return pkgPath;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public void setPkgPath(String pkgPath) {
        this.pkgPath = pkgPath;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getPkgJarUrlPath() {
        return JAR_URL_PREFIX + pkgPath + JAR_URL_SUFFIX;
    }

    public ControllerInfo chooseBestController(Intent intent){
        Action action = intent.getAction();
        for (ControllerInfo controller : controllers) {
            for (Action controllerAction : controller.getActions()) {
                if (controllerAction.equalsByArguments(
                        action.getUrl()
                        , action.getName()
                        , action.getArguments())) {
                    return controller;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (controllers == null) {
            return "pkgName = " + pkgName
                    + ", pkgPath = " + pkgPath
                    + ", controllers = null.";
        }
        StringBuilder controllersString = new StringBuilder();
        for (int i = 0; i < controllers.length; i++) {
            controllersString.append(i);
            controllersString.append(". ");
            controllersString.append(controllers[i]);
            controllersString.append(" ");
        }
        return "pkgName = " + pkgName
                + ", pkgPath = " + pkgPath
                + ", controllers = [" + controllersString + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageInfo that = (PackageInfo) o;
        return Objects.equals(pkgName, that.pkgName) && Objects.equals(pkgPath, that.pkgPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkgName, pkgPath);
    }
}
