package com.metamedicsvr.springtemplate.error.exception;

public class LanguageNotFoundException extends RuntimeException{
    public LanguageNotFoundException(String message) {
        super(message);
    }
}
