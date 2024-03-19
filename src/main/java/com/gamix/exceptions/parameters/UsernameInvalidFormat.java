package com.gamix.exceptions.parameters;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UsernameInvalidFormat extends ExceptionBase {
    public UsernameInvalidFormat() {
        super(ExceptionMessage.USERNAME_INVALID_FORMAT);
    }
}
