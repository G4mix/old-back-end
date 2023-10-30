package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class TokenClaimsException extends ExceptionBase {
    public TokenClaimsException() {
        super(ExceptionMessage.TOKEN_CLAIMS_EXCEPTION);
    }
}
