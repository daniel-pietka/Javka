package com.danielpietka.util;

import com.sun.net.httpserver.HttpExchange;

public class AuthorizationService {

    public boolean authorize(HttpExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        String username = JwtUtil.validateToken(token);
        return username != null; // If token is valid, username will be non-null
    }

    public String getUsernameFromToken(HttpExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return JwtUtil.validateToken(token);
        }
        return null;
    }
}
