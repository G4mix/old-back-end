package com.gamix.exceptions.user;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UserNotFoundByUsername extends ExceptionBase {
    public UserNotFoundByUsername() {
        super(ExceptionMessage.USER_NOT_FOUND_BY_USERNAME);
    }
}
