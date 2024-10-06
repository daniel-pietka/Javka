package com.danielpietka.util;

import com.danielpietka.config.Config;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtParser;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final Key key = Keys.hmacShaKeyFor(Config.getSecretKey().getBytes());

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Config.getTokenExpiration())) // 24 hours
                .signWith(key)
                .compact();
    }

    public static String validateToken(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();

            return parser.parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
