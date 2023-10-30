package com.gamix.exceptions.parameters.password;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordNull extends ExceptionBase {
    public PasswordNull() {
        super(ExceptionMessage.PASSWORD_NULL);
    }
}
