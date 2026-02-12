package com.metamedicsvr.springtemplate.error.exception;

// Exception to be thrown when a CSV file has an invalid format
public class CsvFormatException extends RuntimeException {
    public CsvFormatException(String message) {
        super(message);
    }
}