package com.cineverso.api_cineverso.exceptions;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    private final String detailedMessage;

    public BaseException(HttpStatus status, String message) {
        this(status, message, null);
    }

    public BaseException(HttpStatus status, String message, String detailedMessage) {
        super(message);
        this.status = status;
        this.message = message;
        this.detailedMessage = detailedMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }
}