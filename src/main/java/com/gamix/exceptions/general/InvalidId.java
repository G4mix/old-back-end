package com.gamix.exceptions.general;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class InvalidId extends ExceptionBase {
    public InvalidId() {
        super(ExceptionMessage.INVALID_ID);
    }
}
