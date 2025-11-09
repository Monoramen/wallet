package com.mono.wallet.api.exception;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mono.wallet.enums.OperationType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerController {


    public ExceptionHandlerController() {
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorInfo> handleApiException(ApiException e, WebRequest request) {
        ErrorInfo message = new ErrorInfo(
                e.getStatusCode(),
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(message, e.getErrorType().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ErrorInfo message = new ErrorInfo(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                errorMessage,
                request.getDescription(false)
        );

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorInfo> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {

        Throwable rootCause = ex.getMostSpecificCause();

        ResponseEntity<ErrorInfo> response;

        if ((response = handleInvalidFormatException(rootCause)) != null) return response;
        else if ((response = handleUnrecognizedPropertyException(rootCause)) != null) return response;
        else if ((response = handleMismatchedInputException(rootCause)) != null) return response;

        return buildErrorResponse("Invalid JSON format", "Invalid JSON input");
    }


    private ResponseEntity<ErrorInfo> buildErrorResponse(String message, String details) {
        return new ResponseEntity<>(
                new ErrorInfo(
                        HttpStatus.BAD_REQUEST.value(),
                        new Date(),
                        message,
                        details
                ),
                HttpStatus.BAD_REQUEST
        );
    }


    private ResponseEntity<ErrorInfo> handleInvalidFormatException(Throwable cause) {
        if (!(cause instanceof InvalidFormatException)) return null;

        InvalidFormatException ife = (InvalidFormatException) cause;

        if (ife.getTargetType() == UUID.class) {
            return buildErrorResponse(
                    "Invalid UUID format",
                    "Please use standard 36-character UUID format"
            );
        }

        if (ife.getTargetType() == OperationType.class) {
            String invalidValue = ife.getValue() != null ?
                    ife.getValue().toString() : "null";
            return buildErrorResponse(
                    String.format("Invalid operation type: '%s'", invalidValue),
                    "Allowed values: " + Arrays.toString(OperationType.values())
            );
        }

        return null;
    }

    private ResponseEntity<ErrorInfo> handleMismatchedInputException(Throwable cause) {
        if (!(cause instanceof MismatchedInputException)) return null;

        return buildErrorResponse(
                "Missing or invalid fields",
                "Check request structure"
        );
    }

    private ResponseEntity<ErrorInfo> handleUnrecognizedPropertyException(Throwable cause) {
        if (!(cause instanceof UnrecognizedPropertyException)) return null;

        UnrecognizedPropertyException upe = (UnrecognizedPropertyException) cause;
        return buildErrorResponse(
                "Unrecognized field: '" + upe.getPropertyName() + "'",
                "Check field name spelling"
        );
    }
}