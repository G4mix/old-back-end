package com.gamix.exceptions.parameters.username;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UsernameAlreadyExists extends ExceptionBase {
    private String message;

    public UsernameAlreadyExists() {
        super(ExceptionMessage.USERNAME_ALREADY_EXISTS);
        this.message = "A user with this username already exists.";
    }

    public String getMessage() {
        return message;
    }
}
