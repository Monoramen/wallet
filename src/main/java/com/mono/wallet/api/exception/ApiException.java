package com.mono.wallet.api.exception;

public class ApiException extends RuntimeException {
    private final ApiErrorType errorType;

    public ApiException(ApiErrorType errorType, String errorMessage) {
        super(errorMessage != null ? errorMessage : errorType.getMessage());
        this.errorType = errorType;

    }

    public ApiErrorType getErrorType() {
        return errorType;
    }

    public int getStatusCode() {
        return errorType.getStatus().value();
    }
}
