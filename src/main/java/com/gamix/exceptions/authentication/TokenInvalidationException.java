package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class TokenInvalidationException extends ExceptionBase {
    public TokenInvalidationException() {
        super(ExceptionMessage.TOKEN_INVALIDATION_EXCEPTION);
    }
}
