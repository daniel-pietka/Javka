package com.danielpietka.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Install {
    private static final Logger logger = Logger.getLogger(Install.class.getName());

    public static void installSchema() {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();

            for (String tableName : Schema.tableSchemas.keySet()) {
                if (tableExists(connection, tableName)) {
                    logger.log(Level.INFO, "Table '" + tableName + "' already exists. Skipping creation.");
                } else {
                    createTable(connection, tableName);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error installing database schema: ", e);
        }
    }

    public static boolean tableExists(Connection connection, String tableName) throws SQLException {
        String query = "SHOW TABLES LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, tableName);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static void createTable(Connection connection, String tableName) throws SQLException {
        String createTableQuery = Schema.tableSchemas.get(tableName);

        try (PreparedStatement stmt = connection.prepareStatement(createTableQuery)) {
            stmt.execute();
            logger.info("Table '" + tableName + "' has been created.");
        }
    }
}
