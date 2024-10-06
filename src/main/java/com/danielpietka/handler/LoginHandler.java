package com.danielpietka.handler;

import com.danielpietka.model.LoginRequest;
import com.danielpietka.model.LoginResponse;
import com.danielpietka.service.UserService;
import com.danielpietka.util.JwtUtil;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class LoginHandler implements HttpHandler {
    private final UserService userService;
    private static final Logger logger = Logger.getLogger(LoginHandler.class.getName());
    private final Gson gson = new Gson();

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, 0);
            return;
        }

        try (InputStream is = exchange.getRequestBody();
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            LoginRequest loginRequest = gson.fromJson(isr, LoginRequest.class);
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            if (username == null || password == null) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            var user = userService.authenticateUser(username, password);
            if (user != null) {
                String token = JwtUtil.generateToken(username);
                String response = gson.toJson(new LoginResponse(token, username));
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
            } else {
                exchange.sendResponseHeaders(401, 0);
            }
        } catch (Exception e) {
            logger.severe("Error processing login: " + e.getMessage());
            exchange.sendResponseHeaders(500, 0);
        } finally {
            exchange.getResponseBody().close();
        }
    }
}
