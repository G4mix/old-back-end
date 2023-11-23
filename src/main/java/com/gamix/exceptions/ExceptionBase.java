package com.gamix.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import com.gamix.enums.ExceptionMessage;

public class ExceptionBase extends Exception {
    private final HttpStatus status;
    private final String error, message;

    public ExceptionBase(ExceptionMessage invalidAccessToken) {
        this.error = invalidAccessToken.toString();
        this.status = invalidAccessToken.getHttpStatus();
        this.message = invalidAccessToken.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
