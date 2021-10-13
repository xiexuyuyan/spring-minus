package com.droid.server.clz;

import droid.server.clz.ClassloaderManager;
import droid.server.pm.PackageInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassloaderManagerService extends ClassloaderManager {

    List<ClassLoader> classLoaderList = new ArrayList<>();

    public Map<ClassLoader, List<PackageInfo>> getClassloaderLinkedPackagesList() {
        return classloaderLinkedPackagesList;
    }

    Map<ClassLoader, List<PackageInfo>> classloaderLinkedPackagesList = new ConcurrentHashMap<>();

    public ClassloaderManagerService() {
        System.out.println("ClassloaderManagerService constructor init.");
    }

    public void addClassloader(ClassLoader classLoader) {
        classLoaderList.add(classLoader);
    }

    @Override
    public ClassLoader getDefaultClassloader() {
        return classLoaderList.get(0);
    }


    public void linkPackageToClassloader(ClassLoader classLoader, List<PackageInfo> packageInfoList) {
        classloaderLinkedPackagesList.put(classLoader, packageInfoList);
    }

    public ClassLoader createSystemAppClassloader(List<PackageInfo> mPackages) {
        URL[] appJarFileList = new URL[mPackages.size()];

        for (int i = 0; i < appJarFileList.length; i++) {
            String jarUrlPath = mPackages.get(i).getPkgJarUrlPath();
            try {
                appJarFileList[i] = new URL(jarUrlPath);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return new URLClassLoader(appJarFileList);
    }

    public ClassLoader chooseBestClassloader(PackageInfo packageInfo) {
        for (Map.Entry<ClassLoader, List<PackageInfo>> entry : classloaderLinkedPackagesList.entrySet()) {
            for (PackageInfo info : entry.getValue()) {
                if (info.equals(packageInfo)) {
                    return entry.getKey();
                }
                // todo("Fuzzy matching. Is it correct to return detailed package information
                //  if it only matches simple package information?")
                if (info.getPkgName().equals(packageInfo.getPkgName())) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}
