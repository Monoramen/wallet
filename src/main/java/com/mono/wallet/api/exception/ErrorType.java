package com.mono.wallet.api.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request" ),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource Not Found"),
    CONFLICT(HttpStatus.CONFLICT, "Conflict"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus status;
    private final String message;

    ErrorType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }


    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
