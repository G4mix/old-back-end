package com.gamix.exceptions.user;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UserNotFoundById extends ExceptionBase {
    public UserNotFoundById() {
        super(ExceptionMessage.USER_NOT_FOUND_BY_ID);
    }
}
