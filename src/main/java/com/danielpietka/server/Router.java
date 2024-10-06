package com.danielpietka.server;

import com.danielpietka.service.UserService;
import com.danielpietka.util.AuthorizationService;
import com.danielpietka.util.ErrorResponse;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Router implements HttpHandler {
    private static final Logger logger = Logger.getLogger(Router.class.getName());
    private final Gson gson = new Gson();
    private final Map<String, HttpHandler> routes = new HashMap<>();
    private final AuthorizationService authorizationService = new AuthorizationService();

    public Router(UserService userService) {
    }

    public void addRoute(String path, HttpHandler handler) {
        routes.put(path, handler);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        try {
            if (!"/login".equals(path) && !authorizationService.authorize(exchange)) {
                sendErrorResponse(exchange, 401, "Unauthorized access");
                return;
            }

            HttpHandler handler = routes.get(path);
            if (handler != null) {
                handler.handle(exchange);
            } else {
                sendErrorResponse(exchange, 404, "Not Found");
            }
        } catch (Exception e) {
            logger.severe("Error handling request: " + e.getMessage());
            sendErrorResponse(exchange, 500, "Internal Server Error");
        }
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(message, "An error occurred");
        String jsonResponse = errorResponse.toJson();
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
}
