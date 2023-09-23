package com.gamix.exceptions.parameters.username;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UsernameContainsSpecialChar extends ExceptionBase {
    public UsernameContainsSpecialChar() {
        super(ExceptionMessage.USERNAME_CONTAINS_SPECIAL_CHAR);
    }
}
