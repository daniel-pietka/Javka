package com.danielpietka.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Router implements HttpHandler {
    private static final Logger logger = Logger.getLogger(Router.class.getName());
    private final Map<String, HttpHandler> routes = new HashMap<>();

    public void addRoute(String path, HttpHandler handler) {
        routes.put(path, handler);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        HttpHandler handler = routes.get(path);
        if (handler != null) {
            handler.handle(exchange);
        } else {
            sendNotFoundResponse(exchange);
        }
    }

    private void sendNotFoundResponse(HttpExchange exchange) throws IOException {
        String response = "{\"error\": \"Not Found\"}";
        exchange.sendResponseHeaders(404, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
