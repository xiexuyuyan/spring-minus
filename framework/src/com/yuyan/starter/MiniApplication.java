package com.yuyan.starter;

import com.yuyan.beans.BeanFactory;
import com.yuyan.core.ClassScanner;
import com.yuyan.web.handler.HandlerManager;
import com.yuyan.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.io.IOException;
import java.util.List;

public class MiniApplication {
    /**
     * @param cls app's project structure
     *            usual framework need app's p-j to read its info
     * @param args app's start args
     * */
    public static void run(Class<?> cls, String[] args) {
        System.out.println("hello mini-spring!");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            BeanFactory.initBean(classList);
            HandlerManager.resolveMappingHandler(classList);
            classList.forEach(it-> System.out.println(it.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
