package com.gamix.exceptions.parameters.posts;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class TooManyTags extends ExceptionBase {
    public TooManyTags() {
        super(ExceptionMessage.TOO_MANY_TAGS);
    }
}
