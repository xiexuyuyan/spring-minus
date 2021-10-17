package com.yuyan.harp.application;

import com.yuyan.harp.driver.ResultState;
import droid.app.Application;
import droid.content.PackageInfo;
import droid.server.pm.InstallManager;
import droid.server.pm.PackageManager;

import java.io.InputStream;

public class HarpApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InstallManager im =
                (InstallManager) getSystemService(INSTALL_SERVICE);
        PackageManager pm =
                (PackageManager) getSystemService(PACKAGE_SERVICE);
        PackageInfo packageInfo = pm.getPackageInfo(Thread.currentThread().getName());
        String jarFilePath = packageInfo.getJarPackage().getFilePath();
        String DEFAULT_PATH = "properties/web.result.code.properties";
        InputStream in = im.getResourceStream(jarFilePath, DEFAULT_PATH);
        ResultState.init(in);
    }
}
