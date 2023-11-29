package com.gamix.exceptions.parameters.posts;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class TooManyLinks extends ExceptionBase {
    public TooManyLinks() {
        super(ExceptionMessage.TOO_MANY_LINKS);
    }
}
