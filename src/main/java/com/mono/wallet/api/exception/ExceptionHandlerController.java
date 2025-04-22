package com.mono.wallet.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.View;

import java.util.Date;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController {

    private final View error;

    public ExceptionHandlerController(View error) {
        this.error = error;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorMessage> handleApiException(ApiException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                ex.getStatusCode(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, ex.getErrorType().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                errorMessage,
                request.getDescription(false)
        );

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String errorMessage = "Invalid JSON format";

        Throwable cause = ex.getCause();

        if (cause != null) {
            String causeMessage = cause.getMessage();

            if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
                if (causeMessage.contains("UUID")) {
                    errorMessage = "Invalid UUID format for walletId. Please use standard 36-character UUID.";
                } else if (causeMessage.contains("OperationType")) {
                    errorMessage = "Invalid operation type. Allowed values: WITHDRAW, DEPOSIT.";
                } else {
                    errorMessage = "Invalid value format: " + causeMessage;
                }

            } else if (cause instanceof com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException) {
                com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException upe =
                        (com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException) cause;

                errorMessage = "Unrecognized field '" + upe.getPropertyName() +
                        "'. Check if the field name is correct.";
            } else if (cause instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException) {
                errorMessage = "Incorrect or missing fields. Please check the JSON structure.";
            }
        }

        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                errorMessage,
                "Invalid JSON input"
        );
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }



}
