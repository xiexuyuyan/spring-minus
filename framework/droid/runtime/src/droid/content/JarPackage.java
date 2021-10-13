package droid.content;

import java.util.Arrays;

import static droid.content.PackageInfo.JAR_URL_PREFIX;
import static droid.content.PackageInfo.JAR_URL_SUFFIX;

public class JarPackage {
    String pkgName;
    String filePath;
    String[] clzNames;
    Class<?>[] clzs;
    ClassLoader classLoader;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String[] getClzNames() {
        return clzNames;
    }

    public void setClzNames(String[] clzNames) {
        this.clzNames = clzNames;
    }

    public Class<?>[] getClzs() {
        return clzs;
    }

    public void setClzs(Class<?>[] clzs) {
        this.clzs = clzs;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public String toString() {
        return "JarPackage{" +
                "pkgName='" + pkgName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", clzNames=" + Arrays.toString(clzNames) +
                ", clzs=" + Arrays.toString(clzs) +
                ", classLoader=" + classLoader +
                '}';
    }
}
