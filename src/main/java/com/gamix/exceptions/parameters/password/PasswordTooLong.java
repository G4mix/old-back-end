package com.gamix.exceptions.parameters.password;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordTooLong extends ExceptionBase {
    public PasswordTooLong() {
        super(ExceptionMessage.PASSWORD_TOO_LONG);
    }
}
