package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when a property is missing
public class MissingPropertyException extends RuntimeException {
    public MissingPropertyException(String message) {
        super(message);
    }
}