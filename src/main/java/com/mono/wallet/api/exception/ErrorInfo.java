package com.mono.wallet.api.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorInfo {
    private int statusCode;
    private String timestamp;
    private String message;
    private String description;

    public ErrorInfo(int statusCode, Date date, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(date);
        this.message = message;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}