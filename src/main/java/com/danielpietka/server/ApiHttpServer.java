package com.danielpietka.server;

import com.danielpietka.config.Config;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class ApiHttpServer {
    private static final Logger logger = Logger.getLogger(ApiHttpServer.class.getName());
    private final HttpServer server;
    private final Router router;
    private final Config config;

    public ApiHttpServer(Router router, Config config) throws IOException {
        this.router = router;
        this.config = config;
        int port = config.getServerPort();
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", router);
        server.setExecutor(null);
    }

    public void start() {
        server.start();
        logger.info("Server started on port " + config.getServerPort());
        addShutdownHook();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down the server...");
            server.stop(0);
            logger.info("Server stopped.");
        }));
    }
}
