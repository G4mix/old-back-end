package com.gamix.exceptions.comment;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class TooLongContent extends ExceptionBase {
    public TooLongContent() {
        super(ExceptionMessage.TOO_LONG_COMMENT);
    }
}

