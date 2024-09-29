package com.danielpietka.database;

import com.danielpietka.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
    private static ConnectionManager instance;
    private Connection connection;
    private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());

    private ConnectionManager() {
        try {
            Config config = new Config();
            String dbUrl = config.getDatabaseUrl();
            String dbUser = config.getDatabaseUser();
            String dbPassword = config.getDatabasePassword();
            this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            logger.info("Successfully established connection to the database.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection error: ", e);
        }
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                Config config = new Config();
                String dbUrl = config.getDatabaseUrl();
                String dbUser = config.getDatabaseUser();
                String dbPassword = config.getDatabasePassword();
                this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                logger.info("Re-established connection to the database.");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to re-establish database connection: ", e);
            }
        }
        return this.connection;
    }

    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
                logger.info("The connection to the database has been closed.");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Database connection close error: ", e);
            }
        }
    }
}
