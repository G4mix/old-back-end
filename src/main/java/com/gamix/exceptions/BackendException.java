package com.gamix.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.gamix.enums.ExceptionMessage;

public class BackendException extends RuntimeException {
    private String message;
    private HttpStatus status;
    
    public BackendException(ExceptionMessage message, HttpStatus status) {
        this.message = message.toString();
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    public HttpStatusCode getStatus() {
        return status;
    }
}