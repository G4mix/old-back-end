package com.gamix.exceptions.parameters.password;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordInvalidFormat extends ExceptionBase {
    public PasswordInvalidFormat() {
        super(ExceptionMessage.PASSWORD_INVALID_FORMAT);
    }
}
