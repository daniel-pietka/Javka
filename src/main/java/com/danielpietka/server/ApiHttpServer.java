package com.danielpietka.server;

import com.sun.net.httpserver.HttpServer;
import com.danielpietka.handler.UserHandler;
import com.danielpietka.handler.StudentHandler;
import com.danielpietka.database.ConnectionManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiHttpServer {
    private static final int DEFAULT_PORT = 8080;
    private static final Logger logger = Logger.getLogger(ApiHttpServer.class.getName());
    private static HttpServer server;

    public static void startServer() throws IOException {
        startServer(DEFAULT_PORT);
    }

    public static void startServer(int port) throws IOException {
        Connection connection = ConnectionManager.getInstance().getConnection();

        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/users", new UserHandler(connection));
        server.createContext("/api/students", new StudentHandler(connection));
        server.setExecutor(null);
        server.start();
        logger.info("Server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down the server...");
            ConnectionManager.getInstance().close();
            if (server != null) {
                server.stop(0);
                logger.info("Server stopped.");
            }
        }));
    }
}
