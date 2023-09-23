package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class InvalidRefreshToken extends ExceptionBase {
    public InvalidRefreshToken() {
        super(ExceptionMessage.INVALID_REFRESH_TOKEN);
    }
}
