package com.gamix.exceptions.parameters.email;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class EmailNull extends ExceptionBase {
    public EmailNull() {
        super(ExceptionMessage.EMAIL_NULL);
    }
}
