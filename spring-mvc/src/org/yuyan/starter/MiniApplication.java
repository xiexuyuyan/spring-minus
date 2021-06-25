package org.yuyan.starter;

import org.yuyan.beans.BeanFactory;
import org.yuyan.core.ClassScanner;
import org.yuyan.web.handler.HandlerManager;
import org.yuyan.web.server.TomcatServer;

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
