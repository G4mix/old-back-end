package com.gamix.exceptions.user;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UserNotFound extends ExceptionBase {
    public UserNotFound() {
        super(ExceptionMessage.USER_NOT_FOUND);
    }
}
