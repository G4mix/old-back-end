package com.gamix.exceptions.general;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UnknownError extends ExceptionBase {
    public UnknownError() {
        super(ExceptionMessage.UNKNOWN_ERROR);
    }
}
