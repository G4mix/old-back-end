package com.gamix.exceptions.post;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class PostNotFoundById extends ExceptionBase {
    public PostNotFoundById() {
        super(ExceptionMessage.POST_NOT_FOUND_BY_ID);
    }
}
