package com.gamix.exceptions.parameters.username;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UsernameEmpty extends ExceptionBase {
    public UsernameEmpty() {
        super(ExceptionMessage.USERNAME_EMPTY);
    }
}
