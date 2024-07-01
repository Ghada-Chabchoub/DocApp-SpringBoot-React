package com.example.pfabackendfinal.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String user, String id, Long id1) {
    }
}
