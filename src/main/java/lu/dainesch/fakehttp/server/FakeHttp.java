package lu.dainesch.fakehttp.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import lu.dainesch.fakehttp.FakeHttpConfig;
import org.glassfish.grizzly.CompletionHandler;
import org.glassfish.grizzly.GrizzlyFuture;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class FakeHttp {

    private static final Logger LOG = LoggerFactory.getLogger(FakeHttp.class);

    private final FakeHttpConfig config;
    private final CommListener commListener;
    private HttpServer server;

    public FakeHttp(FakeHttpConfig config) {
        this.config = config;
        this.commListener = new CommListener(config);
        this.commListener.init();

        if (config.getStarted().get()) {
            startServer();
        }
    }

    public void startServer() {
        if (server == null) {
            this.server = HttpServer.createSimpleServer();
        }

        if (server.isStarted()) {
            config.getStarted().set(true);
            return;
        }

        // Config
        //ServerConfiguration serverConf = server.getServerConfiguration();
        
        // remove all listeners
        new ArrayList<>(server.getListeners()).stream().forEach(l -> server.removeListener(l.getName()));

        // network
        NetworkListener listener = new NetworkListener("DefaultListener", "0.0.0.0", config.getPort().get());
        server.addListener(listener);

        // register our listener
        commListener.setServer(server);

        try {
            server.start();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FakeHttp.class.getName()).log(Level.SEVERE, null, ex);
        }
        config.getStarted().set(true);
    }

    public void stopServer() {
        if (server == null || !server.isStarted()) {
            config.getStarted().set(false);
            return;
        }

        final CyclicBarrier barrier = new CyclicBarrier(2);

        GrizzlyFuture<HttpServer> fut = server.shutdown();

        fut.addCompletionHandler(new CompletionHandler<HttpServer>() {
            @Override
            public void cancelled() {

            }

            @Override
            public void failed(Throwable throwable) {

            }

            @Override
            public void completed(HttpServer result) {
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException ex) {
                    LOG.error("Error during server shutdown", ex);
                }
            }

            @Override
            public void updated(HttpServer result) {

            }
        });

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            LOG.error("Error during server shutdown", ex);
        }

        config.getStarted().set(false);
        server = null;

    }

    public void restartServer() {
        if (server != null && server.isStarted()) {
            stopServer();
            startServer();
        }
    }

    public void exit() {
        config.saveConfig();
        stopServer();
    }

}
