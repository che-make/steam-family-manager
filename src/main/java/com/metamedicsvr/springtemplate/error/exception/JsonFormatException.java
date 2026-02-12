package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when a JSON file has an invalid format
public class JsonFormatException extends RuntimeException {
    public JsonFormatException(String message) {
        super(message);
    }
}