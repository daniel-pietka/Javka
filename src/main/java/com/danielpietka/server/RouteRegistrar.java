package com.danielpietka.server;

import com.danielpietka.config.Config;
import com.danielpietka.database.ConnectionManager;
import com.danielpietka.handler.LoginHandler;
import com.danielpietka.handler.StudentHandler;
import com.danielpietka.resource.StudentResource;
import com.danielpietka.service.UserService;
import com.danielpietka.util.JwtUtil;

public class RouteRegistrar {
    public void registerRoutes(Router router, UserService userService, Config config) {
        JwtUtil jwtUtil = new JwtUtil(config.getTokenSecret(), config.getTokenExpiration());
        ConnectionManager connectionManager = ConnectionManager.getInstance(config);
        router.addRoute(config.getApiPath("api.login.path"), new LoginHandler(userService, jwtUtil));
        router.addRoute(config.getApiPath("api.students.path"), new StudentHandler(new StudentResource(connectionManager)));
    }
}
