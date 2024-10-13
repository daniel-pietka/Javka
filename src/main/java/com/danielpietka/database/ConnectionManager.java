package com.danielpietka.database;

import com.danielpietka.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {
    private static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());
    private static ConnectionManager instance;
    private final Config config;
    private Connection connection;

    private ConnectionManager(Config config) {
        this.config = config;
        initializeConnection();
    }

    public static synchronized ConnectionManager getInstance(Config config) {
        if (instance == null) {
            instance = new ConnectionManager(config);
        }
        return instance;
    }

    private void initializeConnection() {
        try {
            String dbUrl = config.getDatabaseUrl();
            String dbUser = config.getDatabaseUser();
            String dbPassword = config.getDatabasePassword();
            this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            logger.info("Successfully established connection to the database.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection error: ", e);
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            initializeConnection();
        }
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                logger.info("The connection to the database has been closed.");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Database connection close error: ", e);
            }
        }
    }
}
