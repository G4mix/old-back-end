package com.gamix.exceptions.parameters.username;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UsernameTooLong extends ExceptionBase {
    public UsernameTooLong() {
        super(ExceptionMessage.USERNAME_TOO_LONG);
    }
}
