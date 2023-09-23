package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class TokenValidationException extends ExceptionBase {
    public TokenValidationException() {
        super(ExceptionMessage.TOKEN_VALIDATION_EXCEPTION);
    }
}
