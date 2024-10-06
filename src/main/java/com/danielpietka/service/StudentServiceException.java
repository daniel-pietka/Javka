package com.danielpietka.service;

public class StudentServiceException extends Exception {
    public StudentServiceException(String message) {
        super(message);
    }

    public StudentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
