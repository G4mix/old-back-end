package com.gamix.exceptions.parameters.password;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordWrong extends ExceptionBase {
    public PasswordWrong() {
        super(ExceptionMessage.PASSWORD_WRONG);
    }
}
