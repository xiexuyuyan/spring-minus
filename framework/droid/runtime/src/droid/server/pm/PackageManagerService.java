package droid.server.pm;

import droid.content.Context;
import droid.content.JarPackage;
import droid.content.PackageInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class PackageManagerService extends PackageManager {
    private final List<PackageInfo> packageInfoList = new ArrayList<>();

    public PackageInfo getPackageInfo(String pkgName) {
        for (PackageInfo packageInfo : packageInfoList) {
            if (packageInfo.getPkgName().equals(pkgName)) {
                return packageInfo;
            }
        }
        return null;
    }

    PackageManagerService(Context context) {
        super(context);
    }

    @Override
    public ClassLoader install(PackageInfo packageInfo) throws MalformedURLException, ClassNotFoundException {
        packageInfoList.add(packageInfo);

        JarPackage jarPackage = packageInfo.getJarPackage();
        String jarFilePath = jarPackage.getFilePath();
        ClassLoader classLoader = createAppClassloader(new String[]{
                jarFilePath
        });
        String[] clzNames = jarPackage.getClzNames();
        Class<?>[] clzs = new Class[clzNames.length];
        for (int i = 0; i < clzNames.length; i++) {
            String installClzName = clzNames[i];
            clzs[i] = classLoader.loadClass(installClzName);
        }
        jarPackage.setClzs(clzs);
        jarPackage.setClassLoader(classLoader);

        return classLoader;
    }

    public ClassLoader createAppClassloader(String[] jarFilePaths) throws MalformedURLException {
        URL[] appJarFileList = new URL[jarFilePaths.length];

        for (int i = 0; i < appJarFileList.length; i++) {
            String jarFilePath = jarFilePaths[i];
            String jarUrlPath = PackageInfo.JAR_URL_PREFIX + jarFilePath + PackageInfo.JAR_URL_SUFFIX;
            appJarFileList[i] = new URL(jarUrlPath);
        }

        return new URLClassLoader(appJarFileList);
    }



}
