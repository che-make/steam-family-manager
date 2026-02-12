package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when a password token has expired
public class ExpirePasswordTokenException extends RuntimeException {
    public ExpirePasswordTokenException(String message) {
        super(message);
    }
}
