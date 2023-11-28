package com.gamix.exceptions.comment;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class CommentIdNotFound extends ExceptionBase {
    public CommentIdNotFound() {
        super(ExceptionMessage.COMMENT_ID_NOT_FOUND);
    }
}
