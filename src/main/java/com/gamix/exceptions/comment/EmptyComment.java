package com.gamix.exceptions.comment;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class EmptyComment extends ExceptionBase {
    public EmptyComment() {
        super(ExceptionMessage.COMMENT_EMPTY);
    }
}
