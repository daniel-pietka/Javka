package com.danielpietka.app;

import com.danielpietka.database.ConnectionManager;
import com.danielpietka.handler.UserHandler;
import com.danielpietka.resource.UserResource;
import com.danielpietka.server.ApiHttpServer;
import com.danielpietka.service.UserService;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0 || "server".equalsIgnoreCase(args[0])) {
            startServer();
        } else {
            startCommandLineMode(args);
        }
    }

    private static void startServer() {
        try {
            ApiHttpServer.startServer();
        } catch (Exception e) {
            System.err.println("Failed to start the server: " + e.getMessage());
        }
    }

    private static void startCommandLineMode(String[] args) {
        UserResource userResource = new UserResource();
        UserService userService = new UserService(userResource);
        UserHandler userHandler = new UserHandler(userService, args);
        userHandler.handleCommand();
    }
}
