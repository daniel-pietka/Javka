package com.danielpietka.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHelper {
    public static String getRequestMethod(HttpExchange exchange) {
        return exchange.getRequestMethod();
    }

    public static String getRequestURI(HttpExchange exchange) {
        return exchange.getRequestURI().toString();
    }

    public static Map<String, String> getQueryParams(HttpExchange exchange) throws UnsupportedEncodingException {
        URI uri = exchange.getRequestURI();
        String query = uri.getQuery();
        Map<String, String> queryParams = new HashMap<>();

        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length > 1) {
                    queryParams.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                            URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
                } else {
                    queryParams.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8), "");
                }
            }
        }

        return queryParams;
    }

    public static int extractIdFromUri(String uri, String basePath) {
        if (uri.startsWith(basePath)) {
            String[] parts = uri.substring(basePath.length()).split("/");
            try {
                return Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid ID in URI: " + uri);
            }
        }
        throw new IllegalArgumentException("Invalid URI for extracting ID: " + uri);
    }
}
