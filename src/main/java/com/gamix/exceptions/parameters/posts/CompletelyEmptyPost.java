package com.gamix.exceptions.parameters.posts;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class CompletelyEmptyPost extends ExceptionBase {
    public CompletelyEmptyPost() {
        super(ExceptionMessage.COMPLETELY_EMPTY_POST);
    }
}
