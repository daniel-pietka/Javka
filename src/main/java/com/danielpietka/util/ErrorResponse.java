package com.danielpietka.util;

import com.google.gson.Gson;

public class ErrorResponse {
    private final String message;
    private final String details;

    public ErrorResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
