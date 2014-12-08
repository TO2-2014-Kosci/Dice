package pl.edu.agh.to2.webgui;

import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.server.Server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Maciej on 2014-12-08.
 */
public class ContextListener implements ServletContextListener {
    public static Server server;
    public static LocalConnectionProxy lcp;
    public MessageListener listener;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Creating server..");
        server = new Server();
        listener = new MessageListener();
        lcp = new LocalConnectionProxy(server, listener);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
