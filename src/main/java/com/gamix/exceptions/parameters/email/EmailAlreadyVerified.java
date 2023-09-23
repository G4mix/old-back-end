package com.gamix.exceptions.parameters.email;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class EmailAlreadyVerified extends ExceptionBase {
    public EmailAlreadyVerified() {
        super(ExceptionMessage.EMAIL_ALREADY_VERIFIED);
    }
}
