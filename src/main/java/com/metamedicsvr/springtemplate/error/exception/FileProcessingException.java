package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when an error occurs while processing file
public class FileProcessingException extends RuntimeException {
    public FileProcessingException(String message) {
        super(message);
    }
}