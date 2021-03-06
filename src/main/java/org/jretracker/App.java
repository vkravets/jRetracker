package org.jretracker;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Main application class.
 */
public class App {

    private final int port;
    private final static Tracker tracker = new Tracker(ConfigurationManager.getInterval(), ConfigurationManager.getMinInterval(), ConfigurationManager.getCountCompleteForDownload());
    private final static Logger logger = Logger.getLogger(App.class.getName());

    private App(int port) {
        this.port = port;
    }

    void run() {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        bootstrap.setOption("keepAlive", false);
        bootstrap.setPipelineFactory(new RetrackerServerPipelineFactory());
        bootstrap.bind(new InetSocketAddress(port));
        logger.info("Server listening on port: " + port);

    }

    public static void main(String[] args) throws Exception {
        new App(ConfigurationManager.getPort()).run();
    }

    public static Tracker getTracker() {
        return tracker;
    }

}
