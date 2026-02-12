package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when an email is not authorized in the institution
public class UnauthorizedEmailException extends RuntimeException {
    public UnauthorizedEmailException(String message) {
        super(message);
    }
}