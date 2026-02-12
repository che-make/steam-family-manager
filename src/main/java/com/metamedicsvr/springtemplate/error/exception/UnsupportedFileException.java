package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when a file is not supported
public class UnsupportedFileException extends RuntimeException {
    public UnsupportedFileException(String message) {
        super(message);
    }
}
