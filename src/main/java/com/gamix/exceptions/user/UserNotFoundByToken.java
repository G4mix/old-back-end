package com.gamix.exceptions.user;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UserNotFoundByToken extends ExceptionBase {
    public UserNotFoundByToken() {
        super(ExceptionMessage.USER_NOT_FOUND_BY_TOKEN);
    }
}
