package com.danielpietka.config;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private final Properties properties = new Properties();

    public Config() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/config.properties");

            if (inputStream == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }

    public String getDatabaseUrl() {
        return properties.getProperty("db.url");
    }

    public String getDatabaseUser() {
        return properties.getProperty("db.user");
    }

    public String getDatabasePassword() {
        return properties.getProperty("db.password");
    }
}
