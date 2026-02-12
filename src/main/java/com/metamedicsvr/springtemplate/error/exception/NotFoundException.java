package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when a resource or entity is not found
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
