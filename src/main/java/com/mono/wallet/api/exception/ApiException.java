package com.mono.wallet.api.exception;

public class ApiException extends RuntimeException {
    private final ErrorType errorType;

    public ApiException(ErrorType errorType, String message) {
        super(message != null ? message : errorType.getMessage());
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {return errorType;}

    public int getStatusCode() {return errorType.getStatus().value();}


}