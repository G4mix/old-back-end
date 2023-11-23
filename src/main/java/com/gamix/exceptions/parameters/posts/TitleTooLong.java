package com.gamix.exceptions.parameters.posts;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class TitleTooLong extends ExceptionBase {
    public TitleTooLong() {
        super(ExceptionMessage.TITLE_TOO_LONG);
    }
}
