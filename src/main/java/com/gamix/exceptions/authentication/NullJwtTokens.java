package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class NullJwtTokens extends ExceptionBase {
    public NullJwtTokens() {
        super(ExceptionMessage.NULL_JWT_TOKENS);
    }
}