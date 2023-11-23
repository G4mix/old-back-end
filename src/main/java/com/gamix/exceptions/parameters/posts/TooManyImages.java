package com.gamix.exceptions.parameters.posts;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class TooManyImages extends ExceptionBase {
    public TooManyImages() {
        super(ExceptionMessage.TOO_MANY_IMAGES);
    }
}
