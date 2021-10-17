package droid.server;

import cat.server.TomcatServer;
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
    ContextImpl mSystemContext;
    TomcatServer tomcatServer;

    static {
        System.out.println(System.getProperty("java.library.path"));
    }

    public static void main(String[] args) {
        new SystemServer().run();
    }

    private void run() {
        Looper.prepareMainLooper();

        mH = new H();

        mSystemContext = ContextImpl.createSystemContext();
        startCoreService();
        try {
            startOtherService();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Looper.loop();
        try {
            throw new Exception("ContextThread looper stop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCoreService(){
        tomcatServer = new TomcatServer();
        tomcatServer.startServer();

        new NativeHandlerThread("native_handler"
                , "memory.droid", "semaphore.droid").start();
    }

    private void startOtherService() throws IOException, ClassNotFoundException {
        CatManagerService catManager =
                (CatManagerService) mSystemContext.getSystemService(Context.TOMCAT_SERVICE);
        catManager.attachTomcat(tomcatServer);

        // todo("Load the APPs through configuration")
        String projectDir = System.getProperty("user.dir");
        String[] jarFilePaths = new String[]{
                projectDir + "/out/app/com.yuyan.harp/harp-0.0.1-alpha.jar",
        };

        Installer.run(mSystemContext, jarFilePaths);
    }


    private H mH;
    public static final int NATIVE_MESSAGE = 10000;

    public H getMainHandler() {
        return mH;
    }

    final class H extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String threadName = Thread.currentThread().getName();
            System.out.println("Thread[" + threadName + "]: handleMessage():msg.what = " + msg.what + ", msg.obj = " + msg.obj);
            if (msg.what == NATIVE_MESSAGE) {
                CommandParser.run(mSystemContext, (String) msg.obj);
            }
        }
    }

    static {
        System.loadLibrary("SystemServer");
    }
    native void waitNativeMessage(Handler handler, String memory, String semaphore);

    final class NativeHandlerThread extends Thread {
        final Handler handler;
        final String memory;
        final String semaphore;

        public NativeHandlerThread(String name, String memory, String semaphore) {
            super(name);
            this.memory = memory;
            this.semaphore = semaphore;
            handler = getMainHandler();
        }

        @Override
        public void run() {
            waitNativeMessage(handler, memory, semaphore);
        }
    }

    static class Installer {
        // TODO("安装jar包考虑重复安装的问题！替换或者阻止重复安装")
        static void run(Context context, String[] jarFilePaths) {
            try {
                _run(context, jarFilePaths);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private static void _run(Context context, String[] jarFilePaths) throws IOException, ClassNotFoundException {
            InstallManagerService ims =
                    (InstallManagerService) context.getSystemService(Context.INSTALL_SERVICE);
            PackageInfo[] packageInfoList = ims.load(jarFilePaths);

            PackageManagerService pms =
                    (PackageManagerService) context.getSystemService(Context.PACKAGE_SERVICE);
            ClassloaderManagerService clzMS =
                    (ClassloaderManagerService) context.getSystemService(Context.CLASSLOADER_SERVICE);
            CatManagerService catManager =
                    (CatManagerService) context.getSystemService(Context.TOMCAT_SERVICE);
            for (PackageInfo packageInfo : packageInfoList) {
                ClassLoader classloader = pms.install(packageInfo);
                catManager.registerServletHandler(packageInfo);
                clzMS.addClassloader(packageInfo.getPkgName(), classloader);
            }
        }
    }


}
