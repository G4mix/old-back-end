package com.gamix.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BackendException extends RuntimeException {
    private String message;
    private HttpStatus status;
    
    public BackendException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    public HttpStatusCode getStatus() {
        return status;
    }
}