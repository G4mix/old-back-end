package com.gamix.exceptions;

import com.gamix.enums.ExceptionMessage;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class ExceptionBase extends RuntimeException {
    private final HttpStatusCode status;
    private final String error, message;

    public ExceptionBase(ExceptionMessage e) {
        this.status = e.getHttpStatus();
        this.error = e.toString();
        this.message = e.getMessage();
    }
}
