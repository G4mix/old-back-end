package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class JwtTokensGenerationException extends ExceptionBase {
    public JwtTokensGenerationException() {
        super(ExceptionMessage.JWT_TOKENS_GENERATION_EXCEPTION);
    }
}
