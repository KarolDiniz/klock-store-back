package com.online.KlockStore.business.exception.api;

public class ApiException extends RuntimeException {
    private final String message;
    private final int statusCode;

    public ApiException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
