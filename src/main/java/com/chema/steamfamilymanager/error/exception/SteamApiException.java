package com.chema.steamfamilymanager.error.exception;

public class SteamApiException extends RuntimeException {
    public SteamApiException(String message) {
        super(message);
    }

    public SteamApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
