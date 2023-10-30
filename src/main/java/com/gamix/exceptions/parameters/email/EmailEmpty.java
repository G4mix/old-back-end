package com.gamix.exceptions.parameters.email;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class EmailEmpty extends ExceptionBase {
    public EmailEmpty() {
        super(ExceptionMessage.EMAIL_EMPTY);
    }
}
