package com.gamix.exceptions.authentication;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordUserNotFound extends ExceptionBase {
    public PasswordUserNotFound() {
        super(ExceptionMessage.PASSWORDUSER_NOT_FOUND);
    }
}
