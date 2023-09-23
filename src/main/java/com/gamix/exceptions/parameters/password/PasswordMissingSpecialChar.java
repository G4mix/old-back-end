package com.gamix.exceptions.parameters.password;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordMissingSpecialChar extends ExceptionBase {
    public PasswordMissingSpecialChar() {
        super(ExceptionMessage.PASSWORD_MISSING_SPECIAL_CHAR);
    }
}
