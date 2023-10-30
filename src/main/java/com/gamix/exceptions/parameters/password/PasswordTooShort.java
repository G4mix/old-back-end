package com.gamix.exceptions.parameters.password;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordTooShort extends ExceptionBase {
    public PasswordTooShort() {
        super(ExceptionMessage.PASSWORD_TOO_SHORT);
    }
}
