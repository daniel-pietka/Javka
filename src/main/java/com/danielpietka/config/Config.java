package com.danielpietka.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private static final Properties properties = new Properties();
    private static final Logger logger = Logger.getLogger(Config.class.getName());

    static {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            if (inputStream == null) {
                logger.log(Level.SEVERE, "Error: Unable to find config.properties");
                throw new RuntimeException("Configuration file not found: config/config.properties");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading config.properties: ", e);
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    public static String getDatabaseUrl() {
        return getProperty("db.url", "jdbc:mysql://localhost:3306/default_db");
    }

    public static String getDatabaseUser() {
        return getProperty("db.user", "root");
    }

    public static String getDatabasePassword() {
        return getProperty("db.password", "root");
    }

    public static String getSecretKey() {
        return getProperty("jwt.secret", "default_secret_key");
    }

    public static int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "8080"));
    }

    public static String getApiPath(String apiName) {
        return getProperty(apiName, "/login");
    }

    public static int getTokenExpiration() {
        return Integer.parseInt(getProperty("token.expiration", "3600000"));
    }

    private static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.log(Level.WARNING, "Property {0} not found in configuration file. Using default: {1}", new Object[]{key, defaultValue});
            return defaultValue;
        }
        return value;
    }
}
