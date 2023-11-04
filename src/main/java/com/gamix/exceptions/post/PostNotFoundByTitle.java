package com.gamix.exceptions.post;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PostNotFoundByTitle extends ExceptionBase {
    public PostNotFoundByTitle() {
        super(ExceptionMessage.POST_NOT_FOUND_BY_TITLE);
    }
}
