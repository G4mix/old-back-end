package com.gamix.exceptions.parameters.password;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordMissingNumber extends ExceptionBase {
    public PasswordMissingNumber() {
        super(ExceptionMessage.PASSWORD_MISSING_NUMBER);
    }
}
