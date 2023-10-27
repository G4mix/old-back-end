package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class InvalidAccessToken extends ExceptionBase {
    public InvalidAccessToken() {
        super(ExceptionMessage.INVALID_ACCESS_TOKEN);
    }
}