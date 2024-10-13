package com.danielpietka.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private static final Logger logger = Logger.getLogger(Config.class.getName());
    private final Properties properties = new Properties();

    public Config(String configFilePath) {
        loadProperties(configFilePath);
    }

    private void loadProperties(String configFilePath) {
        try (InputStream inputStream = Files.newInputStream(Paths.get(configFilePath))) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading config file: " + configFilePath, e);
            throw new RuntimeException("Failed to load configuration file: " + configFilePath, e);
        }
    }

    public String getDatabaseUrl() {
        return properties.getProperty("db.url", "jdbc:mysql://localhost:3306/default_db");
    }

    public String getDatabaseUser() {
        return properties.getProperty("db.user", "root");
    }

    public String getDatabasePassword() {
        return properties.getProperty("db.password", "root");
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port", "8080"));
    }

    public String getApiPath(String apiName) {
        return properties.getProperty(apiName, "/login");
    }

    public String getTokenSecret() {
        return properties.getProperty("token.secret", "default_secret_key");
    }

    public int getTokenExpiration() {
        return Integer.parseInt(properties.getProperty("token.expiration", "3600000"));
    }
}
