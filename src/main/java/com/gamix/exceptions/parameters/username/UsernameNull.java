package com.gamix.exceptions.parameters.username;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UsernameNull extends ExceptionBase {
    public UsernameNull() {
        super(ExceptionMessage.USERNAME_NULL);
    }
}
