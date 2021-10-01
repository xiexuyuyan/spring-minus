package cat.server;

import cat.servlet.DispatchServlet;
import droid.app.Application;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer {
    private Tomcat tomcat;

    public void startServer(Application app){
        tomcat = new Tomcat();
        tomcat.setPort(6699);
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        cat.servlet.DispatchServlet dispatchServlet = new DispatchServlet(app);
        Tomcat.addServlet(context, "default_patcher", dispatchServlet).setAsyncSupported(true);
        context.addServletMappingDecoded("/", "default_patcher");
        tomcat.getHost().addChild(context);

        Thread awaitThread = new Thread("tomcat_await_thread"){
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
