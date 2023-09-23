package com.gamix.exceptions.parameters.email;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class EmailTooLong extends ExceptionBase {
    public EmailTooLong() {
        super(ExceptionMessage.EMAIL_TOO_LONG);
    }
}
