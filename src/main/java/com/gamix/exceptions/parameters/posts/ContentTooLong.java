package com.gamix.exceptions.parameters.posts;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class ContentTooLong extends ExceptionBase {
    public ContentTooLong() {
        super(ExceptionMessage.CONTENT_TOO_LONG);
    }
}
