package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class TokensDoNotMatchException extends ExceptionBase {
    public TokensDoNotMatchException() {
        super(ExceptionMessage.TOKEN_DO_NOT_MATCH_EXCEPTION);
    }   
}
