package com.gamix.exceptions.passwordUser;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordUserAlreadyExists extends ExceptionBase {
    public PasswordUserAlreadyExists() {
        super(ExceptionMessage.PASSWORDUSER_ALREADY_EXISTS);
    }
}
