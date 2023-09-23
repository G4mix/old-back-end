package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class ExcessiveFailedLoginAttempts extends ExceptionBase {
    public ExcessiveFailedLoginAttempts() {
        super(ExceptionMessage.EXCESSIVE_FAILED_LOGIN_ATTEMPTS);
    }
}
