package com.gamix.exceptions.user;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UserNotFoundByEmail extends ExceptionBase {
    public UserNotFoundByEmail() {
        super(ExceptionMessage.USER_NOT_FOUND_BY_EMAIL);
    }
}
