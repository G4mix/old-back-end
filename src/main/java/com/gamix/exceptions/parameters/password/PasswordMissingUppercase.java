package com.gamix.exceptions.parameters.password;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PasswordMissingUppercase extends ExceptionBase {
    public PasswordMissingUppercase() {
        super(ExceptionMessage.PASSWORD_MISSING_UPPERCASE);
    }
}
