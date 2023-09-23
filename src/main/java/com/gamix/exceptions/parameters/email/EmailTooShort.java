package com.gamix.exceptions.parameters.email;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class EmailTooShort extends ExceptionBase {
    public EmailTooShort() {
        super(ExceptionMessage.EMAIL_TOO_SHORT);
    }
}
