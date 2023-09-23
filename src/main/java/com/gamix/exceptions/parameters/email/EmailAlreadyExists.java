package com.gamix.exceptions.parameters.email;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class EmailAlreadyExists extends ExceptionBase {
    public EmailAlreadyExists() {
        super(ExceptionMessage.EMAIL_ALREADY_EXISTS);
        
    }
}
