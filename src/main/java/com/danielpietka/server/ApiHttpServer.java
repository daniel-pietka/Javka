package com.danielpietka.server;

import com.danielpietka.config.Config;
import com.danielpietka.handler.StudentHandler;
import com.danielpietka.handler.LoginHandler;
import com.danielpietka.resource.UserResource;
import com.danielpietka.service.UserService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class ApiHttpServer {
    private static final Logger logger = Logger.getLogger(ApiHttpServer.class.getName());
    private static HttpServer server;

    public static void startServer() throws IOException {
        UserResource userResource = new UserResource();
        UserService userService = new UserService(userResource);

        Router router = new Router(userService);
        registerRoutes(router, userService);

        int port = Config.getServerPort();
        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", router);
        server.setExecutor(null);
        server.start();
        logger.info("Server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down the server...");
            if (server != null) {
                server.stop(0);
                logger.info("Server stopped.");
            }
        }));
    }

    private static void registerRoutes(Router router, UserService userService) {
        router.addRoute(Config.getApiPath("api.login.path"), new LoginHandler(userService));
        router.addRoute(Config.getApiPath("api.students.path"), new StudentHandler());
    }
}
