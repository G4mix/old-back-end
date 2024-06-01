package com.gamix.exceptions;

import com.gamix.enums.ExceptionMessage;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GamixError {
    private final String error, message;

    public GamixError(ExceptionMessage e) {
        this.error = e.toString();
        this.message = e.getMessage();
    }
}
