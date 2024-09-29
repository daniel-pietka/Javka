package com.danielpietka.database;

import java.util.HashMap;
import java.util.Map;

public class Schema {

    public static final Map<String, String> tableSchemas = new HashMap<>();

    static {
        tableSchemas.put("student", "CREATE TABLE student ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "first_name VARCHAR(100), "
                + "last_name VARCHAR(100), "
                + "email VARCHAR(100), "
                + "phone VARCHAR(15), "
                + "login VARCHAR(50), "
                + "password VARCHAR(255))"
        );

        tableSchemas.put("user", "CREATE TABLE user ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(100), "
                + "api_key VARCHAR(255))"
        );
    }
}
