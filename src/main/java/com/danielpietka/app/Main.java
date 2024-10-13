package com.danielpietka.app;

import com.danielpietka.config.Config;
import com.danielpietka.database.ConnectionManager;
import com.danielpietka.handler.UserHandler;
import com.danielpietka.resource.UserResource;
import com.danielpietka.server.ApiHttpServer;
import com.danielpietka.server.Router;
import com.danielpietka.server.RouteRegistrar;
import com.danielpietka.service.UserService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Config config = new Config("config/config.properties");
        ConnectionManager connectionManager = ConnectionManager.getInstance(config);
        if (args.length == 0) {
            startServer(config, connectionManager);
        } else {
            startCommandLineMode(connectionManager, args);
        }
    }

    private static void startServer(Config config, ConnectionManager connectionManager) {
        try {
            Router router = new Router();
            RouteRegistrar registrar = new RouteRegistrar();
            ApiHttpServer server = new ApiHttpServer(router, config);
            registrar.registerRoutes(router, new UserService(new UserResource(connectionManager)), config);
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start the server: " + e.getMessage());
        }
    }

    private static void startCommandLineMode(ConnectionManager connectionManager, String[] args) {
        UserResource userResource = new UserResource(connectionManager);
        UserService userService = new UserService(userResource);
        UserHandler userHandler = new UserHandler(userService, args);
        userHandler.handleCommand();
    }
}
