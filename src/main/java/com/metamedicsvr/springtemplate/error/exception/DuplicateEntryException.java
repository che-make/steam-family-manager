package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when a duplicate entry is found in the database
public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}