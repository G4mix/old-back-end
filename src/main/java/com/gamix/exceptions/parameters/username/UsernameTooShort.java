package com.gamix.exceptions.parameters.username;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UsernameTooShort extends ExceptionBase {
    public UsernameTooShort() {
        super(ExceptionMessage.USERNAME_TOO_SHORT);
    }
}
