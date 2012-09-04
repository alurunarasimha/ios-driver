package org.uiautomation.ios.ide;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.uiautomation.ios.exceptions.IOSAutomationSetupException;
import org.uiautomation.ios.server.IOSServerConfiguration;

import com.beust.jcommander.JCommander;

public class IDEServer {

  private final Server server;

  public static void main(String[] args) throws Exception {

    int port = 5555;
    if (args.length == 1) {
      port = Integer.parseInt(args[0]);
    }

    IDEServer server = new IDEServer(port);
    server.start();
  }

  public void init() throws IOSAutomationSetupException {
    ServletContextHandler servletContextHandler =
        new ServletContextHandler(server, "/ide", true, false);

    servletContextHandler.addServlet(IDEServlet.class, "/*");

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          server.stop();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

  }

  public void start() throws Exception {
    init();
    if (!server.isRunning()) {
      server.start();
    }
  }


  public void stop() throws Exception {
    server.stop();
  }

  public IDEServer(int port) throws IOSAutomationSetupException {
    server = new Server(new InetSocketAddress("localhost", port));
  }

}