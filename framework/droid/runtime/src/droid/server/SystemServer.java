package droid.server;

import cat.handler.ServletHandler;
import cat.server.TomcatServer;
import droid.content.AppBindData;
import droid.content.Context;
import droid.content.ContextImpl;
import droid.content.PackageInfo;
import droid.message.Handler;
import droid.message.Looper;
import droid.message.Message;
import droid.server.cat.CatManagerService;
import droid.server.clzm.ClassloaderManagerService;
import droid.server.pm.InstallManagerService;
import droid.server.pm.PackageManagerService;

import java.io.IOException;

public class SystemServer {
    final ContextImpl mSystemContext;
    TomcatServer tomcatServer;

    public static void main(String[] args) {
        new SystemServer();
    }

    public SystemServer(){
        mSystemContext = ContextImpl.createSystemContext();

        run();
    }

    private void run() {
        startCoreService();
        try {
            startOtherService();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startCoreService(){
        tomcatServer = new TomcatServer();
        tomcatServer.startServer();
    }

    private void startOtherService() throws IOException, ClassNotFoundException {
        CatManagerService catManager =
                (CatManagerService) mSystemContext.getSystemService(Context.TOMCAT_SERVICE);
        catManager.attachTomcat(tomcatServer);

        // todo("Load the APPs through configuration")
        String[] jarFilePaths = new String[]{
                "/out/app/com.yuyan.harp/harp-0.0.1-alpha.jar"
        };

        InstallManagerService ims =
                (InstallManagerService) mSystemContext.getSystemService(Context.INSTALL_SERVICE);
        PackageInfo[] packageInfoList = ims.load(jarFilePaths);

        PackageManagerService pms =
                (PackageManagerService) mSystemContext.getSystemService(Context.PACKAGE_SERVICE);
        ClassloaderManagerService clzMS =
                (ClassloaderManagerService) mSystemContext.getSystemService(Context.CLASSLOADER_SERVICE);
        for (PackageInfo packageInfo : packageInfoList) {
            ClassLoader classloader = pms.install(packageInfo);
            catManager.registerServletHandler(packageInfo);
            clzMS.addClassloader(packageInfo.getPkgName(), classloader);
        }

        for (PackageInfo packageInfo : packageInfoList) {
            System.out.println("packageInfo = " + packageInfo);
            System.out.println("packageInfo.getJarPackage() = " + packageInfo.getJarPackage());
        }
    }

}
