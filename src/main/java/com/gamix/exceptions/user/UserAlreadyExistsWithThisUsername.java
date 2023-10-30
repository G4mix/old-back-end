package com.gamix.exceptions.user;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UserAlreadyExistsWithThisUsername extends ExceptionBase {
    public UserAlreadyExistsWithThisUsername() {
        super(ExceptionMessage.USER_ALREADY_EXISTS_WITH_THIS_USERNAME);
    }
}
