package com.danielpietka.util;

import com.sun.net.httpserver.HttpExchange;

public class AuthorizationService {
    private final JwtUtil jwtUtil;

    public AuthorizationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public boolean authorize(HttpExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        return jwtUtil.validateToken(token);
    }
}
