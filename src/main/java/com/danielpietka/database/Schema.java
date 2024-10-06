package com.danielpietka.database;

import java.util.HashMap;
import java.util.Map;

public class Schema {

    public static final Map<String, String> tableSchemas = new HashMap<>();

    static {
        tableSchemas.put("student", "CREATE TABLE student ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "first_name VARCHAR(100) NOT NULL, "
                + "last_name VARCHAR(100) NOT NULL, "
                + "email VARCHAR(100) NOT NULL, "
                + "birth_date DATE NOT NULL, "
                + "address VARCHAR(255), "
                + "phone_number VARCHAR(15), "
                + "gender VARCHAR(10), "
                + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                + "is_active BOOLEAN DEFAULT TRUE"
                + ")"
        );

        tableSchemas.put("user", "CREATE TABLE user ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(100), "
                + "password VARCHAR(255))"
        );
    }
}
