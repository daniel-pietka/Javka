package com.danielpietka.app;

import com.danielpietka.database.Install;
import com.danielpietka.server.ApiHttpServer;
import com.danielpietka.util.LoggerConfig;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LoggerConfig.setupLogger();

        try {
            Install.installSchema();
            ApiHttpServer.startServer();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Server failed to start: ", e);
        }
    }
}
