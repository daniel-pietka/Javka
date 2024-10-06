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
    private Connection connection;

    private ConnectionManager() {
        try {
            String dbUrl = Config.getDatabaseUrl();
            String dbUser = Config.getDatabaseUser();
            String dbPassword = Config.getDatabasePassword();
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

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                String dbUrl = Config.getDatabaseUrl();
                String dbUser = Config.getDatabaseUser();
                String dbPassword = Config.getDatabasePassword();
                this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to establish database connection: ", e);
                throw e;
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
